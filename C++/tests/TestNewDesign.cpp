#include <cstdlib>
#include <cmath>
#include <iostream>
#include <string.h>
#include <vector>
#include <functional>

#include <AnyMemory.h>
#include <IdMap.h>
#include <BitSet.h>

using namespace std;

struct ComponentBase
{
	const size_t id;
	const string name;
	const AnyMemory defaultValue;

	ComponentBase(const size_t m_id, const string &m_name, const AnyMemory &m_defaultValue) : id(m_id), name(m_name), defaultValue(m_defaultValue) {};
};

template<typename T>
struct Component : ComponentBase
{
	T typedDefaultValue;

	Component(const size_t m_id, const string &m_name, const T &m_typedDefaultValue) : ComponentBase(m_id, m_name, AnyMemory(m_typedDefaultValue)), typedDefaultValue(m_typedDefaultValue) {}
};

struct Entity;

template<typename T>
struct ComponentGet : ComponentBase
{
	BitSet required;
	function<T(Entity&)> get;

	ComponentGet(size_t m_id, const string &m_name, const BitSet &m_required, function<T(Entity&)> m_get) : ComponentBase(m_id, m_name, {}), required(m_required), get(m_get) {}
};

template<typename T>
struct ComponentSet : ComponentBase
{
	BitSet required;
	function<T&(Entity&, T&)> set;

	ComponentSet(size_t m_id, const string &m_name, const BitSet &m_required, function<T&(Entity&, T&)> m_set) : ComponentBase(m_id, m_name, {}), required(m_required), set(m_set) {}
};

struct EntityCore;

struct EntityType
{
	AnyMemory defaultValues;
	IdMap components;
	EntityCore *core;

};

struct Entity
{
	AnyMemory values;
	IdMap map;

	Entity() { }
	~Entity() { }

	template<typename T>
	bool add(const Component<T> &component, const T& value) {
		bool missing = !map.has(component.id);
		if (missing) {
			size_t offset = values.add(value);
			map.add(component.id, offset);
		}
		return missing;
	}
	template<typename T>
	inline bool add(const Component<T> &component) {
		return add(component, component.typedDefaultValue);
	}
	template<typename T>
	inline T& get(const Component<T> &component) {
		return values.get<T>(map.getIndex(component.id));
	}
	template<typename T>
	inline T& sets(const ComponentSet<T> &component, T& out) {
		return ( out = (has(component.required) ? component.set(*this, out) : out) );
	}
	template<typename T>
	inline T& set(const ComponentSet<T> &component, T& out) {
		return ( out = component.set(*this, out) );
	}
	template<typename T>
	inline T gets(const ComponentGet<T> &component) {
		return has(component.required) ? component.get(*this) : T();
	}
	template<typename T>
	inline T get(const ComponentGet<T> &component) {
		return component.get(*this);
	}
	template<typename T>
	inline T gets(const Component<T> &component, const T& missingValue) {
		return map.has(component.id) ? get<T>(component) : missingValue;
	}
	template<typename T>
	inline void set(const Component<T> &component, const T &value) {
		values.set<T>(map.getIndex(component.id), value);
	}
	template<typename T>
	inline bool sets(const Component<T> &component, const T &value) {
		bool has = map.has(component.id);
		if (has) set<T>(component, value);
		return has;
	}
	template<typename T>
	bool put(const Component<T> &component, const T &value) {
		bool missing = !map.has(component.id);
		if (missing) add<T>(component, value); else set<T>(component, value);
		return missing;
	}
	template<typename T>
	inline T* ptr(const Component<T> &component) {
		return values.getPointer<T>(map.getIndex(component.id));
	}
	template<typename T>
	inline T* ptrs(const Component<T> &component) {
		int i = map.getIndexSafe(component.id);
		return (i == -1 ? nullptr : values.getPointer<T>(i));
	}
	template<typename T>
	inline T& operator()(const Component<T> &component) {
		return get<T>(component);
	}
	template<typename T>
	inline T operator()(const ComponentGet<T> &component) {
		return gets<T>(component);
	}
	template<typename T>
	inline bool operator()(const Component<T> &component, const T &value) {
		return sets<T>(component, value);
	}

	bool has(const BitSet &components) {
		return map.getBitSet().contains(components);
	}
};

struct EntityCore
{
	vector<ComponentBase*> components;

	template<typename T>
	Component<T>& newComponent(const string &name, const T &defaultValue) {
		Component<T> *c = new Component<T>(components.size(), name, defaultValue);
		components.push_back(c);
		return *c;
	}
	template<typename T>
	ComponentSet<T>& newComponentSet(const string &name, const BitSet &required, function<T&(Entity&, T&)> set) {
		ComponentSet<T> *c = new ComponentSet<T>(components.size(), name, required, set);
		components.push_back(c);
		return *c;
	}
	template<typename T>
	ComponentGet<T>& newComponentGet(const string &name, const BitSet &required, function<T(Entity&)> get) {
		ComponentGet<T> *c = new ComponentGet<T>(components.size(), name, required, get);
		components.push_back(c);
		return *c;
	}
};

EntityCore Core;
Component<float> 		ANGLE 		= Core.newComponent("angle", 0.0f);
Component<int>	 		TIMES 		= Core.newComponent("times", 0);

// Passing in function reference
float& AngleTimes(Entity &e, float &out) {
	return out = e.get(ANGLE) * e.get(TIMES);
}
ComponentSet<float>		ANGLE_TIMES = Core.newComponentSet<float>("angletimes", {ANGLE.id, TIMES.id}, &AngleTimes);

// Passing in anonymous function (lambda)
ComponentGet<float>		DEGREES 	= Core.newComponentGet<float>("degrees", {ANGLE.id},
	[](Entity &e) -> float {
		return e.get(ANGLE) * 180.0f / 3.14159262f;
	}
);

// Passing in class/struct with function operator overloaded
struct TimesMultiplier {
	int multiplier;
	TimesMultiplier(int m_multiplier) : multiplier(m_multiplier) {}
	int operator()(Entity &e) {
		return multiplier * e.get(TIMES);
	}
};
ComponentGet<float>		TIMES_MULT = Core.newComponentGet<float>("timesmult", {TIMES.id}, TimesMultiplier(5));

int main()
{
	Entity e;

	e.add(ANGLE);
	e.add(TIMES);

	float a = e.get(ANGLE);
	int	  t = e.get(TIMES);

	cout << "ANGLE: " << a << endl;
	cout << "TIMES: " << t << endl;

	e.set(ANGLE, 0.02f);
	e.set(TIMES, 3);

	cout << "ANGLE: " << e.get(ANGLE) << endl;
	cout << "TIMES: " << e.get(TIMES) << endl;

	float angleTimes = 0.0f;
	e.sets(ANGLE_TIMES, angleTimes);
	cout << "ANGLETIMES: " << angleTimes << endl;

	cout << "DEGREES: " << e.gets(DEGREES) << endl;

	cout << "TIMESMULT: " << e.gets(TIMES_MULT) << endl;

	e(ANGLE) = 1.2f;

	cout << "e(ANGLE): " << e(ANGLE) << endl;
	cout << "e(TIMES_MULT): " << e(TIMES_MULT) << endl;

	// Ways of setting a Component's value
	e.set(ANGLE, 3.0f);
	e.sets(ANGLE, 4.0f); // safely  (won't cause error if entity doesn't have an ANGLE, just returns false)
	e.put(ANGLE, 5.0f); // safely (adds component if missing)
	e(ANGLE, 1.0f); // safely
	e(ANGLE) = 2.0f;

	// Ways of getting a Component's value
	cout << e.get(ANGLE) << endl;
	cout << e.gets(ANGLE, 0.0f) << endl; // safely (returns second argument if entity doesn't have an ANGLE)
	cout << e.ptr(ANGLE) << endl; // returns pointer to angle
	cout << e.ptrs(ANGLE) << endl; // safely returns pointer (nullptr if entity doesn't have an ANGLE)
	cout << e(ANGLE) << endl;
	cout << e(DEGREES) << endl;

	return 0;
}
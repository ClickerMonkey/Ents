#include <Component.h>
#include <string>

using namespace std;

ostream& operator<<(ostream &out, const ComponentBase &a)
{
	out << "[ComponentBase]{id:" << a.id << ", name:" << a.name << ", default:" << a.defaultValue << "}";
	return out;
}

template<typename T>
ostream& operator<<(ostream &out, const Component<T> &a)
{
	out << "[Component]{id:" << a.id << ", name:" << a.name << ", type:" << typeid(T).name() << ", default:" << a.typedDefaultValue << "}";
	return out;	
}

template<typename T>
ostream& operator<<(ostream &out, const ComponentGet<T> &a)
{
	out << "[ComponentGet]{id:" << a.id << ", name:" << a.name << ", type:" << typeid(T).name() << "}";
	return out;	
}

template<typename T>
ostream& operator<<(ostream &out, const ComponentSet<T> &a)
{
	out << "[ComponentSet]{id:" << a.id << ", name:" << a.name << ", type:" << typeid(T).name() << "}";
	return out;	
}


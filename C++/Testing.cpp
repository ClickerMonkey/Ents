#include <cstdlib>
#include <cmath>
#include <iostream>
#include <string.h>
#include <vector>

using namespace std;

struct AnyMemory {
private:
  size_t size;
  void *data;
public:

  template<typename T>
  AnyMemory(const T &x) : size(0), data(NULL) {
    add<T>(x);
  }

  AnyMemory(const AnyMemory &copy) : size(0), data(NULL) {
    setSize(copy.size);
    memcpy(data, copy.data, copy.size);
  }

  AnyMemory() : size(0), data(NULL) {
  }

  ~AnyMemory() {
    if (data != NULL) {
      free(data);
      data = NULL;
    }
  }

  inline size_t getSize() const {
    return size;
  }
  inline void* getData() {
    return data;
  }
  inline void setSize(const size_t newSize) {
    data = realloc(data, size = newSize);
  }
  inline void expand(const size_t bytes) {
    setSize(size + bytes);
  }
  size_t append(const AnyMemory &copy) {
    const size_t offset = size;
    setSize(size + copy.size);
    memcpy(data + offset, copy.data, copy.size);
    return offset;
  }

  template<typename T>
  inline bool exists(const size_t offset) const {
    return (offset + sizeof(T) <= size);
  }

  template<typename T>
  inline T* getPointer(const size_t offset) {
    return (T*)(data + offset);
  }

  template<typename T>
  inline T* getSafe(const size_t offset) {
    return exists<T>(offset) ? getPointer<T>(offset) : NULL;
  }

  template<typename T>
  inline T& get(const size_t offset) {
    return *getPointer<T>(offset);
  }

  template<typename T>
  inline void set(const size_t offset, const T &item) {
    *getPointer<T>(offset) = item;
  }

  template<typename T>
  inline bool setSafe(const size_t offset, const T &item) {
    T* ptr = getSafe<T>(offset);
    if (ptr != NULL) {
      *ptr = item;
    }
    return (ptr != NULL);
  }

  template<typename T>
  inline size_t add(const T &item) {
    const size_t offset = size;
    setSize(size + sizeof(T));
    set<T>(offset, item);
    return offset;
  }

  template<typename T>
  inline AnyMemory& operator+=(const T &item) {
    add<T>(item);
    return *this;
  }

  template<typename T>
  inline AnyMemory& operator<<(const T &item) {
    add<T>(item);
    return *this;
  }
};

struct BitSet {
private:
  vector<int> bits;

  inline size_t indexOf(const size_t bitIndex) const {
    return (bitIndex >> 5);
  }
  inline size_t offsetOf(const size_t bitIndex) const {
    return (bitIndex & 31);
  }
public:

  BitSet() {
  }
  BitSet(const vector<int> &indices) {
    setFromIndices(indices);
  }

  void setFromIndices(const vector<int> &indices) {
    for (auto i : indices) {
      set(i);
    }
  }
  void set(const size_t bitIndex) {
    set(bitIndex, true);
  }
  void set(const size_t bitIndex, const bool value) {
    const size_t i = indexOf(bitIndex);
    const size_t mask = (1 << offsetOf(bitIndex));
    while (i >= bits.size()) bits.push_back(0);
    bits[i] &= ~mask;
    if (value) {
      bits[i] |= mask;
    }
  }
  bool get(const size_t bitIndex) const {
    const size_t i = indexOf(bitIndex);
    const size_t mask = (1 << offsetOf(bitIndex));
    return (i < bits.size() && bits[i] & mask);
  }
  bool intersects(const BitSet &other) const {
    int m = min(bits.size(), other.bits.size());
    while (--m >= 0) {
      if (bits[m] & other.bits[m] ) {
        return true;
      }
    }
    return false;
  }
  inline size_t size() {
    return bits.size() << 5;
  }
};

struct IdMap {
public:

   IdMap() {
   }
   IdMap(const vector<int> &ids) : ids(ids) {
      map = buildMap(ids);
      bitset.setFromIndices(ids);
   }

   inline bool has(const int id) const {
      return (id < map.size() && map[id] >= 0);
   }

   inline void alias(const int id, const int aliasId) {
      if (has(id)) {
         mapIndex(aliasId, map[id]);
      }
   }

   inline void add(const int id) {
      if (!has(id)) {
         const int index = ids.size();
         ids.push_back(id);
         mapIndex(id, index);
      }
   }

   inline int getIndex(const int id) const {
      return map[id];
   }

   inline int getId(const size_t index) const {
      return ids[index];
   }

   inline int getIndexSafe(const int id) const {
      return (id < map.size() ? map[id] : -1);
   }

   inline size_t size() const {
      return ids.size();
   }

   static vector<int> buildMap(const vector<int> &ids) {
      int n = 0;
      for (int i = 0; i < ids.size(); i++) {
         n = max( n, ids[i] );
      }
      vector<int> map(n + 1);
      for (int i = 0; i <= n; i++) {
         map[i] = -1;
      }
      for (int i = 0; i < ids.size(); i++) {
         map[ids[i]] = i;
      }
      return map;
   }

  private:
   vector<int> ids;
   vector<int> map;
   BitSet bitset;

   void mapIndex(const int id, const int index) {
      map.reserve( id + 1 );
      map[id] = index;
      bitset.set(id);
   }
 };

struct Vector {
   float x, y;
   Vector() : x(0.0f), y(0.0f) { }
   Vector(float x) : x(x), y(x) { }
   Vector(float x, float y) : x(x), y(y) { }
};

struct ComponentType {
  const size_t id;
  const char *name;
  const AnyMemory defaultValue;

  ComponentType(const size_t id, const char *name, const AnyMemory &defaultValue) 
    : id(id), name(name), defaultValue(defaultValue) {} 
};

class Entity;
class EntityType;

class Controller {
public:
  virtual void control(Entity *e, void *updateState) = 0;
};

class View {
public:
  virtual void draw(Entity *e, void *drawState) = 0;
};

class EntityCore;

class EntityType {
private:
  const size_t id;
  const EntityType* parent;
  int instances;
  IdMap components;
  IdMap controllers;
  size_t viewId;
  AnyMemory defaultComponents;
  vector<int> offsets;

public:

  EntityType(const size_t id, const EntityType *parent, const IdMap &components, const IdMap &controllers, const size_t viewId, const AnyMemory &defaultComponents, const vector<int> &offsets)
    : id(id), parent(parent), components(components), controllers(controllers), viewId(viewId), defaultComponents(defaultComponents), offsets(offsets)
  {
  }

  EntityType* extend(const size_t entityTypeId) {
    return new EntityType( entityTypeId, this, components, controllers, viewId, defaultComponents, offsets );
  }

  inline bool hasComponent(const size_t componentId) const {
    return components.has(componentId);
  }

  inline int getComponentOffset(const size_t componentId) const {
    return offsets[components.getIndex(componentId)];
  }

  inline int getComponentCount() {
    return components.size();
  }

  void setComponentAlias(const int componentId, const int aliasId) {
    components.alias(componentId, aliasId);
  }

  inline bool hasController(const int controllerId) {
    return controllers.has(controllerId);
  }
  inline int getControllerCount() {
    return controllers.size();
  }
  inline int getControllerIndex(const int controllerId) {
    return controllers.getIndex(controllerId);
  }
  void setControllerAlias(const int controllerId, const int aliasId) {
    controllers.alias(controllerId, aliasId);
  }
  void setView(const int view) {
    viewId = view;
  }
  void add(const int componentId);
  void addController(const int controllerId) {
    controllers.add(controllerId);
  }
  void setDefaultComponents(AnyMemory& components) {
    components.append(defaultComponents);
  }

  virtual EntityType* addCustomComponent(const int componentId);

  virtual EntityType* addCustomController(const int controllerId);

  virtual EntityType* setCustomView(const int viewId);

  virtual bool isCustom() {
    return false;
  }

  inline void addInstance() {
    instances++;
  }
  inline void removeInstance() {
    instances--;
  }
  inline int getInstances() const {
    return instances;
  }
};

class EntityTypeCustom : public EntityType {
public:
    EntityTypeCustom(const size_t id, const EntityType *parent, const IdMap &components, const IdMap &controllers, const size_t viewId, const AnyMemory &defaultComponents, const vector<int> &offsets) 
      : EntityType(id, parent, components, controllers, viewId, defaultComponents, offsets) { 
    }

    bool isCustom() {
      return true;
    }

    EntityType* addCustomComponent( const int id ) {
      add( id );
      return this;
    }

    EntityType* addCustomController( const int behaviorId ) {
      addController( behaviorId );
      return this;
    }

    EntityType* setCustomView( const int viewId ) {
      setView( viewId );
      return this;
    }
};

EntityType* EntityType::addCustomComponent(const int componentId) {
  EntityType *custom = new EntityTypeCustom( id, this, components, controllers, viewId, defaultComponents, offsets);
  custom->add(componentId);
  return custom;
}

EntityType* EntityType::addCustomController(const int controllerId) {
  EntityType *custom = new EntityTypeCustom( id, this, components, controllers, viewId, defaultComponents, offsets);
  custom->addController(controllerId);
  return custom;
}

EntityType* EntityType::setCustomView(const int viewId) {
  EntityType *custom = new EntityTypeCustom( id, this, components, controllers, viewId, defaultComponents, offsets);
  custom->setView(viewId);
  return custom;
}

class EntityCore {
public:
  
  static vector<EntityType*> getEntityTypes() {
    return entityTypes;
  }
  inline static EntityType* getEntityType(const size_t entityTypeId) {
    return entityTypes[entityTypeId];
  }
  inline static bool hasEntityType(const size_t entityTypeId) {
    return (entityTypeId < entityTypes.size());
  }
  inline static EntityType* getEntityTypeSafe(const size_t entityTypeId) {
    return hasEntityType(entityTypeId) ? getEntityType(entityTypeId) : NULL;
  }
  static size_t newEntityType(IdMap components, IdMap controllers, int viewId) {
    size_t id = entityTypes.size();

    AnyMemory defaultComponents;
    vector<int> offsets;

    for (int i = 0; i < components.size(); i++) {
      ComponentType *type = getComponent(components.getId(i));
      offsets.push_back( defaultComponents.append( type->defaultValue ) );
    }

    entityTypes.push_back(new EntityType(id, NULL, components, controllers, viewId, defaultComponents, offsets));

    return id;
  }

  static vector<ComponentType*> getComponents() {
    return componentTypes;
  }
  inline static ComponentType* getComponent(const size_t componentId) {
    return componentTypes[componentId];
  }
  inline static bool hasComponent(const size_t componentId) {
    return (componentId < componentTypes.size());
  }
  inline static ComponentType* getComponentSafe(const size_t componentId) {
    return hasComponent(componentId) ? getComponent(componentId) : NULL;
  }
  template<typename T>
  static size_t newComponent(const char *name, T defaultValue) {
    size_t id = componentTypes.size();
    componentTypes.push_back(new ComponentType(id, name, AnyMemory(defaultValue)));
    return id;
  }

  static vector<View*> getViews() {
    return views;
  }
  inline static View* getView(const size_t viewId) {
    return views[viewId];
  }
  inline static bool hasView(const size_t viewId) {
    return (viewId < views.size() && views[viewId] != NULL);
  }
  inline static View* getViewSafe(const size_t viewId) {
    return hasView(viewId) ? getView(viewId) : NULL;
  }
  static size_t addView(View *view) {
    size_t id = views.size();
    views.push_back(view);
    return id;
  }
  static size_t newView() {
    return addView(NULL);
  }
  static void setView(const size_t viewId, View* view) {
    views[viewId] = view;
  }

  static vector<Controller*> geControllers() {
    return controllers;
  }
  inline static Controller* getController(const size_t controllerId) {
    return controllers[controllerId];
  }
  inline static bool hasController(const size_t controllerId) {
    return (controllerId < controllers.size());
  }
  inline static Controller* getControllerSafe(const size_t controllerId) {
    return hasController(controllerId) ? getController(controllerId) : NULL;
  }
  static int addController(Controller *controller) {
    size_t id = controllers.size();
    controllers.push_back(controller);
    return id;
  }

private:
  static vector<ComponentType*> componentTypes;
  static vector<Controller*> controllers;
  static vector<View*> views;
  static vector<EntityType*> entityTypes;
};

// Initialize static EntityCore vectors.
vector<ComponentType*> EntityCore::componentTypes;
vector<Controller*> EntityCore::controllers;
vector<View*> EntityCore::views;
vector<EntityType*> EntityCore::entityTypes;

void EntityType::add(const int componentId) {
    components.add(componentId);
    offsets.push_back(defaultComponents.append(EntityCore::getComponent(componentId)->defaultValue));
}

class Entity {
public:
   EntityType *type;
   AnyMemory components;
   BitSet controllers;
   bool expired;
   bool visible;
   bool enabled;

   Entity(const size_t entityTypeId) 
    : expired(false), visible(true), enabled(true)
   {
      type = EntityCore::getEntityType( entityTypeId );
      type->setDefaultComponents(components);
      type->addInstance();
   }
   Entity(EntityType *entityType) 
    : expired(false), visible(true), enabled(true)
   {
      type = entityType;
      type->setDefaultComponents(components);
      type->addInstance();
   }

   ~Entity() {
      type->removeInstance();
      if (type->isCustom() && type->getInstances() == 0) {
        delete type;
      }
   }
  
   template<typename T>
   inline T* ptr(const int componentId) {
      return components.getPointer<T>( type->getComponentOffset(componentId) );
   }

   template<typename T>
   inline T& get(const size_t componentId) {
    return components.get<T>( type->getComponentOffset(componentId) );
   }

   template<typename T>
   inline T* getSafe(const size_t componentId) {
    const int offset = type->getComponentOffset(componentId);
    return (offset == -1 ? NULL : components.getSafe<T>( offset ));
   }

   template<typename T>
   inline void set(const int componentId, const T &value) {
      components.set<T>( type->getComponentOffset(componentId), value );
   }

   inline bool has(const int componentId) {
      return type->hasComponent(componentId);
   }

   bool add(const int componentId) {
      if (!type->hasComponent(componentId)) {
        EntityType *previousType = type;
        ComponentType *componentType = EntityCore::getComponent(componentId);

        type = type->addCustomComponent(componentId);

        if (type != previousType) {
          previousType->removeInstance();
          type->addInstance();
        }

        components.append(componentType->defaultValue);
      }
   }

private:
   Entity(EntityType *entityType, AnyMemory defaultComponents) 
    : expired(false), visible(true), enabled(true)
   {
      type = entityType;
      type->addInstance();
      components = defaultComponents;
   }
};

// template<typename T>
// class DynamicComponent {
// public:
//    T& (*DynamicFunction)

// };

// void stuff(char *word, int &times)
// {
//    for (int i = 0; i < times; i++) {
//       cout << word << endl;
//    }
// }

// Template Function in class

template<typename T>
class Test {
public:
   typedef T& (*TestFunction)(const Entity *entity, T &out);

   TestFunction function;
};

Vector& position(const Entity *entity, Vector &out)
{
   out.x = 10.0f;
   out.y = -2.0f;
   return out;
}

// Iterators with filtering

template<typename T>
bool FilterNone(const T* item) {
  return true;
}

template<typename T, typename F>
struct VectorIteratorPointer 
{
   vector<T> *v;
   F filter;
   const int dir, stop;
   int index, last;

   VectorIteratorPointer(vector<T> *v, F filter, int start, int dir, int stop) 
      : v(v), filter(filter), dir(dir), stop(stop) 
   {
      last = -1;
      index = findNext(start);
   }

   bool operator!=(const VectorIteratorPointer<T, F> &other) const 
   {
      return (last != other.index);
   }

   VectorIteratorPointer<T, F>& operator++() 
   {
      last = index;
      index = findNext(index + dir);
      return *this;
   }

   T& operator*() 
   {
      return v->at(index);
   }

   int findNext(int start) 
   {
      while (start != stop) {
         if (filter(&v->at(start))) {
            break;
         }
         start += dir;
      }
      return (start == stop ? -1 : start);
   }
};

template<typename T, typename F>
struct VectorIterator 
{
   vector<T> *v;
   F filter;

   VectorIterator(vector<T> *v, F filter) 
      : v(v), filter(filter) 
   {
   }

   VectorIteratorPointer<T, F> begin() 
   {
      return VectorIteratorPointer<T, F>(v, filter, 0, 1, v->size());
   }

   VectorIteratorPointer<T, F> end() 
   {
      return VectorIteratorPointer<T, F>(v, filter, v->size() - 1, -1, -1);
   }
};

// Iterator
template<typename T>
class List {
public:

   typedef bool (*ListFilter)(const T *item);

   void add(const T &item) {
      list.push_back(item);
   }
   List& operator<<(const T &item) {
      list.push_back(item);
      return *this;
   }
   List& operator+=(const T &item) {
      list.push_back(item);
      return *this;
   }

   VectorIteratorPointer<T, ListFilter> begin() {
      return VectorIteratorPointer<T, ListFilter>(&list, FilterNone<T>, 0, 1, list.size());
   }
   VectorIteratorPointer<T, ListFilter> end() {
      return VectorIteratorPointer<T, ListFilter>(&list, FilterNone<T>, list.size() - 1, -1, -1);
   }

   template<typename F>
   VectorIterator<T, F> filter(F filterFunction) {
      return VectorIterator<T, F>(&list, filterFunction);
   }

private:
   vector<T> list;
};

struct DivisbleFunctor 
{
   const int divisor, remainder;
   DivisbleFunctor(const int divisor, const int remainder)
      : divisor(divisor), remainder(remainder)
   {
   }
   bool operator()(const int *item) const
   {
      return (*item) % divisor == remainder;
   }
};

bool is_even(const int *item) {
  return (*item) % 2 == 0;
}



int main(int argc, char **argv)
{
  //***************************************************************************
  // ANYMEMORY
  //***************************************************************************
  // AnyMemory am;
  // am << 345 << 4.0f;
  // cout << am.get<int>(0) << endl;
  // cout << am.get<char>(0) << endl;
  // cout << am.get<float>(4) << endl;
  // cout << am.get<int>(4) << endl;


  //***************************************************************************
  // ITERATION
  //***************************************************************************
  // List<int> x;
  // x << 5 << 4 << 9 << 1;

  // for(auto &y : x) {
  //    cout << y << " ";
  // }
  // cout << endl;

  // DivisbleFunctor odd(2, 1);

  // for(auto &y : x.filter(odd))  {
  //    cout << y << " ";
  // }
  // cout << endl;

  // for(auto &y : x.filter(FilterNone<int>))  {
  //    cout << y << " ";
  // }
  // cout << endl;

  // List<Vector> w;
  // w << Vector(4, 0) << Vector(1, 2) << Vector(3, 1);
  // for(auto &u : w) {
  //     cout << "{" << u.x << "," << u.y << "}" << endl;
  // }
  // cout << endl;

  // List<Vector> a;
  // for(auto &u : a) {
  //     cout << "{" << u.x << "," << u.y << "}" << endl;
  // }
  // cout << endl;

  // List<Vector> b;
  // b += Vector(1, 2);
  // for(auto &u : b) {
  //     cout << "{" << u.x << "," << u.y << "}" << endl;
  // }
  // cout << endl;

  //***************************************************************************
  // BITSET
  //***************************************************************************
  // BitSet a;
  // a.set(0);
  // a.set(1);
  // a.set(2);

  // BitSet b;
  // b.set(3);

  // cout << a.intersects(b) << endl;

  // b.set(1);

  // cout << a.intersects(b) << endl;

  // a.set(451);

  // cout << a.size() << endl;

  size_t POSITION = EntityCore::newComponent<Vector>("position", Vector(1.0f, 0.0f));
  size_t VELOCITY = EntityCore::newComponent<Vector>("velocity", Vector(0.0f, 0.0f));
  size_t ROTATION = EntityCore::newComponent<float>("rotation", 0.0f);
  size_t SCALE = EntityCore::newComponent<float>("scale", 1.0f);
  size_t SPRITE_VIEW = EntityCore::newView();
  size_t SPRITE = EntityCore::newEntityType(IdMap({POSITION, VELOCITY, ROTATION}), IdMap(), SPRITE_VIEW);
  
  Entity e(SPRITE);

  Vector *pos = e.ptr<Vector>(POSITION);
  Vector *vel = e.ptr<Vector>(VELOCITY);

  cout << "pos:" << pos->x << "," << pos->y << endl;
  cout << "vel:" << vel->x << "," << vel->y << endl;
  cout << "rot:" << e.get<float>(ROTATION) << endl;

  e.set<Vector>(POSITION, Vector(5.0f, 4.2f));
  e.set<float>(ROTATION, 45.0f);

  cout << "has scale:" << e.has(SCALE) << endl;

  vel->x = -4.0f;
  vel->y = 7.6f;
  e.add(SCALE);

  cout << "pos:" << pos->x << "," << pos->y << endl;
  cout << "vel:" << vel->x << "," << vel->y << endl;
  cout << "rot:" << e.get<float>(ROTATION) << endl;
  cout << "has scale:" << e.has(SCALE) << endl;
  cout << "scl:" << e.get<float>(SCALE) << endl;

  return 0;
}

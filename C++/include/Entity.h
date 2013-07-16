#ifndef ENTITY_H
#define ENTITY_H

#include <map>

#include <EntityType.h>
#include <BitSet.h>
#include <AnyMemory.h>
#include <EntityCore.h>


class Entity 
{
private:
  
  EntityType *type = nullptr;

  AnyMemory components;

  BitSet controllers;

  bool expired = false;

  bool visible = true;
  
  bool enabled = true;

public:

  Entity(const EntityCore *m_core);

  Entity(EntityType *m_entityType);

  Entity(EntityType *m_entityType, const std::map<size_t,AnyMemory> &m_values);  

  Entity(const EntityCore *m_core, const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId);

  Entity(const EntityCore *m_core, const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId, const std::map<size_t,AnyMemory> &m_values);

  virtual ~Entity();

  template<typename T>
  inline T* ptr(const Component<T> &component) 
  {
    return components.getPointer<T>( type->getComponentOffset(component.id) );
  }

  template<typename T>
  inline T* ptrs(const Component<T> &component) 
  {
    const int offset = type->getComponentOffsetSafe(component.id);
    return (offset == -1 ? nullptr : components.getSafe<T>( offset ));
  }

  template<typename T>
  inline T& get(const Component<T> &component) 
  {
    return components.get<T>( type->getComponentOffset(component.id) );
  }

  template<typename T>
  inline T gets(const Component<T> &component, const T &defaultIfMissing)
  {
    return (has(component) ? get<T>(component) : defaultIfMissing);
  }

  template<typename T>
  inline T& set(const ComponentSet<T> &component, T& out) 
  {
    return ( out = component.set(*this, out) );
  }

  template<typename T>
  inline T& sets(const ComponentSet<T> &component, T& out) 
  {
    return ( out = (has(component.required) ? component.set(*this, out) : out) );
  }

  template<typename T>
  inline T get(const ComponentGet<T> &component) 
  {
    return component.get(*this);
  }

  template<typename T>
  inline T gets(const ComponentGet<T> &component) 
  {
    return has(component.required) ? component.get(*this) : T();
  }

  template<typename T>
  inline bool grab(const Component<T> &component, T *target)
  {
    T* ptr = ptrs<T>(component);
    bool found = (ptr != nullptr);
    if (found) {
      *target = *ptr;
    }
    return found;
  }

  template<typename T>
  bool put(const Component<T> &component, const T &value) 
  {
    bool missing = !has(component);
    if (missing) {
      add<T>(component, value); 
    } else { 
      set<T>(component, value);
    }
    return missing;
  }

  template<typename T>
  T& take(const Component<T> &component)
  {
    if (!has(component)) {
      add<T>(component, T());
    }
    return get<T>(component);
  }

  template<typename T>
  inline void set(const Component<T> &component, const T &value) 
  {
    components.set<T>( type->getComponentOffset(component.id), value );
  }

  template<typename T>
  inline bool sets(const Component<T> &component, const T &value)
  {
    const int offset = type->getComponentOffsetSafe(component.id);
    bool found = (offset != -1);
    if (found) {
      components.set<T>(size_t(offset), value);
    }
    return found;
  }

  void set(const std::map<size_t,AnyMemory> &values);

  inline bool has(const size_t componentId) 
  {
    return type->hasComponent(componentId);
  }

  template<typename T>
  inline bool has(const Component<T> &component) 
  {
    return type->hasComponent(component.id) && type->getCore() == component.core;
  }

  inline bool has(const BitSet& components)
  {
    return type->getComponents().getBitSet().contains( components );
  }

  template<typename T>
  inline T& operator()(const Component<T> &component) 
  {
    return get<T>(component);
  }

  template<typename T>
  inline T operator()(const ComponentGet<T> &component) 
  {
    return gets<T>(component);
  }

  template<typename T>
  inline bool operator()(const Component<T> &component, const T &value) 
  {
    return sets<T>(component, value);
  }

  template<class R, class... A>
  R execute(const Method<R(A...)> &method, A... arguments ) 
  {
    std::function<R(Entity&,A...)> *function = type->getMethod<R,A...>(method.id);
    if (function != nullptr && has(method.required)) {
      return function(*this, arguments...);
    }
    return R();
  }

  inline bool hasController(const size_t controllerId)
  {
    return type->hasController(controllerId);
  }

  inline bool hasControllers(const BitSet& components)
  {
    return type->getControllers().getBitSet().contains( components );
  }

  inline bool isCustom()
  {
    return type->isCustom();
  }

  template<typename T>
  bool add(const Component<T> &component)
  {
    bool missing = !has(component);

    if (missing) {
      setEntityType( type->addCustomComponent(component) );
      components.add<T>(component.typedDefaultValue);
    }

    return missing;
  }

  template<typename T>
  inline bool add(const Component<T> &component, const T &value)
  {
    bool added = add(component);

    if (added) {
      set<T>(component, value);
    }

    return added;
  }

  template<class R, class... A>
  bool addMethod(const Method<R(A...)> &method, const std::function<R(Entity&,A...)> &methodImplementation)
  {
    bool missing = type->hasMethod(method.id);

    if (missing) {
      setEntityType( type->addCustomMethod(method) );
      type->setMethod(method, methodImplementation);
    }

    return missing;
  }

  template<class R, class... A>
  inline bool addMethod(const Method<R(A...)> &method)
  {
    return addMethod(method, method.function);
  }

  template<class R, class... A>
  bool setMethod(const Method<R(A...)> &method, const std::function<R(Entity&,A...)> &methodImplementation)
  {
    bool exists = type->hasMethod(method.id);
    
    if (exists) {
      setEntityType( type->setCustomMethod(method, methodImplementation) );
    }

    return exists;
  }

  inline bool isExpired()
  {
    return expired;
  }

  inline void expire()
  {
    expired = true;
  }

  inline void setVisible(bool isVisible)
  {
    visible = isVisible;
  }

  inline void show()
  {
    visible = true;
  }

  inline void hide()
  {
    visible = false;
  }

  inline bool isVisible() 
  {
    return visible;
  }

  virtual void draw( void *drawState );

  inline void setEnabled(bool isEnabled)
  {
    enabled = isEnabled;
  }

  inline void enable()
  {
    enabled = true;
  }

  inline void disable()
  {
    enabled = false;
  }

  inline bool isEnabled()
  {
    return enabled;
  }

  virtual void update( void *updateState );
  
  inline bool isControllerEnabled( const size_t controllerId )
  {
    return controllers.get( type->getControllerIndex( controllerId ), true );
  }

  inline void setControllerEnabled( const size_t controllerId, bool enabled )
  {
    controllers.set( type->getControllerIndex( controllerId ), enabled );
  }

  inline void setControllerEnabledAll( bool enabled )
  {
    for (size_t i = 0; i < type->getControllerCount(); i++) {
      controllers.set( i, enabled );
    }
  }

  inline void enable( const size_t controllerId )
  {
    setControllerEnabled( controllerId, true );
  }

  inline void disable( const size_t controllerId )
  {
    setControllerEnabled( controllerId, false );
  }

  void addController( const size_t controllerId );

  void setView( const size_t viewId );

  inline size_t getView() const
  {
    return type->getView();
  }

  inline bool hasView() const
  {
    return ( type->getView() != View::NONE );
  }

  virtual Entity* clone(Entity* target);

  virtual Entity* clone();

  inline EntityType* getEntityType()
  {
    return type;
  }

  inline AnyMemory& getComponents()
  {
    return components;
  }

  inline BitSet& getControllerFlags()
  {
    return controllers;
  }

  bool equals(const Entity &other) const;

  int hashCode() const;

  int compareTo(const Entity &other) const;

  inline bool operator==(const Entity &b) const   { return equals( b ); }
  inline bool operator!=(const Entity &b) const   { return !equals( b ); }
  inline bool operator< (const Entity &b) const   { return compareTo( b ) < 0; }
  inline bool operator> (const Entity &b) const   { return compareTo( b ) > 0; }
  inline bool operator<=(const Entity &b) const   { return compareTo( b ) <= 0; }
  inline bool operator>=(const Entity &b) const   { return compareTo( b ) >= 0; }
  
  friend std::ostream& operator<<(std::ostream &out, Entity &e);

private:

  Entity(EntityType *entityType, AnyMemory defaultComponents);

  void setEntityType(EntityType* newType);

};

#endif
#ifndef ENTITY_H
#define ENTITY_H

#include <Common.h>
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

  Entity();

  Entity(const size_t m_entityTypeId);

  Entity(EntityType *m_entityType);

  Entity(const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId);

  virtual ~Entity();

  template<typename T>
  inline T* ptr(const size_t componentId) 
  {
    return components.getPointer<T>( type->getComponentOffset(componentId) );
  }

  template<typename T>
  inline T* ptrs(const size_t componentId) 
  {
    const int offset = type->getComponentOffsetSafe(componentId);
    return (offset == -1 ? nullptr : components.getSafe<T>( offset ));
  }

  template<typename T>
  inline T& get(const size_t componentId) 
  {
    return components.get<T>( type->getComponentOffset(componentId) );
  }

  template<typename T>
  inline T& get(const size_t componentId, T &out)
  {
    DynamicComponent<T> *dynamic = EntityCore::getDynamicComponent<T>(componentId);

    if (dynamic != nullptr && has(dynamic->required)) {
      out = dynamic->compute( this, out );
    }

    return out;
  }

  template<typename T>
  inline T gets(const size_t componentId, const T &defaultIfMissing)
  {
    return (has(componentId) ? get<T>(componentId) : defaultIfMissing);
  }

  template<typename T>
  inline bool grab(const size_t componentId, T *target)
  {
    T* ptr = ptrs<T>(componentId);
    bool found = (ptr != nullptr);
    if (found) {
      *target = *ptr;
    }
    return found;
  }

  template<typename T>
  inline void set(const size_t componentId, const T &value) 
  {
    components.set<T>( type->getComponentOffset(componentId), value );
  }

  template<typename T>
  inline bool sets(const size_t componentId, const T &value)
  {
    const int offset = type->getComponentOffsetSafe(componentId);
    bool found = (offset != -1);
    if (found) {
      components.set<T>(size_t(offset), value);
    }
    return found;
  }

  inline bool has(const size_t componentId) 
  {
    return type->hasComponent(componentId);
  }

  inline bool has(const BitSet& components)
  {
    return type->getComponents().getBitSet().contains( components );
  }

  template<typename T>
  inline bool operator()(const size_t componentId, const T &value)
  {
    return sets<T>(componentId, value);
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

  bool add(const size_t componentId);

  template<typename T>
  inline bool add(const size_t componentId, const T &value)
  {
    bool added = add(componentId);
    if (added) {
      set<T>(componentId, value);
    }
    return added;
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
    visible = false;
  }

  inline void hide()
  {
    visible = true;
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
    return controllers.get( type->getControllerIndex( controllerId ) );
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

  virtual Entity* clone();

  inline EntityType* getEntityType()
  {
    return type;
  }

  inline bool hasCustomType()
  {
    return type->isCustom();
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
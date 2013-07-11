#ifndef ENTITY_H
#define ENTITY_H

#include <Common.h>
#include <EntityType.h>
#include <BitSet.h>
#include <AnyMemory.h>

class Entity 
{
private:
  
  EntityType *type;

  AnyMemory components;

  BitSet controllers;

  bool expired;

  bool visible;
  
  bool enabled;

public:

  Entity(const size_t entityTypeId);

  Entity(EntityType *entityType);

  ~Entity();

  template<typename T>
  inline T* ptr(const size_t componentId) 
  {
    return components.getPointer<T>( type->getComponentOffset(componentId) );
  }

  template<typename T>
  inline T& get(const size_t componentId) 
  {
    return components.get<T>( type->getComponentOffset(componentId) );
  }

  template<typename T>
  inline T* getSafe(const size_t componentId) 
  {
    const int offset = type->getComponentOffset(componentId);
    return (offset == -1 ? NULL : components.getSafe<T>( offset ));
  }

  template<typename T>
  inline void set(const size_t componentId, const T &value) 
  {
    components.set<T>( type->getComponentOffset(componentId), value );
  }

  inline bool has(const size_t componentId) 
  {
    return type->hasComponent(componentId);
  }

  bool add(const size_t componentId);

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
    for (size_t i = 0; i < type->getControllerCount(); i++)
    {
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

  inline bool operator==(const Entity &a, const Entity &b)
  {
    return a.equals( b );
  }

  inline bool operator!=(const Entity &a, const Entity &b)
  {
    return !a.equals( b );
  }

  inline bool operator<(const Entity &a, const Entity &b)
  {
    return a.compareTo( b ) < 0;
  }

  inline bool operator>(const Entity &a, const Entity &b)
  {
    return a.compareTo( b ) > 0;
  }

  inline bool operator<=(const Entity &a, const Entity &b)
  {
    return a.compareTo( b ) <= 0;
  }

  inline bool operator>=(const Entity &a, const Entity &b)
  {
    return a.compareTo( b ) >= 0;
  }
  
private:

  Entity(EntityType *entityType, AnyMemory defaultComponents);

  void setEntityType(EntityType* newType);

};

#endif
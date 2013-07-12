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
    EntityType *type;
    AnyMemory components;
    BitSet controllers;

    // Initialize members in class definition when possible
    bool expired{false}, visible{true}, enabled{true};
    
  public:

    Entity();
    Entity(const size_t entityTypeId);
    Entity(EntityType *entityType);
    virtual ~Entity();

    template<typename T> inline T* ptr(size_t componentId) 
    {
      return components.getPointer<T>( type->getComponentOffset(componentId) );
    }

    template<typename T> inline T& get(size_t componentId) 
    {
      return components.get<T>( type->getComponentOffset(componentId) );
    }

    template<typename T> inline T& get(size_t componentId, T& out)
    {
      DynamicComponent<T>* dynamic{EntityCore::getDynamicComponent<T>(componentId)};

      if (dynamic != nullptr) out = dynamic->compute( this, out );    
      return out;
    }

    template<typename T> inline T& getTry(size_t componentId, T& defaultIfMissing)
    {
      return has(componentId) ? get<T>(componentId) : defaultIfMissing;
    }

    template<typename T> inline bool getAndSet(size_t componentId, T *target)
    {
      T* ptr{getSafe<T>(componentId)};
      bool set{ptr != nullptr};
      if (set) *target = *ptr;
      return set;
    }

    template<typename T> inline T* getSafe(size_t componentId) 
    {
      const int offset = type->getComponentOffset(componentId);
      return (offset == -1 ? nullptr : components.getSafe<T>( offset ));
    }

    template<typename T> inline void set(size_t componentId, const T &value) 
    {
      components.set<T>( type->getComponentOffset(componentId), value );
    }

    inline bool has(size_t componentId) { return type->hasComponent(componentId); }
    bool add(const size_t componentId);

    inline bool isExpired() { return expired; }
    inline void expire() { expired = true; }
    inline void setVisible(bool isVisible) { visible = isVisible; }
    inline void show() { visible = false; }
    inline void hide() { visible = true; }
    inline bool isVisible()  { return visible; }

    virtual void draw( void *drawState );
    
    inline void setEnabled(bool isEnabled) { enabled = isEnabled; }
    inline void enable() { enabled = true; }
    inline void disable() { enabled = false; }
    inline bool isEnabled() { return enabled; }

    virtual void update( void *updateState );
    
    inline bool isControllerEnabled(size_t controllerId )
    {
      return controllers.test( type->getControllerIndex( controllerId ) );
    }

    // Try to maintain consistent style - either use spaces or don't 
    inline void setControllerEnabled(size_t controllerId, bool enabled)
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

    inline void enable( size_t controllerId ) { setControllerEnabled( controllerId, true ); }
    inline void disable( size_t controllerId ) { setControllerEnabled( controllerId, false ); }

    void addController( const size_t controllerId );

    void setView( const size_t viewId );

    virtual Entity* clone();

    inline EntityType* getEntityType() { return type; }
    inline bool hasCustomType() { return type->isCustom(); }
    inline AnyMemory& getComponents() { return components; }
    inline BitSet& getControllerFlags() { return controllers; }

    bool equals(const Entity &other) const;
    int hashCode() const;
    int compareTo(const Entity &other) const;

    inline bool operator==(const Entity &b) const { return equals( b ); }
    inline bool operator!=(const Entity &b) const { return !equals( b ); }
    inline bool operator<(const Entity &b) const { return compareTo( b ) < 0; }
    inline bool operator>(const Entity &b) const { return compareTo( b ) > 0; }
    inline bool operator<=(const Entity &b) const { return compareTo( b ) <= 0; }
    inline bool operator>=(const Entity &b) const { return compareTo( b ) >= 0; }
  
  private:
    Entity(EntityType *entityType, AnyMemory defaultComponents);
    void setEntityType(EntityType* newType);
};

#endif
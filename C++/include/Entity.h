#ifndef ENTITY_H
#define ENTITY_H

#include <Common.h>
#include <EntityType.h>
#include <BitSet.h>
#include <AnyMemory.h>

class Entity 
{
public:

  EntityType *type;
  AnyMemory components;
  BitSet controllers;
  bool expired;
  bool visible;
  bool enabled;

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

private:

  Entity(EntityType *entityType, AnyMemory defaultComponents);

  void setEntityType(EntityType* newType);

};

#endif
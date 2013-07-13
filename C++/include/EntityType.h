#ifndef ENTITYTYPE_H
#define ENTITYTYPE_H

#include <Common.h>
#include <AnyMemory.h>
#include <IdMap.h>

class EntityType 
{
private:

  const size_t id;

  const EntityType* parent = nullptr;

  int instances = 0;

  IdMap components;

  IdMap controllers;

  size_t viewId;

  AnyMemory defaultComponents;

public:

  static const size_t CUSTOM = 0xFFFFFFFF;

  EntityType(const size_t m_id, const EntityType *m_parent, const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId, const AnyMemory &m_defaultComponents);

  virtual ~EntityType();

  EntityType* extend(const size_t entityTypeId);

  inline bool hasComponent(const size_t componentId) const 
  {
    return components.has(componentId);
  }

  inline size_t getComponentOffset(const size_t componentId) const 
  {
    return components.getIndex(componentId);
  }

  inline size_t getComponentOffsetSafe(const size_t componentId) const 
  {
    return components.getIndexSafe(componentId);
  }

  inline size_t getComponentCount() 
  {
    return components.size();
  }

  void setComponentAlias(const size_t componentId, const size_t aliasId);

  inline bool hasController(const size_t controllerId) 
  {
    return controllers.has(controllerId);
  }

  inline size_t getControllerCount() 
  {
    return controllers.size();
  }

  inline int getControllerIndex(const size_t controllerId) 
  {
    return controllers.getIndex(controllerId);
  }

  void setControllerAlias(const size_t controllerId, const size_t aliasId);

  void setView(const size_t view);

  inline size_t getView()
  {
    return viewId;
  }

  bool add(const size_t componentId);

  template<typename T>
  inline bool add(const size_t componentId, const T &value )
  {
    bool added = add(componentId);

    if ( added )
    {
      added = setDefaultValue( componentId, value );
    }

    return added;
  }

  template<typename T>
  inline bool setDefaultValue(const size_t componentId, const T &value)
  {
    bool exists = hasComponent(componentId);

    if (exists)
    {
      defaultComponents.set( getComponentOffset(componentId), value );
    }

    return exists;
  }

  bool addController(const size_t controllerId);

  void setEntityDefaultComponents(AnyMemory& components);

  virtual EntityType* addCustomComponent(const size_t componentId);

  virtual EntityType* addCustomController(const size_t controllerId);

  virtual EntityType* setCustomView(const size_t viewId);

  virtual bool isCustom();

  inline void addInstance() 
  { 
    instances++;
  }

  inline void removeInstance() 
  {
    instances--;

    if (isCustom() && instances == 0) 
    {
      delete this;
    }
  }

  inline int getInstances() const 
  {
    return instances;
  }

  inline IdMap& getControllers()
  {
    return controllers;
  }

  inline IdMap& getComponents()
  {
    return components;
  }

  inline size_t getId() const
  {
    return id;
  }

};

#endif
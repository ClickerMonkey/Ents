#include <EntityType.h>
#include <EntityTypeCustom.h>
#include <EntityCore.h>

using namespace std;

EntityType::EntityType(const size_t m_id, const EntityType *m_parent, const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId, const AnyMemory &m_defaultComponents)
  : id(m_id), parent(m_parent), components(m_components), controllers(m_controllers), viewId(m_viewId), defaultComponents(m_defaultComponents)
{
}

EntityType::~EntityType() 
{
}

EntityType* EntityType::extend(const size_t entityTypeId) 
{
  return new EntityType( entityTypeId, this, components, controllers, viewId, defaultComponents );
}

void EntityType::setComponentAlias(const size_t componentId, const size_t aliasId) 
{
  components.alias(componentId, aliasId);
}

void EntityType::setControllerAlias(const size_t controllerId, const size_t aliasId) 
{
  controllers.alias(controllerId, aliasId);
}

void EntityType::setView(const size_t view) 
{
  viewId = view;
}

bool EntityType::add(const size_t componentId)
{
  bool missing = !hasComponent(componentId);

  if (missing)
  {
    ComponentType* componentType = EntityCore::getComponent(componentId);
    size_t offset = defaultComponents.append(componentType->defaultValue);
    components.add(componentId, offset);
  }

  return missing;
}

bool EntityType::addController(const size_t controllerId) 
{
  bool missing = !hasController(controllerId);

  if (missing)
  {
    controllers.add(controllerId, controllers.size());  
  }

  return missing;
}

void EntityType::setEntityDefaultComponents(AnyMemory& components) 
{
  components.append(defaultComponents);
}

EntityType* EntityType::addCustomComponent(const size_t componentId)
{
  EntityType *custom = new EntityTypeCustom( CUSTOM, this, components, controllers, viewId, defaultComponents);
  custom->add(componentId);
  return custom;
}

EntityType* EntityType::addCustomController(const size_t controllerId)
{
  EntityType *custom = new EntityTypeCustom( CUSTOM, this, components, controllers, viewId, defaultComponents);
  custom->addController(controllerId);
  return custom;
}

EntityType* EntityType::setCustomView(const size_t viewId)
{
  EntityType *custom = new EntityTypeCustom( CUSTOM, this, components, controllers, viewId, defaultComponents);
  custom->setView(viewId);
  return custom;
}

bool EntityType::isCustom() 
{
  return false;
}
#include <Entity.h>
#include <EntityCore.h>

Entity::Entity(const size_t entityTypeId) 
  : expired(false), visible(true), enabled(true)
{
  type = EntityCore::getEntityType( entityTypeId );
  type->setDefaultComponents(components);
  type->addInstance();
}

Entity::Entity(EntityType *entityType) 
  : expired(false), visible(true), enabled(true)
{
  type = entityType;
  type->setDefaultComponents(components);
  type->addInstance();
}

Entity::~Entity() 
{
  type->removeInstance();
}

bool Entity::add(const size_t componentId) 
{
  bool addable = !type->hasComponent(componentId);

  if (addable) 
  {
    setEntityType( type->addCustomComponent(componentId) );
    components.append(EntityCore::getComponent(componentId)->defaultValue);
  }

  return addable;
}

Entity::Entity(EntityType *entityType, AnyMemory defaultComponents) 
  : expired(false), visible(true), enabled(true)
{
  type = entityType;
  type->addInstance();
  components = defaultComponents;
}

void Entity::setEntityType(EntityType* newType)
{
  if (type != newType)
  {
    type->removeInstance();
    newType->addInstance();
  }

  type = newType;
}
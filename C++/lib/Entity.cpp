#include <Entity.h>
#include <EntityCore.h>
#include <EntityTypeCustom.h>

Entity::Entity()
{
  setEntityType(new EntityTypeCustom(EntityType::CUSTOM, nullptr, {}, {}, View::NONE, AnyMemory(), {}));
}

Entity::Entity(size_t entityTypeId) 
{
  type = EntityCore::getEntityType( entityTypeId );
  type->setDefaultComponents(components);
  type->addInstance();
}

Entity::Entity(EntityType* entityType) 
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
{
  type = entityType;
  type->addInstance();
  components = defaultComponents;
}

void Entity::setEntityType(EntityType* newType)
{
  if (type != newType)
  {
    if (type != nullptr) type->removeInstance();  
    newType->addInstance();
  }

  type = newType;
}

void Entity::draw( void *drawState )
{
  // Try to avoid nesting
  if ( !visible ) return;
  
  View* view = EntityCore::getViewSafe( type->getView() );
  if (view != nullptr) view->draw( this, drawState );
}

void Entity::update( void *updateState )
{
  if ( !enabled ) return;

  vector<size_t> ids = type->getControllers().getIds();

  for (size_t i = 0; i < ids.size(); i++)
  {
    if (!controllers.test(i)) continue;
    Controller* controller = EntityCore::getController(ids[i]);
    controller->control( this, updateState );
  }
  
}

void Entity::addController( size_t controllerId )
{
  if ( type->hasController(controllerId) ) return;
  
  setEntityType( type->addCustomController(controllerId) );
  
}

void Entity::setView( size_t viewId )
{
  if ( !type->getView() != viewId ) return;
  
  setEntityType( type->setCustomView(viewId) );
  
}

Entity* Entity::clone()
{
  return new Entity( type, components );
}

bool Entity::equals(const Entity &other) const
{
  return (type == other.type && components == other.components);
}

int Entity::hashCode() const
{
  const int prime = 31;
  int hash = 0;
  hash = prime * hash + type->getId();
  hash = prime * hash + components.hashCode();
  return hash;
}

int Entity::compareTo(const Entity &other) const
{
  int d = (type->getId() - other.type->getId());

  return ( d != 0 ? d : components.compareTo( other.components ) );
}
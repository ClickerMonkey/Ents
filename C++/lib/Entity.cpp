#include <Entity.h>
#include <EntityCore.h>

Entity::Entity()
  : expired(false), visible(true), enabled(true)
{
  setEntityType(new EntityTypeCustom(EntityType::CUSTOM, {}, {}, View::NONE, AnyMemory(), {}}));
}

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
    if (type != NULL)
    {
      type->removeInstance();  
    }
    newType->addInstance();
  }

  type = newType;
}

void Entity::draw( void *drawState )
{
  if ( visible )
  {
     View* view = EntityCore::getViewSafe( type->getView() );

     if (view != NULL)
     {
        view->draw( this, drawState );
     }
  }
}

void Entity::update( void *updateState )
{
  if ( enabled )
  {
    vector<size_t> ids = type->getControllers().getIds();

    for (size_t i = 0; i < ids.size(); i++)
    {
      if (controllers.get(i))
      {
         Controller* controller = EntityCore::getController(ids[i]);

         controller->control( this, updateState );
      }
    }
  }
}

void Entity::addController( const size_t controllerId )
{
  if ( !type->hasController(controllerId) )
  {
    setEntityType( type->addCustomController(controllerId) );
  }
}

void Entity::setView( const size_t viewId )
{
  if ( type->getView() != viewId )
  {
    setEntityType( type->setCustomView(viewId) );
  }
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
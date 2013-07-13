#include <Entity.h>
#include <EntityCore.h>
#include <EntityTypeCustom.h>

using namespace std;

Entity::Entity()
{
  setEntityType( new EntityTypeCustom() );
}

Entity::Entity(const size_t m_entityTypeId)
{
  setEntityType( EntityCore::getEntityType( m_entityTypeId ) );
  type->setEntityDefaultComponents(components);
}

Entity::Entity(EntityType *m_entityType)
{
  setEntityType( m_entityType );
  type->setEntityDefaultComponents(components);
}

Entity::Entity(EntityType *m_entityType, AnyMemory m_defaultComponents)
{
  setEntityType( m_entityType );
  components = m_defaultComponents;
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

void Entity::setEntityType(EntityType* newType)
{
  if (type != newType)
  {
    if (type != nullptr) 
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

     if (view != nullptr)
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
      if (controllers.get(i, true))
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
#include <Entity.h>
#include <EntityCore.h>
#include <EntityTypeCustom.h>

using namespace std;

Entity::Entity()
{
  setEntityType( new EntityTypeCustom() );
}

Entity::Entity(EntityType *m_entityType)
{
  setEntityType( m_entityType );
  type->setEntityDefaultComponents(components);
}

Entity::Entity(EntityType *m_entityType, const map<size_t,AnyMemory> &m_values)
  : Entity( m_entityType )
{
  set(m_values);
}

Entity::Entity(const size_t m_entityTypeId)
  : Entity(EntityCore::getEntityType( m_entityTypeId ))
{
}

Entity::Entity(const size_t m_entityTypeId, const map<size_t,AnyMemory> &m_values)
  : Entity(m_entityTypeId)
{
  set(m_values);
}

Entity::Entity(const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId)
  : Entity(new EntityTypeCustom(m_components, m_controllers, m_viewId))
{
}

Entity::Entity(const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId, const map<size_t,AnyMemory> &m_values)
  : Entity(m_components, m_controllers, m_viewId)
{
  set(m_values);
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

void Entity::set(const map<size_t,AnyMemory> &values)
{
  for (auto const &entry : values) {
    size_t componentId = entry.first;
    int offset = type->getComponentOffsetSafe(componentId);
    if (offset != -1) {
      components.set(size_t(offset), entry.second);
    }
  }
}

bool Entity::add(const size_t componentId) 
{
  bool missing = !type->hasComponent(componentId);

  if (missing) {
    setEntityType( type->addCustomComponent(componentId) );
    components.append(EntityCore::getComponent(componentId)->defaultValue);
  }

  return missing;
}

void Entity::setEntityType(EntityType* newType)
{
  if (type != newType) {
    if (type != nullptr) {
      type->removeInstance();
    }
    (type = newType)->addInstance();
  }
}

void Entity::draw( void *drawState )
{
  if (visible) {
     View* view = EntityCore::getViewSafe( type->getView() );

     if (view != nullptr && has(view->required)) {
        view->draw( this, drawState );
     }
  }
}

void Entity::update( void *updateState )
{
  if (enabled) {
    vector<size_t> ids = type->getControllers().getIds();

    for (size_t i = 0; i < ids.size(); i++) {
      if (controllers.get(i, true)) {
         Controller* controller = EntityCore::getController(ids[i]);
         if (controller != nullptr && has(controller->required)) {
            controller->control( this, updateState );
         }
      }
    }
  }
}

void Entity::addController( const size_t controllerId )
{
  if (!type->hasController(controllerId)) {
    setEntityType( type->addCustomController(controllerId) );
  }
}

void Entity::setView( const size_t viewId )
{
  if (type->getView() != viewId) {
    setEntityType( type->setCustomView(viewId) );
  }
}

Entity* Entity::clone(Entity* target)
{
  target->expired = expired;
  target->visible = visible;
  target->enabled = enabled;
  target->controllers = controllers;
  target->components = components;
  target->setEntityType( type );
  
  return target;
}

Entity* Entity::clone()
{
  Entity* clone = new Entity( type, components );
  clone->expired = expired;
  clone->enabled = enabled;
  clone->visible = visible;
  clone->controllers = controllers;
  return clone;
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

std::ostream& operator<<(std::ostream &out, Entity &e)
{
  const size_t type = e.type->getId();

  out << "{";

  if (type == EntityType::CUSTOM) {
    out << "type:custom, "; 
  } else {
    out << "type:" << type << ", ";
  }
  
  out << "expired:" << (e.expired ? "true" : "false") << ", ";
  out << "visible:" << (e.visible ? "true" : "false") << ", ";
  out << "enabled:" << (e.enabled ? "true" : "false") << ", ";

  IdMap components = e.type->getComponents();

  for (size_t i = 0; i < components.size(); i++) {
    size_t componentId = components.getId(i);
    size_t offset = components.getIndex(componentId);

    ComponentType *componentType = EntityCore::getComponent(componentId);
    AnyMemory value = e.components.sub(offset, componentType->defaultValue.getSize());

    out << componentType->name << ":" << value << ", ";
  }

  out << "controllers:{";

  IdMap controllers = e.type->getControllers();

  for (size_t i = 0; i < controllers.size(); i++) {
    if (i > 0) {
      out << ",";
    }
    out << controllers.getId(i);
  }

  out << "}}";

  return out;
}
#include <EntityCore.h>

using namespace std;

EntityType* EntityCore::newEntityType(IdMap components, IdMap controllers, const size_t viewId) 
{
  AnyMemory defaultComponents;

  for (size_t i = 0; i < components.size(); i++) {
    size_t componentId = components.getId(i);
    ComponentBase *componentBase = getComponent(componentId);
    components.setIndex( componentId, defaultComponents.append( componentBase->defaultValue ) );
  }

  EntityType* type = new EntityType(this, entityTypes.size(), nullptr, components, controllers, viewId, defaultComponents);

  entityTypes.push_back(type);

  return type;
}

EntityType* EntityCore::newEntityTypeExtension(const size_t entityTypeId, IdMap components, IdMap controllers, const size_t viewId)
{
  EntityType *type = getEntityType(entityTypeId)->extend( entityTypes.size() );

  for (size_t i = 0; i < components.size(); i++) {
    type->add(components.getId(i));
  }

  for (size_t i = 0; i < controllers.size(); i++) {
    type->addController(controllers.getId(i));
  }

  if (viewId != View::NONE) {
    type->setView(viewId);
  }

  entityTypes.push_back(type);

  return type;
}
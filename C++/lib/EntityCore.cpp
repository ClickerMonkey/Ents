#include <EntityCore.h>

size_t EntityCore::newEntityType(IdMap components, IdMap controllers, size_t viewId) 
{
  size_t id = entityTypes.size();

  AnyMemory defaultComponents;
  vector<size_t> offsets;

  for (size_t i = 0; i < components.size(); i++) 
  {
    ComponentType *type = getComponent(components.getId(i));
    offsets.push_back( defaultComponents.append( type->defaultValue ) );
  }

  entityTypes.push_back(new EntityType(id, NULL, components, controllers, viewId, defaultComponents, offsets));

  return id;
}

size_t EntityCore::addView(View *view) 
{
  size_t id = views.size();
  views.push_back(view);
  return id;
}

size_t EntityCore::addController(Controller *controller) 
{
  size_t id = controllers.size();
  controllers.push_back(controller);
  return id;
}

vector<ComponentType*> EntityCore::componentTypes;

vector<Controller*> EntityCore::controllers;

vector<View*> EntityCore::views;

vector<EntityType*> EntityCore::entityTypes;
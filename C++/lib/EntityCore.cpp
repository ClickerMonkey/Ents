#include <EntityCore.h>

using namespace std;

vector<EntityType*>& getEntityTypes()
{
  static vector<EntityType*> entityTypes;
  return entityTypes;
}

vector<View*>& getViews()
{
  static vector<View*> views;
  return views;
}

vector<Controller*>& getControllers()
{
  static vector<Controller*> controllers;
  return controllers;
}

vector<ComponentType*>& getComponents()
{
  static vector<ComponentType*> components;
  return components;
}

size_t EntityCore::newEntityType(IdMap components, IdMap controllers, size_t viewId) 
{
  size_t id = getEntityTypes().size();

  AnyMemory defaultComponents;

  for (size_t i = 0; i < components.size(); i++) {
    size_t componentId = components.getId(i);
    ComponentType *componentType = getComponent(componentId);
    components.setIndex( componentId, defaultComponents.append( componentType->defaultValue ) );
  }

  getEntityTypes().push_back(new EntityType(id, nullptr, components, controllers, viewId, defaultComponents));

  return id;
}

size_t EntityCore::addView(View *view) 
{
  size_t id = getViews().size();
  getViews().push_back(view);
  return id;
}

size_t EntityCore::addController(Controller *controller) 
{
  size_t id = getControllers().size();
  getControllers().push_back(controller);
  return id;
}
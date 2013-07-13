#ifndef ENTITYCORE_H
#define ENTITYCORE_H

#include <Common.h>
#include <EntityType.h>
#include <View.h>
#include <Controller.h>
#include <ComponentType.h>
#include <DynamicComponentType.h>
#include <IdMap.h>

std::vector<EntityType*>&    getEntityTypes();
std::vector<View*>&          getViews();
std::vector<Controller*>&    getControllers();
std::vector<ComponentType*>& getComponents();

class EntityCore 
{
public:
  
  inline static EntityType* getEntityType(const size_t entityTypeId) 
  {
    return getEntityTypes().at(entityTypeId);
  }

  inline static bool hasEntityType(const size_t entityTypeId) 
  {
    return (entityTypeId < getEntityTypes().size());
  }

  inline static EntityType* getEntityTypeSafe(const size_t entityTypeId) 
  {
    return hasEntityType(entityTypeId) ? getEntityType(entityTypeId) : nullptr;
  }

  static size_t newEntityType(IdMap components, IdMap controllers, size_t viewId);
  
  inline static ComponentType* getComponent(const size_t componentId) 
  {
    return getComponents().at(componentId);
  }

  inline static bool hasComponent(const size_t componentId) 
  {
    return (componentId < getComponents().size());
  }

  inline static ComponentType* getComponentSafe(const size_t componentId) 
  {
    return hasComponent(componentId) ? getComponent(componentId) : nullptr;
  }

  template<typename T>
  static size_t newComponent(const std::string name, T defaultValue) 
  {
    size_t id = getComponents().size();
    getComponents().push_back(new ComponentType(id, name, AnyMemory(defaultValue)));
    return id;
  }

  template<typename T>
  static size_t newDynamicComponent(const std::string name, DynamicComponent<T> *dynamicComponent)
  {
    size_t id = getComponents().size();
    getComponents().push_back(new DynamicComponentType<T>(id, name, dynamicComponent));
    return id;
  }

  template<typename T>
  static DynamicComponent<T>* getDynamicComponent(const size_t componentId)
  {
    if (!hasComponent(componentId)) {
      return nullptr;
    }

    ComponentType *componentType = getComponent(componentId);

    DynamicComponentType<T> *dynamicComponentType = dynamic_cast<DynamicComponentType<T>*>( componentType );

    if (dynamicComponentType == nullptr) {
      return nullptr;
    }

    return dynamicComponentType->dynamicComponent;
  }

  inline static View* getView(const size_t viewId) 
  {
    return getViews().at(viewId);
  }

  inline static bool hasView(const size_t viewId) 
  {
    return (viewId < getViews().size() && getViews().at(viewId) != nullptr);
  }

  inline static View* getViewSafe(const size_t viewId) 
  {
    return hasView(viewId) ? getView(viewId) : nullptr;
  }

  static size_t addView(View *view);

  static size_t newView() 
  {
    return addView(nullptr);
  }

  static void setView(const size_t viewId, View* view) 
  {
    getViews()[viewId] = view;
  }

  inline static Controller* getController(const size_t controllerId) 
  {
    return getControllers().at(controllerId);
  }

  inline static bool hasController(const size_t controllerId) 
  {
    return (controllerId < getControllers().size());
  }

  inline static Controller* getControllerSafe(const size_t controllerId) 
  {
    return hasController(controllerId) ? getController(controllerId) : nullptr;
  }

  static size_t addController(Controller *controller);
 
};

#endif
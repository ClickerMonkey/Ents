#ifndef ENTITYCORE_H
#define ENTITYCORE_H

#include <EntityType.h>
#include <View.h>
#include <Controller.h>
#include <Component.h>
#include <Method.h>
#include <IdMap.h>

class EntityCore 
{
protected:

  std::vector<EntityType*> entityTypes;
  std::vector<View*> views;
  std::vector<Controller*> controllers;
  std::vector<ComponentBase*> components;
  std::vector<MethodBase*> methods;

public:

  inline EntityType* getEntityType(const size_t entityTypeId) const
  {
    return entityTypes[entityTypeId];
  }

  inline bool hasEntityType(const size_t entityTypeId) const
  {
    return (entityTypeId < entityTypes.size());
  }

  inline EntityType* getEntityTypeSafe(const size_t entityTypeId) const
  {
    return hasEntityType(entityTypeId) ? getEntityType(entityTypeId) : nullptr;
  }

  EntityType* newEntityType(IdMap components, IdMap controllers, const size_t viewId);
  
  inline EntityType* newEntityTypeExtension(const size_t entityTypeId, IdMap components, IdMap controllers)
  {
    return newEntityTypeExtension(entityTypeId, components, controllers, View::NONE);
  }

  inline EntityType* newEntityTypeExtension(const size_t entityTypeId, IdMap components)
  {
    return newEntityTypeExtension(entityTypeId, components, {}, View::NONE);
  }

  EntityType* newEntityTypeExtension(const size_t entityTypeId, IdMap components, IdMap controllers, const size_t viewId);



  inline ComponentBase* getComponent(const size_t componentId) const
  {
    return components[componentId];
  }

  template<typename T>
  inline T* getComponentCast(const size_t componentId) const
  {
    return dynamic_cast<T*>(components[componentId]);
  }

  inline bool hasComponent(const size_t componentId) const
  {
    return (componentId < components.size());
  }

  inline ComponentBase* getComponentSafe(const size_t componentId) const
  {
    return hasComponent(componentId) ? getComponent(componentId) : nullptr;
  }

  template<typename T>
  Component<T>& newComponent(const std::string &name, const T &defaultValue) 
  {
    Component<T> *c = new Component<T>(this, components.size(), name, defaultValue);
    components.push_back(c);
    return *c;
  }

  template<typename T>
  ComponentSet<T>& newComponentSet(const std::string &name, const BitSet &required, const std::function<T&(Entity&, T&)> &set) 
  {
    ComponentSet<T> *c = new ComponentSet<T>(this, components.size(), name, required, set);
    components.push_back(c);
    return *c;
  }

  template<typename T>
  ComponentGet<T>& newComponentGet(const std::string &name, const BitSet &required, const std::function<T(Entity&)> &get) 
  {
    ComponentGet<T> *c = new ComponentGet<T>(this, components.size(), name, required, get);
    components.push_back(c);
    return *c;
  }



  inline MethodBase* getMethod(const size_t methodId) const
  {
    return methods[methodId];
  }

  template<class R, class... A>
  inline Method<R(A...)>* getMethodCast(const size_t methodId) const
  {
    return dynamic_cast<Method<R(A...)>*>(methods[methodId]);
  }

  inline bool hasMethod(const size_t methodId) const
  {
    return (methodId < methods.size());
  }

  inline MethodBase* getMethodSafe(const size_t methodId) const
  {
    return hasMethod(methodId) ? getMethod(methodId) : nullptr;
  }

  template<class R, class... A>
  Method<R(A...)>& newMethod(const std::string &name, const BitSet &required, const std::function<R(Entity&,A...)> &defaultFunction)
  {
    Method<R(A...)> *m = new Method<R(A...)>(this, methods.size(), name, required, defaultFunction);
    methods.push_back(m);
    return *m;
  }



  inline View* getView(const size_t viewId) const
  {
    return views[viewId];
  }

  inline bool hasView(const size_t viewId) const
  {
    return (viewId < views.size() && views[viewId] != nullptr);
  }

  inline View* getViewSafe(const size_t viewId) const
  {
    return hasView(viewId) ? getView(viewId) : nullptr;
  }

  size_t addView(const BitSet &required, const std::function<void(Entity&,void*)> &draw)
  {
    size_t id = views.size();
    views.push_back(new View(this, id, required, draw));
    return id;
  }

  inline size_t newView() 
  {
    size_t id = views.size();
    views.push_back(nullptr);
    return id;
  }

  bool setView(const size_t viewId, const BitSet &required, const std::function<void(Entity&,void*)> &draw)
  {
    if (viewId >= views.size()) {
      return false;
    }

    View *v = views[viewId];

    if (v != nullptr) {
      delete v;
    }

    views[viewId] = new View(this, viewId, required, draw);

    return true;
  }



  inline Controller* getController(const size_t controllerId) const
  {
    return controllers[controllerId];
  }

  inline bool hasController(const size_t controllerId) const
  {
    return (controllerId < controllers.size());
  }

  inline Controller* getControllerSafe(const size_t controllerId) const
  {
    return hasController(controllerId) ? getController(controllerId) : nullptr;
  }

  size_t addController(const BitSet &required, const std::function<void(Entity&,void*)> &control)
  {
    size_t id = controllers.size();
    controllers.push_back(new Controller(this, id, required, control));
    return id;
  }
  
  size_t newController()
  {
    size_t id = controllers.size();
    controllers.push_back(nullptr);
    return id;
  }

  bool setController(const size_t controllerId, const BitSet &required, const std::function<void(Entity&,void*)> &control)
  { 
    if (controllerId >= controllers.size()) {
      return false;
    }

    Controller *c = controllers[controllerId];

    if (c != nullptr) {
      delete c;
    }

    controllers[controllerId] = new Controller(this, controllerId, required, control);

    return true;
  }



};

#endif
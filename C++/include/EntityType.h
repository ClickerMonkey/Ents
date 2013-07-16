#ifndef ENTITYTYPE_H
#define ENTITYTYPE_H

#include <AnyMemory.h>
#include <IdMap.h>
#include <Component.h>
#include <Method.h>

class EntityType 
{
protected:

  const EntityCore *core;

  const size_t id;

  const EntityType *parent = nullptr;

  int instances = 0;

  IdMap components;

  IdMap controllers;

  IdMap methods;

  size_t viewId;

  AnyMemory defaultComponents;

  std::vector<void*> methodMap;

public:

  static const size_t CUSTOM = 0xFFFFFFFF;

  EntityType(const EntityCore *m_core, const size_t m_id, const EntityType *m_parent, const IdMap &m_components, const IdMap &m_controllers, const size_t m_viewId, const AnyMemory &m_defaultComponents);

  virtual ~EntityType();

  EntityType* extend(const size_t entityTypeId);

  inline bool hasComponent(const size_t componentId) const 
  {
    return components.has(componentId);
  }

  inline size_t getComponentOffset(const size_t componentId) const 
  {
    return components.getIndex(componentId);
  }

  inline int getComponentOffsetSafe(const size_t componentId) const 
  {
    return components.getIndexSafe(componentId);
  }

  inline size_t getComponentCount() const
  {
    return components.size();
  }

  void setComponentAlias(const size_t componentId, const size_t aliasId);

  inline bool hasController(const size_t controllerId) 
  {
    return controllers.has(controllerId);
  }

  inline size_t getControllerCount() 
  {
    return controllers.size();
  }

  inline int getControllerIndex(const size_t controllerId) 
  {
    return controllers.getIndex(controllerId);
  }

  void setControllerAlias(const size_t controllerId, const size_t aliasId);

  inline size_t getMethodCount() const
  {
    return methods.size();
  }

  inline bool hasMethod(const size_t methodId) const
  {
    return methods.has(methodId);
  }

  inline int getMethodIndex(const size_t methodId) const
  {
    return methods.getIndex( methodId );
  }

  inline int getMethodIndexSafe(const size_t methodId) const
  {
    return methods.getIndexSafe( methodId );
  }

  template<class R, class... A>
  bool addMethod(const Method<R(A...)> &method)
  {
    bool missing = !hasMethod(method.id);
    if (missing) {
        methodMap.push_back(&method.function);
        methods.add(method.id);
    }
    return missing;
  }

  template<class R, class... A>
  bool setMethod(const Method<R(A...)> &method, const std::function<R(Entity&,A...)> &methodImplementation)
  {
    int index = methods.getIndexSafe(method.id);
    bool exists = (index != -1);
    if (exists) {
      methodMap[index] = &methodImplementation;
    }
    return exists;
  }

  template<class R, class... A>
  std::function<R(Entity&,A...)>* getMethod(const size_t methodId) const
  {
    if (!hasMethod(methodId)) {
      return nullptr;
    }

    return dynamic_cast<std::function<R(Entity&,A...)>*>(methodMap[methodId]);
  }

  void setView(const size_t view);

  inline size_t getView() const
  {
    return viewId;
  }

  template<typename T>
  bool add(const Component<T> &component)
  {
    bool missing = !hasComponent(component.id);
    if (missing) {
      size_t offset = defaultComponents.add<T>(component.typedDefaultValue);
      components.add(component.id, offset);
    }
    return missing;
  }

  bool add(const size_t componentId);

  template<typename T>
  inline bool add(const Component<T> &component, const T &value )
  {
    bool added = add(component);
    if (added) {
      added = setDefaultValue( component, value );
    }
    return added;
  }

  template<typename T>
  inline bool setDefaultValue(const Component<T> &component, const T &value)
  {
    bool exists = hasComponent(component.id);
    if (exists) {
      defaultComponents.set( getComponentOffset(component.id), value );
    }
    return exists;
  }

  bool addController(const size_t controllerId);

  void setEntityDefaultComponents(AnyMemory& components);

  template<typename T>
  EntityType* addCustomComponent(const Component<T> &component) 
  {
    EntityType* target = this;

    if (!isCustom()) {
      target = getCustom();
    }

    target->add(component);

    return target;
  }

  template<class R, class... A>
  EntityType* addCustomMethod(const Method<R(A...)> &method)
  {
    EntityType* target = this;

    if (!isCustom()) {
      target = getCustom();
    }

    target->addMethod(method);

    return target;
  }

  template<class R, class... A>
  EntityType* setCustomMethod(const Method<R(A...)> &method, const std::function<R(Entity&,A...)> &methodImplementation)
  {
    EntityType* target = this;

    if (!isCustom()) {
      target = getCustom();
    }

    target->setMethod(method, methodImplementation);

    return target;
  }

  EntityType* addCustomController(const size_t controllerId);

  EntityType* setCustomView(const size_t viewId);

  inline bool isCustom() 
  {
    return ( id == CUSTOM );
  }

  inline void addInstance() 
  { 
    instances++;
  }

  inline void removeInstance() 
  {
    instances--;

    if (isCustom() && instances == 0) {
      delete this;
    }
  }

  inline int getInstances() const 
  {
    return instances;
  }

  inline IdMap& getControllers()
  {
    return controllers;
  }

  inline IdMap& getComponents()
  {
    return components;
  }

  inline AnyMemory& getDefaultComponents()
  {
    return defaultComponents;
  }

  inline const EntityType* getParent() const
  {
    return parent;
  }

  inline const EntityCore* getCore() const
  {
    return core;
  }

  inline size_t getId() const
  {
    return id;
  }

protected:

  EntityType* getCustom();

};

#endif
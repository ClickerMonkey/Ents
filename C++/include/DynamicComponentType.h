#ifndef DYNAMICCOMPONENT_H
#define DYNAMICCOMPONENT_H

#include <ComponentType.h>

class Entity;

// Use structs if there are no private members
template<typename T> struct DynamicComponent 
{
   virtual T& compute(Entity *e, T &out) = 0;
};

template<typename T> class DynamicComponentFunction : public DynamicComponent<T>
{
   private:
      typedef T& (*DynamicComponentFunctionPointer)(Entity *e, T &out);
      DynamicComponentFunctionPointer function;

   public:
      DynamicComponentFunction(DynamicComponentFunctionPointer func) : function(func) { }
      T& compute(Entity* e, T& out) { return function(e, out); }
};

template<typename T> struct DynamicComponentType : public ComponentType 
{
   DynamicComponent<T> *dynamicComponent;

   DynamicComponentType(const size_t id, const char *name, DynamicComponent<T> *dynamicComponent) 
    : ComponentType(id, name, AnyMemory()), dynamicComponent(dynamicComponent) { }

};

#endif
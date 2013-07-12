#ifndef DYNAMICCOMPONENT_H
#define DYNAMICCOMPONENT_H

#include <ComponentType.h>

class Entity;

template<typename T>
class DynamicComponent 
{
public:

   virtual T& compute(Entity *e, T &out) = 0;

};

template<typename T>
class DynamicComponentFunction : public DynamicComponent<T>
{
private:
   
   typedef T& (*DynamicComponentFunctionPointer)(Entity *e, T &out);

   DynamicComponentFunctionPointer function;

public:

   DynamicComponentFunction(DynamicComponentFunctionPointer function)  
      : function(function)
   {
   }

   T& compute(Entity *e, T &out)
   {
      return function(e, out);
   }
   
};

template<typename T>
struct DynamicComponentType : public ComponentType 
{

   DynamicComponent<T> *dynamicComponent;

   DynamicComponentType(const size_t id, const char *name, DynamicComponent<T> *dynamicComponent) 
    : ComponentType(id, name, AnyMemory()), dynamicComponent(dynamicComponent)
   {
   }

};

#endif
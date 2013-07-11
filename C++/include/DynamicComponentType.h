#ifndef DYNAMICCOMPONENT_H
#define DYNAMICCOMPONENT_H

#include <Entity.h>
#include <ComponentType.h>

template<typename T>
class DynamicComponent 
{
public:

   virtual T& compute(Entity *e, T &out) = 0;

};

template<typename T>
struct DynamicComponentType : public ComponentType 
{

   const DynamicComponent<T> dynamicComponent;

   DynamicComponentType(const size_t id, const char *name, const AnyMemory &defaultValue, const DynamicComponent<T> &dynamicComponent) 
    : ComponentType(id, name, defaultValue), dynamicComponent(dynamicComponent)
   {
   }

};

#endif
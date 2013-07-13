#ifndef DYNAMICCOMPONENT_H
#define DYNAMICCOMPONENT_H

#include <ComponentType.h>
#include <BitSet.h>

class Entity;

template<typename T>
class DynamicComponent 
{
public:

   const BitSet required;

   DynamicComponent(const BitSet &m_required) : required(m_required)
   {
   }

   virtual T& compute(Entity *e, T &out) = 0;

};

template<typename T>
class DynamicComponentFunction : public DynamicComponent<T>
{
public:

   typedef T& (*DynamicComponentFunctionPointer)(Entity *e, T &out);

   DynamicComponentFunctionPointer function;

   DynamicComponentFunction(const BitSet &m_required, DynamicComponentFunctionPointer m_function)
      : DynamicComponent<T>(m_required), function(m_function)
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

   DynamicComponentType(const size_t m_id, const std::string m_name, DynamicComponent<T> *m_dynamicComponent) 
    : ComponentType(m_id, m_name, AnyMemory()), dynamicComponent(m_dynamicComponent)
   {
   }

};

#endif
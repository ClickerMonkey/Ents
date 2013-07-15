#ifndef COMPONENT_H
#define COMPONENT_H

#include <functional>

#include <AnyMemory.h>
#include <BitSet.h>


class Entity;
class EntityCore;


struct ComponentBase
{
   const EntityCore *core;
   const size_t id;
   const std::string name;
   const AnyMemory defaultValue;

   ComponentBase(const EntityCore* m_core, const size_t m_id, const std::string &m_name, const AnyMemory &m_defaultValue) 
      : core(m_core), id(m_id), name(m_name), defaultValue(m_defaultValue) 
   {
   }

   friend std::ostream& operator<<(std::ostream &out, const ComponentBase &a);
};

template<typename T> struct Component;
template<typename T> std::ostream& operator<<(std::ostream &, const Component<T>&);

template<typename T>
struct Component : ComponentBase
{
   const T typedDefaultValue;

   Component(const EntityCore* m_core, const size_t m_id, const std::string &m_name, const T &m_typedDefaultValue) 
      : ComponentBase(m_core, m_id, m_name, AnyMemory(m_typedDefaultValue)), typedDefaultValue(m_typedDefaultValue) 
   {
   }

   friend std::ostream& operator<< <>(std::ostream &out, const Component<T> &a);
};

template<typename T> struct ComponentGet;
template<typename T> std::ostream& operator<<(std::ostream &, const ComponentGet<T>&);

template<typename T>
struct ComponentGet : ComponentBase
{
   const BitSet required;
   const std::function<T(Entity&)> get;

   ComponentGet(const EntityCore* m_core, const size_t m_id, const std::string &m_name, const BitSet &m_required, const std::function<T(Entity&)> &m_get) 
      : ComponentBase(m_core, m_id, m_name, {}), required(m_required), get(m_get) 
   {
   }

   friend std::ostream& operator<< <>(std::ostream &out, const ComponentGet<T> &a);
};

template<typename T> struct ComponentSet;
template<typename T> std::ostream& operator<<(std::ostream &, const ComponentSet<T>&);

template<typename T>
struct ComponentSet : ComponentBase
{
   const BitSet required;
   const std::function<T&(Entity&, T&)> set;

   ComponentSet(const EntityCore* m_core, const size_t m_id, const std::string &m_name, const BitSet &m_required, const std::function<T&(Entity&, T&)> &m_set) 
      : ComponentBase(m_core, m_id, m_name, {}), required(m_required), set(m_set) 
   {
   }

   friend std::ostream& operator<< <>(std::ostream &out, const ComponentSet<T> &a);
};

#endif
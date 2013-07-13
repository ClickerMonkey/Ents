#ifndef COMPONENTTYPE_H
#define COMPONENTTYPE_H

#include <AnyMemory.h>

struct ComponentType 
{

   const size_t id;

   const std::string name;
   
   const AnyMemory defaultValue;

   ComponentType(const size_t m_id, const std::string m_name, const AnyMemory &m_defaultValue) 
    : id(m_id), name(m_name), defaultValue(m_defaultValue) 
   {
   }

   virtual ~ComponentType()
   {
   }

   friend std::ostream& operator<<(std::ostream &out, const ComponentType &a);   

};

#endif
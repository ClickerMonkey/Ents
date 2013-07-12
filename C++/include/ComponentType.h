#ifndef COMPONENTTYPE_H
#define COMPONENTTYPE_H

#include <AnyMemory.h>

struct ComponentType 
{

   const size_t id;

   const std::string name;
   
   const AnyMemory defaultValue;

   ComponentType(const size_t id, const std::string name, const AnyMemory &defaultValue) 
    : id(id), name(name), defaultValue(defaultValue) 
   {
   }

   virtual ~ComponentType()
   {
   }

   friend std::ostream& operator<<(std::ostream &out, const ComponentType &a);   

};

#endif
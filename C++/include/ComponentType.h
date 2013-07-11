#ifndef COMPONENTTYPE_H
#define COMPONENTTYPE_H

#include <AnyMemory.h>

struct ComponentType 
{

   const size_t id;

   const char *name;
   
   const AnyMemory defaultValue;

   ComponentType(const size_t id, const char *name, const AnyMemory &defaultValue) 
    : id(id), name(name), defaultValue(defaultValue) 
   {
   }

};

#endif
#ifndef COMPONENTTYPE_H
#define COMPONENTTYPE_H

#include <string>
#include <AnyMemory.h>

struct ComponentType 
{
   const size_t id;
   const std::string name;
   const AnyMemory defaultValue;

   // const char? use std::string
   ComponentType(size_t id, const std::string& name, const AnyMemory& defaultValue) : id(id), name(name), defaultValue(defaultValue) { }
   virtual ~ComponentType() { }
};

#endif
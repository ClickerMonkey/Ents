#ifndef CONTROLLER_H
#define CONTROLLER_H

#include <BitSet.h>

class Entity;

class Controller 
{
public:

   const BitSet required;

   Controller(const BitSet &m_required) : required(m_required)
   {
   }

   virtual void control(Entity *e, void *updateState) = 0;

};

typedef void (*ControllerFunctionPointer)(Entity *e, void *updateState);

class ControllerFunction : public Controller
{
public:
   
   const ControllerFunctionPointer function;

   ControllerFunction(const BitSet &m_required, ControllerFunctionPointer m_function) 
      : Controller(m_required), function(m_function)
   {
   }

   void control(Entity *e, void *updateState)
   {
      function(e, updateState);
   }

};

#endif
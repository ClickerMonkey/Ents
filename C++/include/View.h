#ifndef VIEW_H
#define VIEW_H

#include <Common.h>
#include <BitSet.h>

class Entity;

class View
{
public:

   static const size_t NONE = 0xFFFFFFFF;

   const BitSet required;

   View(const BitSet &m_required) : required(m_required)
   {
   }

   virtual void draw(Entity *e, void *drawState) = 0;

};

typedef void (*ViewFunctionPointer)(Entity *e, void *drawState);

class ViewFunction : public View
{
public:

   const ViewFunctionPointer function;

   ViewFunction(const BitSet &m_required, ViewFunctionPointer m_function) 
      : View(m_required), function(m_function)
   {
   }

   void draw(Entity *e, void *drawState)
   {
      function(e, drawState);
   }

};

#endif
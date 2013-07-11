#ifndef VIEW_H
#define VIEW_H

#include <Entity.h>

class View 
{
public:

  virtual void draw(Entity *e, void *drawState) = 0;

};

typedef void (*ViewFunctionPointer)(Entity *e, void *drawState);

class ViewFunction : public View
{
private:
   const ViewFunctionPointer function;

public:

   ViewFunction(ViewFunctionPointer function) 
      : function(function)
   {
   }

   void draw(Entity *e, void *drawState)
   {
      function(e, drawState);
   }

};

#endif
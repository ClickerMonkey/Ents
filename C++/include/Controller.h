#ifndef CONTROLLER_H
#define CONTROLLER_H

class Entity;

class Controller 
{
public:

  virtual void control(Entity *e, void *updateState) = 0;

};

typedef void (*ControllerFunctionPointer)(Entity *e, void *updateState);

class ControllerFunction : public Controller
{
private:
   
   const ControllerFunctionPointer function;

public:

   ControllerFunction(ControllerFunctionPointer function) 
      : function(function)
   {
   }

   void control(Entity *e, void *updateState)
   {
      function(e, updateState);
   }

};

#endif
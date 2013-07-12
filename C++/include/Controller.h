#ifndef CONTROLLER_H
#define CONTROLLER_H

class Entity;

class Controller 
{
   public:
      virtual void control(Entity* e, void *updateState) = 0;
};

// Using is much better than typedef
using ControllerFunctionPointer = void(*)(Entity* e, void* updateState);

class ControllerFunction : public Controller
{
   private:
      const ControllerFunctionPointer& function;

   public:
      // Do not use same names for arguments and memebers
      ControllerFunction(ControllerFunctionPointer func) : function(func) { }

      void control(Entity* e, void* updateState) { function(e, updateState); }
};

#endif
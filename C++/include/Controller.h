#ifndef CONTROLLER_H
#define CONTROLLER_H

#include <Entity.h>

class Controller 
{
public:

  virtual void control(Entity *e, void *updateState) = 0;

};

#endif
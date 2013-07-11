#ifndef VIEW_H
#define VIEW_H

#include <Entity.h>

class View 
{
public:

  virtual void draw(Entity *e, void *drawState) = 0;

};

#endif
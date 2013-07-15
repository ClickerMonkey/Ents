#ifndef CONTROLLER_H
#define CONTROLLER_H

#include <functional>

#include <BitSet.h>

class Entity;
class EntityCore;

struct Controller 
{

   const EntityCore *core;
   const size_t id;
   const BitSet required;
   const std::function<void(Entity&,void*)> control;

   Controller(const EntityCore *m_core, const size_t m_id, const BitSet &m_required, const std::function<void(Entity&,void*)> &m_control) 
      : core(m_core), id(m_id), required(m_required), control(m_control)
   {
   }

};

#endif
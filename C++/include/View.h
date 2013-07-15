#ifndef VIEW_H
#define VIEW_H

#include <functional>

#include <BitSet.h>

class Entity;
class EntityCore;

struct View
{

   static const size_t NONE = 0xFFFFFFFF;

   const EntityCore *core;
   const size_t id;
   const BitSet required;
   const std::function<void(Entity&,void*)> draw;

   View(const EntityCore *m_core, const size_t m_id, const BitSet &m_required, const std::function<void(Entity&,void*)> &m_draw)
      : core(m_core), id(m_id), required(m_required), draw(m_draw)
   {
   }

};

#endif
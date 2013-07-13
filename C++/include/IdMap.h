#ifndef IDMAP_H
#define IDMAP_H

#include <Common.h>
#include <BitSet.h>

struct IdMap 
{
public:

   IdMap() 
   {
   }

   IdMap(const std::vector<size_t> &ids)
   {
      for (size_t i = 0; i < ids.size(); i++) 
      {
         add(ids[i], i);
      }
   }

   IdMap(std::initializer_list<size_t> ids) 
   {
      int index = 0;
      for (auto const &x : ids)
      {
         add(x, index++);
      }
   }

   inline bool has(const size_t id) const 
   {
      return (id < map.size() && map[id] >= 0);
   }

   inline void alias(const size_t id, const size_t aliasId) 
   {
      if (has(id)) {
         mapIndex(aliasId, map[id]);
      }
   }

   inline void add(const size_t id, const int index) 
   {
      if (!has(id)) {
         ids.push_back(id);
         mapIndex(id, index);
      }
   }

   inline int getIndex(const size_t id) const 
   {
      return map[id];
   }

   inline void setIndex(const size_t id, const int index)
   {
      mapIndex(id, index);
   }

   inline size_t getId(const size_t index) const 
   {
      return ids[index];
   }

   inline int getIndexSafe(const size_t id) const 
   {
      return (id < map.size() ? map[id] : -1);
   }

   inline size_t size() const 
   {
      return ids.size();
   }

   inline std::vector<size_t>& getIds()
   {
      return ids;
   }

   inline BitSet& getBitSet()
   {
      return bitset;
   }

private:

   std::vector<size_t> ids;
   std::vector<int> map;
   BitSet bitset;

   void mapIndex(const size_t id, const size_t index) 
   {
      while (id >= map.size()) {
         map.push_back(-1);
      }
      map[id] = index;
      bitset.set(id);
   }

};

#endif
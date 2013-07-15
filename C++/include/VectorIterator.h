#ifndef VECTORITERATOR_H
#define VECTORITERATOR_H

#include <functional>
#include <vector>

template<typename T>
struct VectorIteratorPointer 
{
   std::vector<T> *v;
   const std::function<bool(T)> filter;
   const int dir, stop;
   int index, last;

   VectorIteratorPointer(std::vector<T> *m_v, const std::function<bool(T)> &m_filter, int m_start, int m_dir, int m_stop) 
      : v(m_v), filter(m_filter), dir(m_dir), stop(m_stop) 
   {
      last = -1;
      index = findNext(m_start);
   }

   bool operator!=(const VectorIteratorPointer<T> &other) const 
   {
      return (last != other.index);
   }

   VectorIteratorPointer<T>& operator++() 
   {
      last = index;
      index = findNext(index + dir);
      return *this;
   }

   T& operator*() 
   {
      return v->at(index);
   }

   int findNext(int start) 
   {
      while (start != stop) {
         if (filter(v->at(start))) {
            break;
         }
         start += dir;
      }
      
      return (start == stop ? -1 : start);
   }
};

template<typename T>
struct VectorIterator 
{
   std::vector<T> *v;
   const std::function<bool(T)> filter;

   VectorIterator(std::vector<T> *m_v, const std::function<bool(T)> &m_filter)
      : v(m_v), filter(m_filter)
   {
   }

   VectorIteratorPointer<T> begin() 
   {
      return VectorIteratorPointer<T>(v, filter, 0, 1, v->size());
   }

   VectorIteratorPointer<T> end() 
   {
      return VectorIteratorPointer<T>(v, filter, v->size() - 1, -1, -1);
   }
};

#endif
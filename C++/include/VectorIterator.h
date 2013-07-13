#ifndef VECTORITERATOR_H
#define VECTORITERATOR_H

#include <Common.h>

template<typename T, typename F>
struct VectorIteratorPointer 
{
   std::vector<T> *v;
   F filter;
   const int dir, stop;
   int index, last;

   VectorIteratorPointer(std::vector<T> *v, F filter, int start, int dir, int stop) 
      : v(v), filter(filter), dir(dir), stop(stop) 
   {
      last = -1;
      index = findNext(start);
   }

   bool operator!=(const VectorIteratorPointer<T, F> &other) const 
   {
      return (last != other.index);
   }

   VectorIteratorPointer<T, F>& operator++() 
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

template<typename T, typename F>
struct VectorIterator 
{
   std::vector<T> *v;
   F filter;

   VectorIterator(std::vector<T> *v, F filter) 
      : v(v), filter(filter) 
   {
   }

   VectorIteratorPointer<T, F> begin() 
   {
      return VectorIteratorPointer<T, F>(v, filter, 0, 1, v->size());
   }

   VectorIteratorPointer<T, F> end() 
   {
      return VectorIteratorPointer<T, F>(v, filter, v->size() - 1, -1, -1);
   }
};

#endif
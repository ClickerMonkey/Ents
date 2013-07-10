#include <cstdlib>
#include <cmath>
#include <iostream>
#include <string.h>
#include <vector>

using namespace std;

struct Vector {
   float x, y;
   Vector() : x(0.0f), y(0.0f) { }
   Vector(float x) : x(x), y(x) { }
   Vector(float x, float y) : x(x), y(y) { }
};

class EntityType {
public:
   inline int getComponentOffset(int componentId) const {return 0;}
   template<typename T>
   inline T* getComponentPointer(const int componentId, const void *data) const {
      return (T*)(data + getComponentOffset(componentId));
   }
};

class Entity {
public:
   EntityType *type;
   void *data;

   template<typename T>
   inline T& get(const int componentId) const {
      T* ptr = type->getComponentPointer<T>(componentId, data);
      return *ptr;
   }

   template<typename T>
   inline void set(const int componentId, const T &value) {
      T *ptr = type->getComponentPointer<T>(componentId, data);
      *ptr = value;
   }
};

// template<typename T>
// class DynamicComponent {
// public:
//    T& (*DynamicFunction)

// };

// void stuff(char *word, int &times)
// {
//    for (int i = 0; i < times; i++) {
//       cout << word << endl;
//    }
// }

// Template Function in class

template<typename T>
class Test {
public:
   typedef T& (*TestFunction)(const Entity *entity, T &out);

   TestFunction function;
};

Vector& position(const Entity *entity, Vector &out)
{
   out.x = 10.0f;
   out.y = -2.0f;
   return out;
}

// Iterators with filtering

template<typename T>
bool FilterNone(const T* item) {
  return true;
}

template<typename T, typename F>
struct VectorIteratorPointer 
{
   vector<T> *v;
   F filter;
   const int dir, stop;
   int index, last;

   VectorIteratorPointer(vector<T> *v, F filter, int start, int dir, int stop) 
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
         if (filter(&v->at(start))) {
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
   vector<T> *v;
   F filter;

   VectorIterator(vector<T> *v, F filter) 
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

// Iterator
template<typename T>
class List {
public:

   typedef bool (*ListFilter)(const T *item);

   void add(const T &item) {
      list.push_back(item);
   }
   List& operator<<(const T &item) {
      list.push_back(item);
      return *this;
   }
   List& operator+=(const T &item) {
      list.push_back(item);
      return *this;
   }

   VectorIteratorPointer<T, ListFilter> begin() {
      return VectorIteratorPointer<T, ListFilter>(&list, FilterNone<T>, 0, 1, list.size());
   }
   VectorIteratorPointer<T, ListFilter> end() {
      return VectorIteratorPointer<T, ListFilter>(&list, FilterNone<T>, list.size() - 1, -1, -1);
   }

   template<typename F>
   VectorIterator<T, F> filter(F filterFunction) {
      return VectorIterator<T, F>(&list, filterFunction);
   }

private:
   vector<T> list;
};

struct DivisbleFunctor 
{
   const int divisor, remainder;
   DivisbleFunctor(const int divisor, const int remainder)
      : divisor(divisor), remainder(remainder)
   {
   }
   bool operator()(const int *item) const
   {
      return (*item) % divisor == remainder;
   }
};

bool is_even(const int *item) {
  return (*item) % 2 == 0;
}

int main(int argc, char **argv)
{
   List<int> x;
   x << 5 << 4 << 9 << 1;

   for(auto &y : x) {
      cout << y << " ";
   }
   cout << endl;

   DivisbleFunctor odd(2, 1);

   for(auto &y : x.filter(odd))  {
      cout << y << " ";
   }
   cout << endl;

   for(auto &y : x.filter(FilterNone<int>))  {
      cout << y << " ";
   }
   cout << endl;

   List<Vector> w;
   w << Vector(4, 0) << Vector(1, 2) << Vector(3, 1);
   for(auto &u : w) {
       cout << "{" << u.x << "," << u.y << "}" << endl;
   }
   cout << endl;

   List<Vector> a;
   for(auto &u : a) {
       cout << "{" << u.x << "," << u.y << "}" << endl;
   }
   cout << endl;

   List<Vector> b;
   b += Vector(1, 2);
   for(auto &u : b) {
       cout << "{" << u.x << "," << u.y << "}" << endl;
   }
   cout << endl;

   return 0;
}

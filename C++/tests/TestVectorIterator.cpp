#include <VectorIterator.h>

using namespace std;

struct Vector 
{
   float x, y;
   Vector() : x(0.0f), y(0.0f) { }
   Vector(float x) : x(x), y(x) { }
   Vector(float x, float y) : x(x), y(y) { }
};

// Simple List with filtered iteration

template<typename T>
bool FilterNone(const T* item) 
{
  return (item != nullptr);
}

template<typename T>
class List 
{
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

// A functor that can be used for filtering
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

// A function that can be used for filtering
bool is_even(const int *item) 
{
  return (*item) % 2 == 0;
}

int main()
{
  List<int> x;
  x << 5 << 4 << 9 << 1;

  cout << "Iterating through all elements (5,4,9,1)..." << endl;
  for(auto &y : x) {
     cout << y << " ";
  }
  cout << endl;

  DivisbleFunctor odd(2, 1);

  cout << "Iterating through odd elements using a functor..." << endl;
  for(auto &y : x.filter(odd))  {
     cout << y << " ";
  }
  cout << endl;

  cout << "Iterating through all elements using a templated function..." << endl;
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

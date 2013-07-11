#ifndef ANYMEMORY_H
#define ANYMEMORY_H

#include <Common.h>

struct AnyMemory 
{
private:

  size_t size;

  char *data;

public:

  template<typename T>
  AnyMemory(const T &x) : size(0), data(NULL) 
  {
    add<T>(x);
  }

  AnyMemory(const AnyMemory &copy);

  AnyMemory();

  ~AnyMemory();

  inline size_t getSize() const 
  {
    return size;
  }

  inline char* getData() 
  {
    return data;
  }

  inline void setSize(const size_t newSize) 
  {
    data = (char*)realloc(data, size = newSize);
  }

  inline void expand(const size_t bytes) 
  {
    setSize(size + bytes);
  }

  size_t append(const AnyMemory &copy);

  template<typename T>
  inline bool exists(const size_t offset) const 
  {
    return (offset + sizeof(T) <= size);
  }

  template<typename T>
  inline T* getPointer(const size_t offset) 
  {
    return (T*)(data + offset);
  }

  template<typename T>
  inline T* getSafe(const size_t offset) 
  {
    return exists<T>(offset) ? getPointer<T>(offset) : NULL;
  }

  template<typename T>
  inline T& get(const size_t offset) 
  {
    return *getPointer<T>(offset);
  }

  template<typename T>
  inline void set(const size_t offset, const T &item) 
  {
    *getPointer<T>(offset) = item;
  }

  template<typename T>
  inline bool setSafe(const size_t offset, const T &item) 
  {
    T* ptr = getSafe<T>(offset);
    if (ptr != NULL) {
      *ptr = item;
    }
    return (ptr != NULL);
  }

  template<typename T>
  inline size_t add(const T &item) 
  {
    const size_t offset = size;
    setSize(size + sizeof(T));
    set<T>(offset, item);
    return offset;
  }

  template<typename T>
  inline AnyMemory& operator+=(const T &item) 
  {
    add<T>(item);
    return *this;
  }

  template<typename T>
  inline AnyMemory& operator<<(const T &item) 
  {
    add<T>(item);
    return *this;
  }

};

#endif
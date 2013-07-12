#ifndef ANYMEMORY_H
#define ANYMEMORY_H

#include <Common.h>

struct AnyMemory 
{
  private:
    size_t size;
    char* data;

  public:
    AnyMemory();

    // in C++11 it's not a good idea to use NULL, use nullptr
    template<typename T> AnyMemory(const T &x) : size(0), data(nullptr) { add<T>(x); }

    AnyMemory(const AnyMemory &copy);
    ~AnyMemory();

    inline size_t getSize() const { return size; }
    inline char* getData() const { return data; }

    // if you are passing parameters by value there's no sense in using const
    inline void setSize(size_t newSize) { data = (char*)realloc(data, size = newSize); }
    inline void expand(size_t bytes)    { setSize(size + bytes); }

    size_t append(const AnyMemory &copy);

    template<typename T> inline bool exists(size_t offset) const    { return (offset + sizeof(T) <= size); }
    template<typename T> inline T* getPointer(size_t offset) const  { return static_cast<T*>(data + offset); } // Don't use C-style casts as they are ambiguous
    template<typename T> inline T* getSafe(size_t offset) const     { return exists<T>(offset) ? getPointer<T>(offset) : nullptr; }

    template<typename T> inline T& get(size_t offset)  { return *getPointer<T>(offset); }
    template<typename T> inline void set(size_t offset, const T& item) { *getPointer<T>(offset) = item; }

    template<typename T> inline bool setSafe(size_t offset, const T& item) 
    {
      // I like using brace initaliziation whenever possible
      T* ptr{getSafe<T>(offset)};
      if(ptr != nullptr) { *ptr = item; return true; }
      return false;
    }
    template<typename T> inline size_t add(const T& item) 
    {
      const size_t offset{size};
      setSize(size + sizeof(T));
      set<T>(offset, item);
      return offset;
    }

    bool equals(const AnyMemory& other) const;
    int hashCode() const;
    int compareTo(const AnyMemory& other) const;

    template<typename T> inline AnyMemory& operator+=(const T& item) { add<T>(item); return *this; }
    template<typename T> inline AnyMemory& operator<<(const T& item) { add<T>(item); return *this; }

    inline bool operator==(const AnyMemory& b) const  { return equals(b); }
    inline bool operator!=(const AnyMemory& b) const  { return !equals(b); }
    inline bool operator<(const AnyMemory& b) const   { return compareTo(b) < 0; }
    inline bool operator>(const AnyMemory& b) const   { return compareTo(b) > 0; }
    inline bool operator<=(const AnyMemory& b) const  { return compareTo(b) <= 0; }
    inline bool operator>=(const AnyMemory& b) const  { return compareTo(b) >= 0; }

    inline char* operator[](size_t index) const { return (index < size ? (data + index) : nullptr); }
};

#endif
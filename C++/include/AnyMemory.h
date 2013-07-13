#ifndef ANYMEMORY_H
#define ANYMEMORY_H

#include <Common.h>

struct AnyMemory 
{
private:

  size_t size = 0;

  char *data = nullptr;

public:

  AnyMemory();

  template<typename T>
  AnyMemory(const T &x)
  {
    add<T>(x);
  }

  template<typename T>
  AnyMemory(std::initializer_list<T> list)
  {
    for (const auto &x : list) {
      add<T>(x);
    }
  }

  AnyMemory(const AnyMemory &copy);

  ~AnyMemory();

  void set(const AnyMemory &copy);

  inline size_t getSize() const 
  {
    return size;
  }

  inline char* getData() const
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

  AnyMemory sub(const size_t offset, const size_t bytes);

  size_t append(const AnyMemory &copy);

  template<typename T>
  inline bool exists(const size_t offset) const 
  {
    return (offset + sizeof(T) <= size);
  }

  template<typename T>
  inline T* getPointer(const size_t offset) const
  {
    return (T*)(data + offset);
  }

  template<typename T>
  inline T* getSafe(const size_t offset) 
  {
    return exists<T>(offset) ? getPointer<T>(offset) : nullptr;
  }

  template<typename T>
  inline T& get(const size_t offset) const
  {
    return *getPointer<T>(offset);
  }

  template<typename T>
  inline T& getAligned(const size_t index)
  {
    return get<T>(index * sizeof(T));
  }

  template<typename T>
  inline T* getAlignedPointer(const size_t index)
  {
    return getPointer<T>(index * sizeof(T));
  }

  template<typename T>
  inline T* getAlignedSafe(const size_t index)
  {
    return getSafe<T>(index * sizeof(T));
  }

  template<typename T>
  inline void set(const size_t offset, const T &value) 
  {
    *getPointer<T>(offset) = value;
  }

  template<typename T>
  inline bool setSafe(const size_t offset, const T &value) 
  {
    T* ptr = getSafe<T>(offset);
    if (ptr != nullptr) {
      *ptr = value;
    }
    return (ptr != nullptr);
  }

  inline bool set(const size_t offset, const AnyMemory& other) {
    bool large = (offset + other.size <= size);
    if (large) {
      memcpy(data + offset, other.data, other.size);
    }
    return large;
  }

  template<typename T>
  inline void setAligned(const size_t index, const T &value)
  {
    set<T>(index * sizeof(T), value);
  }

  template<typename T>
  inline bool setAlignedSafe(const size_t index, const T &value)
  {
    return setSafe<T>(index * sizeof(T), value);
  }

  template<typename T>
  inline size_t add(const T &value) 
  {
    const size_t offset = size;
    setSize(size + sizeof(T));
    set<T>(offset, value);
    return offset;
  }

  template<typename T>
  inline size_t getCapacity()
  {
    return (size / sizeof(T));
  }

  bool equals(const AnyMemory &other) const;

  int hashCode() const;

  int compareTo(const AnyMemory &other) const;

  template<typename T>
  inline AnyMemory& operator+=(const T &value) 
  {
    add<T>(value);
    return *this;
  }

  template<typename T>
  inline AnyMemory& operator<<(const T &value) 
  {
    add<T>(value);
    return *this;
  }

  inline AnyMemory& operator=(const AnyMemory &other)
  {
    set(other);
    return *this;
  }

  inline char* operator[](const size_t index)
  {
    return ( index < size ? (data + index) : nullptr );
  }

  inline bool operator==(const AnyMemory &b) const  { return equals( b ); }
  inline bool operator!=(const AnyMemory &b) const  { return !equals( b ); }
  inline bool operator< (const AnyMemory &b) const  { return compareTo( b ) < 0; }
  inline bool operator> (const AnyMemory &b) const  { return compareTo( b ) > 0; }
  inline bool operator<=(const AnyMemory &b) const  { return compareTo( b ) <= 0; }
  inline bool operator>=(const AnyMemory &b) const  { return compareTo( b ) >= 0; }

  friend std::ostream& operator<<(std::ostream &out, const AnyMemory &a);

};

#endif
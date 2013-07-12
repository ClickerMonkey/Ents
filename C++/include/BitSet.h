#ifndef BITSET_H
#define BITSET_H

#include <Common.h>

struct BitSet 
{
private:

  std::vector<size_t> bits;

public:

  BitSet() 
  {
  }

  BitSet(const std::vector<size_t> &indices) 
  {
    setFromIndices(indices);
  }

  BitSet(std::initializer_list<size_t> indices) 
  {
    for (const auto &i : indices) {
      set(i);
    } 
  }

  void setFromIndices(const std::vector<size_t> &indices) 
  {
    for (const auto &i : indices) {
      set(i);
    }
  }

  inline void set(const size_t bitIndex) 
  { 
    set(bitIndex, true);
  }

  void set(const size_t bitIndex, const bool value);

  bool get(const size_t bitIndex, const bool defaultValue = false) const;

  bool intersects(const BitSet &other) const;

  inline size_t size() const
  {
    return bits.size() << 5;
  }

  bool equals(const BitSet &other) const;

  inline bool operator==(const BitSet &b) const  { return equals( b ); }
  inline bool operator!=(const BitSet &b) const  { return !equals( b );  }

private:
  
  inline size_t indexOf(const size_t bitIndex) const 
  {
    return (bitIndex >> 5);
  }

  inline size_t maskOf(const size_t bitIndex) const 
  {
    return (1 << (bitIndex & 31));
  }

};

std::ostream& operator<<(std::ostream &out, const BitSet &a);

#endif
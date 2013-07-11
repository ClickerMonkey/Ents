#ifndef BITSET_H
#define BITSET_H

#include <Common.h>

struct BitSet 
{
private:

  vector<size_t> bits;

public:

  BitSet() 
  {
  }

  BitSet(const vector<size_t> &indices) 
  {
    setFromIndices(indices);
  }

  BitSet(initializer_list<size_t> indices) 
  {
    for (auto i : indices) set(i);
  }

  void setFromIndices(const vector<size_t> &indices) 
  {
    for (auto i : indices) set(i);
  }

  void set(const size_t bitIndex) 
  { 
    set(bitIndex, true);
  }

  void set(const size_t bitIndex, const bool value) 
  {
    const size_t i = indexOf(bitIndex);
    const size_t mask = maskOf(bitIndex);
    while (i >= bits.size()) bits.push_back(0);
    bits[i] &= ~mask;
    if (value) {
      bits[i] |= mask;
    }
  }

  bool get(const size_t bitIndex) const 
  {
    const size_t i = indexOf(bitIndex);
    const size_t mask = maskOf(bitIndex);
    return (i < bits.size() && bits[i] & mask);
  }

  bool intersects(const BitSet &other) const 
  {
    int m = min(bits.size(), other.bits.size());
    while (--m >= 0) {
      if (bits[m] & other.bits[m] ) {
        return true;
      }
    }
    return false;
  }

  inline size_t size() 
  {
    return bits.size() << 5;
  }

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

#endif
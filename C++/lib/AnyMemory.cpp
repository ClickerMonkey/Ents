#include <AnyMemory.h>

AnyMemory::AnyMemory(const AnyMemory &copy) : size(0), data(NULL) 
{
  setSize(copy.size);
  memcpy(data, copy.data, copy.size);
}

AnyMemory::AnyMemory() : size(0), data(NULL) 
{
}

AnyMemory::~AnyMemory() 
{
  if (data != NULL) 
  {
    free(data);
    data = NULL;
  }
}

size_t AnyMemory::append(const AnyMemory &copy) 
{
  const size_t offset = size;

  setSize(size + copy.size);
  memcpy(data + offset, copy.data, copy.size);

  return offset;
}

bool AnyMemory::equals(const AnyMemory &other) const
{
  return ( size == other.size && memcmp(data, other.data, size) == 0 );
}

int AnyMemory::hashCode() const
{
  const int prime = 31;
  int hash = 0;
  for (size_t i = 0; i < size; i++)
  {
    hash = hash * prime + (*((char*)(data + i)));
  }
  return hash;
}

int AnyMemory::compareTo(const AnyMemory &other) const
{
  int d = (size - other.size);

  return ( d != 0 ? d : memcmp( data, other.data, size ) );
}
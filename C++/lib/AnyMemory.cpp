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
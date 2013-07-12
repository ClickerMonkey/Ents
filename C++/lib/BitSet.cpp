#include <BitSet.h>

using namespace std;

void BitSet::set(const size_t bitIndex, const bool value) 
{
	const size_t i = indexOf(bitIndex);
	const size_t mask = maskOf(bitIndex);

	while (i >= bits.size()) bits.push_back(0);

	bits[i] &= ~mask;
	if (value) 
	{
		bits[i] |= mask;
	}
}

bool BitSet::get(const size_t bitIndex, const bool defaultValue) const
{
	const size_t i = indexOf(bitIndex);
	const size_t mask = maskOf(bitIndex);

	return (i < bits.size() ? (bits[i] & mask) : defaultValue);
}

bool BitSet::intersects(const BitSet &other) const
{
	int m = min(bits.size(), other.bits.size());

	while (--m >= 0) 
	{
  		if (bits[m] & other.bits[m]) 
  		{
	    	return true;
  		}
	}

	return false;
}

bool BitSet::equals(const BitSet &other) const
{
	if (bits.size() != other.bits.size())
	{
		return false;
	}

	int m = bits.size();

	while (--m >= 0)
	{
		if (bits[m] != other.bits[m])
		{
			return false;
		}
	}

	return true;
}

ostream& operator<<(ostream &out, const BitSet &a)
{
  out << "{";

  for (size_t i = 0; i < a.size(); i++)
  {
    out << a.get(i);
  }

  out << "}";

  return out;
}
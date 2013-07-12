#include <assert.h>
#include <BitSet.h>

using namespace std;

void testEmptyConstructor()
{
  cout << __func__ << endl;
   
  BitSet a;

  assert( a.size() == 0 );
  assert( a.get(0) == false );
  assert( a.get(0, true) == true );
}

void testVectorConstructor()
{
  cout << __func__ << endl;
   
  vector<size_t> indices = {0, 1, 4, 5};

  BitSet a(indices);

  assert( a.size() == 32 );
  assert( a.get(0) );
  assert( a.get(1) );
  assert(!a.get(2) );
  assert(!a.get(3) );
  assert( a.get(4) );
  assert( a.get(5) );
  assert(!a.get(6) );
}

void testInitializerListConstructor()
{
  cout << __func__ << endl;
   
  BitSet a = {0, 1, 4, 5, 32};

  assert( a.size() == 64 );
  assert( a.get(0) );
  assert( a.get(1) );
  assert(!a.get(2) );
  assert(!a.get(3) );
  assert( a.get(4) );
  assert( a.get(5) );
  assert(!a.get(6) );
  assert(!a.get(31) );
  assert( a.get(32) );
  assert(!a.get(33) );
}

void testFromIndices()
{
  cout << __func__ << endl;
   
  BitSet a;

  assert( a.size() == 0 );
 
  vector<size_t> indices = {0, 1, 4, 5};

  a.setFromIndices(indices);

  assert( a.size() == 32 );
  assert( a.get(0) );
  assert( a.get(1) );
  assert(!a.get(2) );
  assert(!a.get(3) );
  assert( a.get(4) );
  assert( a.get(5) );
  assert(!a.get(6) );
}

void testSet()
{
  cout << __func__ << endl;
   
  BitSet a;

  assert(!a.get(0) );

  a.set(0, true);

  assert( a.get(0) );

  a.set(0, false);

  assert(!a.get(0) );

  a.set(0);

  assert( a.get(0) );
  assert(!a.get(345) );
  
  a.set(345);

  assert( a.get(345) );
}

void testIntersects()
{
  cout << __func__ << endl;
  
  BitSet a = {1, 2, 4, 5};
  BitSet b = {1, 6};
  BitSet c = {0, 3, 6};

  assert( a.intersects(b) );
  assert(!a.intersects(c) );
  assert( b.intersects(c) );
}

void testToString()
{
  cout << __func__ << endl;
   
  BitSet a = {0, 1, 2, 3, 5};

  cout << a << endl;
}

void testEquals()
{
  cout << __func__ << endl;
   
  BitSet a = {0, 1, 2, 3, 5};
  BitSet b = {0, 1, 2, 3, 5};
  BitSet c = {0, 1, 2, 3, 6};

  assert( a == b );
  assert( a != c );
  assert( b != c );
}

int main()
{
  testEmptyConstructor();
  testVectorConstructor();
  testInitializerListConstructor();
  testFromIndices();
  testSet();
  testIntersects();
  testToString();
  testEquals();

  cout << "ALL TESTS PASS" << endl;

  return 0;
}

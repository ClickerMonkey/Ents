#include <assert.h>
#include <BitSet.h>

using namespace std;

void testEmptyConstructor()
{
  cout << "Running " << __func__ << "() ... ";
   
  BitSet a;

  assert( a.size() == 0 );
  assert( a.get(0) == false );
  assert( a.get(0, true) == true );

   cout << "Pass" << endl;
}

void testVectorConstructor()
{
  cout << "Running " << __func__ << "() ... ";
   
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

  cout << "Pass" << endl;
}

void testInitializerListConstructor()
{
  cout << "Running " << __func__ << "() ... ";
   
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

  cout << "Pass" << endl;
}

void testFromIndices()
{
  cout << "Running " << __func__ << "() ... ";
   
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

  cout << "Pass" << endl;
}

void testSet()
{
  cout << "Running " << __func__ << "() ... ";
   
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

  cout << "Pass" << endl;
}

void testIntersects()
{
  cout << "Running " << __func__ << "() ... ";
  
  BitSet a = {1, 2, 4, 5};
  BitSet b = {1, 6};
  BitSet c = {0, 3, 6};

  assert( a.intersects(b) );
  assert(!a.intersects(c) );
  assert( b.intersects(c) );

  cout << "Pass" << endl;
}

void testContains()
{
  cout << "Running " << __func__ << "() ... ";
  
  BitSet a = {1, 2, 3, 4, 6, 7};
  BitSet b = {2, 3};
  BitSet c = {1, 2, 5};

  assert( a.contains(b) );
  assert(!a.contains(c) );
  assert(!b.contains(a) );
  assert(!c.contains(a) );

  cout << "Pass" << endl;
}

void testToString()
{
  cout << "Running " << __func__ << "() ... ";
   
  BitSet a = {0, 1, 2, 3, 5};

  cout << a << " ";

  cout << "Pass" << endl;
}

void testEquals()
{
  cout << "Running " << __func__ << "() ... ";
   
  BitSet a = {0, 1, 2, 3, 5};
  BitSet b = {0, 1, 2, 3, 5};
  BitSet c = {0, 1, 2, 3, 6};

  assert( a == b );
  assert( a != c );
  assert( b != c );

  cout << "Pass" << endl;
}

int main()
{
  cout << "Starting " << __FILE__ << "..." << endl;

  testEmptyConstructor();
  testVectorConstructor();
  testInitializerListConstructor();
  testFromIndices();
  testSet();
  testIntersects();
  testContains();
  testToString();
  testEquals();

  cout << __FILE__ << " has ran successfully." << endl;

  return 0;
}

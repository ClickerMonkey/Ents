#include <assert.h>
#include <IdMap.h>

using namespace std;

//*****************************************************************************
// TEST CASES
//*****************************************************************************

void testDefaultConstructor()
{
   cout << __func__ << endl;

   IdMap im;

   assert( im.size() == 0 );
}

void testVectorConstructor()
{
   cout << __func__ << endl;

   vector<size_t> indices = {0, 1, 5};

   IdMap im(indices);

   assert( im.size() == 3 );
   assert( im.has(0) );
   assert( im.has(1) );
   assert(!im.has(2) );
   assert(!im.has(3) );
   assert(!im.has(4) );
   assert( im.has(5) );
   assert(!im.has(6) );
}

void testInitializerListConstructor()
{
   cout << __func__ << endl;
   
   IdMap im = {0, 1, 5};

   assert( im.size() == 3 );
   assert( im.has(0) );
   assert( im.has(1) );
   assert(!im.has(2) );
   assert(!im.has(3) );
   assert(!im.has(4) );
   assert( im.has(5) );
   assert(!im.has(6) );
}

void testGetIndex()
{
   cout << __func__ << endl;
   
   IdMap im = {0, 1, 5};

   assert( im.getIndex(0) == 0 );
   assert( im.getIndex(1) == 1 );
   assert( im.getIndex(5) == 2 );
}

void testGetIndexSafe()
{
   cout << __func__ << endl;
   
   IdMap im = {0, 1, 5};

   assert( im.getIndexSafe(0) == 0 );
   assert( im.getIndexSafe(1) == 1 );
   assert( im.getIndexSafe(5) == 2 );
   assert( im.getIndexSafe(7) ==-1 );
}

void testAlias()
{
   cout << __func__ << endl;
   
   IdMap im = {0, 1, 5};
   im.alias(1, 6); // index of 6 is now 1
   im.alias(5, 7); // index of 7 is now 2

   assert( im.getIndex(6) == 1 );
   assert( im.getIndex(7) == 2 );
}

void testAdd()
{
   cout << __func__ << endl;
   
   IdMap im = {0, 1, 5};

   assert( im.size() == 3 );

   im.add(7);

   assert( im.size() == 4 );
   assert( im.getIndex(7) == 3 );
}

void testGetId()
{
   cout << __func__ << endl;
   
   IdMap im = {0, 1, 5};

   assert( im.getId(0) == 0 );
   assert( im.getId(1) == 1 );
   assert( im.getId(2) == 5 );
}

int main()
{
   testDefaultConstructor();
   testVectorConstructor();
   testInitializerListConstructor();
   testGetIndex();
   testGetIndexSafe();
   testAlias();
   testAdd();
   testGetId();

   cout << "ALL TESTS PASS" << endl;

   return 0;
}

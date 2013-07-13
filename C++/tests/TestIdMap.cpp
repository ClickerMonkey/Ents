#include <assert.h>
#include <IdMap.h>

using namespace std;

//*****************************************************************************
// TEST CASES
//*****************************************************************************

void testDefaultConstructor()
{
   cout << "Running " << __func__ << "() ... ";

   IdMap im;

   assert( im.size() == 0 );

   cout << "Pass" << endl;
}

void testVectorConstructor()
{
   cout << "Running " << __func__ << "() ... ";

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

   cout << "Pass" << endl;
}

void testInitializerListConstructor()
{
   cout << "Running " << __func__ << "() ... ";
   
   IdMap im = {0, 1, 5};

   assert( im.size() == 3 );
   assert( im.has(0) );
   assert( im.has(1) );
   assert(!im.has(2) );
   assert(!im.has(3) );
   assert(!im.has(4) );
   assert( im.has(5) );
   assert(!im.has(6) );

   cout << "Pass" << endl;
}

void testGetIndex()
{
   cout << "Running " << __func__ << "() ... ";
   
   IdMap im = {0, 1, 5};

   assert( im.getIndex(0) == 0 );
   assert( im.getIndex(1) == 1 );
   assert( im.getIndex(5) == 2 );

   cout << "Pass" << endl;
}

void testGetIndexSafe()
{
   cout << "Running " << __func__ << "() ... ";
   
   IdMap im = {0, 1, 5};

   assert( im.getIndexSafe(0) == 0 );
   assert( im.getIndexSafe(1) == 1 );
   assert( im.getIndexSafe(5) == 2 );
   assert( im.getIndexSafe(7) ==-1 );

   cout << "Pass" << endl;
}

void testAlias()
{
   cout << "Running " << __func__ << "() ... ";
   
   IdMap im = {0, 1, 5};
   im.alias(1, 6); // index of 6 is now 1
   im.alias(5, 7); // index of 7 is now 2

   assert( im.getIndex(6) == 1 );
   assert( im.getIndex(7) == 2 );

   cout << "Pass" << endl;
}

void testAdd()
{
   cout << "Running " << __func__ << "() ... ";
   
   IdMap im = {0, 1, 5};

   assert( im.size() == 3 );

   im.add(7, im.size());

   assert( im.size() == 4 );
   assert( im.getIndex(7) == 3 );

   cout << "Pass" << endl;
}

void testGetId()
{
   cout << "Running " << __func__ << "() ... ";
   
   IdMap im = {0, 1, 5};

   assert( im.getId(0) == 0 );
   assert( im.getId(1) == 1 );
   assert( im.getId(2) == 5 );

   cout << "Pass" << endl;
}

void testToString()
{
   cout << "Running " << __func__ << "() ... ";
   
   IdMap im = {0, 1, 5};

   cout << im << " ";

   cout << "Pass" << endl;
}

int main()
{
   cout << "Starting " << __FILE__ << "..." << endl;

   testDefaultConstructor();
   testVectorConstructor();
   testInitializerListConstructor();
   testGetIndex();
   testGetIndexSafe();
   testAlias();
   testAdd();
   testGetId();
   testToString();

   cout << __FILE__ << " has ran successfully." << endl;

   return 0;
}

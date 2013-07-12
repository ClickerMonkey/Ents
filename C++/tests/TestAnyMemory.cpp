#include <assert.h>
#include <AnyMemory.h>

//*****************************************************************************
// TEST CASES
//*****************************************************************************

void testValueConstructor()
{
   AnyMemory am(3.0f);

   assert( am.getSize() == 4 );
   assert( am.get<float>(0) == 3.0f );
}

void testDefaultConstructor()
{
   AnyMemory am;

   assert( am.getSize() == 0 );
}

void testInputOperator()
{
   AnyMemory am;
   am << 345 << 4.0f;

   assert( am.getSize() == 8 );
   assert( am.get<int>(0) == 345 );
   assert( am.get<float>(4) == 4.0f );
}

void testSetSize()
{
   AnyMemory am;

   assert( am.getSize() == 0 );

   am.setSize( 1024 );

   assert( am.getSize() == 1024 );
}

void testExpand()
{
   AnyMemory am;

   assert( am.getSize() == 0 );

   am.expand( 4 );

   assert( am.getSize() == 4 );

   am.expand( 4 );

   assert( am.getSize() == 8 );   
}

void testExists()
{
   AnyMemory am;

   assert( !am.exists<char>(0) );
   assert( !am.exists<char>(1) );

   am.setSize( 8 );

   assert( am.exists<char>(0) );
   assert( am.exists<char>(1) );
   assert( am.exists<char>(7) );
   assert( !am.exists<char>(8) );

   assert( am.exists<long long>(0) );
   assert( !am.exists<long long>(1) );

   assert( am.exists<int>(4) );
   assert( !am.exists<int>(5) );

   assert( am.exists<short>(6) );
   assert( !am.exists<short>(7) );
}

void testSafe()
{
   AnyMemory am(345);

   int *x = am.getSafe<int>(0);

   assert( x != nullptr );
   assert( *x == 345 );

   int *y = am.getSafe<int>(1);

   assert( y == nullptr );
}

int main()
{
   testValueConstructor();
   testDefaultConstructor();
   testInputOperator();
   testSetSize();
   testExpand();
   testExists();
   testSafe();

   cout << "SUCCESS" << endl;

   return 0;
}

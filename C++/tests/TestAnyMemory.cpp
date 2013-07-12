#include <assert.h>
#include <AnyMemory.h>

using namespace std;

//*****************************************************************************
// TEST CASES
//*****************************************************************************

void testValueConstructor()
{
   cout << __func__ << endl;

   AnyMemory am(3.0f);

   assert( am.getSize() == 4 );
   assert( am.get<float>(0) == 3.0f );
}

void testDefaultConstructor()
{
   cout << __func__ << endl;

   AnyMemory am;

   assert( am.getSize() == 0 );
}

void testCopyConstructor()
{
   cout << __func__ << endl;

   AnyMemory am1(3.0f);
   AnyMemory am2(am1);

   assert( am2.getSize() == 4 );
   assert( am2.get<float>(0) == 3.0f );
}

void testInitializerListConstructor()
{
   cout << __func__ << endl;
   
   AnyMemory am = {1, 2, 4, 8};

   assert( am.getSize() == 16 );
   assert( am.get<int>(0) == 1 );
   assert( am.get<int>(4) == 2 );
}

void testAdd()
{
   cout << __func__ << endl;
   
   AnyMemory am;

   am.add(1);
   am.add(2);

   assert( am.getSize() == 8 );
   assert( am.get<int>(0) == 1 );
   assert( am.get<int>(4) == 2 );
}

void testInputOperator()
{
   cout << __func__ << endl;
   
   AnyMemory am;
   am << 345 << 4.0f;

   assert( am.getSize() == 8 );
   assert( am.get<int>(0) == 345 );
   assert( am.get<float>(4) == 4.0f );
}

void testSetSize()
{
   cout << __func__ << endl;
   
   AnyMemory am;

   assert( am.getSize() == 0 );

   am.setSize( 1024 );

   assert( am.getSize() == 1024 );
}

void testExpand()
{
   cout << __func__ << endl;
   
   AnyMemory am;

   assert( am.getSize() == 0 );

   am.expand( 4 );

   assert( am.getSize() == 4 );

   am.expand( 4 );

   assert( am.getSize() == 8 );   
}

void testExists()
{
   cout << __func__ << endl;
   
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

void testGetSafe()
{
   cout << __func__ << endl;
   
   AnyMemory am(345);

   int *x = am.getSafe<int>(0);

   assert( x != nullptr );
   assert( *x == 345 );

   int *y = am.getSafe<int>(1);

   assert( y == nullptr );
}

void testSetSafe()
{
   cout << __func__ << endl;
   
   AnyMemory am = {1, 2};

   assert( am.setSafe<int>(0, 4) );
   assert( am.setSafe<int>(4, 8) );
   assert( !am.setSafe<int>(5, 16) );

   assert( am.get<int>(0) == 4 );
   assert( am.get<int>(4) == 8 );
}

void testPointer()
{
   cout << __func__ << endl;
   
   AnyMemory am(345);

   int *x = am.getPointer<int>(0);

   assert( x != nullptr );
   assert( *x == 345 );

   *x = 698;

   assert( am.get<int>(0) == 698 );
}

void testToString()
{
   cout << __func__ << endl;
   
   AnyMemory am(345);

   cout << am << endl;
}

void testAppend()
{
   cout << __func__ << endl;
   
   AnyMemory am1(3.0f);
   AnyMemory am2(345);
   AnyMemory am3(true);

   AnyMemory am4;
   am4.append(am1);
   am4.append(am2);
   am4.append(am3);

   assert( am4.get<float>(0) == 3.0f );
   assert( am4.get<int>(4) == 345 );
   assert( am4.get<bool>(8) == true );
}

void testGetAligned()
{
   cout << __func__ << endl;
   
   AnyMemory am = {1, 2, 4, 8};

   assert( am.getAligned<int>(0) == 1 );
   assert( am.getAligned<int>(1) == 2 );
   assert( am.getAligned<int>(2) == 4 );
   assert( am.getAligned<int>(3) == 8 );
}

void testGetAlignedPointer()
{
   cout << __func__ << endl;
   
   AnyMemory am = {1, 2, 3, 4};

   assert( am.getAlignedPointer<int>(0) != nullptr );
   assert( am.getAlignedPointer<int>(1) != nullptr );
   assert( am.getAlignedPointer<int>(2) != nullptr );
   assert( am.getAlignedPointer<int>(3) != nullptr );
}

void testGetAlignedSafe()
{
   cout << __func__ << endl;
   
   AnyMemory am = {1, 2, 3, 4};

   assert( am.getAlignedSafe<int>(0) != nullptr );
   assert( am.getAlignedSafe<int>(1) != nullptr );
   assert( am.getAlignedSafe<int>(2) != nullptr );
   assert( am.getAlignedSafe<int>(3) != nullptr );
   assert( am.getAlignedSafe<int>(4) == nullptr );
}

void testSetAligned()
{
   cout << __func__ << endl;
   
   AnyMemory am = {1, 2, 3, 4};

   am.setAligned(0, 5);
   am.setAligned(1, 6);
   am.setAligned(2, 7);
   am.setAligned(3, 8);

   assert( am.getAligned<int>(0) == 5 );
   assert( am.getAligned<int>(1) == 6 );
   assert( am.getAligned<int>(2) == 7 );
   assert( am.getAligned<int>(3) == 8 );
}

void testSetAlignedSafe()
{
   cout << __func__ << endl;
   
   AnyMemory am = {1, 2, 3, 4};

   assert( am.setAlignedSafe(0, 5) );
   assert( am.setAlignedSafe(1, 6) );
   assert( am.setAlignedSafe(2, 7) );
   assert( am.setAlignedSafe(3, 8) );
   assert(!am.setAlignedSafe(4, 9) );

   assert( am.getAlignedSafe<int>(0) != nullptr );
   assert( am.getAlignedSafe<int>(1) != nullptr );
   assert( am.getAlignedSafe<int>(2) != nullptr );
   assert( am.getAlignedSafe<int>(3) != nullptr );
   assert( am.getAlignedSafe<int>(4) == nullptr );
}

void testCapacity()
{
   cout << __func__ << endl;
   
   AnyMemory am = {1, 2, 3, 4};

   assert( am.getCapacity<int>() == 4 );
   assert( am.getCapacity<char>() == 16 );
   assert( am.getCapacity<long long>() == 2 );
   assert( am.getCapacity<int[4]>() == 1 );
}

void testEquals()
{
   cout << __func__ << endl;
   
   AnyMemory am1 = {1, 2, 3};
   AnyMemory am2 = {3, 4};
   AnyMemory am3 = {1, 2};
   AnyMemory am4 = {3, 4};

   assert( am1 != am2 );
   assert( am1 != am3 );
   assert( am1 != am4 );

   assert( am2 != am3 );
   assert( am2 == am4 );

   assert( am3 != am4 );
}

void testHashCode()
{
   cout << __func__ << endl;
   
   AnyMemory am1 = {1, 2, 3};
   AnyMemory am2 = {3, 4};
   AnyMemory am3 = {1, 2};
   AnyMemory am4 = {3, 4};

   assert( am1.hashCode() != am2.hashCode() );
   assert( am1.hashCode() != am3.hashCode() );
   assert( am1.hashCode() != am4.hashCode() );

   assert( am2.hashCode() != am3.hashCode() );
   assert( am2.hashCode() == am4.hashCode() );

   assert( am3.hashCode() != am4.hashCode() );
}

void testCompareTo()
{
   cout << __func__ << endl;
   
   AnyMemory am1 = {1};
   AnyMemory am2 = {1, 2};
   AnyMemory am3 = {2, 1};
   AnyMemory am4 = {1, 3};

   assert( am1 < am2 );
   assert( am1 < am3 );
   assert( am1 < am4 );

   assert( am2 < am3 );
   assert( am2 < am4 );

   assert( am3 > am4 );
}

int main()
{
   testValueConstructor();
   testDefaultConstructor();
   testCopyConstructor();
   testInitializerListConstructor();
   testAdd();
   testInputOperator();
   testSetSize();
   testExpand();
   testExists();
   testGetSafe();
   testSetSafe();
   testPointer();
   testToString();
   testAppend();
   testGetAligned();
   testGetAlignedPointer();
   testGetAlignedSafe();
   testSetAligned();
   testSetAlignedSafe();
   testCapacity();
   testEquals();
   testHashCode();
   testCompareTo();

   cout << "ALL TESTS PASS" << endl;

   return 0;
}

#include <assert.h>
#include <AnyMemory.h>

using namespace std;

//*****************************************************************************
// TEST CASES
//*****************************************************************************

void testValueConstructor()
{
   cout << "Running " << __func__ << "() ... ";

   AnyMemory am(3.0f);

   assert( am.getSize() == 4 );
   assert( am.get<float>(0) == 3.0f );

   cout << "Pass" << endl;
}

void testDefaultConstructor()
{
   cout << "Running " << __func__ << "() ... ";

   AnyMemory am;

   assert( am.getSize() == 0 );

   cout << "Pass" << endl;
}

void testCopyConstructor()
{
   cout << "Running " << __func__ << "() ... ";

   AnyMemory am1(3.0f);
   AnyMemory am2(am1);

   assert( am2.getSize() == 4 );
   assert( am2.get<float>(0) == 3.0f );

   cout << "Pass" << endl;
}

void testInitializerListConstructor()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am = {1, 2, 4, 8};

   assert( am.getSize() == 16 );
   assert( am.get<int>(0) == 1 );
   assert( am.get<int>(4) == 2 );

   cout << "Pass" << endl;
}

void testAdd()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am;

   am.add(1);
   am.add(2);

   assert( am.getSize() == 8 );
   assert( am.get<int>(0) == 1 );
   assert( am.get<int>(4) == 2 );

   cout << "Pass" << endl;
}

void testInputOperator()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am;
   am << 345 << 4.0f;

   assert( am.getSize() == 8 );
   assert( am.get<int>(0) == 345 );
   assert( am.get<float>(4) == 4.0f );

   cout << "Pass" << endl;
}

void testSetSize()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am;

   assert( am.getSize() == 0 );

   am.setSize( 1024 );

   assert( am.getSize() == 1024 );

   cout << "Pass" << endl;
}

void testExpand()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am;

   assert( am.getSize() == 0 );

   am.expand( 4 );

   assert( am.getSize() == 4 );

   am.expand( 4 );

   assert( am.getSize() == 8 );   

   cout << "Pass" << endl;
}

void testExists()
{
   cout << "Running " << __func__ << "() ... ";
   
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

   cout << "Pass" << endl;
}

void testGetSafe()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am(345);

   int *x = am.getSafe<int>(0);

   assert( x != nullptr );
   assert( *x == 345 );

   int *y = am.getSafe<int>(1);

   assert( y == nullptr );

   cout << "Pass" << endl;
}

void testSetSafe()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am = {1, 2};

   assert( am.setSafe<int>(0, 4) );
   assert( am.setSafe<int>(4, 8) );
   assert( !am.setSafe<int>(5, 16) );

   assert( am.get<int>(0) == 4 );
   assert( am.get<int>(4) == 8 );

   cout << "Pass" << endl;
}

void testPointer()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am(345);

   int *x = am.getPointer<int>(0);

   assert( x != nullptr );
   assert( *x == 345 );

   *x = 698;

   assert( am.get<int>(0) == 698 );

   cout << "Pass" << endl;
}

void testToString()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am(345);

   cout << am << " ";

   cout << "Pass" << endl;
}

void testAppend()
{
   cout << "Running " << __func__ << "() ... ";
   
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

   cout << "Pass" << endl;
}

void testSub()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am1 = {1, 2, 4, 8};
   AnyMemory am2 = am1.sub(4, 8);

   assert( am2.getSize() == 8 );
   assert( am2.get<int>(0) == 2 );
   assert( am2.get<int>(4) == 4 );

   cout << "Pass" << endl;
}

void testGetAligned()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am = {1, 2, 4, 8};

   assert( am.getAligned<int>(0) == 1 );
   assert( am.getAligned<int>(1) == 2 );
   assert( am.getAligned<int>(2) == 4 );
   assert( am.getAligned<int>(3) == 8 );

   cout << "Pass" << endl;
}

void testGetAlignedPointer()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am = {1, 2, 3, 4};

   assert( am.getAlignedPointer<int>(0) != nullptr );
   assert( am.getAlignedPointer<int>(1) != nullptr );
   assert( am.getAlignedPointer<int>(2) != nullptr );
   assert( am.getAlignedPointer<int>(3) != nullptr );

   cout << "Pass" << endl;
}

void testGetAlignedSafe()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am = {1, 2, 3, 4};

   assert( am.getAlignedSafe<int>(0) != nullptr );
   assert( am.getAlignedSafe<int>(1) != nullptr );
   assert( am.getAlignedSafe<int>(2) != nullptr );
   assert( am.getAlignedSafe<int>(3) != nullptr );
   assert( am.getAlignedSafe<int>(4) == nullptr );

   cout << "Pass" << endl;
}

void testSetAligned()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am = {1, 2, 3, 4};

   am.setAligned(0, 5);
   am.setAligned(1, 6);
   am.setAligned(2, 7);
   am.setAligned(3, 8);

   assert( am.getAligned<int>(0) == 5 );
   assert( am.getAligned<int>(1) == 6 );
   assert( am.getAligned<int>(2) == 7 );
   assert( am.getAligned<int>(3) == 8 );

   cout << "Pass" << endl;
}

void testSetAlignedSafe()
{
   cout << "Running " << __func__ << "() ... ";
   
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

   cout << "Pass" << endl;
}

void testCapacity()
{
   cout << "Running " << __func__ << "() ... ";
   
   AnyMemory am = {1, 2, 3, 4};

   assert( am.getCapacity<int>() == 4 );
   assert( am.getCapacity<char>() == 16 );
   assert( am.getCapacity<long long>() == 2 );
   assert( am.getCapacity<int[4]>() == 1 );

   cout << "Pass" << endl;
}

void testEquals()
{
   cout << "Running " << __func__ << "() ... ";
   
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

   cout << "Pass" << endl;
}

void testHashCode()
{
   cout << "Running " << __func__ << "() ... ";
   
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

   cout << "Pass" << endl;
}

void testCompareTo()
{
   cout << "Running " << __func__ << "() ... ";
   
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

   cout << "Pass" << endl;
}

int main()
{
   cout << "Starting " << __FILE__ << "..." << endl;

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
   testSub();
   testGetAligned();
   testGetAlignedPointer();
   testGetAlignedSafe();
   testSetAligned();
   testSetAlignedSafe();
   testCapacity();
   testEquals();
   testHashCode();
   testCompareTo();

   cout << __FILE__ << " has ran successfully." << endl;

   return 0;
}

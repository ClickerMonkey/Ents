#include <assert.h>
#include <EntityCore.h>

using namespace std;

size_t C1 = EntityCore::newComponent<int>("C1", 1);
size_t C2 = EntityCore::newComponent<int>("C2", 2);
size_t C3 = EntityCore::newComponent<int>("C3", 3);
size_t C4 = EntityCore::newComponent<int>("C4", 4);

size_t U1 = EntityCore::addController(nullptr);
size_t U2 = EntityCore::addController(nullptr);
size_t U3 = EntityCore::addController(nullptr);

size_t V1 = EntityCore::newView();
size_t V2 = EntityCore::newView();

void testConstructor()
{	
  	cout << "Running " << __func__ << "() ... ";
  
  	EntityType et(0, nullptr, {C1, C2, C3}, {U1, U2}, View::NONE, {1, 2, 3});

  	assert( et.getComponentCount() == 3 );
  	assert( et.hasComponent(C1) );
  	assert( et.hasComponent(C2) );
  	assert( et.hasComponent(C3) );
  	assert(!et.hasComponent(C4) );

  	assert( et.getControllerCount() == 2 );
  	assert( et.hasController(U1) );
  	assert( et.hasController(U2) );
  	assert(!et.hasController(U3) );

  	cout << "Pass" << endl;
}

void testAdd()
{	
  	cout << "Running " << __func__ << "() ... ";
  
	EntityType et(0, nullptr, {}, {}, View::NONE, AnyMemory());

  	assert( et.getComponentCount() == 0 );

  	assert( et.add(C1) );

  	assert( et.getComponentCount() == 1 );
  	assert( et.hasComponent(C1) );
  	assert( et.getDefaultComponents().get<int>(0) == 1 );
  	assert(!et.hasComponent(C2) );

  	assert( et.add(C2, 22) );

	assert( et.hasComponent(C2) );
  	assert( et.getComponentCount() == 2 );
  	assert( et.getDefaultComponents().get<int>(4) == 22 );

  	assert(!et.add(C1, 11) );

  	cout << "Pass" << endl;
}

void testAddController()
{
	cout << "Running " << __func__ << "() ... ";
  
	EntityType et(0, nullptr, {}, {}, View::NONE, AnyMemory());

  	assert( et.getControllerCount() == 0 );
  	assert(!et.hasController(U1) );

  	assert( et.addController(U1) );

  	assert( et.hasController(U1) );
  	assert( et.getControllerCount() == 1 );
  	assert(!et.addController(U1) );
  	assert(!et.hasController(U2) );

  	assert( et.addController(U2) );

	assert( et.hasController(U2) );
  	assert( et.getControllerCount() == 2 );

  	cout << "Pass" << endl;
}

void testAlias()
{
	cout << "Running " << __func__ << "() ... ";
  
	EntityType et(0, nullptr, {C1, C2}, {}, View::NONE, {1, 2});

	assert( et.hasComponent(C1) );
	assert( et.hasComponent(C2) );
	assert(!et.hasComponent(C3) );
	assert(!et.hasComponent(C4) );

	assert( et.getComponentOffsetSafe(C1) == 0 );
	assert( et.getComponentOffsetSafe(C2) == 1 );
	assert( et.getComponentOffsetSafe(C3) ==-1 );
	assert( et.getComponentOffsetSafe(C4) ==-1 );

	et.setComponentAlias(C1, C3);
	et.setComponentAlias(C2, C4);

	assert( et.hasComponent(C3) );
	assert( et.hasComponent(C4) );

	assert( et.getComponentOffsetSafe(C1) == 0 );
	assert( et.getComponentOffsetSafe(C2) == 1 );
	assert( et.getComponentOffsetSafe(C3) == 0 );
	assert( et.getComponentOffsetSafe(C4) == 1 );

  	cout << "Pass" << endl;
}

void testSetDefaultValue()
{
	cout << "Running " << __func__ << "() ... ";

	EntityType et(0, nullptr, {C1, C2}, {}, View::NONE, {1, 2});

	assert( et.getDefaultComponents().get<int>(0) == 1 );

	assert( et.setDefaultValue(C1, 11) );
	assert(!et.setDefaultValue(C3, 33) );

	assert( et.getDefaultComponents().get<int>(0) == 11 );

  	cout << "Pass" << endl;
}

void testExtend()
{
	cout << "Running " << __func__ << "() ... ";

	EntityType *e1 = new EntityType(0, nullptr, {C1, C2}, {}, View::NONE, {1, 2});
	EntityType *e2 = e1->extend(1);

	assert( e1 != e2 );
	assert( e1->getParent() == nullptr );
	assert( e2->getParent() == e1 );

	assert( e1->getComponents().getIds() == e2->getComponents().getIds() );
	assert( e1->getControllers().getIds() == e2->getControllers().getIds() );
	assert( e1->getDefaultComponents() == e2->getDefaultComponents() );

	assert(!e1->isCustom() );
	assert(!e2->isCustom() );

	delete e2;
	delete e1;

  	cout << "Pass" << endl;
}

int main()
{
	cout << "Starting " << __FILE__ << "..." << endl;

  	testConstructor();
  	testAdd();
  	testAddController();
  	testAlias();
  	testSetDefaultValue();
  	testExtend();

	cout << __FILE__ << " has ran successfully." << endl;

	return 0;
}
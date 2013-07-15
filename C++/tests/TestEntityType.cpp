#include <assert.h>
#include <EntityCore.h>

using namespace std;

EntityCore Core;

Component<int> C1 = Core.newComponent("C1", 1);
Component<int> C2 = Core.newComponent("C2", 2);
Component<int> C3 = Core.newComponent("C3", 3);
Component<int> C4 = Core.newComponent("C4", 4);

size_t U1 = Core.newController();
size_t U2 = Core.newController();
size_t U3 = Core.newController();

size_t V1 = Core.newView();
size_t V2 = Core.newView();

void testConstructor()
{	
  	cout << "Running " << __func__ << "() ... ";
  
  	EntityType et(&Core, 0, nullptr, {C1.id, C2.id, C3.id}, {U1, U2}, View::NONE, {1, 2, 3});

  	assert( et.getComponentCount() == 3 );
  	assert( et.hasComponent(C1.id) );
  	assert( et.hasComponent(C2.id) );
  	assert( et.hasComponent(C3.id) );
  	assert(!et.hasComponent(C4.id) );

  	assert( et.getControllerCount() == 2 );
  	assert( et.hasController(U1) );
  	assert( et.hasController(U2) );
  	assert(!et.hasController(U3) );

  	cout << "Pass" << endl;
}

void testAdd()
{	
  	cout << "Running " << __func__ << "() ... ";
  
	EntityType et(&Core, 0, nullptr, {}, {}, View::NONE, AnyMemory());

  	assert( et.getComponentCount() == 0 );

  	assert( et.add(C1) );

  	assert( et.getComponentCount() == 1 );
  	assert( et.hasComponent(C1.id) );
  	assert( et.getDefaultComponents().get<int>(0) == 1 );
  	assert(!et.hasComponent(C2.id) );

  	assert( et.add(C2, 22) );

	assert( et.hasComponent(C2.id) );
  	assert( et.getComponentCount() == 2 );
  	assert( et.getDefaultComponents().get<int>(4) == 22 );

  	assert(!et.add(C1, 11) );

  	cout << "Pass" << endl;
}

void testAddController()
{
	cout << "Running " << __func__ << "() ... ";
  
	EntityType et(&Core, 0, nullptr, {}, {}, View::NONE, AnyMemory());

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
  
	EntityType et(&Core, 0, nullptr, {C1.id, C2.id}, {}, View::NONE, {1, 2});

	assert( et.hasComponent(C1.id) );
	assert( et.hasComponent(C2.id) );
	assert(!et.hasComponent(C3.id) );
	assert(!et.hasComponent(C4.id) );

	assert( et.getComponentOffsetSafe(C1.id) == 0 );
	assert( et.getComponentOffsetSafe(C2.id) == 1 );
	assert( et.getComponentOffsetSafe(C3.id) ==-1 );
	assert( et.getComponentOffsetSafe(C4.id) ==-1 );

	et.setComponentAlias(C1.id, C3.id);
	et.setComponentAlias(C2.id, C4.id);

	assert( et.hasComponent(C3.id) );
	assert( et.hasComponent(C4.id) );

	assert( et.getComponentOffsetSafe(C1.id) == 0 );
	assert( et.getComponentOffsetSafe(C2.id) == 1 );
	assert( et.getComponentOffsetSafe(C3.id) == 0 );
	assert( et.getComponentOffsetSafe(C4.id) == 1 );

  	cout << "Pass" << endl;
}

void testSetDefaultValue()
{
	cout << "Running " << __func__ << "() ... ";

	EntityType et(&Core, 0, nullptr, {C1.id, C2.id}, {}, View::NONE, {1, 2});

	assert( et.getDefaultComponents().get<int>(0) == 1 );

	assert( et.setDefaultValue(C1, 11) );
	assert(!et.setDefaultValue(C3, 33) );

	assert( et.getDefaultComponents().get<int>(0) == 11 );

  	cout << "Pass" << endl;
}

void testExtend()
{
	cout << "Running " << __func__ << "() ... ";

	EntityType *e1 = new EntityType(&Core, 0, nullptr, {C1.id, C2.id}, {}, View::NONE, {1, 2});
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
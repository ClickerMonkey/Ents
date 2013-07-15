#include <assert.h>
#include <Entity.h>

using namespace std;

EntityCore Core;

// Component Creation
Component<float> LEFT 	= Core.newComponent("left", 0.0f);
Component<float> RIGHT 	= Core.newComponent("right", 0.0f);
Component<float> SPEED 	= Core.newComponent("speed", 0.0f);

// Controller, View, Dynamic Component, & EntityType Creation
ComponentGet<float> CENTER 	= Core.newComponentGet<float>("center", {LEFT.id, RIGHT.id},
	[](Entity &e) -> float {
		return ( e(LEFT) + e(RIGHT) ) * 0.5f;
	}
);

size_t MOTION = Core.addController({LEFT.id, RIGHT.id, SPEED.id},
	[](Entity &e, void *updateState) {
		float dt = *((float*)updateState);
		float s = e(SPEED);

		e(LEFT) += s * dt;
		e(RIGHT) += s * dt;
	}
);

size_t EXTENT_VIEW = Core.addView({LEFT.id, RIGHT.id}, 
	[](Entity &e, void *drawState) {
		string graphics = *((string*)drawState);
		cout << "Drawing extent at {" << e(LEFT) << "->" << e(RIGHT) << "} with " << graphics << "." << endl;
	}
);

EntityType* EXTENT = Core.newEntityType({LEFT.id, RIGHT.id}, {MOTION}, EXTENT_VIEW);

void testConstructorId()
{
   	cout << "Running " << __func__ << "() ... ";

   	Entity e(EXTENT);

   	assert( e.has(LEFT) );
   	assert( e.has(RIGHT) );
   	assert( e.hasController(MOTION) );

	cout << "Pass" << endl;
}

void testConstructorReference()
{
  	cout << "Running " << __func__ << "() ... ";	

	EntityType *type = Core.getEntityType(EXTENT->getId());

	Entity e(type);

   	assert( e.has(LEFT) );
   	assert( e.has(RIGHT) );
   	assert( e.hasController(MOTION) );
   	assert( e.getEntityType() == type );

	cout << "Pass" << endl;
}

void testCustomEntity()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(&Core);

	assert( e.isCustom() );

	assert(!e.has(LEFT) );

	e.add(LEFT);

	assert( e.has(LEFT) );

	e.set(LEFT, 3.0f);

	assert( e.get(LEFT) == 3.0f );

	cout << "Pass" << endl;
}

void testCustomEntityDefined()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(&Core, {LEFT.id}, {}, View::NONE);

	assert( e.has(LEFT) );
	assert(!e.has(RIGHT) );
	assert(!e.hasController(MOTION) );

	cout << "Pass" << endl;
}

void testSetMethod()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	e(LEFT, 3.5f);

	assert( e.get(LEFT) == 3.5f );

	e(LEFT) = 3.467f;

	assert( e.get(LEFT) == 3.467f );

	cout << "Pass" << endl;
}

void testToString()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	e(LEFT) = 3.0f;
	e(RIGHT) = 5.5f;

	cout << e << " ";

	cout << "Pass" << endl;
}

void testPtr()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	float* l = e.ptr(LEFT);
	float* r = e.ptr(RIGHT);

	assert( l != nullptr );
	assert( r != nullptr );

	assert( *l != 3.5f );
	assert( *r !=-2.0f );

	*l = 3.5f;
	*r =-2.0f;

	assert( *l == 3.5f );
	assert( *r ==-2.0f );

	assert( e.get(LEFT) == 3.5f );
	assert( e.get(RIGHT) ==-2.0f );

	cout << "Pass" << endl;
}

void testPtrSafe()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(&Core, {LEFT.id, RIGHT.id}, {}, View::NONE);

	assert( e.has(LEFT) );
	assert( e.has(RIGHT) );
	assert(!e.has(SPEED) );

	assert( e.ptrs(LEFT) != nullptr );
	assert( e.ptrs(RIGHT) != nullptr );
	assert( e.ptrs(SPEED) == nullptr );

	cout << "Pass" << endl;
}

void testGet()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	e.get(LEFT) = 3.0f;
	e.get(RIGHT) = 5.0f;

	assert( e.get(LEFT) == 3.0f );
	assert( e.get(RIGHT) == 5.0f );

	cout << "Pass" << endl;
}

void testGetDynamic()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	e(LEFT) = 2.0f;
	e(RIGHT) = 3.0f;

	float center = e(CENTER);

	assert( center == 2.5f );

	cout << "Pass" << endl;
}

void testGetDynamicMissing()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(&Core, {LEFT.id}, {}, View::NONE);

	assert( e(LEFT, 2.0f) );
	assert(!e(RIGHT, 3.0f) );

	float center = e.gets(CENTER);

	assert( center == 0.0f );

	cout << "Pass" << endl;
}

void testGetSafe()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(&Core, {LEFT.id}, {}, View::NONE);

	e(LEFT, 2.0f);
	e(RIGHT, 3.5f);

	assert( e.gets(LEFT, 0.0f) == 2.0f );
	assert( e.gets(RIGHT, 0.0f) == 0.0f );

	cout << "Pass" << endl;
}

void testGrab()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(&Core, {LEFT.id}, {}, View::NONE);

	e(LEFT, 2.0f);
	e(RIGHT, 3.5f);

	float left = 0.0f;
	assert( e.grab(LEFT, &left) );
	assert( left == 2.0f );

	float right = 0.0f;
	assert(!e.grab(RIGHT, &right) );
	assert( right == 0.0f );

	cout << "Pass" << endl;
}

void testHasComponents()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(&Core, {LEFT.id, RIGHT.id}, {}, View::NONE);

	assert( e.has({LEFT.id}) );
	assert( e.has({RIGHT.id}) );
	assert( e.has({RIGHT.id, LEFT.id}) );
	assert(!e.has({SPEED.id}) );
	assert(!e.has({LEFT.id,SPEED.id}) );

	cout << "Pass" << endl;
}

void testHasController()
{
	cout << "Running " << __func__ << "() ... ";

	Entity a(&Core, {LEFT.id}, {MOTION}, View::NONE);

	assert( a.hasController(MOTION) );

	Entity b(&Core, {LEFT.id}, {}, View::NONE);

	assert(!b.hasController(MOTION) );

	cout << "Pass" << endl;
}

void testAdd()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(&Core, {LEFT.id}, {MOTION}, View::NONE);

	assert( e.has(LEFT) );
	assert(!e.has(RIGHT) );

	e(LEFT, 2.0f);

	assert( e.add(RIGHT, 3.4f) );

	assert( e.has(RIGHT) );
	assert( e.gets(LEFT, 0.0f) == 2.0f );
	assert( e.gets(RIGHT, 0.0f) == 3.4f );
	assert( e.isCustom() );

	assert(!e.add(LEFT) );
	assert(!e.add(RIGHT) );
	assert( e.add(SPEED) );

	e(SPEED, 3.0f);

	assert( e.gets(SPEED, 0.0f) == 3.0f );

	cout << "Pass" << endl;
}

void testExpire()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	assert(!e.isExpired() );

	e.expire();

	assert( e.isExpired() );

	cout << "Pass" << endl;
}

void testVisible()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	assert( e.isVisible() );

	e.hide();

	assert(!e.isVisible() );

	e.show();

	assert( e.isVisible() );

	cout << "Pass" << endl;
}

// testVisibleDraw() 
Component<int> DRAWS = Core.newComponent("draws", 0);

size_t DRAWS_VIEW = Core.addView({DRAWS.id}, 
	[](Entity &e, void *drawState) {
		e(DRAWS)++;
	}
);

EntityType* DRAWS_TYPE = Core.newEntityType({DRAWS.id}, {}, DRAWS_VIEW);

void testVisibleDraw()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(DRAWS_TYPE);

	assert( e.get(DRAWS) == 0 );

	e.draw(nullptr);

	assert( e.get(DRAWS) == 1 );

	e.hide();
	e.draw(nullptr);

	assert( e.get(DRAWS) == 1 );

	e.show();
	e.draw(nullptr);

	assert( e.get(DRAWS) == 2 );

	cout << "Pass" << endl;
}

void testSetView()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(DRAWS_TYPE);

	assert( e.hasView() );
	assert( e.getView() == DRAWS_VIEW );
	assert(!e.isCustom() );

	e.setView( View::NONE );

	assert( e.isCustom() );
	assert(!e.hasView() );
	assert( e.getView() == View::NONE );
	
	cout << "Pass" << endl;
}

void testEnabled()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	assert( e.isEnabled() );

	e.disable();

	assert(!e.isEnabled() );

	e.enable();

	assert( e.isEnabled() );

	cout << "Pass" << endl;
}

// testVisibleDraw() 
Component<int> UPDATES = Core.newComponent("draws", 0);

size_t UPDATES_CONTROLLER = Core.addController({UPDATES.id}, 
	[] (Entity& e, void *updateState) {
		e(UPDATES)++;
	}
);

EntityType* UPDATES_TYPE = Core.newEntityType({UPDATES.id}, {UPDATES_CONTROLLER}, View::NONE);

void testEnabledUpdate()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(UPDATES_TYPE);

	assert( e.get(UPDATES) == 0 );

	e.update(nullptr);

	assert( e.get(UPDATES) == 1 );

	e.disable();
	e.update(nullptr);

	assert( e.get(UPDATES) == 1 );

	e.enable();
	e.update(nullptr);

	assert( e.get(UPDATES) == 2 );

	cout << "Pass" << endl;
}

void testControllerEnabled()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(UPDATES_TYPE);

	assert( e.get(UPDATES) == 0 );

	e.update(nullptr);

	assert( e.get(UPDATES) == 1 );

	assert( e.isControllerEnabled(UPDATES_CONTROLLER) );

	e.disable( UPDATES_CONTROLLER );

	assert( !e.isControllerEnabled(UPDATES_CONTROLLER) );

	e.update(nullptr);

	assert( e.get(UPDATES) == 1 );

	e.enable( UPDATES_CONTROLLER );

	assert( e.isControllerEnabled(UPDATES_CONTROLLER) );

	e.update(nullptr);

	assert( e.get(UPDATES) == 2 );

	cout << "Pass" << endl;
}

void testAddController()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(&Core, {UPDATES.id}, {}, View::NONE);

	assert(!e.hasController(UPDATES_CONTROLLER) );
	assert( e.get(UPDATES) == 0 );

	e.update(nullptr);

	assert( e.get(UPDATES) == 0 );

	e.addController(UPDATES_CONTROLLER);
	assert( e.hasController(UPDATES_CONTROLLER) );

	e.update(nullptr);

	assert( e.get(UPDATES) == 1 );

	cout << "Pass" << endl;
}

void testClone()
{
	cout << "Running " << __func__ << "() ... ";

	Entity *a = new Entity(EXTENT);
	a->set(LEFT, 2.0f);
	a->set(RIGHT, 3.7f);

	Entity *b = a->clone();

	assert( a != b );
	assert( b->get(LEFT) == 2.0f );
	assert( b->get(RIGHT) == 3.7f );

	a->set(LEFT, -3.5f);
	a->set(RIGHT, 5.6f);

	assert( b->get(LEFT) == 2.0f );
	assert( b->get(RIGHT) == 3.7f );

	delete b;
	delete a;

	cout << "Pass" << endl;
}

void testSetMap()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);
	e.set({{LEFT.id,3.0f},{RIGHT.id,4.5f}});

	assert( e.get(LEFT) == 3.0f );
	assert( e.get(RIGHT) == 4.5f );
	
	cout << "Pass" << endl;
}

void testEquals()
{
	cout << "Running " << __func__ << "() ... ";

	Entity a(EXTENT, {{LEFT.id,3.0f},{RIGHT.id,4.5f}});
	Entity b(EXTENT, {{LEFT.id,3.0f},{RIGHT.id,4.5f}});
	Entity c(EXTENT, {{LEFT.id,3.1f},{RIGHT.id,4.5f}});
	Entity d(&Core);
	Entity e(&Core);

	assert( a == b );
	assert( a != c );
	assert( a != d );
	assert( a != e );

	assert( b != c );
	assert( b != d );
	assert( b != e );

	assert( c != d );
	assert( c != e );

	assert( d != e ); // Their types are different, maybe I should do more than testing address?

	cout << "Pass" << endl;
}

void testHashCode()
{
	cout << "Running " << __func__ << "() ... ";

	Entity a(EXTENT, {{LEFT.id,3.0f},{RIGHT.id,4.5f}});
	Entity b(EXTENT, {{LEFT.id,3.0f},{RIGHT.id,4.5f}});
	Entity c(EXTENT, {{LEFT.id,3.1f},{RIGHT.id,4.5f}});
	Entity d(&Core);
	Entity e(&Core);

	assert( a.hashCode() == b.hashCode() );
	assert( a.hashCode() != c.hashCode() );
	assert( a.hashCode() != d.hashCode() );
	assert( a.hashCode() != e.hashCode() );

	assert( b.hashCode() != c.hashCode() );
	assert( b.hashCode() != d.hashCode() );
	assert( b.hashCode() != e.hashCode() );

	assert( c.hashCode() != d.hashCode() );
	assert( c.hashCode() != e.hashCode() );

	assert( d.hashCode() == e.hashCode() );
	
	cout << "Pass" << endl;
}

void testCompareTo()
{
	cout << "Running " << __func__ << "() ... ";

	Entity a(EXTENT, {{LEFT.id,3.0f},{RIGHT.id,4.5f}});
	Entity b(EXTENT, {{LEFT.id,3.1f},{RIGHT.id,4.5f}});
	Entity c(EXTENT, {{LEFT.id,3.0f},{RIGHT.id,4.6f}});
	Entity d(&Core);
	
	assert( a < b );
	assert( a < b );
	assert( a > d ); // Entities without type go first
	assert( b > c );
	assert( b > d );
	assert( c > d );

	cout << "Pass" << endl 	;
}

int main()
{
	cout << "Starting " << __FILE__ << "..." << endl;

	testConstructorId();
	testConstructorReference();
	testCustomEntity();
	testCustomEntityDefined();
	testSetMethod();
	testToString();
	testPtr();
	testPtrSafe();
	testGet();
	testGetDynamic();
	testGetDynamicMissing();
	testGetSafe();
	testGrab();
	testHasComponents();
	testHasController();
	testAdd();
	testExpire();
	testVisible();
	testVisibleDraw();
	testSetView();
	testEnabled();
	testEnabledUpdate();
	testControllerEnabled();
	testAddController();
	testClone();
	testSetMap();
	testEquals();
	testHashCode();
	testCompareTo();

   	cout << __FILE__ << " has ran successfully." << endl;

	return 0;
}
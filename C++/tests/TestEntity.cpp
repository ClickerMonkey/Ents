#include <assert.h>
#include <Entity.h>

using namespace std;

// Component Creation
size_t LEFT 	= EntityCore::newComponent<float>("left", 0.0f);
size_t RIGHT 	= EntityCore::newComponent<float>("right", 0.0f);
size_t SPEED 	= EntityCore::newComponent<float>("speed", 0.0f);

// Controller, View, & Dynamic Component Definitions
ControllerFunction MotionController({LEFT, RIGHT, SPEED}, 
	[](Entity *e, void *updateState) {
		float dt = *((float*)updateState);
		float *l = e->ptr<float>(LEFT);
		float *r = e->ptr<float>(RIGHT);
		float s = e->get<float>(SPEED);
		*l += s * dt;
		*r += s * dt;
	}
);
DynamicComponentFunction<float> CenterDynamicComponent({LEFT, RIGHT}, 
	[](Entity *e, float &out) -> float& {
	    float l = e->get<float>(LEFT);
	    float r = e->get<float>(RIGHT);
	    return ( out = (l + r) * 0.5f );
	}
);
ViewFunction ExtentView({LEFT,RIGHT}, 
	[](Entity *e, void *drawState) {
		string graphics = *((string*)drawState);
		float l = e->get<float>(LEFT);
		float r = e->get<float>(RIGHT);
		cout << "Drawing extent at {" << l << "->" << r << "} with " << graphics << "." << endl;
	}
);

// Controller, View, Dynamic Component, & EntityType Creation
size_t CENTER 		= EntityCore::newDynamicComponent<float>("center", &CenterDynamicComponent);
size_t MOTION 		= EntityCore::addController(&MotionController);
size_t EXTENT_VIEW 	= EntityCore::addView(&ExtentView);
size_t EXTENT 		= EntityCore::newEntityType({LEFT, RIGHT}, {MOTION}, EXTENT_VIEW);

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

	EntityType *type = EntityCore::getEntityType(EXTENT);

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

	Entity e;

	assert( e.isCustom() );

	int* x = e.ptrs<int>(325);

	assert( x == nullptr );
	assert(!e.has(LEFT) );

	e.add(LEFT);

	assert( e.has(LEFT) );

	e.set(LEFT, 3.0f);

	assert( e.get<float>(LEFT) == 3.0f );

	cout << "Pass" << endl;
}

void testCustomEntityDefined()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e({LEFT}, {}, View::NONE);

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

	assert( e.get<float>(LEFT) == 3.5f );

	cout << "Pass" << endl;
}

void testToString()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	e(LEFT, 3.0f);
	e(RIGHT, 5.5f);

	cout << e << " ";

	cout << "Pass" << endl;
}

void testPtr()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	float* l = e.ptr<float>(LEFT);
	float* r = e.ptr<float>(RIGHT);

	assert( l != nullptr );
	assert( r != nullptr );

	assert( *l != 3.5f );
	assert( *r !=-2.0f );

	*l = 3.5f;
	*r =-2.0f;

	assert( *l == 3.5f );
	assert( *r ==-2.0f );

	assert( e.get<float>(LEFT) == 3.5f );
	assert( e.get<float>(RIGHT) ==-2.0f );

	cout << "Pass" << endl;
}

void testPtrSafe()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e({LEFT, RIGHT}, {}, View::NONE);

	assert( e.has(LEFT) );
	assert( e.has(RIGHT) );
	assert(!e.has(SPEED) );

	assert( e.ptrs<float>(LEFT) != nullptr );
	assert( e.ptrs<float>(RIGHT) != nullptr );
	assert( e.ptrs<float>(SPEED) == nullptr );

	cout << "Pass" << endl;
}

void testGet()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	e.get<float>(LEFT) = 3.0f;
	e.get<float>(RIGHT) = 5.0f;

	assert( e.get<float>(LEFT) == 3.0f );
	assert( e.get<float>(RIGHT) == 5.0f );

	cout << "Pass" << endl;
}

void testGetDynamic()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);

	e(LEFT, 2.0f);
	e(RIGHT, 3.0f);

	float center = 0.0f;
	e.get(CENTER, center);

	assert( center == 2.5f );

	cout << "Pass" << endl;
}

void testGetDynamicMissing()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e({LEFT}, {}, View::NONE);

	assert( e(LEFT, 2.0f) );
	assert(!e(RIGHT, 3.0f) );

	float center = 0.1234f;
	e.get(CENTER, center);

	assert( center == 0.1234f );

	cout << "Pass" << endl;
}

void testGetSafe()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e({LEFT}, {}, View::NONE);

	e(LEFT, 2.0f);
	e(RIGHT, 3.5f);

	assert( e.gets(LEFT, 0.0f) == 2.0f );
	assert( e.gets(RIGHT, 0.0f) == 0.0f );

	cout << "Pass" << endl;
}

void testGrab()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e({LEFT}, {}, View::NONE);

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

	Entity e({LEFT, RIGHT}, {}, View::NONE);

	assert( e.has({LEFT}) );
	assert( e.has({RIGHT}) );
	assert( e.has({RIGHT, LEFT}) );
	assert(!e.has({SPEED}) );
	assert(!e.has({LEFT,SPEED}) );

	cout << "Pass" << endl;
}

void testHasController()
{
	cout << "Running " << __func__ << "() ... ";

	Entity a({LEFT}, {MOTION}, View::NONE);

	assert( a.hasController(MOTION) );

	Entity b({LEFT}, {}, View::NONE);

	assert(!b.hasController(MOTION) );

	cout << "Pass" << endl;
}

void testAdd()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e({LEFT}, {MOTION}, View::NONE);

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
size_t DRAWS = EntityCore::newComponent<int>("draws", 0);

ViewFunction DrawView({DRAWS}, 
	[](Entity *e, void *drawState) {
		e->get<int>(DRAWS)++;
	}
);

size_t DRAWS_VIEW = EntityCore::addView(&DrawView);
size_t DRAWS_TYPE = EntityCore::newEntityType({DRAWS}, {}, DRAWS_VIEW);

void testVisibleDraw()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(DRAWS_TYPE);

	assert( e.get<int>(DRAWS) == 0 );

	e.draw(nullptr);

	assert( e.get<int>(DRAWS) == 1 );

	e.hide();
	e.draw(nullptr);

	assert( e.get<int>(DRAWS) == 1 );

	e.show();
	e.draw(nullptr);

	assert( e.get<int>(DRAWS) == 2 );

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
size_t UPDATES = EntityCore::newComponent<int>("draws", 0);

ControllerFunction UpdateController({UPDATES}, 
	[](Entity *e, void *updateState) {
		e->get<int>(UPDATES)++;
	}
);

size_t UPDATES_CONTROLLER = EntityCore::addController(&UpdateController);
size_t UPDATES_TYPE = EntityCore::newEntityType({UPDATES}, {UPDATES_CONTROLLER}, View::NONE);

void testEnabledUpdate()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(UPDATES_TYPE);

	assert( e.get<int>(UPDATES) == 0 );

	e.update(nullptr);

	assert( e.get<int>(UPDATES) == 1 );

	e.disable();
	e.update(nullptr);

	assert( e.get<int>(UPDATES) == 1 );

	e.enable();
	e.update(nullptr);

	assert( e.get<int>(UPDATES) == 2 );

	cout << "Pass" << endl;
}

void testControllerEnabled()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(UPDATES_TYPE);

	assert( e.get<int>(UPDATES) == 0 );

	e.update(nullptr);

	assert( e.get<int>(UPDATES) == 1 );

	assert( e.isControllerEnabled(UPDATES_CONTROLLER) );

	e.disable( UPDATES_CONTROLLER );

	assert( !e.isControllerEnabled(UPDATES_CONTROLLER) );

	e.update(nullptr);

	assert( e.get<int>(UPDATES) == 1 );

	e.enable( UPDATES_CONTROLLER );

	assert( e.isControllerEnabled(UPDATES_CONTROLLER) );

	e.update(nullptr);

	assert( e.get<int>(UPDATES) == 2 );

	cout << "Pass" << endl;
}

void testAddController()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e({UPDATES}, {}, View::NONE);

	assert(!e.hasController(UPDATES_CONTROLLER) );
	assert( e.get<int>(UPDATES) == 0 );

	e.update(nullptr);

	assert( e.get<int>(UPDATES) == 0 );

	e.addController(UPDATES_CONTROLLER);
	assert( e.hasController(UPDATES_CONTROLLER) );

	e.update(nullptr);

	assert( e.get<int>(UPDATES) == 1 );

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
	assert( b->get<float>(LEFT) == 2.0f );
	assert( b->get<float>(RIGHT) == 3.7f );

	a->set(LEFT, -3.5f);
	a->set(RIGHT, 5.6f);

	assert( b->get<float>(LEFT) == 2.0f );
	assert( b->get<float>(RIGHT) == 3.7f );

	delete b;
	delete a;

	cout << "Pass" << endl;
}

void testSetMap()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(EXTENT);
	e.set({{LEFT,3.0f},{RIGHT,4.5f}});

	assert( e.get<float>(LEFT) == 3.0f );
	assert( e.get<float>(RIGHT) == 4.5f );
	
	cout << "Pass" << endl;
}

void testEquals()
{
	cout << "Running " << __func__ << "() ... ";

	Entity a(EXTENT, {{LEFT,3.0f},{RIGHT,4.5f}});
	Entity b(EXTENT, {{LEFT,3.0f},{RIGHT,4.5f}});
	Entity c(EXTENT, {{LEFT,3.1f},{RIGHT,4.5f}});
	Entity d;
	Entity e;

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

	Entity a(EXTENT, {{LEFT,3.0f},{RIGHT,4.5f}});
	Entity b(EXTENT, {{LEFT,3.0f},{RIGHT,4.5f}});
	Entity c(EXTENT, {{LEFT,3.1f},{RIGHT,4.5f}});
	Entity d;
	Entity e;

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

	Entity a(EXTENT, {{LEFT,3.0f},{RIGHT,4.5f}});
	Entity b(EXTENT, {{LEFT,3.1f},{RIGHT,4.5f}});
	Entity c(EXTENT, {{LEFT,3.0f},{RIGHT,4.6f}});
	Entity d;
	
	assert( a < b );
	assert( a < b );
	assert( a > d ); // Entities without type go first
	assert( b > c );
	assert( b > d );
	assert( c > d );

	cout << "Pass" << endl;
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
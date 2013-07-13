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
	testGetDynamic();
	testGetDynamicMissing();
	testGetSafe();
	testGrab();
	testHasComponents();
	testHasController();
	testAdd();


   	cout << __FILE__ << " has ran successfully." << endl;

	return 0;
}
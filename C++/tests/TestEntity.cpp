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
   cout << __func__ << endl;

   Entity e(EXTENT);

   assert( e.has(LEFT) );
   assert( e.has(RIGHT) );
   assert( e.hasController(MOTION) );
}

void testConstructorReference()
{
  	cout << __func__ << endl;	

	EntityType *type = EntityCore::getEntityType(EXTENT);

	Entity e(type);

   	assert( e.has(LEFT) );
   	assert( e.has(RIGHT) );
   	assert( e.hasController(MOTION) );
   	assert( e.getEntityType() == type );
}

void testDynamicEntity()
{
	cout << __func__ << endl;

	Entity e;

	assert( e.isCustom() );

	int* x = e.ptrs<int>(325);

	assert( x == nullptr );
	assert(!e.has(LEFT) );

	e.add(LEFT);

	assert( e.has(LEFT) );

	e.set(LEFT, 3.0f);

	assert( e.get<float>(LEFT) == 3.0f );
}

int main()
{
	testConstructorId();
	testConstructorReference();
	testDynamicEntity();

   	cout << "ALL TESTS PASS" << endl;

	return 0;
}
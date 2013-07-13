#include <assert.h>
#include <Entity.h>

using namespace std;

struct Vector { float x, y; };

size_t POSITION = EntityCore::newComponent<Vector>("position", {0.0f, 0.0f});
size_t VELOCITY = EntityCore::newComponent<Vector>("velocity", {0.0f, 0.0f});
size_t ACCELERATION = EntityCore::newComponent<Vector>("acceleration", {0.0f, 0.0f});

ControllerFunction PhysicsSimpleController({POSITION, VELOCITY}, 
	[](Entity *e, void *updateState) {
		Vector *p = e->ptr<Vector>(POSITION);
		Vector *v = e->ptr<Vector>(VELOCITY);
		float dt = *((float*)updateState);
		p->x += v->x * dt;
		p->y += v->y * dt;
	}
);

ControllerFunction PhysicsController({POSITION, VELOCITY, ACCELERATION}, 
	[](Entity *e, void *updateState) {
		Vector *p = e->ptr<Vector>(POSITION);
		Vector *v = e->ptr<Vector>(VELOCITY);
		Vector *a = e->ptr<Vector>(ACCELERATION);
		float dt = *((float*)updateState);
		p->x += v->x * dt;
		p->y += v->y * dt;
		v->x += a->x * dt;
		v->y += a->y * dt;
	}
);

size_t PHYSICS_SIMPLE = EntityCore::addController(&PhysicsSimpleController);
size_t PHYSICS = EntityCore::addController(&PhysicsController);
size_t SPRITE  = EntityCore::newEntityType({POSITION, VELOCITY}, {PHYSICS_SIMPLE}, View::NONE);

void testUpdate()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(SPRITE);
	e.enable(PHYSICS_SIMPLE);

	Vector *p = e.ptr<Vector>(POSITION);
	Vector *v = e.ptr<Vector>(VELOCITY);

	p->x = 5.0f;
	p->y = 2.0f;
	v->x = 3.0f;
	v->y =-1.5f;

	float dt = 0.5f;

	e.update(&dt);

	assert( p->x == 6.5f );
	assert( p->y == 1.25f );

	e.disable(PHYSICS_SIMPLE);
	e.update(&dt);

	assert( p->x == 6.5f );
	assert( p->y == 1.25f );

	e.enable(PHYSICS_SIMPLE);
	e.update(&dt);
	
	assert( p->x == 8.0f );
	assert( p->y == 0.5f );

  	cout << "Pass" << endl;
}

void testRequired()
{
	cout << "Running " << __func__ << "() ... ";
	
	Entity e(SPRITE);

	assert( e.has(PhysicsSimpleController.required) );
	assert( !e.has(PhysicsController.required) );

  	cout << "Pass" << endl;
}

int main()
{
   	cout << "Starting " << __FILE__ << "..." << endl;

	testUpdate();
	testRequired();

  	cout << __FILE__ << " has ran successfully." << endl;

	return 0;
}
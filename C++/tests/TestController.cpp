#include <assert.h>
#include <Entity.h>

using namespace std;

struct Vector { float x, y; };

size_t POSITION = EntityCore::newComponent<Vector>("position", {0.0f, 0.0f});
size_t VELOCITY = EntityCore::newComponent<Vector>("velocity", {0.0f, 0.0f});

ControllerFunction PhysicsController({POSITION, VELOCITY}, 
	[](Entity *e, void *updateState) {
		Vector *p = e->ptr<Vector>(POSITION);
		Vector *v = e->ptr<Vector>(VELOCITY);
		float dt = *((float*)updateState);
		p->x += v->x * dt;
		p->y += v->y * dt;
	}
);

size_t PHYSICS = EntityCore::addController(&PhysicsController);
size_t SPRITE  = EntityCore::newEntityType({POSITION, VELOCITY}, {PHYSICS}, View::NONE);

void testUpdate()
{
	cout << __func__ << endl;

	Entity e(SPRITE);
	e.enable(PHYSICS);

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

	e.disable(PHYSICS);
	e.update(&dt);

	assert( p->x == 6.5f );
	assert( p->y == 1.25f );

	e.enable(PHYSICS);
	e.update(&dt);
	
	assert( p->x == 8.0f );
	assert( p->y == 0.5f );
}

int main()
{
	testUpdate();

  	cout << "ALL TESTS PASS" << endl;

	return 0;
}
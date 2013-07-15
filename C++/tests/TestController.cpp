#include <assert.h>
#include <Entity.h>

using namespace std;

struct Vector { float x, y; 
	Vector() {}
	Vector(float m_x, float m_y) : x(m_x), y(m_y) {}
	Vector& operator+=(const Vector& a) {
		x += a.x;
		y += a.y;
		return *this;
	}
	Vector operator*(const float scalar) {
		return {x * scalar, y * scalar};
	}
};

EntityCore Core;

Component<Vector> POSITION 		= Core.newComponent("position", Vector(0.0f, 0.0f));
Component<Vector> VELOCITY 		= Core.newComponent("velocity", Vector(0.0f, 0.0f));
Component<Vector> ACCELERATION 	= Core.newComponent("acceleration", Vector(0.0f, 0.0f));

size_t PHYSICS_SIMPLE = Core.addController({POSITION.id, VELOCITY.id}, 
	[](Entity &e, void *updateState) {
		float dt = *((float*)updateState);
		e(POSITION) += e(VELOCITY) * dt;
	}
);

size_t PHYSICS = Core.addController({POSITION.id, VELOCITY.id, ACCELERATION.id},
	[](Entity &e, void *updateState) {
		float dt = *((float*)updateState);
		e(POSITION) += e(VELOCITY) * dt;
		e(VELOCITY) += e(ACCELERATION) * dt;
	}
);

EntityType* SPRITE  = Core.newEntityType({POSITION.id, VELOCITY.id}, {PHYSICS_SIMPLE}, View::NONE);

void testUpdate()
{
	cout << "Running " << __func__ << "() ... ";

	Entity e(SPRITE);
	e.enable(PHYSICS_SIMPLE);

	Vector *p = e.ptr(POSITION);
	Vector *v = e.ptr(VELOCITY);

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

int main()
{
   	cout << "Starting " << __FILE__ << "..." << endl;

	testUpdate();

  	cout << __FILE__ << " has ran successfully." << endl;

	return 0;
}
#include <assert.h>
#include <Entity.h>

using namespace std;

struct Vector { float x, y; 
	Vector() {}
	Vector(float m_x, float m_y) : x(m_x), y(m_y) {}
};

EntityCore Core;

Component<Vector>  POSITION 	= Core.newComponent("position", Vector(0.0f, 0.0f));
size_t 			   SPRITE_VIEW 	= Core.newView();
EntityType*        SPRITE  		= Core.newEntityType({POSITION.id}, {}, SPRITE_VIEW);

void testDraw()
{
   	cout << "Running " << __func__ << "() ... ";
   
	string graphics = "OpenGL";

	Entity e(SPRITE);

	e(POSITION).x = 5.1f;
	e(POSITION).y = 2.34f;

	e.draw( &graphics );

	cout << "Pass" << endl;
}

int main()
{
	cout << "Starting " << __FILE__ << "..." << endl;

	Core.setView(SPRITE_VIEW, {POSITION.id}, 
		[] (Entity &e, void *drawState) {
			string graphics = *((string*)drawState);
			Vector p = e(POSITION);
			cout << "Drawing sprite at {" << p.x << ", " << p.y << "} with " << graphics << "." << " ";
		}
	);

	testDraw();

  	cout << __FILE__ << " has ran successfully." << endl;

	return 0;
}
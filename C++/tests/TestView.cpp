#include <assert.h>
#include <Entity.h>

using namespace std;

struct Vector { float x, y; };

size_t POSITION 	= EntityCore::newComponent<Vector>("position", {0.0f, 0.0f});
size_t SPRITE_VIEW 	= EntityCore::newView();
size_t SPRITE  		= EntityCore::newEntityType({POSITION}, {}, SPRITE_VIEW);

ViewFunction SpriteView({POSITION}, 
	[](Entity *e, void *drawState) {
		string graphics = *((string*)drawState);

		Vector *p = e->ptr<Vector>(POSITION);
		
		cout << "Drawing sprite at {" << p->x << ", " << p->y << "} with " << graphics << "." << " ";
	}
);

void testDraw()
{
   	cout << "Running " << __func__ << "() ... ";
   
	string graphics = "OpenGL";

	Entity e(SPRITE);

	Vector *p = e.ptr<Vector>(POSITION);

	p->x = 5.1f;
	p->y = 2.34f;

	e.draw( &graphics );

	cout << "Pass" << endl;
}

int main()
{
	cout << "Starting " << __FILE__ << "..." << endl;

	EntityCore::setView(SPRITE_VIEW, &SpriteView);

	testDraw();

  	cout << __FILE__ << " has ran successfully." << endl;

	return 0;
}
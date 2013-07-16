#include <Method.h>
#include <Entity.h>
#include <EntityCore.h>

using namespace std;

EntityCore Core;

Component<float> LEFT 		= Core.newComponent("left",0.0f);
Component<float> RIGHT 		= Core.newComponent("right",0.0f);
EntityType* EXTENT 			= Core.newEntityType({LEFT.id, RIGHT.id}, {}, View::NONE);


void shrinkDefault(Entity &e, float amount) {
	float gap = (e.get(RIGHT) - e.get(LEFT)) * 0.5f;
	float center = gap + e.get(LEFT);
	e.set(LEFT, center - gap * amount);
	e.set(RIGHT, center + gap * amount);
}

Method<void(float)> SHRINK = Core.newMethod<void,float>("shrink", {LEFT.id, RIGHT.id}, shrinkDefault);

int main()
{
	EXTENT->addMethod(SHRINK);

	Entity e(EXTENT);

	e.set(LEFT, 2.0f);
	e.set(RIGHT, 6.0f);

	e.execute(SHRINK, 0.5f);

	cout << e(LEFT) << " -> " << e(RIGHT) << endl;

	return 0;
} 
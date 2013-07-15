#include <Method.h>

using namespace std;

class EntityCore {
};

class Entity {
public:
	const EntityCore *core;
	const int id;
	Entity(const EntityCore* m_core, const int m_id) : core(m_core), id(m_id) {}
};

int times(Entity& e, int x) {
	return x * e.id;
}

int main()
{
	EntityCore core;
	Entity e(&core, 4);

	Method<float(int)> m(&core, 0, "test", times);

	int x = m.function(e, 4);

	cout << "x: " << x << endl;

	return 0;
} 
#include <EntityCore.h>
#include <Entity.h>

struct Vector 
{
   float x, y;
   Vector() : x(0.0f), y(0.0f) { }
   Vector(float x) : x(x), y(x) { }
   Vector(float x, float y) : x(x), y(y) { }
};

int main()
{
  size_t POSITION = EntityCore::newComponent<Vector>("position", Vector(1.0f, 0.0f));
  size_t VELOCITY = EntityCore::newComponent<Vector>("velocity", Vector(0.0f, 0.0f));
  size_t ROTATION = EntityCore::newComponent<float>("rotation", 0.0f);
  size_t SCALE = EntityCore::newComponent<float>("scale", 1.0f);
  size_t SPRITE_VIEW = EntityCore::newView();
  size_t SPRITE = EntityCore::newEntityType({POSITION, VELOCITY, ROTATION}, {}, SPRITE_VIEW);
  
  Entity e(SPRITE);

  Vector *pos = e.ptr<Vector>(POSITION);
  Vector *vel = e.ptr<Vector>(VELOCITY);

  cout << "pos:" << pos->x << "," << pos->y << endl;
  cout << "vel:" << vel->x << "," << vel->y << endl;
  cout << "rot:" << e.get<float>(ROTATION) << endl;

  e.set<Vector>(POSITION, Vector(5.0f, 4.2f));
  e.set<float>(ROTATION, 45.0f);

  cout << "has scale:" << e.has(SCALE) << endl;

  vel->x = -4.0f;
  vel->y = 7.6f;
  e.add(SCALE);

  cout << "pos:" << pos->x << "," << pos->y << endl;
  cout << "vel:" << vel->x << "," << vel->y << endl;
  cout << "rot:" << e.get<float>(ROTATION) << endl;
  cout << "has scale:" << e.has(SCALE) << endl;
  cout << "scl:" << e.get<float>(SCALE) << endl;

  return 0;
}

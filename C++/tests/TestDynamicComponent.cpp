#include <assert.h>
#include <Entity.h>

// Normal Components and Entity Type
size_t RIGHT = EntityCore::newComponent<float>("right", 0.0f);
size_t LEFT = EntityCore::newComponent<float>("left", 0.0f);
size_t XAXIS = EntityCore::newEntityType({RIGHT, LEFT}, {}, View::NONE);

// Dynamic Component Function 
float& center(Entity *e, float &out)
{
  float l = e->get<float>(LEFT);
  float r = e->get<float>(RIGHT);

  return ( out = (l + r) * 0.5f );
}
DynamicComponentFunction<float> centerDynamicComponent(center);

// Add to EntityCore
size_t CENTER = EntityCore::newDynamicComponent<float>("center", &centerDynamicComponent);



//*****************************************************************************
// TEST CASES
//*****************************************************************************

void testSimple()
{
  Entity e(XAXIS);

  e.set(LEFT, 1.0f);
  e.set(RIGHT, 5.0f);

  float centerOut = 0.0f;
  e.get(CENTER, centerOut);

  assert( centerOut == 3.0f );
}

void testWithDefault()
{
  Entity e(XAXIS);

  e.set(RIGHT, 4.0f);

  float centerOut = 0.0f;
  e.get(CENTER, centerOut);

  assert( centerOut == 2.0f );
}

int main()
{  
  testSimple();
  testWithDefault();

  cout << "SUCCESS" << endl;

  return 0;
}
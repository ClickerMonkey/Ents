#include <assert.h>
#include <Entity.h>

using namespace std;

EntityCore Core;

Component<float>      LEFT   = Core.newComponent("left", 0.0f);
Component<float>      RIGHT  = Core.newComponent("right", 0.0f);
ComponentGet<float>   CENTER = Core.newComponentGet<float>("center", {LEFT.id, RIGHT.id}, 
  [](Entity &e) -> float {
    return ( e(LEFT) + e(RIGHT) ) * 0.5f;
  }
);


EntityType* XAXIS = Core.newEntityType({RIGHT.id, LEFT.id}, {}, View::NONE);


//*****************************************************************************
// TEST CASES
//*****************************************************************************

void testSimple()
{
  cout << "Running " << __func__ << "() ... ";
  
  Entity e(XAXIS);

  e.set(LEFT, 1.0f);
  e.set(RIGHT, 5.0f);

  float centerOut = e.get(CENTER);
  
  assert( centerOut == 3.0f );

  cout << "Pass" << endl;
}

void testWithDefault()
{
  cout << "Running " << __func__ << "() ... ";
  
  Entity e(XAXIS);

  e(RIGHT) = 4.0f;

  float centerOut = e.get(CENTER);

  assert( centerOut == 2.0f );

  cout << "Pass" << endl;
}

int main()
{  
  cout << "Starting " << __FILE__ << "..." << endl;

  testSimple();
  testWithDefault();

  cout << __FILE__ << " has ran successfully." << endl;

  return 0;
}
#include <assert.h>
#include <EntityList.h>

using namespace std;

#define USE(x)	(void)(x)

static int DRAW_COUNT = 0;
void CounterView(Entity &e, void *drawState) {
   DRAW_COUNT++;
}

static int UPDATE_COUNT = 0;
void CounterUpdate(Entity &e, void *updateState) {
   UPDATE_COUNT++;
}

EntityCore Core;

Component<int> C1 = Core.newComponent("C1", 1);
Component<int> C2 = Core.newComponent("C2", 2);
Component<int> C3 = Core.newComponent("C3", 3);
Component<int> C4 = Core.newComponent("C4", 4);

size_t U1 = Core.addController({}, &CounterUpdate);
size_t U2 = Core.addController({}, &CounterUpdate);
size_t U3 = Core.addController({}, &CounterUpdate);

size_t V1 = Core.addView({}, &CounterView);
size_t V2 = Core.addView({}, &CounterView);

EntityType* E1 = Core.newEntityType({C1.id, C2.id}, {U1}, V1);
EntityType* E2 = Core.newEntityType({C1.id, C2.id, C3.id, C4.id}, {U2}, V2);

void testEmptyConstructor()
{
   cout << "Running " << __func__ << "() ... ";

   EntityList el(&Core);

   assert( el.getSize() == 0 );

   el.clean();
   el.update(nullptr);
   el.draw(nullptr);
   el.getEntities();

   int t = 0;

   for (auto const &e : el) {
   		USE(e);
   		t++;
   }
   assert( t == 0 );

   for (auto const &e : el.filterByComponents({C1.id})) {
   		USE(e);
   		t++;
   }
   assert( t == 0 );

   for (auto const &e : el.filterByControllers({U1})) {
   		USE(e);
   		t++;
   }
   assert( t == 0 );

   for (auto const &e : el.filterByValue(C1.id, {3.5f})) {
   		USE(e);
   		t++;
   }
   assert( t == 0 );

   for (auto const &e : el.filterByVisible(true)) {
   		USE(e);
   		t++;
   }
   assert( t == 0 );

   for (auto const &e : el.filterByEnabled(false)) {
   		USE(e);
   		t++;
   }
   assert( t == 0 );

   for (auto const &e : el.filterByExpired(true)) {
   		USE(e);
   		t++;
   }
   assert( t == 0 );

   assert( el.get(0) == nullptr );

   cout << "Pass" << endl;
}

void testInitializerListConstructor()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1), b(E1), c(E2);
   EntityList el = {&a, &b, &c};

   assert( el.getSize() == 3 );

   cout << "Pass" << endl;
}

void testAdd()
{
   cout << "Running " << __func__ << "() ... ";

   EntityList el(&Core);

   assert( el.getSize() == 0 );

   Entity a(E1), b(E1), c(E2);

   el.add( &a );
   el.add( &b );
   el.add( &c );

   assert( el.getSize() == 3 );

   cout << "Pass" << endl;
}

void testAddVector()
{
   cout << "Running " << __func__ << "() ... ";

   EntityList el(&Core);

   assert( el.getSize() == 0 );

   Entity a(E1), b(E1), c(E2);

   el.add({&a, &b, &c});

   assert( el.getSize() == 3 );   

   cout << "Pass" << endl;
}

void testClean()
{
   cout << "Running " << __func__ << "() ... ";

   EntityList el(&Core);

   assert( el.getSize() == 0 );
   
   el.clean();

   assert( el.getSize() == 0 );

   Entity a(E1), b(E1), c(E2);
   el.add({&a, &b, &c});

   assert( el.getSize() == 3 );

   el.clean();

   assert( el.getSize() == 3 );

   a.expire();
   el.clean();

   assert( el.getSize() == 2 );

   assert( el.get(0) == &b );
   assert( el.get(1) == &c );

   b.expire();
   c.expire();
   el.clean();

   assert( el.getSize() == 0 );

   cout << "Pass" << endl;
}

void testDraw()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1), b(E1), c(E2);
   EntityList el = {&a, &b, &c};

   DRAW_COUNT = 0;

   el.draw(nullptr);

   assert( DRAW_COUNT == 3 );

   a.hide();
   DRAW_COUNT = 0;
   el.draw(nullptr);

   assert( DRAW_COUNT == 2 );

   cout << "Pass" << endl;
}

void testUpdate()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1), b(E1), c(E2);
   EntityList el = {&a, &b, &c};

   UPDATE_COUNT = 0;
   el.update(nullptr);
   assert( UPDATE_COUNT == 3 );

   a.disable();
   UPDATE_COUNT = 0;
   el.update(nullptr);
   assert( UPDATE_COUNT == 2 );

   a.enable();
   UPDATE_COUNT = 0;
   el.update(nullptr);
   assert( UPDATE_COUNT == 3 );

   a.expire();
   el.update(nullptr);
   UPDATE_COUNT = 0;
   el.update(nullptr);
   assert( UPDATE_COUNT == 2 );
   assert( el.getSize() == 2 );

   el.expire();
   UPDATE_COUNT = 0;
   el.update(nullptr);
   assert( UPDATE_COUNT == 0 );

   cout << "Pass" << endl;
}

void testIteration()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1), b(E1), c(E2);
   EntityList el = {&a, &b, &c};

   int index = 0;
   for (auto e : el) {
   		if (index == 0) {
   			assert( &a == e );
   		} else if (index == 1) {
   			assert( &b == e );
   		} else if (index == 2) {
   			assert( &c == e );
   		}
   		index++;
   }

   assert( index == 3 );

   cout << "Pass" << endl;
}

void testFilterByComponents()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1), b(E1), c(E2), d(E2);
   EntityList el = {&a, &b, &c, &d};

   int index = 0;
   for (auto e : el.filterByComponents({C3.id})) {
   		if (index == 0) {
   			assert( &c == e );
   		} else if (index == 1) {
   			assert( &d == e );
   		}
   		index++;
   }

   assert( index == 2 );

   cout << "Pass" << endl;
}

void testFilterByControllers()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1), b(E1), c(E2), d(E2);
   EntityList el = {&a, &b, &c, &d};

   int index = 0;
   for (auto e : el.filterByControllers({U1})) {
   		if (index == 0) {
   			assert( &a == e );
   		} else if (index == 1) {
   			assert( &b == e );
   		}
   		index++;
   }

   assert( index == 2 );

   cout << "Pass" << endl;
}

void testFilterByValue()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1,{{C1.id, 11}});
   Entity b(E1,{{C1.id, 7}});
   Entity c(E2,{{C1.id, 8}});
   Entity d(E1,{{C1.id, 11}});
   Entity e(E2,{{C1.id, 11}});

   EntityList el = {&a, &b, &c, &d, &e};

   int index = 0;
   for (auto entity : el.filterByValue(C1.id, {11})) {
   		if (index == 0) {
   			assert( &a == entity );
   		} else if (index == 1) {
   			assert( &d == entity );
   		} else if (index == 2) {
   			assert( &e == entity );
   		}
   		index++;
   }

   assert( index == 3 );

   index = 0;

   for (auto entity : el.filterByValue(C2.id, {22})) {
   		USE(entity);
   		index++;
   }

   assert( index == 0 );

   cout << "Pass" << endl;
}

void testFilterByValueStrict()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1,{{C1.id, 11}});
   Entity b(E1,{{C1.id, 7}});
   Entity c(E2,{{C1.id, 8}});
   Entity d(E1,{{C1.id, 11}});
   Entity e(E2,{{C1.id, 11}});

   EntityList el = {&a, &b, &c, &d, &e};

   int index = 0;
   for (auto entity : el.filterByValue(C1, 11)) {
         if (index == 0) {
            assert( &a == entity );
         } else if (index == 1) {
            assert( &d == entity );
         } else if (index == 2) {
            assert( &e == entity );
         }
         index++;
   }

   assert( index == 3 );

   index = 0;

   for (auto entity : el.filterByValue(C2, 22)) {
         USE(entity);
         index++;
   }

   assert( index == 0 );

   cout << "Pass" << endl;
}

void testFilterByVisible()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1); a.show();
   Entity b(E1); b.hide();
   Entity c(E2); c.hide();
   Entity d(E1); d.show();
   Entity e(E2); e.show();

   EntityList el = {&a, &b, &c, &d, &e};

   int index = 0;
   for (auto entity : el.filterByVisible(true)) {
   		if (index == 0) {
   			assert( &a == entity );
   		} else if (index == 1) {
   			assert( &d == entity );
   		} else if (index == 2) {
   			assert( &e == entity );
   		}
   		index++;
   }

   assert( index == 3 );

   index = 0;
   for (auto entity : el.filterByVisible(false)) {
   		if (index == 0) {
   			assert( &b == entity );
   		} else if (index == 1) {
   			assert( &c == entity );
   		}
   		index++;
   }

   assert( index == 2 );

   cout << "Pass" << endl;
}

void testFilterByEnabled()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1); a.enable();
   Entity b(E1); b.disable();
   Entity c(E2); c.disable();
   Entity d(E1); d.enable();
   Entity e(E2); e.enable();

   EntityList el = {&a, &b, &c, &d, &e};

   int index = 0;
   for (auto entity : el.filterByEnabled(true)) {
   		if (index == 0) {
   			assert( &a == entity );
   		} else if (index == 1) {
   			assert( &d == entity );
   		} else if (index == 2) {
   			assert( &e == entity );
   		}
   		index++;
   }

   assert( index == 3 );

   index = 0;
   for (auto entity : el.filterByEnabled(false)) {
   		if (index == 0) {
   			assert( &b == entity );
   		} else if (index == 1) {
   			assert( &c == entity );
   		}
   		index++;
   }

   assert( index == 2 );

   cout << "Pass" << endl;
}

void testFilterByExpired()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1); a.expire();
   Entity b(E1);
   Entity c(E2);
   Entity d(E1); d.expire();
   Entity e(E2); e.expire();

   EntityList el = {&a, &b, &c, &d, &e};

   int index = 0;
   for (auto entity : el.filterByExpired(true)) {
   		if (index == 0) {
   			assert( &a == entity );
   		} else if (index == 1) {
   			assert( &d == entity );
   		} else if (index == 2) {
   			assert( &e == entity );
   		}
   		index++;
   }

   assert( index == 3 );

   index = 0;
   for (auto entity : el.filterByExpired(false)) {
   		if (index == 0) {
   			assert( &b == entity );
   		} else if (index == 1) {
   			assert( &c == entity );
   		}
   		index++;
   }

   assert( index == 2 );

   el.clean();

   index = 0;
   for (auto entity : el) {
   		if (index == 0) {
   			assert( &b == entity );
   		} else if (index == 1) {
   			assert( &c == entity );
   		}
   		index++;
   }

   assert( index == 2 );

   cout << "Pass" << endl;
}

void testAdditionOperator()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1), b(E1), c(E2), d(E2);
   EntityList el(&Core);

   assert( el.getSize() == 0 );

   el += &a;
   el += &b;

   assert( el.getSize() == 2 );

   el += &c;
   el += &d;

   assert( el.getSize() == 4 );

   cout << "Pass" << endl;
}

void testInputOperator()
{
   cout << "Running " << __func__ << "() ... ";

   Entity a(E1), b(E1), c(E2), d(E2);
   EntityList el(&Core);

   assert( el.getSize() == 0 );

   el << &a << &b;

   assert( el.getSize() == 2 );

   el << &c << &d;

   assert( el.getSize() == 4 );

   cout << "Pass" << endl;
}

int main()
{
	cout << "Starting " << __FILE__ << "..." << endl;

	testEmptyConstructor();
	testInitializerListConstructor();
	testAdd();
	testAddVector();
	testClean();
	testDraw();
	testUpdate();
	testIteration();
	testFilterByComponents();
	testFilterByControllers();
	testFilterByValue();
   testFilterByValueStrict();
	testFilterByVisible();
	testFilterByEnabled();
	testFilterByExpired();
	testAdditionOperator();
	testInputOperator();

  	cout << __FILE__ << " has ran successfully." << endl;

	return 0;
}
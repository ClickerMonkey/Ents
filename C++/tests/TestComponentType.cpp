#include <assert.h>
#include <ComponentType.h>

using namespace std;

void testToString()
{
	cout << __func__ << endl;

	ComponentType ct(0, "position", AnyMemory(5.4f));

	cout << ct << endl;
}

int main()
{
	testToString();

  	cout << "ALL TESTS PASS" << endl;

	return 0;
}
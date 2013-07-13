#include <assert.h>
#include <ComponentType.h>

using namespace std;

void testToString()
{
	cout << "Running " << __func__ << "() ... ";

	ComponentType ct(0, "position", AnyMemory(5.4f));

	cout << ct << endl;

  	cout << "Pass" << endl;
}

int main()
{
	cout << "Starting " << __FILE__ << "..." << endl;

	testToString();

  	cout << __FILE__ << " has ran successfully." << endl;

	return 0;
}
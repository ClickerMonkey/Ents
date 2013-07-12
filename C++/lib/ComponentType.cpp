#include <ComponentType.h>
#include <string>

using namespace std;

ostream& operator<<(ostream &out, const ComponentType &a)
{
	out << "{id:" << a.id << ", name:" << a.name << ", default:" << "UNIMPLEMENTED" << "}";
	return out;
}
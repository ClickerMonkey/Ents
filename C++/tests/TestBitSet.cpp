#include <BitSet.h>

int main()
{
  BitSet a;
  a.set(0);
  a.set(1);
  a.set(2);

  BitSet b;
  b.set(3);

  cout << a.intersects(b) << endl;

  b.set(1);

  cout << a.intersects(b) << endl;

  a.set(451);

  cout << a.size() << endl;

  return 0;
}

#include <AnyMemory.h>

int main()
{
  AnyMemory am;
  am << 345 << 4.0f;
  cout << am.get<int>(0) << endl;
  cout << am.get<char>(0) << endl;
  cout << am.get<float>(4) << endl;
  cout << am.get<int>(4) << endl;

  return 0;
}

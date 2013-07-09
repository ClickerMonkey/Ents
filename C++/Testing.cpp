#include <cstdlib>
#include <iostream>
#include <vector>

using namespace std;

class IdMap {
public:

  IdMap(vector<int> ids) {
     this->ids = ids;
     this->map = buildMap(ids);
  }

  inline bool has(const int id) const {
     return (id < map.size() && map[id] >= 0);
  }

  inline void alias(const int id, const int aliasId) {
     if (has(id)) {
        mapIndex(aliasId, map[id]);
     }
  }

  inline void add(const int id) {
     if (!has(id)) {
        const int index = ids.size();
        ids.push_back(id);
        mapIndex(id, index);
     }
  }

  inline int getIndex(const int id) const {
     return map[id];
  }

  inline int getIndexSafe(const int id) const {
     return (id < map.size() ? map[id] : -1);
  }

  inline int size() const {
     return ids.size();
  }

  IdMap* copy() const {
    vector<int> idsCopy = ids;
    vector<int> mapCopy = map;
    return new IdMap(idsCopy, mapCopy);
  }

  static vector<int> buildMap(const vector<int> &ids) {
     int n = 0;
     for (int i = 0; i < ids.size(); i++) {
        n = max( n, ids[i] );
     }
     vector<int> map(n + 1);
     for (int i = 0; i <= n; i++) {
        map[i] = -1;
     }
     for (int i = 0; i < ids.size(); i++) {
        map[ids[i]] = i;
     }
     return map;
  }

private:
  vector<int> ids;
  vector<int> map;

  IdMap(vector<int> &ids, vector<int> &map) : ids(ids), map(map) {
  }

  void mapIndex(const int id, const int index) {
     map.reserve( id + 1 );
     map[id] = index;
  }
};

int main(int argc, char **argv)
{
  int arr[] = {0, 2, 3, 5};
  vector<int> ids(arr, arr + 4);

  IdMap map(ids);

  cout << map.getIndex(0) << endl;
  cout << map.getIndex(1) << endl;
  cout << map.getIndex(2) << endl;
  cout << map.getIndex(3) << endl;
  cout << map.getIndex(4) << endl;
  cout << map.getIndex(5) << endl;

  map.add(1);

  cout << map.getIndex(1) << endl;

	return 0;
}
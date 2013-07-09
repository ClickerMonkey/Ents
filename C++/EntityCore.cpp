
#define alloc(type,size) 	((type*)calloc(sizeof(type), size))

namespace EntityCore
{

   template<typename T>
   T* array_copy(const T *data) {
      if (data == NULL) {
         return NULL;
      }
      const int Dsize = sizeof(data);
      const int length = Dsize / sizeof(T);
      T *copied = new T[ length ];
      memcpy( copied, data, Dsize );
      return copied;
   }

   template<typename T>
   T* array_resize(const T *data, const int length) {
      if (data == NULL) {
         return new T[length];
      }
      T *resized = new T[length];
      memcpy(resized, data, min(sizeof(data), length * sizeof(T)));
      delete [] data;   
      return resized;
   }

   template<typename T>
   T* array_add(const T *data, const T &item) {
      if (data == NULL) {
         return new T[] {item};
      }
      const int Dsize = sizeof(data);
      const int length = Dsize / sizeof(T);
      T *added = new T[ length + 1 ];
      memcpy( added, data, Dsize );
      delete [] data;
      added[length] = item;
      return added;
   }

   inline void* data_copy(void *data, const int bytes)
   {
      void* v = calloc(bytes, 1);
      memcpy( v, data, bytes );
      return v;
   }

   inline void* data_new(const int bytes)
   {
      return calloc(bytes, 1);
   }

   class IdMap {
   public:

     IdMap(const vector<int> &ids) {
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

     void mapIndex(const int id, const int index) {
        map.reserve( id + 1 );
        map[id] = index;
     }
   };

   class EntityType {
   public:
      const int id;
      const EntityType *parent;
      vector<int> componentMap;
      vector<int> components;
      int entitySize;
      void *entityDefault;

      EntityType(int id) 
         : id(id), parent(NULL), componentMap(vector<int>()), components(vector<int>())
      {
         updateEntityMeta();
      }

      ~EntityType() 
      {
      }
   
      EntityType* extend(int entityTypeId) const
      {
         return new EntityType(entityTypeId, this, componentMap, components);
      }

      inline bool hasComponent(int componentId) const
      {
         return (componentId < componentMapSize && componentMap[componentId]);
      }

      bool hasComponents(int n, ...) const
      {
         va_list args;
         va_start(args, n);

         while (--n >= 0) {
            if (!hasComponent(va_arg(args, int))) {
               va_end(args);
               return false;
            }
         }

         va_end(args);

         return true;
      }

      inline int getComponentIndex(int componentId) const
      {
         return componentMap[componentId] + 1;
      }

      inline int getComponentCount() const 
      {
         return componentCount;
      }

      void setAlias(int componentId, int aliasId) 
      {
         map(aliasId, componentMap[componentId]);
      }

      void* createComponents()
      {
         return data_copy(entityDefault, entitySize);
      }

      void add(int componentId)
      {
         const int N = components.size();

         components.push_back(componentId);

         map( componentId, N );
      }

      virtual EntityType* addDynamically(int componentId)
      {
         EntityTypeDynamic *dynamic = new EntityTypeDynamic( id, this, componentMap, components );
         dynamic->add( componentId );
         return dynamic;
      }

   protected:
      EntityType(int id, const vector<int> &componentMap, const vector<int> &components)
         : id(id), parent(NULL), componentMap(componentMap), components(components), 
      {
         updateEntityMeta();
      }

      EntityType(int id, EntityType *parent, const vector<int> &componentMap, const vector<int> &components)
         : id(id), parent(parent), componentMap(componentMap), components(components), 
      {
         updateEntityMeta();
      }
      
      void map(int componentId, int index)
      {           
         if (componentMap.size() <= componentId)
         {
            componentMap.resize(componentId + 1);
         }

         componentMap[componentId] = index;
      }

      void updateEntityMeta()
      {
         entitySize = 0;

         for (vector<int>::size_type i = 0; i != components.size(); i++)
         {
            entitySize += EntityCore::getComponent(components[i]).size;
         }

         entityDefault = data_new(entitySize);

         void* insert = entityDefault;

         for (vector<int>::size_type i = 0; i != components.size(); i++)
         {
            ComponentType componentType = EntityCore::getComponent(components[i]);
            memcpy(insert, componentType.defaultValue, componentType.size);
            insert += componentType.size;
         }
      }
   };

   class EntityTypeDynamic : public Entity type {
   public: 

      EntityTypeDynamic(const int id, const EntityType *parent, const vector<int> &componentMap, const vector<int> &components)
         : EntityType(id, parent, componentMap, components )
      {
      }

      EntityType* addDynamically(int componentId) 
      {
         add( componentId );
         return this;
      }
   };

   class ComponentType {
   public:
      int id;
      char *name;
      int size;
      void *defaultValue;

      ComponentType(int id, char *name, int size, void *defaultValue)
         : id(id), name(name), size(size), defaultValue(defaultValue)
      {
      }
   };

   class DynamicComponentType : public ComponentType {
   public:

   };

   template<typename T>
   typedef T (*DynamicComponent)(Entity *entity, T *out);

   class EntityCore {
   public: 

      static inline int getNextComponentId() {
         return components.size();
      }

      static inline ComponentType& getComponent(int componentId) {
         return components[componentId];
      }

      template<typename T>
      static int newComponent(const char *name, const T &defaultValue) {
         int id = getNextComponentId();
         components.push_back(new ComponentType(id, name, sizeof(T), data_copy(&defaultValue, sizeof(T))));
         return id;
      }

      template<typename T>
      static int newDynamicComponent(const char *name, DynamicComponent<T> dynamicComponent) {
         int id = getNextComponentId();

         return id;
      }

   private:
      static vector<ComponentType> components;
      static vector<EntityType> entityTypes;
   };

}

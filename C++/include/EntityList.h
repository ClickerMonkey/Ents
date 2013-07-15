#ifndef ENTITYLIST_H
#define ENTITYLIST_H

#include <functional>

#include <Entity.h>
#include <VectorIterator.h>
#include <Component.h>

class EntityList : public Entity
{
private:
	
	std::vector<Entity*> entities;
	
public:

	EntityList( const EntityCore *m_core );

	EntityList( EntityType *m_type );

	EntityList( std::initializer_list<Entity*> e );

	void add( Entity *e );

	template<typename Iterator>
	void add( Iterator first, Iterator end )
	{
		for (Iterator i = first; i != end; i++) {
			add(i);
		}
	}

	void add( const std::vector<Entity*> &e );

	void clean();

	void draw( void *drawState );

	void update( void *updateState );

    VectorIteratorPointer<Entity*> begin();

	VectorIteratorPointer<Entity*> end();

	inline VectorIterator<Entity*> filter(const std::function<bool(Entity*)> &filterFunction)
	{
		return VectorIterator<Entity*>(&entities, filterFunction);
	}

 	VectorIterator<Entity*> filterByComponents(const BitSet &componentIds);

	VectorIterator<Entity*> filterByControllers(const BitSet &controllerIds);

	template<typename T>
	VectorIterator<Entity*> filterByValue(const Component<T> &component, const T &value)
	{
		return filter( [&] (Entity* e) -> bool {
			T* p = e->ptrs(component);
			if (p == nullptr) {
				return false;
			}
			return (*p == value);
		});
	}

	VectorIterator<Entity*> filterByVisible(const bool visible);

	VectorIterator<Entity*> filterByEnabled(const bool enabled);

	VectorIterator<Entity*> filterByExpired(const bool expired);

	VectorIterator<Entity*> filterByEntityType(const EntityType* entityType);	

	std::vector<Entity*>& getEntities()
	{
		return entities;
	}

	inline Entity* get(const size_t index)
	{
		return (index < entities.size() ? entities.at(index) : nullptr);
	}

	inline size_t getSize()
	{
		return entities.size();
	}

	inline Entity* operator[](const size_t index)
	{
		return ( index < entities.size() ? entities.at(index) : nullptr );
	}

	inline EntityList& operator+=(Entity *e)
	{
		add( e );
		return *this;
	}

	inline EntityList& operator<<(Entity *e)
	{
		add( e );
		return *this;
	}

protected:

	virtual void onEntityAdd( Entity*, const size_t ) {}

	virtual void onEntityRemove( Entity*, const size_t ) {}

	virtual void onEntityUpdated( Entity*, void* ) {}

private:

	void internalAdd( Entity *e );

};

#endif
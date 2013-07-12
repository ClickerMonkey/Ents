#ifndef ENTITYLIST_H
#define ENTITYLIST_H

#include <Entity.h>
#include <VectorIterator.h>

typedef bool (*EntityFilter)( Entity *e);

struct EntityComponentFilter;
struct EntityControllerFilter;
struct EntityValueFilter;
struct EntityVisibleFilter;
struct EntityEnabledFilter;

class EntityList : public Entity
{
private:
	
	vector<Entity*> entities;
	
public:

	EntityList();
	
	EntityList( const size_t entityTypeId );

	EntityList( EntityType *type );

	void add( Entity *e );

	void add( initializer_list<Entity*> e );
	
	template<typename Iterator>
	void add( Iterator first, Iterator end )
	{
		for (Iterator i = first; i != end; i++)
		{
			add(i);
		}
	}

	void add( const vector<Entity*> &e );

	void clean();

	void draw( void *drawState );

	void update( void *updateState );

   VectorIteratorPointer<Entity*, EntityFilter> begin();

	VectorIteratorPointer<Entity*, EntityFilter> end();

	template<typename F>
	inline VectorIterator<Entity*, F> filter(F filterFunction)
	{
		return VectorIterator<Entity*, F>(&entities, filterFunction);
	}

 	VectorIterator<Entity*, EntityComponentFilter> filterByComponents(initializer_list<size_t> componentIds);

	VectorIterator<Entity*, EntityControllerFilter> filterByControllers(initializer_list<size_t> controllerIds);

	VectorIterator<Entity*, EntityValueFilter> filterByValue(const size_t componentId, AnyMemory &value);

	VectorIterator<Entity*, EntityVisibleFilter> filterByVisible(bool visible);

	VectorIterator<Entity*, EntityEnabledFilter> filterByEnabled(bool enabled);

	vector<Entity*>& getEntities()
	{
		return entities;
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
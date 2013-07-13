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
struct EntityExpiredFilter;

class EntityList : public Entity
{
private:
	
	std::vector<Entity*> entities;
	
public:

	EntityList();
	
	EntityList( const size_t m_entityTypeId );

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

    VectorIteratorPointer<Entity*, EntityFilter> begin();

	VectorIteratorPointer<Entity*, EntityFilter> end();

	template<typename F>
	inline VectorIterator<Entity*, F> filter(F filterFunction)
	{
		return VectorIterator<Entity*, F>(&entities, filterFunction);
	}

 	VectorIterator<Entity*, EntityComponentFilter> filterByComponents(std::initializer_list<size_t> componentIds);

	VectorIterator<Entity*, EntityControllerFilter> filterByControllers(std::initializer_list<size_t> controllerIds);

	VectorIterator<Entity*, EntityValueFilter> filterByValue(const size_t componentId, const AnyMemory &value);

	VectorIterator<Entity*, EntityVisibleFilter> filterByVisible(bool visible);

	VectorIterator<Entity*, EntityEnabledFilter> filterByEnabled(bool enabled);

	VectorIterator<Entity*, EntityExpiredFilter> filterByExpired(bool expired);

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

struct EntityComponentFilter
{
	BitSet components;

	EntityComponentFilter(BitSet m_components)
		: components(m_components)
	{
	}

	bool operator()( Entity *e )
	{
		return components.intersects( e->getEntityType()->getComponents().getBitSet() );
	}
};

struct EntityControllerFilter
{
	BitSet controllers;

	EntityControllerFilter(BitSet m_controllers)
		: controllers(m_controllers)
	{
	}

	bool operator()( Entity *e )
	{
		return controllers.intersects( e->getEntityType()->getControllers().getBitSet() );
	}
};

struct EntityValueFilter
{
	const size_t componentId;
	const AnyMemory value;

	EntityValueFilter( const size_t m_componentId, const AnyMemory &m_value )
		: componentId(m_componentId), value(m_value)
	{
	}

	bool operator()( Entity *e )
	{
		if (e->has(componentId)) {

			size_t offset = e->getEntityType()->getComponentOffset(componentId);
			AnyMemory components = e->getComponents();

			if (offset + value.getSize() <= components.getSize()) {

				void *componentPointer = (void*)(components.getData() + offset);
				void *valuePointer = (void*)value.getData();

				return ( memcmp( componentPointer, valuePointer, value.getSize() ) == 0 );	
			}
		}

		return false;
	}
};

struct EntityVisibleFilter
{
	bool visible;

	EntityVisibleFilter(bool m_visible)
		: visible(m_visible)
	{
	}

	bool operator()( Entity *e )
	{
		return e->isVisible() == visible;
	}
};

struct EntityEnabledFilter
{
	bool enabled;

	EntityEnabledFilter(bool m_enabled)
		: enabled(m_enabled)
	{
	}

	bool operator()( Entity *e )
	{
		return e->isEnabled() == enabled;
	}
};

struct EntityExpiredFilter
{
	bool expired;

	EntityExpiredFilter(bool m_expired)
		: expired(m_expired)
	{
	}

	bool operator()( Entity *e )
	{
		return e->isExpired() == expired;
	}
};


#endif
#include <EntityList.h>

using namespace std;

EntityList::EntityList( const EntityCore *m_core )
	: Entity(m_core)
{
}

EntityList::EntityList( EntityType *m_type )
	: Entity(m_type)
{
}

EntityList::EntityList( initializer_list<Entity*> e )
	: Entity((*(e.begin()))->getEntityType()->getCore())
{
	for (const auto &x : e) {
		internalAdd(x);
	}
}

void EntityList::add( Entity *e )
{
	internalAdd( e );
}

void EntityList::add( const vector<Entity*> &e )
{
	for (const auto &x : e) {
		internalAdd(x);	
	}
}

void EntityList::clean()
{
	size_t alive = 0;

	for (size_t i = 0; i < entities.size(); i++) {
		Entity *e = entities.at(i);
		
		if (e->isExpired()) {
			onEntityRemove( e, i );
		} else {
			entities[alive++] = e;
		}
	}

	if (alive == 0) {
		entities.clear();
	} else if (alive < entities.size()) {
		entities.erase( entities.begin() + alive, entities.end() );
	}
}

void EntityList::draw( void *drawState )
{
	Entity::draw( drawState );

	for (const auto &e : entities) {
		e->draw( drawState );
	}
}

void EntityList::update( void *updateState )
{
	Entity::update( updateState );

	if (isExpired()) {
		return;
	}

	for (const auto &e : entities) {
		e->update( updateState );
		onEntityUpdated( e, updateState );
	}

	clean();	
}

void EntityList::internalAdd( Entity *e )
{
	onEntityAdd( e, entities.size() );

	entities.push_back( e );
}

bool FilterNone( Entity *e )
{
	return (e != nullptr);
}

VectorIteratorPointer<Entity*> EntityList::begin()
{
	return VectorIteratorPointer<Entity*>(&entities, &FilterNone, 0, 1, entities.size());
}

VectorIteratorPointer<Entity*> EntityList::end()
{
 	return VectorIteratorPointer<Entity*>(&entities, &FilterNone, entities.size() - 1, -1, -1);
}

VectorIterator<Entity*> EntityList::filterByComponents(const BitSet &componentIds)
{
	return filter( [&] (Entity *e) -> bool 
	{
		return componentIds.intersects( e->getEntityType()->getComponents().getBitSet() );
	});
}

VectorIterator<Entity*> EntityList::filterByControllers(const BitSet &controllerIds)
{
	return filter( [&] (Entity *e) -> bool 
	{
		return controllerIds.intersects( e->getEntityType()->getControllers().getBitSet() );
	});
}

VectorIterator<Entity*> EntityList::filterByValue(const size_t componentId, const AnyMemory &value)
{
	return filter( [&] (Entity *e) -> bool 
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
	});
}

VectorIterator<Entity*> EntityList::filterByVisible(bool visible)
{
	return filter( [&] (Entity *e) -> bool 
	{
		return ( e->isVisible() == visible );
	});
}

VectorIterator<Entity*> EntityList::filterByEnabled(bool enabled)
{
	return filter( [&] (Entity *e) -> bool
	{
		return ( e->isEnabled() == enabled );
	});
}

VectorIterator<Entity*> EntityList::filterByExpired(bool expired)
{
	return filter( [&] (Entity *e) -> bool
	{
		return ( e->isExpired() == expired );
	});
}
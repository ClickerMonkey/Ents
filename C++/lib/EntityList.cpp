#include <EntityList.h>

using namespace std;

EntityList::EntityList()
	: Entity()
{
}

EntityList::EntityList( const size_t m_entityTypeId )
	: Entity( m_entityTypeId )
{
}

EntityList::EntityList( EntityType *m_type )
	: Entity( m_type )
{
}

EntityList::EntityList( initializer_list<Entity*> e )
	: Entity()
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

VectorIteratorPointer<Entity*, EntityFilter> EntityList::begin()
{
	return VectorIteratorPointer<Entity*, EntityFilter>(&entities, FilterNone, 0, 1, entities.size());
}

VectorIteratorPointer<Entity*, EntityFilter> EntityList::end()
{
 	return VectorIteratorPointer<Entity*, EntityFilter>(&entities, FilterNone, entities.size() - 1, -1, -1);
}

VectorIterator<Entity*, EntityComponentFilter> EntityList::filterByComponents(initializer_list<size_t> componentIds)
{
	return filter( EntityComponentFilter(BitSet(componentIds)) );
}

VectorIterator<Entity*, EntityControllerFilter> EntityList::filterByControllers(initializer_list<size_t> controllerIds)
{
	return filter( EntityControllerFilter(BitSet(controllerIds)) );
}

VectorIterator<Entity*, EntityValueFilter> EntityList::filterByValue(const size_t componentId, const AnyMemory &value)
{
	return filter( EntityValueFilter(componentId, value) );
}

VectorIterator<Entity*, EntityVisibleFilter> EntityList::filterByVisible(bool visible)
{
	return filter( EntityVisibleFilter(visible) );
}

VectorIterator<Entity*, EntityEnabledFilter> EntityList::filterByEnabled(bool enabled)
{
	return filter( EntityEnabledFilter(enabled) );
}

VectorIterator<Entity*, EntityExpiredFilter> EntityList::filterByExpired(bool expired)
{
	return filter( EntityExpiredFilter(expired) );
}
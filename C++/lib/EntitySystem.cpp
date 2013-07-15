#include <EntitySystem.h>
#include <utility>

using namespace std;

void cleanMap( map<size_t,vector<EntityList*>> &vmap )
{
	for (auto &entry : vmap) {
		for (auto &list : entry.second) {
			list->clean();
		}
	}
}

size_t EntitySystem::clean()
{
	entities.clean();

	cleanMap( componentListeners );
	cleanMap( controllerListeners );

	return entities.getSize();
}

void notifyListsInMap( Entity *e, const IdMap& indices, map<size_t,vector<EntityList*>> &vmap )
{
	for (size_t i = 0; i < indices.size(); i++) {

		size_t id = indices.getId(i);
		auto iterator = vmap.find( id );

		if (iterator != vmap.end()) {
			for (auto &listener : iterator.second) {
				listener.add( e );
			}
		}
	}
}

Entity* EntitySystem::track( Entity *e )
{
	entities.add( e );

	IdMap components = e->getEntityType()->getComponents();
	notifyListsInMap( e, components, componentListeners );

	IdMap controllers = e->getEntityType()->getControllers();
	notifyListsInMap( e, controllers, controllerListeners );

	return e;
}

void addEntityListToMapBasedOnBitSet( EntityList *listener, const IdMap &indices, map<size_t,vector<EntityList*>> &vmap )
{
	for (size_t i = 0; i < indices.size(); i++) {

		size_t id = indices.getId(i);
		auto iterator = vmap.find( id );

		if (iterator == vmap.end()) {
			vmap.insert({id, {listener}});
		} else {
			iterator.second.push_back(listener);
		}
	}
}

void EntitySystem::listenForComponents( EntityList *listener, const IdMap &components )
{
	addEntityListToMapBasedOnBitSet( listener, components, componentListeners );

	for (auto &e : entities) {
		if (e.has(components)) {
			listener.add(e);
		}
	}
}

void EntitySystem::listenForControllers( EntityList *listener, const IdMap &controllers )
{
	addEntityListToMapBasedOnBitSet( listener, controllers, controllerListeners );

	for (auto &e : entities) {
		if (e.hasControllers(controllers)) {
			listener.add(e);
		}
	}
}

size_t removeEntityListFromMap( EntityList* listener, map<size_t,vector<EntityList*>> &vmap )
{
	size_t removed = 0;

	for (auto &entry : vmap) {

		vector<EntityList*> *list = &entry.second;

		for (auto iterator = list->begin(); iterator != list->end();) {
			if (iterator == listener) {
				iterator = list->erase(iterator);
				removed++;
			} else {
				++iterator;
			}
		}
	}

	return removed;
}

size_t EntitySystem::removeFromComponents( EntityList *listener ) 
{
	return removeEntityListFromMap( listener, componentListeners );
}

size_t EntitySystem::removeFromControllers( EntityList* listener ) 
{
	return removeEntityListFromMap( listener, controllerListeners );
}
#include <EntityList.h>

EntityList::EntityList()
	: Entity()
{
}

EntityList::EntityList( const size_t entityTypeId )
	: Entity( entityTypeId )
{
}

EntityList::EntityList( EntityType *type )
	: Entity( type )
{
}

void EntityList::add( Entity *e )
{
	internalAdd( e );
}

void EntityList::add( initializer_list<Entity*> e )
{
	for (auto x : e) 
	{
		internalAdd(x);
	}
}

void EntityList::add( const vector<Entity*> &e )
{
	for (auto x : e) 
	{
		internalAdd(x);	
	}
}

void EntityList::clean()
{
	size_t alive = 0;

	for (size_t i = 0; i < entities.size(); i++)
	{
		Entity *e = entities[i];

		if ( e->isExpired() )
		{
			onEntityRemove( e, i );
		}
		else
		{
			entities[alive++] = e;
		}
	}

	if (alive != entities.size())
	{
		entities.erase( entities.begin() + alive, entities.end() + entities.size() );
	}
}

void EntityList::draw( void *drawState )
{
	Entity::draw( drawState );

	for (auto e : entities)
	{
		e->draw( drawState );
	}
}

void EntityList::update( void *updateState )
{
	Entity::update( updateState );

	if ( isExpired() )
	{
		return;
	}

	for (auto e : entities)
	{
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
	return (e != NULL);
}

VectorIteratorPointer<Entity*, EntityFilter> EntityList::begin()
{
	return VectorIteratorPointer<Entity*, EntityFilter>(&entities, FilterNone, 0, 1, entities.size());
}

VectorIteratorPointer<Entity*, EntityFilter> EntityList::end()
{
 	return VectorIteratorPointer<Entity*, EntityFilter>(&entities, FilterNone, entities.size() - 1, -1, -1);
}

struct EntityComponentFilter
{
	BitSet components;

	EntityComponentFilter(BitSet components)
		: components(components)
	{
	}

	bool operator()( Entity *e )
	{
		return components.intersects( e->getEntityType()->getComponents().getBitSet() );
	}
};

VectorIterator<Entity*, EntityComponentFilter> EntityList::filterByComponents(initializer_list<size_t> componentIds)
{
	return filter( EntityComponentFilter(BitSet(componentIds)) );
}

struct EntityControllerFilter
{
	BitSet controllers;

	EntityControllerFilter(BitSet controllers)
		: controllers(controllers)
	{
	}

	bool operator()( Entity *e )
	{
		return controllers.intersects( e->getEntityType()->getControllers().getBitSet() );
	}
};

VectorIterator<Entity*, EntityControllerFilter> EntityList::filterByControllers(initializer_list<size_t> controllerIds)
{
	return filter( EntityControllerFilter(BitSet(controllerIds)) );
}


struct EntityValueFilter
{
	const size_t componentId;
	AnyMemory value;

	EntityValueFilter( const size_t componentId, AnyMemory &value )
		: componentId(componentId), value(value)
	{
	}

	bool operator()( Entity *e )
	{
		bool equals = e->has(componentId);

		if ( equals )
		{
			size_t offset = e->getEntityType()->getComponentOffset(componentId);
			AnyMemory components = e->getComponents();

			equals = ( offset + value.getSize() <= components.getSize() );

			if (equals)
			{
				void *componentPointer = (void*)(components.getData() + offset);
				void *valuePointer = (void*)value.getData();

				equals = ( memcmp( componentPointer, valuePointer, value.getSize() ) == 0 );	
			}
		}

		return equals;
	}
};

VectorIterator<Entity*, EntityValueFilter> EntityList::filterByValue(const size_t componentId, AnyMemory &value)
{
	return filter( EntityValueFilter(componentId, value) );
}


struct EntityVisibleFilter
{
	bool visible;

	EntityVisibleFilter(bool visible)
		: visible(visible)
	{
	}

	bool operator()( Entity *e )
	{
		return e->isVisible() == visible;
	}
};

VectorIterator<Entity*, EntityVisibleFilter> EntityList::filterByVisible(bool visible)
{
	return filter( EntityVisibleFilter(visible) );
}


struct EntityEnabledFilter
{
	bool enabled;

	EntityEnabledFilter(bool enabled)
		: enabled(enabled)
	{
	}

	bool operator()( Entity *e )
	{
		return e->isEnabled() == enabled;
	}
};

VectorIterator<Entity*, EntityEnabledFilter> EntityList::filterByEnabled(bool enabled)
{
	return filter( EntityEnabledFilter(enabled) );
}
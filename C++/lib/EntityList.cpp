#include <EntityList.h>

EntityList::EntityList()
	: Entity( NULL )
{
}

EntityList::EntityList( const size_t entityTypeId )
	: Entity( entityTypeId )
{
}

EntityList::EntityList( EntityType *type )
	: Entity( type );
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

		onEntityUpdate( e, updateState );
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

VectorIteratorPointer<Entity*, EntityFilter> EntityList::end();
{
 	return VectorIteratorPointer<Entity*, EntityFilter>(&entities, FilterNone, entities.size() - 1, -1, -1);
}

struct ComponentFilter : FilterFunctor
{
	BitSet components;

	ComponentFilter(BitSet &components)
		: components(components)
	{
	}

	bool operator()( Entity *e )
	{
		return components.intersects( e->getType()->getComponents()->getBitSet() );
	}
};

VectorIterator<Entity*, FilterFunctor> EntityList::filterByComponents(initializer_list<size_t> componentIds)
{
	return filter( ComponentFilter(BitSet(componentIds)) );
}

struct ControllerFilter : FilterFunctor
{
	BitSet controllers;

	ControllerFilter(BitSet &controllers)
		: controllers(controllers)
	{
	}

	bool operator()( Entity *e )
	{
		return controllers.intersects( e->getType()->getControllers()->getBitSet() );
	}
};

VectorIterator<Entity*, FilterFunctor> EntityList::filterByControllers(initializer_list<size_t> controllerIds)
{
	return filter( ControllerFilter(BitSet(controllerIds)) );
}


struct ValueFilter : FilterFunctor
{
	const size_t componentId;
	const AnyMemory value;

	ValueFilter( const size_t componentId, const AnyMemory &value )
		: componentId(componentId), value(value)
	{
	}

	bool operator()( Entity *e )
	{
		bool equals = e->has(componentId);

		if ( equals )
		{
			size_t offset = e->getType()->getComponentOffset(componentId);
			AnyMemory components = e->getComponents();

			equals = (offset + value.getSize() <= components.getSize() );

			if (equals)
			{
				equals = memcmp( components[offset], value[0], value.getSize() ) == 0;	
			}
		}

		return equals;
	}
};

VectorIterator<Entity*, FilterFunctor> EntityList::filterByValue(const size_t componentId, const AnyMemory &value)
{
	return filter( ValueFilter(componentId, value) );
}


struct VisibleFilter : FilterFunctor
{
	bool visible;

	VisibleFilter(bool visible)
		: visible(visible)
	{
	}

	bool operator()( Entity *e )
	{
		return e->isVisible() == visible;
	}
};

VectorIterator<Entity*, FilterFunctor> EntityList::filterByVisible(bool visible)
{
	return filter( VisibleFilter(visible) );
}


struct EnabledFilter : FilterFunctor
{
	bool enabled;

	VisibleFilter(bool enabled)
		: enabled(enabled)
	{
	}

	bool operator()( Entity *e )
	{
		return e->isEnabled() == enabled;
	}
};

VectorIterator<Entity*, FilterFunctor> EntityList::filterByEnabled(bool enabled)
{
	return filter( EnabledFilter(enabled) );
}
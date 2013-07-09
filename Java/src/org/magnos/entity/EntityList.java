package org.magnos.entity;

import java.util.Arrays;
import java.util.Iterator;

public class EntityList extends Entity implements Iterable<Entity>
{

	protected Entity[] entities = {};
	protected int entityCount = 0;
	protected EntityGroupIterator iterator;
	
	public EntityList(int entityTypeId, int initialCapacity)
	{
		super( entityTypeId );
		
		this.entities = new Entity[ initialCapacity ];
		this.iterator = new EntityGroupIterator();
	}
	
	protected void onEntityAdd(Entity e, int index)
	{
	}
	
	protected void onEntityRemove(Entity e, int index)
	{
	}
	
	protected void onEntityUpdated(Entity e, int index, Object updateState)
	{
	}
	
	public void add(Entity e)
	{
		if (entities.length == entityCount)
		{
			entities = Arrays.copyOf( entities, entities.length + (entities.length >> 1) );
		}
		
		entities[entityCount] = e;
		
		onEntityAdd( e, entityCount );
		
		entityCount++;
	}
	
	@Override
	public void update(Object updateState)
	{
		super.update( updateState );
		
		if (isExpired())
		{
			return;
		}
		
		int alive = 0;
		
		for (int i = 0; i < entityCount; i++)
		{
			final Entity e = entities[i];
			
			e.update( updateState );
			
			onEntityUpdated( e, i, updateState );
			
			if ( e.isExpired() )
			{
				onEntityRemove( e, i );
				
				e.onEntityListRemoval( this );
			}
			else
			{
				entities[alive++] = e;
			}
		}
		
		while (entityCount > alive)
		{
			entities[--entityCount] = null;
		}
		
		if ( !iterator.hasNext() )
		{
			iterator = new EntityGroupIterator();
		}
	}

	@Override
	public Iterator<Entity> iterator()
	{
		return (iterator.hasNext() ? new EntityGroupIterator() : iterator.reset() );
	}
	
	private class EntityGroupIterator implements Iterator<Entity>
	{
		
		private int index = 0;
		
		public EntityGroupIterator reset()
		{
			index = 0;
			
			return this;
		}

		@Override
		public boolean hasNext()
		{
			return index < entityCount;
		}

		@Override
		public Entity next()
		{
			return entities[index++];
		}

		@Override
		public void remove()
		{
			entities[index - 1].expire();
		}
		
	}
	
	
}

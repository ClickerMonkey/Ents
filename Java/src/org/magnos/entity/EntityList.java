package org.magnos.entity;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;

public class EntityList extends Entity implements Iterable<Entity>
{
	
	public static int DEFAULT_CAPACITY = 16;

	protected Entity[] entities = {};
	protected int entityCount = 0;
	
	// Iterators
	protected EntityIterator iterator;
	protected final ComponentIterator componentIterator;
	protected final ControllerIterator controllerIterator;
	protected final ValueIterator valueIterator;
	protected final VisibleIterator visibleIterator;
	protected final EnabledIterator enabledIterator;
	
	public EntityList()
	{
		this( null, DEFAULT_CAPACITY );
	}
	
	public EntityList(int initialCapacity)
	{
		this( null, initialCapacity );
	}
	
	public EntityList(int entityTypeId, int initialCapacity)
	{
		this( EntityCore.getEntityType(entityTypeId), initialCapacity );
	}
	
	protected EntityList( EntityType entityType, int initialCapacity )
	{
		super( entityType );
		
		this.entities = new Entity[ initialCapacity ];
		this.iterator = new EntityIterator();
		this.componentIterator = new ComponentIterator();
		this.controllerIterator = new ControllerIterator();
		this.valueIterator = new ValueIterator();
		this.visibleIterator = new VisibleIterator();
		this.enabledIterator = new EnabledIterator();
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
	
	public void pad(int count)
	{
		if (entityCount + count >= entities.length)
		{
			int nextCapacity = entities.length + (entities.length >> 1);
			int minimumCapacity = entityCount + count;
			
			entities = Arrays.copyOf(entities, Math.max( nextCapacity, minimumCapacity ) );
		}
	}
	
	private void internalAdd(Entity entity)
	{
		entities[entityCount] = entity;
		
		onEntityAdd( entity, entityCount );
		
		entityCount++;
	}
	
	public void add(Entity entity)
	{
		pad( 1 );
		internalAdd( entity );
	}
	
	public void add(Entity ... entityArray)
	{
		pad( entityArray.length );
		
		for (int i = 0; i < entityArray.length; i++)
		{
			internalAdd( entityArray[i] );
		}
	}
	
	public void addRange( Entity[] entityArray, int from, int to )
	{
		pad( to - from );
		
		while (from < to) 
		{
			internalAdd( entityArray[from++] );
		}
	}
	
	public void addAll(EntityList list)
	{
		addRange(list.entities, 0, list.entityCount);
	}
	
	public void addAll(Collection<Entity> entityCollection)
	{
		pad( entityCollection.size() );
		
		for (Entity e : entityCollection)
		{
			internalAdd( e );
		}
	}
	
	public void addAll(Iterator<Entity> iterator)
	{
		while (iterator.hasNext())
		{
			add(iterator.next());
		}
	}
	
	public void addAll(Iterable<Entity> iterable)
	{
		addAll( iterable.iterator() );
	}
	
	public void clean()
	{
		int alive = 0;
		
		for (int i = 0; i < entityCount; i++)
		{
			final Entity e = entities[i];
			
			if ( e.isExpired() )
			{
				onEntityRemove( e, i );
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
	}
	
	@Override
	public void draw(Object drawState)
	{
		super.draw( drawState );
		
		for (int i = 0; i < entityCount; i++)
		{
			entities[i].draw( drawState );
		}
	}
	
	@Override
	public void update(Object updateState)
	{
		super.update( updateState );
		
		if (isExpired())
		{
			return;
		}
		
		for (int i = 0; i < entityCount; i++)
		{
			final Entity e = entities[i];
			
			e.update( updateState );
			
			onEntityUpdated( e, i, updateState );
		}
		
		this.clean();
		
		if ( !iterator.hasNext() )
		{
			iterator = new EntityIterator();
		}
	}

	@Override
	public Iterator<Entity> iterator()
	{
		return (iterator.hasNext() ? new EntityIterator() : iterator.reset() );
	}
	
	public Iterable<Entity> filterByComponents(int ... componentIds)
	{
		BitSet componentsBits = EntityUtility.createBitset(componentIds);
		
		return ( componentIterator.hasNext() ? new ComponentIterator(componentsBits) : componentIterator.reset(componentsBits) );
	}
	
	public Iterable<Entity> filterByControllers(int ... controllerIds)
	{
		BitSet controllersBits = EntityUtility.createBitset(controllerIds);
		
		return ( controllerIterator.hasNext() ? new ControllerIterator(controllersBits) : controllerIterator.reset(controllersBits) );
	}
	
	public Iterable<Entity> filterByValue(int componentId, Object value)
	{
		return ( valueIterator.hasNext() ? new ValueIterator(componentId, value) : valueIterator.reset(componentId, value) );
	}
	
	public Iterable<Entity> filterByVisible(boolean visible)
	{
		return ( visibleIterator.hasNext() ? new VisibleIterator(visible) : visibleIterator.reset(visible) );
	}
	
	public Iterable<Entity> filterByEnabled(boolean enabled)
	{
		return ( enabledIterator.hasNext() ? new EnabledIterator(enabled) : enabledIterator.reset(enabled) );
	}
	
	public int size()
	{
		return entityCount;
	}
	
	public Entity getEntity(int index)
	{
		return entities[index];
	}
	
	private class EntityIterator implements Iterator<Entity>
	{
		
		private int index = 0;
		
		public EntityIterator reset()
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
	
	private abstract class FilterIterator implements Iterable<Entity>, Iterator<Entity>
	{

		private int index, last;
		
		protected abstract boolean isValid(Entity e);
		
		protected int findNext(int last)
		{
			while (last < entityCount)
			{
				Entity e = entities[last];
				
				if (isValid(e))
				{
					break;
				}
				
				last++;
			}
			
			return last;
		}
		
		@Override
		public boolean hasNext() 
		{
			return index < entityCount;
		}

		@Override
		public Entity next() 
		{
			last = index;
			index = findNext( index );
			return entities[last];
		}

		@Override
		public void remove() 
		{
			entities[last].expire();
		}

		@Override
		public Iterator<Entity> iterator() 
		{
			last = -1;
			index = findNext( 0 );
			
			return this;
		}
		
	}
	
	private class ComponentIterator extends FilterIterator
	{

		public BitSet components;

		public ComponentIterator()
		{
		}
		
		public ComponentIterator(BitSet components)
		{
			reset( components );
		}
		
		public ComponentIterator reset( BitSet components )
		{
			this.components = components;
			
			return this;
		}
		
		@Override
		protected boolean isValid(Entity e) 
		{
			EntityType type = e.type;
			
			return ( type != null && type.components.bitset.intersects(components) );
		}
	}
	
	private class ControllerIterator extends FilterIterator
	{
		
		public BitSet controllers;

		public ControllerIterator()
		{
		}
		
		public ControllerIterator(BitSet controllers)
		{
			reset( controllers );
		}
		
		public ControllerIterator reset( BitSet controllers )
		{
			this.controllers = controllers;
			
			return this;
		}
		
		@Override
		protected boolean isValid(Entity e) 
		{
			EntityType type = e.type;
			
			return ( type != null && type.controllers.bitset.intersects(controllers) );
		}
	}

	private class ValueIterator extends FilterIterator
	{
		private int componentId;
		private Object value;

		public ValueIterator()
		{
		}
		
		public ValueIterator(int componentId, Object value)
		{
			reset( componentId, value );
		}
		
		public ValueIterator reset( int componentId, Object value )
		{
			this.componentId = componentId;
			this.value = value;
			
			return this;
		}
		
		@Override
		protected boolean isValid(Entity e) 
		{
			return e.has(componentId) && EntityUtility.equals( e.get(componentId), value );
		}	
	}
	
	private class VisibleIterator extends FilterIterator
	{
		private boolean visible;
		
		public VisibleIterator()
		{
		}
		
		public VisibleIterator(boolean visible)
		{
			reset( visible );
		}
		
		public VisibleIterator reset(boolean visible)
		{
			this.visible = visible;
			
			return this;
		}
		
		@Override
		protected boolean isValid(Entity e) 
		{
			return e.isVisible() == visible;
		}	
	}
	
	private class EnabledIterator extends FilterIterator
	{
		private boolean enabled;
		
		public EnabledIterator()
		{
		}
		
		public EnabledIterator(boolean enabled)
		{
			reset( enabled );
		}
		
		public EnabledIterator reset(boolean enabled)
		{
			this.enabled = enabled;
			
			return this;
		}
		
		@Override
		protected boolean isValid(Entity e) 
		{
			return e.isEnabled() == enabled;
		}
	}
	
}

package org.magnos.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class EntityList extends Entity implements Iterable<Entity>
{
	
	public static int DEFAULT_CAPACITY = 16;

	protected Entity[] entities = {};
	protected int entityCount = 0;
	
	// Iterators
	protected EntityIterator iterator = new EntityIterator();
	protected final ComponentIterator componentIterator = new ComponentIterator();
	protected final ControllerIterator controllerIterator = new ControllerIterator();
	protected final ValueIterator valueIterator = new ValueIterator();
	protected final VisibleIterator visibleIterator = new VisibleIterator();
	protected final EnabledIterator enabledIterator = new EnabledIterator();
	protected final ExpiredIterator expiredIterator = new ExpiredIterator();
	protected final EntityTypeIterator entityTypeIterator = new EntityTypeIterator();
	protected final MethodIterator methodIterator = new MethodIterator();
	protected final MethodResultIterator methodResultIterator = new MethodResultIterator();
	
	public EntityList(EntityCore core)
	{
		this( new EntityType( core ), DEFAULT_CAPACITY );
	}
	
	public EntityList(EntityCore core, int initialCapacity)
	{
		this( new EntityType( core ), initialCapacity );
	}
	
	protected EntityList( EntityType entityType, int initialCapacity )
	{
		super( entityType );
		
		this.entities = new Entity[ initialCapacity ];
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
		BitSet componentsBits = new BitSet(componentIds);
		
		return ( componentIterator.hasNext() ? new ComponentIterator(componentsBits) : componentIterator.reset(componentsBits) );
	}
	
	public Iterable<Entity> filterByControllers(int ... controllerIds)
	{
		BitSet controllersBits = new BitSet(controllerIds);
		
		return ( controllerIterator.hasNext() ? new ControllerIterator(controllersBits) : controllerIterator.reset(controllersBits) );
	}
	
	public <T> Iterable<Entity> filterByValue(Component<T> component, T value)
	{
		return ( valueIterator.hasNext() ? new ValueIterator(component, value) : valueIterator.reset(component, value) );
	}
	
	public Iterable<Entity> filterByVisible(boolean visible)
	{
		return ( visibleIterator.hasNext() ? new VisibleIterator(visible) : visibleIterator.reset(visible) );
	}
	
	public Iterable<Entity> filterByEnabled(boolean enabled)
	{
		return ( enabledIterator.hasNext() ? new EnabledIterator(enabled) : enabledIterator.reset(enabled) );
	}
	
	public Iterable<Entity> filterByExpired(boolean expired)
	{
		return ( expiredIterator.hasNext() ? new ExpiredIterator(expired) : expiredIterator.reset(expired) );
	}
	
	public Iterable<Entity> filterByEntityType(EntityType type)
	{
		return ( entityTypeIterator.hasNext() ? new EntityTypeIterator(type) : entityTypeIterator.reset(type) );
	}
	
	public <R> Iterable<Entity> filterByMethod(Method<R> method)
	{
		return ( methodIterator.hasNext() ? new MethodIterator(method.id) : methodIterator.reset(method.id) );
	}
	
	public <R> Iterable<Entity> filterByMethodResult(Method<R> method, R result, Object ... arguments)
	{
		return ( methodResultIterator.hasNext() ? new MethodResultIterator(method, result, arguments) : methodResultIterator.reset(method, result, arguments) );
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
		private Component<?> component;
		private Object value;

		public ValueIterator()
		{
		}
		
		public ValueIterator(Component<?> component, Object value)
		{
			reset( component, value );
		}
		
		public ValueIterator reset( Component<?> component, Object value )
		{
			this.component = component;
			this.value = value;
			
			return this;
		}
		
		@Override
		protected boolean isValid(Entity e) 
		{
			return e.has(component) && EntityUtility.equals( e.get(component), value );
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
	
	private class ExpiredIterator extends FilterIterator
	{
		private boolean expired;
		
		public ExpiredIterator()
		{
		}
		
		public ExpiredIterator(boolean expired)
		{
			reset( expired );
		}
		
		public ExpiredIterator reset(boolean expired)
		{
			this.expired = expired;
			
			return this;
		}
		
		@Override
		protected boolean isValid(Entity e) 
		{
			return e.isExpired() == expired;
		}
	}
	
	private class EntityTypeIterator extends FilterIterator
	{
		private EntityType type;
		
		public EntityTypeIterator()
		{
		}
		
		public EntityTypeIterator(EntityType type)
		{
			reset( type );
		}
		
		public EntityTypeIterator reset(EntityType type)
		{
			this.type = type;
			
			return this;
		}
		
		@Override
		protected boolean isValid(Entity e) 
		{
			return e.getType() == type;
		}
	}
	
	private class MethodIterator extends FilterIterator
	{
		private int methodId;
		
		public MethodIterator()
		{
		}
		
		public MethodIterator(int methodId)
		{
			reset( methodId );
		}
		
		public MethodIterator reset(int methodId)
		{
			this.methodId = methodId;
			
			return this;
		}
		
		@Override
		protected boolean isValid(Entity e) 
		{
			return e.type.hasMethod( methodId );
		}
	}
	
	private class MethodResultIterator extends FilterIterator
	{
		private Method<?> method;
		private Object[] arguments;
		private Object expectedResult;
		
		public MethodResultIterator()
		{
		}
		
		public <R> MethodResultIterator(Method<R> method, R expectedResult, Object[] arguments)
		{
			reset( method, expectedResult, arguments );
		}
		
		public <R> MethodResultIterator reset(Method<R> method, R expectedResult, Object[] arguments)
		{
			this.method = method;
			this.expectedResult = expectedResult;
			this.arguments = arguments;
			
			return this;
		}
		
		@Override
		protected boolean isValid(Entity e) 
		{
			return EntityUtility.equals( expectedResult, e.executes( method, arguments ) );
		}
	}
	
}

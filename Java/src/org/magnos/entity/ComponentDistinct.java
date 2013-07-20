package org.magnos.entity;

import java.util.Arrays;

/**
 * A component that adds a value directly to the Entity, so that every entity 
 * has it's own distinct value. When added to an Entity a factory is invoked
 * to generate a default value, and when an Entity is cloned the factory is
 * also invoked to clone a given value.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The component value type.
 */
@SuppressWarnings("unchecked")
class ComponentDistinct<T> extends Component<T>
{
	
	/**
	 * The factory that creates the default value on the entity and also 
	 * handles cloning values from Entity to cloned Entity.
	 */
	private final ComponentFactory<T> factory;
	
	/**
	 * Instantiates a ComponentDistinct.
	 * 
	 * @param id
	 * 		The id of the component.
	 * @param name
	 * 		The name of the component.
	 * @param factory
	 * 		The factory used to create and clone values.
	 */
	protected ComponentDistinct( int id, String name, ComponentFactory<T> factory )
	{
		super( id, name );

		this.factory = factory;
	}

	@Override
	protected void postCustomAdd( Entity e )
	{
		ComponentValuedHandler handler = (ComponentValuedHandler) e.template.handlers[id];

		if ( handler.componentIndex >= e.values.length )
		{
			e.values = Arrays.copyOf( e.values, handler.componentIndex + 1 );
		}

		e.values[handler.componentIndex] = factory.create();
	}

	@Override
	protected TemplateComponent<T> add( Template template )
	{
		final ComponentFactory<?>[] factories = template.factories;
		
		int factoryIndex = EntityUtility.indexOfSame( factories, null );

		if ( factoryIndex == -1 )
		{
			factoryIndex = factories.length;

			template.factories = EntityUtility.append( factories, factory );
		}
		else
		{
			factories[factoryIndex] = factory;
		}

		return new ComponentValuedHandler( factoryIndex );
	}

	private class ComponentValuedHandler implements TemplateComponent<T>
	{
		private final int componentIndex;

		private ComponentValuedHandler( int componenentIndex )
		{
			this.componentIndex = componenentIndex;
		}

		@Override
		public void set( Entity e, T value )
		{
			e.values[componentIndex] = value;
		}

		@Override
		public T get( Entity e )
		{
			return (T) e.values[componentIndex];
		}

		@Override
		public T take(Entity e, T target) 
		{
			return factory.copy( (T)e.values[componentIndex], target );
		}

		@Override
		public void remove( Template template )
		{
			template.factories[componentIndex] = null;
		}
	}

}
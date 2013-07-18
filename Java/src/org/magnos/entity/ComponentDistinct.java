package org.magnos.entity;

import java.util.Arrays;

class ComponentDistinct<T> extends Component<T>
{
	
	public ComponentFactory<T> factory;

	
	public ComponentDistinct( int id, String name, ComponentFactory<T> factory )
	{
		super( id, name );

		this.factory = factory;
	}

	@SuppressWarnings("unchecked")
	protected void postCustomAdd( Entity e )
	{
		ComponentValuedHandler handler = (ComponentValuedHandler) e.template.handlers[id];

		if ( handler.componentIndex >= e.values.length )
		{
			e.values = Arrays.copyOf( e.values, handler.componentIndex + 1 );
		}

		e.values[handler.componentIndex] = factory.create();
	}

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
		int componentIndex;

		public ComponentValuedHandler( int componenentIndex )
		{
			this.componentIndex = componenentIndex;
		}

		public void set( Entity e, T value )
		{
			e.values[componentIndex] = value;
		}

		@SuppressWarnings("unchecked")
		public T get( Entity e )
		{
			return (T) e.values[componentIndex];
		}

		public void remove( Template template )
		{
			template.factories[componentIndex] = null;
		}
	}

}
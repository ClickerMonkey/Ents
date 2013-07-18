package org.magnos.entity;

public class ComponentShared<T> extends Component<T>
{

	public ComponentFactory<T> factory;

	public ComponentShared( int id, String name, ComponentFactory<T> factory )
	{
		super( id, name );

		this.factory = factory;
	}

	public void postCustomAdd( Entity e )
	{

	}

	public TemplateComponent<T> add( Template template )
	{
		return new ComponentSharedHandler( factory.create() );
	}

	private class ComponentSharedHandler implements TemplateComponent<T>
	{
		public T value;

		public ComponentSharedHandler( T value )
		{
			this.value = value;
		}

		public void set( Entity e, T value )
		{
			this.value = value;
		}

		public T get( Entity e )
		{
			return value;
		}

		public void remove( Template template )
		{

		}
	}

}

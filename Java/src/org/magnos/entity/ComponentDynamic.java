package org.magnos.entity;

public class ComponentDynamic<T> extends Component<T>
{
	public static interface Dynamic<T>
	{
		public T get( Entity e );

		public void set( Entity e, T target );
	}

	public Dynamic<T> dynamic;

	public final ComponentDynamicHandler handler;

	
	public ComponentDynamic( int id, String name, Dynamic<T> dynamic )
	{
		super( id, name );

		this.dynamic = dynamic;
		this.handler = new ComponentDynamicHandler();
	}

	public void postCustomAdd( Entity e )
	{

	}

	public TemplateComponent<T> add( Template template )
	{
		return handler;
	}

	private class ComponentDynamicHandler implements TemplateComponent<T>
	{
		public void set( Entity e, T value )
		{
			dynamic.set( e, value );
		}

		public T get( Entity e )
		{
			return dynamic.get( e );
		}

		public void remove( Template template )
		{

		}
	}

}
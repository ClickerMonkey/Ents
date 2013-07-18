package org.magnos.entity;

class ComponentDynamic<T> extends Component<T>
{


	public DynamicValue<T> dynamic;

	public final ComponentDynamicHandler handler;

	
	public ComponentDynamic( int id, String name, DynamicValue<T> dynamic )
	{
		super( id, name );

		this.dynamic = dynamic;
		this.handler = new ComponentDynamicHandler();
	}

	protected void postCustomAdd( Entity e )
	{

	}

	protected TemplateComponent<T> add( Template template )
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
package org.magnos.entity;

public class ComponentAlias<T> extends Component<T>
{
	public int componentId;

	public ComponentAlias( int id, String name, int componentId )
	{
		super( id, name );

		this.componentId = componentId;
	}

	public void postCustomAdd( Entity e )
	{

	}

	public TemplateComponent<T> add( Template template )
	{
		return new ComponentAliasHandler( template );
	}

	@SuppressWarnings("unchecked")
	private class ComponentAliasHandler implements TemplateComponent<T>
	{
		public final Template template;

		public ComponentAliasHandler( Template template )
		{
			this.template = template;
		}

		public void set( Entity e, T value )
		{
			TemplateComponent<T> handler = (TemplateComponent<T>) template.handlers[componentId];
			handler.set( e, value );
		}

		public T get( Entity e )
		{
			TemplateComponent<T> handler = (TemplateComponent<T>) template.handlers[componentId];
			return handler.get( e );
		}

		public void remove( Template template )
		{

		}
	}

}
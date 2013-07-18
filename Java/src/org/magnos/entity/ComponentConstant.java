package org.magnos.entity;

public class ComponentConstant<T> extends Component<T>
{

	public T constant;

	public boolean settable;

	public final ComponentConstantHandler handler;

	
	public ComponentConstant( int id, String name, T constant, boolean settable )
	{
		super( id, name );

		this.constant = constant;
		this.settable = settable;
		this.handler = new ComponentConstantHandler();
	}

	public void postCustomAdd( Entity e )
	{

	}

	public TemplateComponent<T> add( Template template )
	{
		return handler;
	}

	private class ComponentConstantHandler implements TemplateComponent<T>
	{
		public void set( Entity e, T value )
		{
			if ( settable )
			{
				constant = value;
			}
		}

		public T get( Entity e )
		{
			return constant;
		}

		public void remove( Template template )
		{

		}
	}

}
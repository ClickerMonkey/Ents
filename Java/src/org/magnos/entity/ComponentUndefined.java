package org.magnos.entity;

class ComponentUndefined<T> extends Component<T>
{

	public ComponentUndefined( int id, String name )
	{
		super( id, name );
	}

	protected void postCustomAdd( Entity e )
	{

	}

	protected TemplateComponent<T> add( Template template )
	{
		throw new RuntimeException( "An undefined component cannot be added to a Template" );
	}

}
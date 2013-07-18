package org.magnos.entity;

public class ComponentUndefined<T> extends Component<T>
{

	public ComponentUndefined( int id, String name )
	{
		super( id, name );
	}

	public void postCustomAdd( Entity e )
	{

	}

	public TemplateComponent<T> add( Template template )
	{
		throw new RuntimeException( "An undefined component cannot be added to an Template" );
	}

}
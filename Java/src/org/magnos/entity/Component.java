package org.magnos.entity;

public abstract class Component<T> extends Id
{

	public Component( int id, String name )
	{
		super( id, name );
	}

	public abstract TemplateComponent<T> add( Template template );

	public abstract void postCustomAdd( Entity e );

}
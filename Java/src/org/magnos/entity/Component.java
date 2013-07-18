package org.magnos.entity;

public abstract class Component<T> extends Id
{

	public Component( int id, String name )
	{
		super( id, name );
	}

	protected abstract TemplateComponent<T> add( Template template );

	protected abstract void postCustomAdd( Entity e );

}
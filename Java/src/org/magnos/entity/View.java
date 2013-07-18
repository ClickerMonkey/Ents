package org.magnos.entity;

public class View extends Id
{

	public static interface Renderer
	{
		void draw( Entity e, Object drawState );
	}

	public Renderer renderer;

	public View( int id, String name, Renderer renderer )
	{
		super( id, name );

		this.renderer = renderer;
	}

}
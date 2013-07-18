package org.magnos.entity;

public class Controller extends Id
{

	public static interface Control
	{
		public void control( Entity e, Object updateState );
	}

	public Control control;

	public Controller( int id, String name, Control control )
	{
		super( id, name );

		this.control = control;
	}

}
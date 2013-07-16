package org.magnos.entity;


public class Component<T> extends ComponentBase
{

	public final ComponentFactory<T> factory;
	
	public Component( EntityCore core, int id, String name, ComponentFactory<T> factory )
	{
		super( core, id, name );
		
		this.factory = factory;
	}

}

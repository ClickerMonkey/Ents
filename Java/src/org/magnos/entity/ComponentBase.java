package org.magnos.entity;

public class ComponentBase extends Identified
{

	public final EntityCore core;
	public final String name;
	
	public ComponentBase(EntityCore core, int id, String name)
	{
		super( id );
		
		this.core = core;
		this.name = name;
	}
	
}

package org.magnos.entity;

public class ControllerBase extends Identified
{
	
	public final EntityCore core;
	public final String name;
	public final BitSet required;
	public final Controller controller;
	
	public ControllerBase(EntityCore core, int id, String name, BitSet required, Controller controller)
	{
		super( id );
		
		this.core = core;
		this.name = name;
		this.required = required;
		this.controller = controller;
	}
	
}

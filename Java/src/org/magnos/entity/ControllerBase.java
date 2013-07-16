package org.magnos.entity;

public class ControllerBase
{
	
	public final EntityCore core;
	public final int id;
	public final String name;
	public final BitSet required;
	public final Controller controller;
	
	public ControllerBase(EntityCore core, int id, String name, BitSet required, Controller controller)
	{
		this.core = core;
		this.id = id;
		this.name = name;
		this.required = required;
		this.controller = controller;
	}
	
}

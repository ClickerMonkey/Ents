package org.magnos.entity;

public class MethodBase extends Identified
{
	
	public final EntityCore core;
	public final String name;
	public final BitSet required;
	
	public MethodBase(EntityCore core, int id, String name, BitSet required)
	{
		super( id );
		
		this.core = core;
		this.name = name;
		this.required = required;
	}
	
}

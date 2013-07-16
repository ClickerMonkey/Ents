package org.magnos.entity;

public class ViewBase extends Identified
{
	
	public final EntityCore core;
	public final String name;
	public final BitSet required;
	public final View view;
	
	public ViewBase(EntityCore core, int id, String name, BitSet required, View view)
	{
		super( id );
		
		this.core = core;
		this.name = name;
		this.required = required;
		this.view = view;
	}
	
}

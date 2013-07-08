package org.magnos.entity;

public class ComponentType 
{
	
	public final int id;
	public final String name;
	public final Class<?> type;
	public final ComponentFactory factory;
	
	protected ComponentType(int id, String name, Class<?> type, ComponentFactory factory)
	{
		this.id = id;
		this.name = name;
		this.type = type;
		this.factory = factory;
	}
	
}

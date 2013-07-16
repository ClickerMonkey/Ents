package org.magnos.entity;

public class ComponentGet<T> extends ComponentBase
{

	public static interface Get<T>
	{
		public T get(Entity e);
	}
	
	public final BitSet required;
	public final Get<T> getter;
	
	public ComponentGet( EntityCore core, int id, String name, BitSet required, Get<T> getter )
	{
		super( core, id, name );
		
		this.required = required;
		this.getter = getter;
	}

}

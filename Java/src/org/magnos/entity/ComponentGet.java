package org.magnos.entity;

public class ComponentGet<T> extends ComponentBase
{

	public static interface Get<T>
	{
		public T get(Entity e);
	}
	
	public final BitSet required;
	public final Get<T> get;
	
	public ComponentGet( EntityCore core, int id, String name, BitSet required, Get<T> get )
	{
		super( core, id, name );
		
		this.required = required;
		this.get = get;
	}

}

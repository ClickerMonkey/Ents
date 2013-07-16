package org.magnos.entity;

public class ComponentSet<T> extends ComponentBase
{

	public static interface Set<T>
	{
		public T set(Entity e, T target);
	}

	public final BitSet required;
	public final Set<T> setter;
	
	public ComponentSet( EntityCore core, int id, String name, BitSet required, Set<T> setter )
	{
		super( core, id, name );
		
		this.required = required;
		this.setter = setter;
	}

}

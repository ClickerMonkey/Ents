package org.magnos.entity;

public class Method<R> extends MethodBase
{
	
	public static interface Execute<R>
	{
		R execute( Entity e, Object ... arguments );
	}
	
	public final Execute<R> execute;
	
	public Method(EntityCore core, int id, String name, BitSet required, Execute<R> execute)
	{
		super( core, id, name, required );
		
		this.execute = execute;
	}
	
}

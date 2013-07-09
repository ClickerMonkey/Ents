package org.magnos.entity;

public interface ComponentFactory
{
	public Object create();

	public Object clone( Object value );
}

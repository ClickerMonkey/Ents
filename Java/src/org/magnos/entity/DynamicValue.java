package org.magnos.entity;

public interface DynamicValue<T>
{
	public T get( Entity e );

	public void set( Entity e, T target );
}
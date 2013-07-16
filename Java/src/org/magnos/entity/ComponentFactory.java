package org.magnos.entity;

public interface ComponentFactory<T>
{
	public T create();

	public T clone( T value );
}

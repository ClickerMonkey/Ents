package org.magnos.entity;

public interface TemplateComponent<T>
{
	
	public void set( Entity e, T value );

	public T get( Entity e );

	public void remove( Template template );
	
}
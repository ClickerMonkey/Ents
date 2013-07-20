package org.magnos.entity;

interface TemplateComponent<T>
{
	
	public void set( Entity e, T value );

	public T get( Entity e );
	
	public T take( Entity e, T target );

	public void remove( Template template );
	
}
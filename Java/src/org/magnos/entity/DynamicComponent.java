package org.magnos.entity;

public interface DynamicComponent<T> 
{
	public T compute(Entity e, T out);
}

package org.magnos.entity;


public class ComponentFactoryNull<T> implements ComponentFactory<T>
{

	@Override
	public T create()
	{
		return null;
	}

	@Override
	public T clone( Object value )
	{
		return null;
	}

}

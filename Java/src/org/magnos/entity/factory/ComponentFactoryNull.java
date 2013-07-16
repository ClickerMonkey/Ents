package org.magnos.entity.factory;

import org.magnos.entity.ComponentFactory;

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

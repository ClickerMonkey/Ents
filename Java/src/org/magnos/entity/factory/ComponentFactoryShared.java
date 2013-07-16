package org.magnos.entity.factory;

import org.magnos.entity.ComponentFactory;

public class ComponentFactoryShared<T> implements ComponentFactory<T>
{

	public T sharedComponentValue;

	public ComponentFactoryShared( T sharedComponentValue )
	{
		this.sharedComponentValue = sharedComponentValue;
	}

	@Override
	public T create()
	{
		return sharedComponentValue;
	}

	@Override
	public T clone( T value )
	{
		return sharedComponentValue;
	}

}

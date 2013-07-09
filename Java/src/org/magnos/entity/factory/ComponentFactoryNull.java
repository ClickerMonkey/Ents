package org.magnos.entity.factory;

import org.magnos.entity.ComponentFactory;

public class ComponentFactoryNull implements ComponentFactory
{

	private static final ComponentFactoryNull instance = new ComponentFactoryNull();

	public static ComponentFactoryNull get()
	{
		return instance;
	}

	@Override
	public Object create()
	{
		return null;
	}

	@Override
	public Object clone( Object value )
	{
		return null;
	}

}

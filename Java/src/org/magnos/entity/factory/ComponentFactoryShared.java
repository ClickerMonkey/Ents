package org.magnos.entity.factory;

import org.magnos.entity.ComponentFactory;

public class ComponentFactoryShared implements ComponentFactory
{
	
	public Object sharedComponentValue;
	
	public ComponentFactoryShared(Object sharedComponentValue)
	{
		this.sharedComponentValue = sharedComponentValue;
	}
	
	@Override
	public Object create() 
	{
		return sharedComponentValue;
	}
	
	@Override
	public Object clone(Object value)
	{
		return sharedComponentValue;
	}

}

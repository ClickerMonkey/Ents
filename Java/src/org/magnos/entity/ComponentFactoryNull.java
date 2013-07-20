package org.magnos.entity;

/**
 * A {@link ComponentFactory} implementation that provides a null default
 * value for a component and also returns null when given a value to clone.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The component value type.
 */
public class ComponentFactoryNull<T> implements ComponentFactory<T>
{

	@Override
	public T create()
	{
		return null;
	}

	@Override
	public T clone(T value) 
	{
		return null;
	}

	@Override
	public T copy(T from, T to) 
	{
		return to;
	}

}

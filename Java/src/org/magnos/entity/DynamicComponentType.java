package org.magnos.entity;

public class DynamicComponentType<T> extends ComponentType
{

	public final DynamicComponent<T> component;

	protected DynamicComponentType( int id, String name, Class<?> type, DynamicComponent<T> component )
	{
		super( id, name, type, null );

		this.component = component;
	}

}

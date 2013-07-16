package org.magnos.entity;

import java.lang.reflect.InvocationTargetException;

public abstract class MethodReflection<R> implements Method.Execute<R>
{

	protected final Object object;
	protected final java.lang.reflect.Method method;
	
	public MethodReflection(Object object, String methodName, Class<?> ... methodParameters) throws NoSuchMethodException, SecurityException
	{
		this( object, object.getClass().getMethod( methodName, methodParameters ) );
	}
	
	public MethodReflection(Object object, java.lang.reflect.Method method)
	{
		this.object = object;
		this.method = method;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public R execute( Entity e, Object... args )
	{
		try
		{
			return (R) method.invoke( object, args );
		}
		catch ( IllegalAccessException err ) 
		{
			throw new RuntimeException( err );
		}
		catch ( IllegalArgumentException err ) 
		{
			throw new RuntimeException( err );	
		}
		catch ( InvocationTargetException err )
		{
			throw new RuntimeException( err );
		}
	}
	
}

package org.magnos.entity;

import java.lang.reflect.InvocationTargetException;

public class MethodReflection<R> implements Method.Execute<R>
{

	protected final Object object;
	protected final java.lang.reflect.Method method;
	
	public MethodReflection(Object object, String methodName, Class<?> ... methodParameters) throws NoSuchMethodException, SecurityException
	{
		this( object, object.getClass().getMethod( methodName, EntityUtility.prepend(Entity.class, methodParameters ) ) );
	}
	
	public MethodReflection(Class<?> staticClass, String staticMethodName, Class<?> ... staticMethodParameters) throws NoSuchMethodException, SecurityException
	{
		this( null, staticClass.getMethod( staticMethodName, EntityUtility.prepend( Entity.class, staticMethodParameters ) ) );
	}
	
	private MethodReflection(Object object, java.lang.reflect.Method method)
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
			return (R) method.invoke( object, EntityUtility.prepend( e, args ) );
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
	
	public static <R> MethodReflection<R> staticMethod(Class<R> returnType, Class<?> staticClass, String staticMethodName, Class<?> ... staticMethodParameters)
	{
		try
		{
			return new MethodReflection<R>( staticClass, staticMethodName, staticMethodParameters );
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static <R> MethodReflection<R> localMethod(Class<R> returnType, Object object, String methodName, Class<?> ... methodParameters)
	{
		try
		{
			return new MethodReflection<R>( object, methodName, methodParameters );
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
}

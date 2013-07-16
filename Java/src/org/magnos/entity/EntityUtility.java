package org.magnos.entity;

import java.util.Arrays;

public class EntityUtility 
{
	
	public static <T> T[] prepend(T element, T[] array)
	{
		array = Arrays.copyOf( array, array.length + 1 );
		System.arraycopy( array, 0, array, 1, array.length - 1 );
		array[0] = element;
		return array;
	}
	
	public static <T> T[] append(T[] array, T element)
	{
		array = Arrays.copyOf( array, array.length + 1 );
		array[ array.length - 1 ] = element;
		return array;
	}

	public static boolean equals(Object a, Object b)
	{
		return (a == b || (a != null && b != null && a.equals(b)));
	}
	
	public static <T> boolean equals(T[] a, T[] b)
	{
		if ( a == b ) {
			return true;
		}
		if ( a == null || b == null ) {
			return false;
		}
		if (a.length != b.length) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			if (!equals(a[i], b[i])) {
				return false;
			}
		}
		return true;
	}
	
	
}

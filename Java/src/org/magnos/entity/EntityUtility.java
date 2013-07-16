package org.magnos.entity;

public class EntityUtility 
{

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

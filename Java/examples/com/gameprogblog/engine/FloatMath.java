
package com.gameprogblog.engine;

import java.util.Arrays;
import java.util.List;
import java.util.Random;



public class FloatMath
{

	public static final Random random = new Random();
	public static final float PI = (float)Math.PI;
	public static final float PI2 = (float)(Math.PI * 2.0);
	public static final float EPSILON = 0.00001f;

	public static boolean equals( float a, float b )
	{
		return equals( a, b, EPSILON );
	}

	public static boolean equals( float a, float b, float epsilon )
	{
		return Math.abs( a - b ) < epsilon;
	}

	public static float sqrt( float x )
	{
		return (float)Math.sqrt( x );
	}

	public static float cos( float x )
	{
		return (float)Math.cos( x );
	}

	public static float sin( float x )
	{
		return (float)Math.sin( x );
	}

	public static float clamp( float d, float min, float max )
	{
		return (d < min ? min : (d > max ? max : d));
	}

	public static int clamp( int d, int min, int max )
	{
		return (d < min ? min : (d > max ? max : d));
	}

	public static Vector closest( Vector s, Vector e, Vector v, Vector out )
	{
		float dx = e.x - s.x;
		float dy = e.y - s.y;
		float delta = ((v.x - s.x) * dx + (v.y - s.y) * dy) / (dx * dx + dy * dy);

		delta = clamp( delta, 0, 1 );

		out.x = (s.x + delta * dx);
		out.y = (s.y + delta * dy);

		return out;
	}

	public static float interceptTime( Vector shooter, float shooterSpeed, Vector targetPosition, Vector targetVelocity)
	{
		 float tx = targetPosition.x - shooter.x;
		 float ty = targetPosition.y - shooter.y;
		 
		 float a = sqr( targetVelocity.x ) + sqr( targetVelocity.y ) - sqr( shooterSpeed );
		 float b = 2 * (targetVelocity.x * tx + targetVelocity.y * ty);
		 float c = sqr( tx ) + sqr( ty );    

		 float t0 = Float.MIN_VALUE;
		 float t1 = Float.MIN_VALUE;

		 if (Math.abs(a) < EPSILON) 
		 {
			 if (Math.abs(b) < EPSILON) 
			 {
				 if (Math.abs(c) < EPSILON) 
				 {
					 t0 = 0.0f;
					 t1 = 0.0f;
				 }
			 } 
			 else 
			 {
				 t0 = -c / b;
				 t1 = -c / b;
			 }
		 }
		 else 
		 {
			 float disc = sqr( b ) - 4 * a * c;
			 
			 if (disc >= 0) 
			 {
				 disc = sqrt(disc);
				 a = 2 * a;
				 t0 = (-b - disc) / a;
				 t1 = (-b + disc) / a;
			 }
		 }
		 
		 if ( t0 != Float.MIN_VALUE )
		 {
			 float t = Math.min( t0, t1 );
			 
			 if (t < 0)
			 {
				 t = Math.max(t0, t1);    
			 }
			 
			 if (t > 0) 
			 {
				 return t;
			 }
		 }
		 
		 return -1;
	}
	
	public static float sqr( float x )
	{
		return x * x;
	}
	
	public static float getDistanceFromLine(Vector point, Vector start, Vector end)
	{
		float lineLength = start.distance( end );
		float startToPoint = point.distance( start );
		float endToPoint = point.distance( end );

		return getTriangleHeight( lineLength, startToPoint, endToPoint );
	}
	
	public static float getDistanceFromLine(Vector point, Vector start, Vector end, Vector temp)
	{
		temp.set( start );
		float lineLength = temp.distance( end );
		float startToPoint = temp.distance( point );
		temp.set( end );
		float endToPoint = temp.distance( point );
		
		return getTriangleHeight( lineLength, startToPoint, endToPoint );
	}
	
	public static float getTriangleHeight(float base, float side1, float side2)
	{
		float p = (base + side1 + side2) * 0.5f;
		float area = sqrt( p * (p - base) * (p - side1) * (p - side2) );
		float height = area * 2.0f / base;
		
		return height;
	}
	
	/**
	 * Returns whether a circle is within view of an object at the origin 
	 * facing some direction.
	 * 
	 * @param origin
	 * 	The origin of the view.
	 * @param direction
	 * 	The direction of the view, must be a normalized vector.
	 * @param fov
	 * 	The vector where x=cos(FOV/2) and y=sin(FOV/2), must be normalized by definition.
	 * @param circle
	 * 	The center of the circle.
	 * @param radius
	 * 	The radius of the circle.
	 * @param entirely
	 * 	True if this method should return whether the circle is completely 
	 * 	within view, or false if this method should return whether the circle 
	 * 	is partially within view.
	 * @return
	 * 	True if the target is in view, otherwise false.
	 */
	public static boolean isCircleInView( Vector origin, Vector direction, Vector fov, Vector circle, float radius, boolean entirely )
	{
		// Calculate upper and lower vectors
		// Calculate forward vector between target and origin
		// Rotate forward by upper, resulting vector.y is the distance from upper to target
		// Rotate forward by lower, resulting vector.y is the distance from lower to target
		// If it doesn't matter if it's entirely in view, add radius*2 to the distances.
		
		float fovy = Math.abs( fov.y );
		float dxfx = direction.x * fov.x;
		float dxfy = direction.x * fovy;
		float dyfx = direction.y * fov.x;
		float dyfy = direction.y * fovy;
		float upperX = (dxfx - dyfy);
		float upperY = (dxfy + dyfx);
		float lowerX = (dxfx + dyfy);
		float lowerY = (dyfx - dxfy);
		float forwardX = circle.x - origin.x;
		float forwardY = circle.y - origin.y;
		float upperDist = (upperX * forwardY + upperY * forwardX);
		float lowerDist =-(lowerX * forwardY + lowerY * forwardX);
		
		if (!entirely)
		{
			upperDist += radius * 2;
			lowerDist += radius * 2;
		}
		
		return (lowerDist >= radius && upperDist >= radius) || // FOV <= 90
				 (fov.x < 0 && (upperDist >= radius || lowerDist >= radius)); // FOV >= 90
	}

//	public static boolean isCircleInView( Vector origin, Vector direction, Vector fov, Vector circle, float radius, FieldOfView fovType )
//	{
//		if ( fovType == FieldOfView.IGNORE )
//		{
//			return true;
//		}
//		
//		if ( fovType == FieldOfView.HALF )
//		{
//			radius = 0f;
//		}
//		
//		return isCircleInView( origin, direction, fov, circle, radius, fovType == FieldOfView.FULL );
//	}

	public static int factorial( int x )
	{
		int n = x;
		while (--x >= 1)
		{
			n *= x;
		}
		return n;
	}

	// greatest common divisor, 32-bit integer
	public static int gcd( int a, int b )
	{
		int shift = 0;

		if (a == 0 || b == 0)
		{
			return (a | b);
		}

		for (shift = 0; ((a | b) & 1) == 0; ++shift)
		{
			a >>= 1;
			b >>= 1;
		}

		while ((a & 1) == 0)
		{
			a >>= 1;
		}

		do
		{
			while ((b & 1) == 0)
			{
				b >>= 1;
			}
			if (a < b)
			{
				b -= a;
			}
			else
			{
				int d = a - b;
				a = b;
				b = d;
			}
			b >>= 1;
		}
		while (b != 0);

		return (a << shift);
	}

	// greatest common divisor, 64-bit integer
	public static long gcd( long a, long b )
	{
		int shift = 0;

		if (a == 0 || b == 0)
		{
			return (a | b);
		}

		for (shift = 0; ((a | b) & 1) == 0; ++shift)
		{
			a >>= 1;
			b >>= 1;
		}

		while ((a & 1) == 0)
		{
			a >>= 1;
		}

		do
		{
			while ((b & 1) == 0)
			{
				b >>= 1;
			}
			if (a < b)
			{
				b -= a;
			}
			else
			{
				long d = a - b;
				a = b;
				b = d;
			}
			b >>= 1;
		}
		while (b != 0);

		return (a << shift);
	}

	// Calculates the combination of the given integer n and m. Un-ordered
	// collection of distinct elements.
	// C(n,m) = n! / m!(n - m)!
	public static long choose( long n, long m )
	{
		long num = 1, den = 1, gcd;

		if (m > (n >> 1))
		{
			m = n - m;
		}

		while (m >= 1)
		{
			num *= n--;
			den *= m--;
			gcd = gcd( num, den );
			num /= gcd;
			den /= gcd;
		}

		return num;
	}

	// Calculates the combination of the given integer n and m. Un-ordered
	// collection of distinct elements.
	// C(n,m) = n! / m!(n - m)!
	public static int choose( int n, int m )
	{
		int num = 1, den = 1, gcd;

		if (m > (n >> 1))
		{
			m = n - m;
		}

		while (m >= 1)
		{
			num *= n--;
			den *= m--;
			gcd = gcd( num, den );
			num /= gcd;
			den /= gcd;
		}

		return num;
	}

	public static <T> T[] add( T e, T[] elements )
	{
		int size = elements.length;
		elements = Arrays.copyOf( elements, size + 1 );
		elements[size] = e;
		return elements;
	}
	

	public static float[] add(float e, float[] elements)
	{
		int size = elements.length;
		elements = Arrays.copyOf(elements, size + 1);
		elements[size] = e;
		return elements;
	}
	
	public static int[] add(int e, int[] elements)
	{
		int size = elements.length;
		elements = Arrays.copyOf(elements, size + 1);
		elements[size] = e;
		return elements;
	}
	
	public static boolean[] add(boolean e, boolean[] elements)
	{
		int size = elements.length;
		elements = Arrays.copyOf(elements, size + 1);
		elements[size] = e;
		return elements;
	}

	public static void setRandomSeed( long randomSeed )
	{
		random.setSeed( randomSeed );
	}

	public static float randomFloat()
	{
		return random.nextFloat();
	}

	public static float randomFloat( float x )
	{
		return random.nextFloat() * x;
	}

	public static float randomFloat( float min, float max )
	{
		return random.nextFloat() * (max - min) + min;
	}

	public static int randomSign()
	{
		return (random.nextInt( 2 ) << 1) - 1;
	}

	public static int randomInt()
	{
		return random.nextInt();
	}

	public static int randomInt( int x )
	{
		return random.nextInt( x );
	}

	public static int randomInt( int min, int max )
	{
		return random.nextInt( max - min + 1 ) + min;
	}

	public static long randomLong()
	{
		return random.nextLong();
	}
	
	public static long randomLong( long min, long max )
	{
		return (long)((max - min + 1 ) * random.nextDouble() + min);
	}

	public static boolean randomBoolean()
	{
		return random.nextBoolean();
	}

	public static <E extends Enum<E>> E random( Class<E> enumClass )
	{
		return random( enumClass.getEnumConstants() );
	}

	public static <T> T random( T[] elements )
	{
		return elements[random.nextInt( elements.length )];
	}

	public static <T> T random( T[] elements, T returnOnNull )
	{
		return (elements == null || elements.length == 0 ? returnOnNull : elements[random.nextInt( elements.length )]);
	}

	public static <T> T random( T[] elements, int min, int max, T returnOnNull )
	{
		return (elements == null || elements.length == 0 ? returnOnNull : elements[random.nextInt( max - min + 1 ) + min]);
	}

	public static <T> T random( List<T> elements )
	{
		return elements.get( random.nextInt( elements.size() ) );
	}

	public static <T> T random( List<T> elements, int min, int max, T returnOnNull )
	{
		return (elements == null || elements.size() == 0 ? returnOnNull : elements.get( random.nextInt( max - min + 1 ) + min ));
	}

	public static final int MATRIX_DIMENSIONS = 4;

	public static Vector parametricCubicCurve(float delta, Vector[] points, float[][] matrix, float weight, Vector out)
	{
		final int n = points.length - 1;
		final float a = delta * n;
		final int i = clamp( (int)a, 0, n - 1 );
		final float d = a - i;
	
		final Vector p0 = (i == 0 ? points[0] : points[i - 1]);
		final Vector p1 = points[i];
		final Vector p2 = points[i + 1];
		final Vector p3 = (i == n - 1 ? points[n] : points[i + 2]);
		
		FloatMath.cubicCurve( d, p0, p1, p2, p3, matrix, out );
		
		out.muli( weight );
		
		return out;
	}

	public static void cubicCurve(float t, Vector p0, Vector p1, Vector p2, Vector p3, float[][] matrix, Vector out)
	{
		final Vector temp = out.clone();
		final float[] ts = { 1.0f, t, t * t, t * t * t };
		
		out.muli( 0 );
		
		for (int i = 0; i < MATRIX_DIMENSIONS; i++)
		{
			temp.clear();
			temp.addsi( p0, matrix[i][0] );
			temp.addsi( p1, matrix[i][1] );
			temp.addsi( p2, matrix[i][2] );
			temp.addsi( p3, matrix[i][3] );
			
			out.addsi( temp, ts[i] );
		}
	}

	public static void cubicCurveCached(Vector p0, Vector p1, Vector p2, Vector p3, float[][] matrix, Vector[] out)
	{
		for (int i = 0; i < MATRIX_DIMENSIONS; i++)
		{
			Vector a = out[i];
			
			a.set( p0 );
			a.muli( matrix[i][0] );
			a.addsi( p1, matrix[i][1] );
			a.addsi( p2, matrix[i][2] );
			a.addsi( p3, matrix[i][3] );
		}
	}

}

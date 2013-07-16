package org.magnos.entity.helper;

import org.magnos.entity.ComponentFactory;

public class Scalar implements ComponentFactory<Scalar>
{
	
	public float x;

	public Scalar()
	{
	}
	
	public Scalar( float x )
	{
		this.x = x;
	}

	@Override
	public Scalar create()
	{
		return new Scalar( x );
	}

	@Override
	public Scalar clone( Scalar value )
	{
		return new Scalar( value.x );
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits( x );
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		Scalar other = (Scalar) obj;
		if ( Float.floatToIntBits( x ) != Float.floatToIntBits( other.x ) )
			return false;
		return true;
	}
	
}
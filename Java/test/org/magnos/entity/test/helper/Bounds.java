package org.magnos.entity.test.helper;

import org.magnos.entity.ComponentFactory;

public class Bounds implements ComponentFactory<Bounds>
{
	
	public float left, top, right, bottom;

	public Bounds()
	{
	}
	
	public Bounds(float left, float top, float right, float bottom)
	{
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	@Override
	public Bounds create()
	{
		return copy( this, new Bounds() );
	}

	@Override
	public Bounds clone( Bounds value )
	{
		return copy( value, new Bounds() );
	}

	@Override
	public Bounds copy(Bounds from, Bounds to) 
	{
		to.left = from.left;
		to.top = from.top;
		to.right = from.right;
		to.bottom = from.bottom;
		
		return to;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits( bottom );
		result = prime * result + Float.floatToIntBits( left );
		result = prime * result + Float.floatToIntBits( right );
		result = prime * result + Float.floatToIntBits( top );
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
		Bounds other = (Bounds) obj;
		if ( Float.floatToIntBits( bottom ) != Float.floatToIntBits( other.bottom ) )
			return false;
		if ( Float.floatToIntBits( left ) != Float.floatToIntBits( other.left ) )
			return false;
		if ( Float.floatToIntBits( right ) != Float.floatToIntBits( other.right ) )
			return false;
		if ( Float.floatToIntBits( top ) != Float.floatToIntBits( other.top ) )
			return false;
		return true;
	}
	
}

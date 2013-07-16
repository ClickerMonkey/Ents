package org.magnos.entity.helper;

import org.magnos.entity.ComponentFactory;

public class Index implements ComponentFactory<Index>
{
	
	public int x;
	
	public Index()
	{
	}

	public Index( int x )
	{
		this.x = x;
	}

	@Override
	public Index create()
	{
		return new Index( x );
	}

	@Override
	public Index clone( Index value )
	{
		return new Index( value.x );
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
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
		Index other = (Index) obj;
		if ( x  != other.x )
			return false;
		return true;
	}
	
}
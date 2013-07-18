package org.magnos.entity;

public class BitSet extends java.util.BitSet
{

	private static final long serialVersionUID = 1L;

	public BitSet()
	{
	}

	public BitSet( int size )
	{
		super( size );
	}
	
	public BitSet( int ... ids )
	{
		super( ids.length );

		for (int i = 0; i < ids.length; i++)
		{
			set( ids[i] );
		}
	}

	public BitSet( Id... ids )
	{
		super( ids.length );

		for (int i = 0; i < ids.length; i++)
		{
			set( ids[i].id );
		}
	}

	public boolean contains( BitSet other )
	{
		int i = -1;

		while ( ( i = other.nextSetBit( i + 1 ) ) >= 0 )
		{
			if ( !get( i ) )
			{
				return false;
			}
		}

		return true;
	}

}

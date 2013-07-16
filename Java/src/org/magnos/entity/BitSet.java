package org.magnos.entity;

public class BitSet extends java.util.BitSet
{
	
	private static final long serialVersionUID = 1L;

	public BitSet()
	{
	}

	public BitSet(int ... indices)
	{
		super( indices.length );
		
		for (int i = 0; i < indices.length; i++) 
		{
			set( indices[i] );
		}
	}
	
	public BitSet(Identified ... identified)
	{
		super( identified.length );
		
		for (int i = 0; i < identified.length; i++) 
		{
			set( identified[i].id );
		}
	}
	
	public boolean contains(BitSet other)
	{
		int i = -1;
		
		while ((i = other.nextSetBit( i + 1 )) >= 0)
		{
			if (!get( i )) 
			{
				return false;
			}
		}
		
		return true;
	}
	
	public boolean get(int bitIndex, boolean defaultValue)
	{
		return ( bitIndex >= length() ? defaultValue : get( bitIndex ) );
	}
	
}

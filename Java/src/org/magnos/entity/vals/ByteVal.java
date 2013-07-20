package org.magnos.entity.vals;

import org.magnos.entity.ComponentFactory;

/**
 * A mutable wrapper around a byte that should replace the usage of the
 * {@link java.lang.Byte}. Using {@link java.lang.Byte} should be 
 * avoided, the "invisible" autoboxing and unboxing can cause a significant
 * performance hit in games when used inappropriately.  
 * 
 * @author Philip Diffenderfer
 *
 */
public class ByteVal implements ComponentFactory<ByteVal> 
{
	
	public byte v;
	
	public ByteVal()
	{
	}
	
	public ByteVal(byte v)
	{
		this.v = v;
	}
	
	public ByteVal(ByteVal iv)
	{
		this.v = iv.v;
	}

	@Override
	public ByteVal create() 
	{
		return new ByteVal( v );
	}

	@Override
	public ByteVal clone(ByteVal value) 
	{
		return new ByteVal( value );
	}

	@Override
	public ByteVal copy(ByteVal from, ByteVal to) 
	{
		to.v = from.v;
		
		return to;
	}

	@Override
	public int hashCode() 
	{
		return v;
	}

	@Override
	public boolean equals(Object obj) 
	{
		return (obj instanceof ByteVal) && ((ByteVal)obj).v == v;
	}

	@Override
	public String toString() 
	{
		return String.valueOf( v );
	}

}

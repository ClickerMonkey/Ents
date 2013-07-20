package org.magnos.entity.vals;

import org.magnos.entity.ComponentFactory;

/**
 * A mutable wrapper around a short that should replace the usage of the
 * {@link java.lang.Short}. Using {@link java.lang.Short} should be 
 * avoided, the "invisible" autoboxing and unboxing can cause a significant
 * performance hit in games when used inappropriately.  
 * 
 * @author Philip Diffenderfer
 *
 */
public class ShortVal implements ComponentFactory<ShortVal> 
{
	
	public short v;
	
	public ShortVal()
	{
	}
	
	public ShortVal(short v)
	{
		this.v = v;
	}
	
	public ShortVal(ShortVal iv)
	{
		this.v = iv.v;
	}

	@Override
	public ShortVal create() 
	{
		return new ShortVal( v );
	}

	@Override
	public ShortVal clone(ShortVal value) 
	{
		return new ShortVal( value );
	}

	@Override
	public ShortVal copy(ShortVal from, ShortVal to) 
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
		return (obj instanceof ShortVal) && ((ShortVal)obj).v == v;
	}

	@Override
	public String toString() 
	{
		return String.valueOf( v );
	}

}

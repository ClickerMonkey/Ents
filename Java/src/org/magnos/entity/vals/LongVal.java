package org.magnos.entity.vals;

import org.magnos.entity.ComponentFactory;

/**
 * A mutable wrapper around a long that should replace the usage of the
 * {@link java.lang.Long}. Using {@link java.lang.Long} should be 
 * avoided, the "invisible" autoboxing and unboxing can cause a significant
 * performance hit in games when used inappropriately.  
 * 
 * @author Philip Diffenderfer
 *
 */
public class LongVal implements ComponentFactory<LongVal> 
{
	
	public long v;
	
	public LongVal()
	{
	}
	
	public LongVal(long v)
	{
		this.v = v;
	}
	
	public LongVal(LongVal iv)
	{
		this.v = iv.v;
	}

	@Override
	public LongVal create() 
	{
		return new LongVal( v );
	}

	@Override
	public LongVal clone(LongVal value) 
	{
		return new LongVal( value );
	}

	@Override
	public LongVal copy(LongVal from, LongVal to) 
	{
		to.v = from.v;
		
		return to;
	}

	@Override
	public int hashCode() 
	{
		return (int)(v ^ (v >>> 32));
	}

	@Override
	public boolean equals(Object obj) 
	{
		return (obj instanceof LongVal) && ((LongVal)obj).v == v;
	}

	@Override
	public String toString() 
	{
		return String.valueOf( v );
	}

}

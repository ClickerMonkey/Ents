package org.magnos.entity.vals;

import org.magnos.entity.ComponentFactory;

/**
 * A mutable wrapper around a boolean that should replace the usage of the
 * {@link java.lang.BoolVal}. Using {@link java.lang.BoolVal} should be 
 * avoided, the "invisible" autoboxing and unboxing can cause a significant
 * performance hit in games when used inappropriately.  
 * 
 * @author Philip Diffenderfer
 *
 */
public class BoolVal implements ComponentFactory<BoolVal> 
{
	
	public boolean v;
	
	public BoolVal()
	{
	}
	
	public BoolVal(boolean v)
	{
		this.v = v;
	}
	
	public BoolVal(BoolVal iv)
	{
		this.v = iv.v;
	}

	@Override
	public BoolVal create() 
	{
		return new BoolVal( v );
	}

	@Override
	public BoolVal clone(BoolVal value) 
	{
		return new BoolVal( value );
	}

	@Override
	public BoolVal copy(BoolVal from, BoolVal to) 
	{
		to.v = from.v;
		
		return to;
	}

	@Override
	public int hashCode() 
	{
		return (v ? 1231 : 1237);
	}

	@Override
	public boolean equals(Object obj) 
	{
		return (obj instanceof BoolVal) && ((BoolVal)obj).v == v;
	}

	@Override
	public String toString() 
	{
		return String.valueOf( v );
	}

}

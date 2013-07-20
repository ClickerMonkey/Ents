package org.magnos.entity.vals;

import org.magnos.entity.ComponentFactory;

/**
 * A mutable wrapper around an int that should replace the usage of the
 * {@link java.lang.Integer}. Using {@link java.lang.Integer} should be 
 * avoided, the "invisible" autoboxing and unboxing can cause a significant
 * performance hit in games when used inappropriately.  
 * 
 * @author Philip Diffenderfer
 *
 */
public class IntVal implements ComponentFactory<IntVal> 
{
	
	public int v;
	
	public IntVal()
	{
	}
	
	public IntVal(int v)
	{
		this.v = v;
	}
	
	public IntVal(IntVal iv)
	{
		this.v = iv.v;
	}

	@Override
	public IntVal create() 
	{
		return new IntVal( v );
	}

	@Override
	public IntVal clone(IntVal value) 
	{
		return new IntVal( value );
	}

	@Override
	public IntVal copy(IntVal from, IntVal to) 
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
		return (obj instanceof IntVal) && ((IntVal)obj).v == v;
	}

	@Override
	public String toString() 
	{
		return String.valueOf( v );
	}

}

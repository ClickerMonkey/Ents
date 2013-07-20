package org.magnos.entity.vals;

import org.magnos.entity.ComponentFactory;

/**
 * A mutable wrapper around a char that should replace the usage of the
 * {@link java.lang.Character}. Using {@link java.lang.Character} should be 
 * avoided, the "invisible" autoboxing and unboxing can cause a significant
 * performance hit in games when used inappropriately.  
 * 
 * @author Philip Diffenderfer
 *
 */
public class CharVal implements ComponentFactory<CharVal> 
{
	
	public char v;
	
	public CharVal()
	{
	}
	
	public CharVal(char v)
	{
		this.v = v;
	}
	
	public CharVal(CharVal iv)
	{
		this.v = iv.v;
	}

	@Override
	public CharVal create() 
	{
		return new CharVal( v );
	}

	@Override
	public CharVal clone(CharVal value) 
	{
		return new CharVal( value );
	}

	@Override
	public CharVal copy(CharVal from, CharVal to) 
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
		return (obj instanceof CharVal) && ((CharVal)obj).v == v;
	}

	@Override
	public String toString() 
	{
		return String.valueOf( v );
	}

}

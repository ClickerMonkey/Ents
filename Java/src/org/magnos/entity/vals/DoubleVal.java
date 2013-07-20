package org.magnos.entity.vals;

import org.magnos.entity.ComponentFactory;

/**
 * A mutable wrapper around a double that should replace the usage of the
 * {@link java.lang.Double}. Using {@link java.lang.Double} should be 
 * avoided, the "invisible" autoboxing and unboxing can cause a significant
 * performance hit in games when used inappropriately.  
 * 
 * @author Philip Diffenderfer
 *
 */
public class DoubleVal implements ComponentFactory<DoubleVal> 
{
	
	public double v;
	
	public DoubleVal()
	{
	}
	
	public DoubleVal(double v)
	{
		this.v = v;
	}
	
	public DoubleVal(DoubleVal iv)
	{
		this.v = iv.v;
	}

	@Override
	public DoubleVal create() 
	{
		return new DoubleVal( v );
	}

	@Override
	public DoubleVal clone(DoubleVal value) 
	{
		return new DoubleVal( value );
	}

	@Override
	public DoubleVal copy(DoubleVal from, DoubleVal to) 
	{
		to.v = from.v;
		
		return to;
	}

	@Override
	public int hashCode() 
	{
        long bits = Double.doubleToLongBits(v);
        
        return (int)(bits ^ (bits >>> 32));
	}

	@Override
	public boolean equals(Object obj) 
	{
		return (obj instanceof DoubleVal) && (Double.doubleToLongBits(((DoubleVal)obj).v) == Double.doubleToLongBits(v));
	}

	@Override
	public String toString() 
	{
		return String.valueOf( v );
	}

}

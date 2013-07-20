package org.magnos.entity.filters;

import org.magnos.entity.Entity;
import org.magnos.entity.EntityFilter;

/**
 * A filter that returns all Entities.
 * 
 * @author Philip Diffenderfer
 * @see EntityFilter
 *
 */
public class DefaultFilter extends EntityFilter 
{

	/**
	 * Instantiates a new DefaultFilter.
	 * 
	 * @param defaultFilterCapacity
	 * 		The default capacity of this filter. The filter works by filling an
	 * 		array of entities that meet the filtering criteria. If the array is
	 * 		not large enough it resizes to 150% it's previous size.
	 */
	public DefaultFilter(int defaultFilterCapacity) 
	{
		super(defaultFilterCapacity);
	}
	
	@Override
	public boolean isValid(Entity e) 
	{
		return true;
	}

}

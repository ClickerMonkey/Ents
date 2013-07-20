package org.magnos.entity.filters;

import org.magnos.entity.Entity;
import org.magnos.entity.EntityFilter;

/**
 * A filter that only returns Entities that are enabled.
 * 
 * @author Philip Diffenderfer
 * @see EntityFilter
 *
 */
public class EnabledFilter extends EntityFilter 
{
	
	/**
	 * Instantiates a new EnabledFilter.
	 * 
	 * @param defaultFilterCapacity
	 * 		The default capacity of this filter. The filter works by filling an
	 * 		array of entities that meet the filtering criteria. If the array is
	 * 		not large enough it resizes to 150% it's previous size.
	 */
	public EnabledFilter(int defaultFilterCapacity) 
	{
		super(defaultFilterCapacity);
	}
	
	@Override
	public boolean isValid(Entity e) 
	{
		return e.isEnabled();
	}

}

package org.magnos.entity.filters;

import org.magnos.entity.Entity;
import org.magnos.entity.EntityFilter;

/**
 * A filter that only returns Entities that are visible.
 * 
 * @author Philip Diffenderfer
 * @see EntityFilter
 *
 */
public class VisibleFilter extends EntityFilter 
{
	
	/**
	 * Instantiates a new VisibleFilter.
	 * 
	 * @param defaultFilterCapacity
	 * 		The default capacity of this filter. The filter works by filling an
	 * 		array of entities that meet the filtering criteria. If the array is
	 * 		not large enough it resizes to 150% it's previous size.
	 */
	public VisibleFilter(int defaultFilterCapacity) 
	{
		super(defaultFilterCapacity);
	}
	
	@Override
	public boolean isValid(Entity e) 
	{
		return e.isVisible();
	}

}

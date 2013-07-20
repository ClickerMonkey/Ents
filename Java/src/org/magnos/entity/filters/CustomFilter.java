package org.magnos.entity.filters;

import org.magnos.entity.Entity;
import org.magnos.entity.EntityFilter;

/**
 * A filter that returns all Entities that are considered "Custom". An entity 
 * is considered custom if it was defined without a template or has had a 
 * component, controller, or view added/set directly to it.
 * 
 * @author Philip Diffenderfer
 * @see EntityFilter
 *
 */
public class CustomFilter extends EntityFilter 
{

	/**
	 * Instantiates a new CustomFilter.
	 * 
	 * @param defaultFilterCapacity
	 * 		The default capacity of this filter. The filter works by filling an
	 * 		array of entities that meet the filtering criteria. If the array is
	 * 		not large enough it resizes to 150% it's previous size.
	 */
	public CustomFilter(int defaultFilterCapacity) 
	{
		super(defaultFilterCapacity);
	}
	
	@Override
	public boolean isValid(Entity e) 
	{
		return e.isCustom();
	}

}

package org.magnos.entity.filters;

import org.magnos.entity.Entity;
import org.magnos.entity.EntityChain;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityList;

/**
 * A filter that returns the opposite of what another filter would return.
 * 
 * @author Philip Diffenderfer
 * @see EntityFilter
 *
 */
public class NegativeFilter extends EntityFilter 
{

	protected EntityFilter filter;
	
	/**
	 * Instantiates a new NegativeFilter.
	 * 
	 * @param defaultFilterCapacity
	 * 		The default capacity of this filter. The filter works by filling an
	 * 		array of entities that meet the filtering criteria. If the array is
	 * 		not large enough it resizes to 150% it's previous size.
	 */
	public NegativeFilter(int defaultFilterCapacity) 
	{
		super(defaultFilterCapacity);
	}
	
	/**
	 * Resets the NegativeFilter specifying the root entity and the
	 * filter to return the negation (opposite) of.
	 * 
	 * @param root
	 * 		The root entity to filter. This entity is typically an
	 * 		{@link EntityChain} or {@link EntityList} which both can contain 
	 * 		any number of entities.
	 * @param filter
	 * 		The filter to return the negation (opposite) of.
	 * @return
	 * 		The {@link Iterable} filter by components.
	 */
	public EntityFilter reset(Entity root, EntityFilter filter)
	{
		this.filter = filter;
		
		return super.reset(root);
	}
	
	@Override
	public boolean isValid(Entity e) 
	{
		return !filter.isValid( e );
	}

}

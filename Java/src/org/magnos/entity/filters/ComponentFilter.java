package org.magnos.entity.filters;

import org.magnos.entity.BitSet;
import org.magnos.entity.Component;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityChain;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityList;

/**
 * A filter that only returns entities that have given components.
 * 
 * @author Philip Diffenderfer
 * @see EntityFilter
 *
 */
public class ComponentFilter extends EntityFilter 
{
	
	protected BitSet components;

	/**
	 * Instantiates a new ComponentFilter.
	 * 
	 * @param defaultFilterCapacity
	 * 		The default capacity of this filter. The filter works by filling an
	 * 		array of entities that meet the filtering criteria. If the array is
	 * 		not large enough it resizes to 150% it's previous size.
	 */
	public ComponentFilter(int defaultFilterCapacity) 
	{
		super(defaultFilterCapacity);
	}
	
	/**
	 * Resets the ComponentFilter specifying the root entity and the
	 * set of components to filter by.
	 * 
	 * @param root
	 * 		The root entity to filter. This entity is typically an
	 * 		{@link EntityChain} or {@link EntityList} which both can contain 
	 * 		any number of entities.
	 * @param components
	 * 		The set of components each entity returned by the filter will have.
	 * @return
	 * 		The {@link Iterable} filter by components.
	 */
	public EntityFilter reset(Entity root, Component<?> ... components)
	{
		this.components = new BitSet( components );
		
		return reset( root );
	}
	
	@Override
	public boolean isValid(Entity e) 
	{
		return e.has( components );
	}

}

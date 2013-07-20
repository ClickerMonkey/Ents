package org.magnos.entity.filters;

import org.magnos.entity.Component;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityChain;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityList;
import org.magnos.entity.EntityUtility;

/**
 * A filter that only returns entities that have a given component and their
 * component has a specific value. 
 * 
 * @author Philip Diffenderfer
 * @see EntityFilter
 *
 * @param <T>
 * 		The component value type.
 */
public class ValueFilter<T> extends EntityFilter 
{
	
	protected T value;
	protected Component<T> component;

	/**
	 * Instantiates a new ValueFilter.
	 * 
	 * @param defaultFilterCapacity
	 * 		The default capacity of this filter. The filter works by filling an
	 * 		array of entities that meet the filtering criteria. If the array is
	 * 		not large enough it resizes to 150% it's previous size.
	 */
	public ValueFilter(int defaultFilterCapacity) 
	{
		super(defaultFilterCapacity);
	}
	
	/**
	 * Resets the ComponentFilter specifying the root entity and the
	 * component and value to filter by.
	 * 
	 * @param root
	 * 		The root entity to filter. This entity is typically an
	 * 		{@link EntityChain} or {@link EntityList} which both can contain 
	 * 		any number of entities.
	 * @param component
	 * 		The component the entity must have.
	 * @param value
	 * 		The value of the given component the entity must have.
	 * @return
	 * 		The {@link Iterable} filter by components.
	 */
	public EntityFilter reset(Entity root, Component<T> component, T value)
	{
		this.component = component;
		this.value = value;
		
		return super.reset( root );
	}
	
	@Override
	public boolean isValid(Entity e) 
	{
		T entityValue = e.gets( component );
		
		return EntityUtility.equals( entityValue, value );
	}

}

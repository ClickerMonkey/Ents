package org.magnos.entity.filters;

import org.magnos.entity.Entity;
import org.magnos.entity.EntityChain;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityList;
import org.magnos.entity.Template;

/**
 * A filter that only returns entities that have a very specific template (and 
 * not a custom one). The template comparison is done by reference and not by
 * the actual components, controllers, and view on the template and Entity.
 * 
 * @author Philip Diffenderfer
 * @see EntityFilter
 *
 */
public class TemplateExactFilter extends EntityFilter 
{
	
	protected Template template;
	
	/**
	 * Instantiates a new TemplateExactFilter.
	 * 
	 * @param defaultFilterCapacity
	 * 		The default capacity of this filter. The filter works by filling an
	 * 		array of entities that meet the filtering criteria. If the array is
	 * 		not large enough it resizes to 150% it's previous size.
	 */
	public TemplateExactFilter(int defaultFilterCapacity) 
	{
		super(defaultFilterCapacity);
	}
	
	/**
	 * Resets the ComponentFilter specifying the root entity and the
	 * template the Entities must have.
	 * 
	 * @param root
	 * 		The root entity to filter. This entity is typically an
	 * 		{@link EntityChain} or {@link EntityList} which both can contain 
	 * 		any number of entities.
	 * @param template
	 * 		The template the Entities filtered must have.
	 * @return
	 * 		The {@link Iterable} filter by components.
	 */
	public EntityFilter reset(Entity root, Template template)
	{
		this.template = template;
		
		return reset( root );
	}
	
	@Override
	public boolean isValid(Entity e) 
	{
		return e.getTemplate() == template;
	}

}

package org.magnos.entity;

public class EntityChain extends Entity 
{

	protected Entity first;
	protected Entity last;
	protected boolean inheritVisible;
	protected boolean inheritEnabled;
	
	public EntityChain() 
	{
		this( EntityCore.newTemplate(), null, null, false, false );
	}
	
	public EntityChain(Template template) 
	{
		this( template, null, null, false, false );
	}
	
	public EntityChain(Entity first, Entity last, boolean inheritVisible, boolean inheritEnabled) 
	{
		this( EntityCore.newTemplate(), first, last, inheritVisible, inheritEnabled );
	}
	
	public EntityChain(Template template, Entity first, Entity last, boolean inheritVisible, boolean inheritEnabled) 
	{
		super(template);
		
		this.first = first;
		this.last = last;
		this.inheritEnabled = inheritEnabled;
		this.inheritVisible = inheritVisible;
	}
	
	@Override
	public void draw(Object drawState)
	{
		if (visible || !inheritVisible)
		{
			if (first != null)
			{
				first.draw(drawState);
			}
			
			super.draw(drawState);
			
			if (last != null)
			{
				last.draw(drawState);
			}
		}
	}
	
	@Override
	public void update(Object updateState)
	{
		if (enabled || !inheritEnabled)
		{
			if (first != null)
			{
				first.update(updateState);
			}
			
			super.update(updateState);
			
			if (last != null)
			{
				last.update(updateState);
			}
		}
	}
	
	@Override
	protected void fill(EntityFilter filter)
	{
		filter.prepare(3);
		
		if (first != null)
		{
			first.fill(filter);
		}
		
		filter.push(this);
		
		if (last != null)
		{
			last.fill(filter);
		}
	}
	
}

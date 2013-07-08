package org.magnos.entity;

public class EntityTypeDynamic extends EntityType
{

	protected EntityTypeDynamic(int id, EntityType parent, int[] map, int[] components)
	{
		super( id, parent, map, components );
	}
	
	public EntityType addDynamically(int id)
	{
		add(id);
		
		return this;
	}
	
}

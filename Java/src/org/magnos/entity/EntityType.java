package org.magnos.entity;

import java.util.Arrays;

public class EntityType 
{

	public final int id;
	public final EntityType parent;
	protected int[] componentMap = {};
	protected int[] components = {};
	protected int[] behaviors = {};
	protected int view = -1;
	
	public EntityType(int id)
	{
		this( id, null, new int[] {}, new int[] {} );
	}
	
	protected EntityType(int id, int[] map, int[] components)
	{
		this( id, null, map, components );
	}
	
	protected EntityType(int id, EntityType parent, int[] map, int[] components)
	{
		this.id = id;
		this.parent = parent;
		this.componentMap = map;
		this.components = components;
	}
	
	public final EntityType extend(int entityTypeId)
	{
		return new EntityType(entityTypeId, this, Arrays.copyOf(componentMap, componentMap.length), Arrays.copyOf(components, components.length));
	}

	public final boolean hasComponent(int componentId)
	{
		return componentId < componentMap.length && componentMap[componentId] != 0;
	}
	
	public final boolean hasComponents(int ... componentIds)
	{
		for (int i = 0; i < componentIds.length; i++)
		{
			if (!hasComponent(componentIds[i]))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public final int getComponentIndex(int componentId)
	{
		return componentMap[componentId] + 1;
	}
	
	public final int getComponentCount()
	{
		return components.length;
	}
	
	public final void setAlias(int componentId, int aliasId)
	{
		if (hasComponent(componentId))
		{
			map( aliasId, componentMap[componentId] );
		}
	}
	
	public Object[] createComponents()
	{
		final int N = components.length + 1;
		final Object[] values = new Object[ N ];
		
		for (int i = 1; i < N; i++)
		{
			values[i] = EntityCore.getComponent(components[i - 1]).factory.create();
		}
		
		return values;
	}
	
	public void add(int componentId)
	{
		final int N = components.length;
		
		components = Arrays.copyOf(components, N + 1);
		components[ N ] = componentId;
		
		map( componentId, N );
	}
	
	public EntityType addDynamically(int componentId)
	{
		EntityTypeDynamic dynamic = new EntityTypeDynamic( id, this, Arrays.copyOf( componentMap, componentMap.length ), components );
		
		dynamic.add(componentId);
		
		return dynamic;
	}
	
	private void map(int componentId, int index)
	{
		if (componentMap.length <= componentId)
		{
			componentMap = Arrays.copyOf(componentMap, componentId + 1);
		}
		
		componentMap[componentId] = index;
	}
	
}

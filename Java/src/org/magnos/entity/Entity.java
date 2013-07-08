package org.magnos.entity;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class Entity 
{

	protected EntityType type;
	protected Object[] components;
	
	public Entity(int entityTypeId)
	{
		this( EntityCore.getEntityType( entityTypeId ) );
	}
	
	public Entity(EntityType entityType)
	{
		this( entityType, entityType.createComponents() );
	}
	
	private Entity(EntityType entityType, Object[] components)
	{
		this.type = entityType;
		this.components = components;
	}
	
	public boolean has(int id) 
	{
		return type.hasComponent(id);
	}
	
	public <T> T get(int componentId) 
	{
		return (T)components[type.getComponentIndex(componentId)];
	}
	
	public <T> T get(int componentId, Class<T> castType) 
	{
		return (T)components[type.getComponentIndex(componentId)];
	}
	
	public <T> T getSafe(int componentId, Class<T> castType)
	{
		ComponentType componentType = EntityCore.getComponent(componentId);
		
		boolean correctType = castType.isAssignableFrom( componentType.type ); 
		
		return ( correctType ? get( componentId, castType ) : null );
	}
	
	public <T> T getDynamic(int componentId, T out)
	{
		ComponentType componentType = EntityCore.getComponent(componentId);
		
		if (componentType instanceof DynamicComponentType)
		{
			DynamicComponentType<T> dynamicComponentType = (DynamicComponentType<T>)componentType;
			
			return dynamicComponentType.component.compute(this, out);
		}
		
		return null;
	}
	
	public void set(int componentId, Object value)
	{
		components[type.getComponentIndex(componentId)] = value;
	}
	
	public void add(int componentId, Object defaultValue)
	{
		if (!type.hasComponent(componentId)) 
		{
			type = type.addDynamically(componentId);
			components = Arrays.copyOf(components, type.getComponentCount() + 1);
			set(componentId, defaultValue);	
		}
	}
	
	public Entity clone(boolean deep)
	{
		Object[] copy = new Object[ components.length ];
		
		for (int i = 1; i < components.length; i++)
		{
			if (deep)
			{
				copy[i] = EntityCore.getComponent(type.components[i - 1]).factory.clone( components[i] );	
			}
			else
			{
				copy[i] = components[i];
			}
		}
		
		return new Entity(type, copy);
	}
	
	public EntityType getType()
	{
		return type;
	}
	
	public Object[] getComponents()
	{
		return components;
	}
	
}

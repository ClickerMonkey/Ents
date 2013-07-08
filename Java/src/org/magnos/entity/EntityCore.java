package org.magnos.entity;

import java.util.Arrays;

import org.magnos.entity.factory.ComponentFactoryNull;

public class EntityCore 
{
	
	private static ComponentType[] componentTypes = {};
	private static EntityType[] entityTypes = {};
	
	
	public static EntityType getEntityType(int entityTypeId)
	{
		return entityTypes[entityTypeId];
	}
	
	public static int newEntity( int ... componentIds )
	{
		return newEntityExtension( null, componentIds );
	}
	
	public static int newEntityExtension( int parentEntityTypeId, int ... componentIds)
	{
		return newEntityExtension( getEntityType( parentEntityTypeId ), componentIds );
	}
	
	public static int newEntityExtension( EntityType parent, int ... componentIds)
	{
		int id = entityTypes.length;
		
		EntityType type = (parent != null ? parent.extend(id) : new EntityType(id) );
		
		for (int i = 0; i < componentIds.length; i++)
		{
			type.add( componentIds[i] );	
		}
		
		entityTypes = add(type, entityTypes);
		
		return id;
	}
	
	
	public static ComponentType getComponent(int id)
	{
		return componentTypes[id];
	}
	
	public static <T> int newComponent( String name, Class<T> type )
	{
		return newComponent( name, type, ComponentFactoryNull.get() );
	}
	
	public static <T> int newComponent( String name, Class<T> type, ComponentFactory factory )
	{
		int id = componentTypes.length;
		
		componentTypes = add( new ComponentType(id, name, type, factory), componentTypes );
		
		return id;
	}
	
	public static <T> int newDynamicComponent( String name, Class<T> type, DynamicComponent<T> component )
	{
		int id = componentTypes.length;
		
		componentTypes = add( new DynamicComponentType<T>(id, name, type, component), componentTypes );
		
		return id;
	}
	
	private static <T> T[] add(T item, T[] array)
	{
		int index = array.length;
		array = Arrays.copyOf(array, index + 1);
		array[index] = item;
		return array;
	}
	
}

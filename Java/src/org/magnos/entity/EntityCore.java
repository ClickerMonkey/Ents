package org.magnos.entity;

import java.util.Arrays;

import org.magnos.entity.factory.ComponentFactoryNull;

public class EntityCore
{

	private static ComponentType[] componentTypes = {};

	private static EntityType[] entityTypes = {};

	private static Controller[] controllers = {};

	private static View[] views = {};
	
	private static EntityList[][] listeners = {};


	public static EntityType[] getEntityTypes()
	{
		return entityTypes;
	}
	
	public static EntityType getEntityType( int entityTypeId )
	{
		return entityTypes[entityTypeId];
	}

	public static EntityType getEntityTypeSafe( int entityTypeId )
	{
		return hasEntityType( entityTypeId ) ? getEntityType( entityTypeId ) : null;
	}

	public static boolean hasEntityType( int entityTypeId )
	{
		return ( entityTypeId >= 0 && entityTypeId < entityTypes.length );
	}

	public static int newEntity( IdMap components, IdMap controllers, int view )
	{
		int id = entityTypes.length;
		
		entityTypes = add( new EntityType( id, components, controllers, view ), entityTypes );
		
		return id;
	}

	public static int newEntityExtension( int parentEntityTypeId, IdMap components, IdMap controllers, int view )
	{
		return newEntityExtension( getEntityType( parentEntityTypeId ), components, controllers, view );
	}

	public static int newEntityExtension( EntityType parent, IdMap components, IdMap controllers, int view )
	{
		int id = entityTypes.length;

		EntityType type = parent.extend( id );
		
		for (int i = 0; i < components.size(); i++)
		{
			type.add( components.ids[i] );
		}
		
		for (int i = 0; i < controllers.size(); i++)
		{
			type.addController( controllers.ids[i] );
		}
		
		if ( view != -1 )
		{
			type.setView( view );	
		}

		entityTypes = add( type, entityTypes );

		return id;
	}

	
	public static ComponentType[] getComponents()
	{
		return componentTypes;
	}

	public static ComponentType getComponent( int componentId )
	{
		return componentTypes[componentId];
	}

	public static ComponentType getComponentSafe( int componentId )
	{
		return hasComponent( componentId ) ? getComponent( componentId ) : null;
	}

	public static boolean hasComponent( int componentId )
	{
		return ( componentId >= 0 && componentId < componentTypes.length );
	}

	public static <T> int newComponent( String name, Class<T> type )
	{
		return newComponent( name, type, ComponentFactoryNull.get() );
	}

	public static <T> int newComponent( String name, Class<T> type, ComponentFactory factory )
	{
		int id = componentTypes.length;

		componentTypes = add( new ComponentType( id, name, type, factory ), componentTypes );
		listeners = add( new EntityList[0], listeners );
		
		return id;
	}

	public static <T> int newDynamicComponent( String name, Class<T> type, DynamicComponent<T> component )
	{
		int id = componentTypes.length;

		componentTypes = add( new DynamicComponentType<T>( id, name, type, component ), componentTypes );
		listeners = add( new EntityList[0], listeners );
		
		return id;
	}
	
	public static Controller[] getControllers()
	{
		return controllers;
	}

	public static Controller getController( int controllerId )
	{
		return controllers[controllerId];
	}

	public static Controller getControllerSafe( int controllerId )
	{
		return hasController( controllerId ) ? getController( controllerId ) : null;
	}

	public static boolean hasController( int controllerId )
	{
		return ( controllerId >= 0 && controllerId < controllers.length );
	}

	public static int addController( Controller controller )
	{
		int id = controllers.length;

		controllers = add( controller, controllers );

		return id;
	}

	public static View[] getViews()
	{
		return views;
	}
	
	public static View getView( int viewId )
	{
		return views[viewId];
	}

	public static View getViewSafe( int viewId )
	{
		return hasView( viewId ) ? getView( viewId ) : null;
	}

	public static boolean hasView( int viewId )
	{
		return ( viewId >= 0 && viewId < views.length && views[viewId] != null );
	}

	public static int addView( View view )
	{
		int id = views.length;

		views = add( view, views );

		return id;
	}

	public static int newView()
	{
		return addView( null );
	}

	public static void setView( int viewId, View view )
	{
		views[viewId] = view;
	}

	private static <T> T[] add( T item, T[] array )
	{
		int index = array.length;
		array = Arrays.copyOf( array, index + 1 );
		array[index] = item;
		return array;
	}
	
	public static void addListener(EntityList list, int ... components)
	{
		for (int i = 0; i < components.length; i++)
		{
			final int k = components[i]; 
			
			listeners[k] = add( list, listeners[k] );
		}
	}

}

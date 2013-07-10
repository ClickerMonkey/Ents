package org.magnos.entity;

import java.util.ArrayList;

import org.magnos.entity.factory.ComponentFactoryNull;

public class EntityCore
{

	private static ArrayList<ComponentType> componentTypes = new ArrayList<ComponentType>();

	private static ArrayList<EntityType> entityTypes = new ArrayList<EntityType>();

	private static ArrayList<Controller> controllers = new ArrayList<Controller>();

	private static ArrayList<View> views = new ArrayList<View>();
	
	private static ArrayList<ArrayList<EntityList>> listeners = new ArrayList<ArrayList<EntityList>>();

	private static EntityList listening = new EntityList();

	public static ArrayList<EntityType> getEntityTypes()
	{
		return entityTypes;
	}
	
	public static EntityType getEntityType( int entityTypeId )
	{
		return entityTypes.get(entityTypeId);
	}

	public static EntityType getEntityTypeSafe( int entityTypeId )
	{
		return hasEntityType( entityTypeId ) ? getEntityType( entityTypeId ) : null;
	}

	public static boolean hasEntityType( int entityTypeId )
	{
		return ( entityTypeId >= 0 && entityTypeId < entityTypes.size() );
	}

	public static int newEntityType( IdMap components, IdMap controllers, int view )
	{
		int id = entityTypes.size();
		
		entityTypes.add( new EntityType( id, components, controllers, view ) );
		
		return id;
	}

	public static int newEntityTypeExtension( int parentEntityTypeId, IdMap components, IdMap controllers, int view )
	{
		return newEntityTypeExtension( getEntityType( parentEntityTypeId ), components, controllers, view );
	}

	public static int newEntityTypeExtension( EntityType parent, IdMap components, IdMap controllers, int view )
	{
		int id = entityTypes.size();

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

		entityTypes.add( type );

		return id;
	}

	
	public static ArrayList<ComponentType> getComponents()
	{
		return componentTypes;
	}

	public static ComponentType getComponent( int componentId )
	{
		return componentTypes.get(componentId);
	}

	public static ComponentType getComponentSafe( int componentId )
	{
		return hasComponent( componentId ) ? getComponent( componentId ) : null;
	}

	public static boolean hasComponent( int componentId )
	{
		return ( componentId >= 0 && componentId < componentTypes.size() );
	}

	public static <T> int newComponent( String name, Class<T> type )
	{
		return newComponent( name, type, ComponentFactoryNull.get() );
	}

	public static <T> int newComponent( String name, Class<T> type, ComponentFactory factory )
	{
		int id = componentTypes.size();

		componentTypes.add( new ComponentType( id, name, type, factory ) );
		listeners.add( new ArrayList<EntityList>() );
		
		return id;
	}

	public static <T> int newDynamicComponent( String name, Class<T> type, DynamicComponent<T> component )
	{
		int id = componentTypes.size();

		componentTypes.add( new DynamicComponentType<T>( id, name, type, component ) );
		listeners.add( new ArrayList<EntityList>() );
		
		return id;
	}
	
	public static ArrayList<Controller> getControllers()
	{
		return controllers;
	}

	public static Controller getController( int controllerId )
	{
		return controllers.get(controllerId);
	}

	public static Controller getControllerSafe( int controllerId )
	{
		return hasController( controllerId ) ? getController( controllerId ) : null;
	}

	public static boolean hasController( int controllerId )
	{
		return ( controllerId >= 0 && controllerId < controllers.size() );
	}

	public static int addController( Controller controller )
	{
		int id = controllers.size();

		controllers.add( controller );

		return id;
	}

	public static ArrayList<View> getViews()
	{
		return views;
	}
	
	public static View getView( int viewId )
	{
		return views.get(viewId);
	}

	public static View getViewSafe( int viewId )
	{
		return hasView( viewId ) ? getView( viewId ) : null;
	}

	public static boolean hasView( int viewId )
	{
		return ( viewId >= 0 && viewId < views.size() && views.get(viewId) != null );
	}

	public static int addView( View view )
	{
		int id = views.size();

		views.add( view );

		return id;
	}

	public static int newView()
	{
		return addView( null );
	}

	public static void setView( int viewId, View view )
	{
		views.set( viewId, view );
	}
	
	public static void clean()
	{
		listening.clean();
	}
	
	public static void listenTo(Entity e)
	{
		listening.add( e );
	}

	public static void addListener(EntityList list, int ... components)
	{
		for (int i = 0; i < components.length; i++)
		{
			final int k = components[i]; 
			
			listeners.get(k).add( list );
		}
	}

}

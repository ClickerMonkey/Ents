package org.magnos.entity;

import java.util.ArrayList;

import org.magnos.entity.factory.ComponentFactoryNull;

@SuppressWarnings("unchecked")
public class EntityCore
{

	private ArrayList<ComponentBase> components = new ArrayList<ComponentBase>();
	private ArrayList<ControllerBase> controllers = new ArrayList<ControllerBase>();
	private ArrayList<ViewBase> views = new ArrayList<ViewBase>();
	private ArrayList<MethodBase> methods = new ArrayList<MethodBase>();
	private ArrayList<EntityType> entityTypes = new ArrayList<EntityType>();

	public ArrayList<EntityType> getEntityTypes()
	{
		return entityTypes;
	}
	
	public EntityType getEntityType( int entityTypeId )
	{
		return entityTypes.get(entityTypeId);
	}

	public EntityType getEntityTypeSafe( int entityTypeId )
	{
		return hasEntityType( entityTypeId ) ? getEntityType( entityTypeId ) : null;
	}

	public boolean hasEntityType( int entityTypeId )
	{
		return ( entityTypeId >= 0 && entityTypeId < entityTypes.size() );
	}

	public EntityType newEntityType( IdMap components, IdMap controllers, IdMap methods, int view )
	{
		EntityType type = new EntityType( this, entityTypes.size(), components, controllers, methods, view );
		entityTypes.add( type );
		return type;
	}

	public EntityType newEntityTypeExtension( EntityType parent, IdMap components, IdMap controllers, IdMap methods, int view )
	{
		EntityType type = parent.extend( entityTypes.size() );
		
		for (int i = 0; i < components.size(); i++)
		{
			type.add( components.ids[i] );
		}
		
		for (int i = 0; i < controllers.size(); i++)
		{
			type.addController( controllers.ids[i] );
		}
		
		for (int i = 0; i < methods.size(); i++)
		{
			type.addMethod( getMethodCast( methods.ids[i] ) );
		}
		
		if ( view != -1 )
		{
			type.setView( view );	
		}

		entityTypes.add( type );

		return type;
	}

	
	public ArrayList<ComponentBase> getComponents()
	{
		return components;
	}

	public ComponentBase getComponent( int componentId )
	{
		return components.get(componentId);
	}

	public ComponentBase getComponentSafe( int componentId )
	{
		return hasComponent( componentId ) ? getComponent( componentId ) : null;
	}
	
	public <T> Component<T> getComponentCast( int componentId )
	{
		return (Component<T>) components.get(componentId);
	}

	public boolean hasComponent( int componentId )
	{
		return ( componentId >= 0 && componentId < components.size() );
	}
	
	public <T> Component<T> newComponent( String name )
	{
		return newComponent( name, new ComponentFactoryNull<T>() );
	}

	public <T> Component<T> newComponent( String name, ComponentFactory<T> factory )
	{
		Component<T> component = new Component<T>( this, components.size(), name, factory ); 
		components.add( component );
		return component;
	}

	public <T> ComponentGet<T> newComponentGet( String name, BitSet required, ComponentGet.Get<T> get )
	{
		ComponentGet<T> component = new ComponentGet<T>( this, components.size(), name, required, get );
		components.add( component );
		return component;
	}

	public <T> ComponentSet<T> newComponentSet( String name, BitSet required, ComponentSet.Set<T> set )
	{
		ComponentSet<T> component = new ComponentSet<T>( this, components.size(), name, required, set );
		components.add( component );
		return component;
	}
	
	
	public ArrayList<ControllerBase> getControllers()
	{
		return controllers;
	}

	public ControllerBase getController( int controllerId )
	{
		return controllers.get(controllerId);
	}

	public ControllerBase getControllerSafe( int controllerId )
	{
		return hasController( controllerId ) ? getController( controllerId ) : null;
	}
	
	public Controller getControllerImplementation( int controllerId )
	{
		return hasController( controllerId ) ? getController( controllerId ).controller : null;
	}

	public boolean hasController( int controllerId )
	{
		return ( controllerId >= 0 && controllerId < controllers.size() && controllers.get( controllerId ).controller != null );
	}

	public int newController( String name, BitSet required, Controller controller )
	{
		int id = controllers.size();
		controllers.add( new ControllerBase( this, id, name, required, controller ) );
		return id;
	}
	
	public int newController()
	{
		return newController( null, null, null );
	}
	
	public void setController( int controllerId, String name, BitSet required, Controller controller )
	{
		controllers.set( controllerId, new ControllerBase( this, controllerId, name, required, controller ) );
	}

	
	public ArrayList<ViewBase> getViews()
	{
		return views;
	}
	
	public ViewBase getView( int viewId )
	{
		return views.get(viewId);
	}

	public ViewBase getViewSafe( int viewId )
	{
		return hasView( viewId ) ? getView( viewId ) : null;
	}
	
	public boolean hasView( int viewId )
	{
		return ( viewId >= 0 && viewId < views.size() && views.get(viewId).view != null );
	}

	public int newView( String name, BitSet required, View view )
	{
		int id = views.size();
		views.add( new ViewBase( this, id, name, required, view ) );
		return id;
	}

	public int newView()
	{
		return newView( null, null, null );
	}

	public void setView( int viewId, String name, BitSet required, View view )
	{
		views.set( viewId, new ViewBase( this, viewId, name, required, view ) );
	}
	


	
	public ArrayList<MethodBase> getMethods()
	{
		return methods;
	}
	
	public MethodBase getMethod( int methodId )
	{
		return methods.get(methodId);
	}

	public MethodBase getMethodSafe( int methodId )
	{
		return hasMethod( methodId ) ? getMethod( methodId ) : null;
	}
	
	public <R> Method<R> getMethodCast( int methodId )
	{
		return (Method<R>) methods.get( methodId );
	}

	public boolean hasMethod( int methodId )
	{
		return ( methodId >= 0 && methodId < methods.size() && methods.get(methodId) != null );
	}

	public <R> Method<R> newMethod( String name, BitSet required, Method.Execute<R> execute )
	{
		Method<R> method = new Method<R>( this, methods.size(), name, required, execute );
		methods.add( method );
		return method;
	}

}

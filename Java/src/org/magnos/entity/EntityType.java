package org.magnos.entity;

import java.util.ArrayList;


public final class EntityType extends Identified
{

	public static final int CUSTOM = -1;
	
	public final EntityCore core;
	
	public final EntityType parent;

	public IdMap components;

	public IdMap controllers;
	
	public IdMap methods;
	
	public ArrayList<Method.Execute<?>> methodImplementations;
	
	public int view;
	
	protected EntityType( EntityCore core, int id, IdMap components, IdMap controllers, IdMap methods, int view )
	{
		this( core, id, null, components, controllers, methods, createMethodImplementations( core, methods ), view );
	}

	protected EntityType( EntityCore core )
	{
		this( core, CUSTOM, null, new IdMap(), new IdMap(), new IdMap(), new ArrayList<Method.Execute<?>>(), View.NONE );
	}
	
	private EntityType( EntityCore core, int id, EntityType parent, IdMap components, IdMap controllers, IdMap methods, ArrayList<Method.Execute<?>> methodImplementations, int view )
	{
		super( id );
		
		this.core = core;
		this.parent = parent;
		this.components = components;
		this.controllers = controllers;
		this.methods = methods;
		this.methodImplementations = methodImplementations;
		this.view = view;
	}

	protected EntityType extend( int entityTypeId )
	{
		return new EntityType( core, entityTypeId, this, components.getCopy(), controllers.getCopy(), methods.getCopy(), new ArrayList<Method.Execute<?>>( methodImplementations ), view );
	}

	public boolean add( int componentId )
	{
		boolean adding = !components.has( componentId ); 
		
		if ( adding )
		{
			components.add( componentId );
		}
		
		return adding;
	}
	
	public boolean hasComponent( int componentId )
	{
		return components.has( componentId );
	}

	public boolean hasComponents( int... componentIds )
	{
		return components.hasAll( componentIds );
	}

	public int getComponentIndex( int componentId )
	{
		return components.getIndex( componentId ) + 1;
	}
	
	public int getComponentCount()
	{
		return components.size();
	}

	public <T> EntityType setComponentAlias( Component<T> component, Component<T> alias )
	{
		components.alias( component.id, alias.id );
		
		return this;
	}
	
	public Object[] createComponents()
	{
		final int N = components.size() + 1;
		final Object[] values = new Object[N];

		for (int i = 1; i < N; i++)
		{
			values[i] = core.getComponentCast( components.ids[i - 1] ).factory.create();
		}

		return values;
	}

	public Object cloneComponentAtIndex( int index, Object value )
	{
		return core.getComponentCast( components.ids[index - 1] ).factory.clone( value );
	}



	public boolean addController( int controllerId )
	{
		boolean adding = !controllers.has( controllerId );
		
		if (adding)
		{
			controllers.add( controllerId );	
		}
		
		return adding;
	}
	
	public boolean hasController( int controllerId )
	{
		return controllers.has( controllerId );
	}

	public boolean hasControllers( int... controllerIds )
	{
		return controllers.hasAll( controllerIds );
	}

	public int getControllerIndex( int controllerId )
	{
		return controllers.getIndex( controllerId );
	}

	public int getControllerCount()
	{
		return controllers.size();
	}

	public EntityType setControllerAlias( int controllerId, int aliasId )
	{
		controllers.alias( controllerId, aliasId );
		
		return this;
	}
	
	

	public void setView( int viewId )
	{
		view = viewId;
	}
	
	public boolean hasView( int viewId ) 
	{
		return (view == viewId);
	}
	
	public boolean hasView()
	{
		return (view != View.NONE);
	}
	
	
	
	public <R> boolean addMethod( Method<R> method )
	{
		boolean adding = !hasMethod( method.id );
		
		if ( adding )
		{
			methods.add( method.id );
			methodImplementations.add( method.execute );
		}
		
		return adding;
	}
	
	public <R> void setMethod( Method<R> method, Method.Execute<R> execute )
	{
		methodImplementations.set( method.id, execute );
	}
	
	public boolean hasMethod( int methodId )
	{
		return methods.has( methodId );
	}

	public boolean hasMethods( int... methodIds )
	{
		return methods.hasAll( methodIds );
	}

	public int getMethodIndex( int methodId )
	{
		return methods.getIndex( methodId );
	}

	public int getMethodCount()
	{
		return methods.size();
	}

	public <R> EntityType setMethodAlias( Method<R> method, Method<R> alias )
	{
		methods.alias( method.id, alias.id );
		
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <R> Method.Execute<R> getMethodImplementation( int methodId )
	{
		return (Method.Execute<R>) methodImplementations.get( methods.getIndex( methodId ) );
	}
	
	@SuppressWarnings("unchecked")
	public <R> Method.Execute<R> getMethodImplementationSafe( int methodId )
	{
		int index = methods.getIndex( methodId );
		
		return ( index == -1 ? null : (Method.Execute<R>) methodImplementations.get( index ) );
	}

	public <T> EntityType addCustomComponent( int componentId )
	{
		if ( hasComponent( componentId ) ) {
			return this;
		}
		
		EntityType target = getCustomTarget();
		target.add( componentId );
		return target;
	}

	public EntityType addCustomController( int controllerId )
	{
		if ( hasController( controllerId ) ) {
			return this;
		}
		
		EntityType target = getCustomTarget();
		target.addController( controllerId );
		return target;
	}

	public EntityType setCustomView( int viewId )
	{
		if ( hasView( viewId ) ) {
			return this;
		}
		
		EntityType target = getCustomTarget();
		target.setView( viewId );
		return target;
	}
	
	public <T> EntityType setCustomComponentAlias( Component<T> component, Component<T> alias )
	{
		if ( components.getIndexSafe( component.id ) == components.getIndexSafe( alias.id ) ) {
			return this;
		}
		
		EntityType target = getCustomTarget();
		target.setComponentAlias( component, alias );
		return target;
	}
	
	public EntityType setCustomControllerAlias( int controllerId, int aliasId )
	{
		if ( controllers.getIndexSafe( controllerId ) == controllers.getIndexSafe( aliasId ) ) {
			return this;
		}
		
		EntityType target = getCustomTarget();
		target.setControllerAlias( controllerId, aliasId );
		return target;
	}
	
	public <R> EntityType addCustomMethod(Method<R> method)
	{
		if ( hasMethod( method.id ) ) {
			return this;
		}
		
		EntityType target = getCustomTarget();
		target.addMethod( method );
		return target;
	}

	public <R> EntityType setCustomMethod(Method<R> method, Method.Execute<R> execute)
	{
		if ( hasMethod( method.id ) && methodImplementations.get( methods.getIndex( method.id ) ) == execute ) {
			return this;
		}
		
		EntityType target = getCustomTarget();
		target.setMethod( method, execute );
		return target;
	}
	
	public <R> EntityType setCustomMethodAlias( Method<R> method, Method<R> alias )
	{
		if ( methods.getIndexSafe( method.id ) == methods.getIndexSafe( alias.id ) ) {
			return this;
		}
		
		EntityType target = getCustomTarget();
		target.setMethodAlias( method, alias );
		return target;
	}
	
	private EntityType getCustomTarget()
	{
		return isCustom() ? this : extend( CUSTOM );
	}
	
	public boolean isCustom()
	{
		return (id == CUSTOM);
	}
	
	private static ArrayList<Method.Execute<?>> createMethodImplementations(EntityCore core, IdMap methods)
	{
		ArrayList<Method.Execute<?>> methodImplementations = new ArrayList<Method.Execute<?>>( methods.size() );
		
		for (int i = 0; i < methods.size(); i++)
		{
			methodImplementations.add( core.getMethodCast( methods.ids[ i ] ).execute );
		}
		
		return methodImplementations;
	}

}

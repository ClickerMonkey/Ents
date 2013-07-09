package org.magnos.entity;


public class EntityType
{

	public final int id;

	public final EntityType parent;

	public IdMap components;

	public IdMap controllers;

	public int view;

	
	public EntityType( int id )
	{
		this( id, null, new IdMap(), new IdMap(), -1 );
	}

	protected EntityType( int id, IdMap components, IdMap controllers, int view )
	{
		this( id, null, components, controllers, view );
	}

	protected EntityType( int id, EntityType parent, IdMap components, IdMap controllers, int view )
	{
		this.id = id;
		this.parent = parent;
		this.components = components;
		this.controllers = controllers;
		this.view = view;
	}

	public final EntityType extend( int entityTypeId )
	{
		return new EntityType( entityTypeId, this, components.getCopy(), controllers.getCopy(), view );
	}

	public final boolean hasComponent( int componentId )
	{
		return components.has( componentId );
	}

	public final boolean hasComponents( int... componentIds )
	{
		return components.hasAll( componentIds );
	}

	public final int getComponentIndex( int componentId )
	{
		return components.getIndex( componentId ) + 1;
	}

	public final int getComponentCount()
	{
		return components.size();
	}

	public final void setComponentAlias( int componentId, int aliasId )
	{
		components.alias( componentId, aliasId );
	}

	public Object[] createComponents()
	{
		final int N = components.size() + 1;
		final Object[] values = new Object[N];

		for (int i = 1; i < N; i++)
		{
			values[i] = EntityCore.getComponent( components.ids[i - 1] ).factory.create();
		}

		return values;
	}

	public Object cloneComponentAtIndex( int index, Object value )
	{
		return EntityCore.getComponent( components.ids[index - 1] ).factory.clone( value );
	}

	public final boolean hasController( int controllerId )
	{
		return controllers.has( controllerId );
	}

	public final boolean hasControllers( int... controllerIds )
	{
		return controllers.hasAll( controllerIds );
	}

	public final int getControllerCount()
	{
		return controllers.size();
	}

	public final int getControllerIndex( int controllerId )
	{
		return controllers.getIndex( controllerId );
	}

	public final void setControllerAlias( int controllerId, int aliasId )
	{
		controllers.alias( controllerId, aliasId );
	}

	public final void setView( int viewId )
	{
		view = viewId;
	}

	public final void add( int componentId )
	{
		components.add( componentId );
	}

	public final void addController( int controllerId )
	{
		controllers.add( controllerId );
	}

	public EntityType addCustomComponent( int componentId )
	{
		EntityTypeCustom custom = new EntityTypeCustom( id, this, components.getCopy(), controllers.getCopy(), view );

		custom.add( componentId );

		return custom;
	}

	public EntityType addCustomController( int controllerId )
	{
		EntityTypeCustom custom = new EntityTypeCustom( id, this, components.getCopy(), controllers.getCopy(), view );

		custom.addController( controllerId );

		return custom;
	}

	public EntityType setCustomView( int viewId )
	{
		EntityTypeCustom custom = new EntityTypeCustom( id, this, components.getCopy(), controllers.getCopy(), view );

		custom.setView( viewId );

		return custom;
	}

	public boolean isCustom()
	{
		return false;
	}

}

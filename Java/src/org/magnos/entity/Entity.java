package org.magnos.entity;

import java.util.Arrays;
import java.util.BitSet;

@SuppressWarnings("unchecked")
public class Entity
{

	protected EntityType type;

	protected Object[] components;

	protected BitSet controllers;
	
	protected boolean expired;
	

	public Entity( int entityTypeId )
	{
		this( EntityCore.getEntityType( entityTypeId ) );
	}

	public Entity( EntityType entityType )
	{
		this( entityType, entityType.createComponents() );
	}

	private Entity( EntityType entityType, Object[] components )
	{
		this.type = entityType;
		this.components = components;
		this.controllers = new BitSet( type.controllers.size() );
	}
	
	/*
	 * Expire Methods
	 */
	
	public boolean isExpired()
	{
		return expired;
	}
	
	public void expire()
	{
		expired = true;
	}
	
	public void onEntityListRemoval(EntityList list)
	{
		
	}
	
	/*
	 * View Draw
	 */

	public void draw( Object drawState )
	{
		View view = EntityCore.getViewSafe( type.view );

		if ( view != null )
		{
			view.draw( this, drawState );
		}
	}
	
	/*
	 * Controller Update
	 */

	public void update( Object updateState )
	{
		int[] controllerIds = type.controllers.ids;

		for (int i = 0; i < controllerIds.length; i++)
		{
			if ( controllers.get( i ) )
			{
				EntityCore.getController( controllerIds[i] ).control( this, updateState );
			}
		}
	}
	
	/*
	 * Controller Functions 
	 */

	public boolean isEnabled( int controllerId )
	{
		return controllers.get( type.getControllerIndex( controllerId ) );
	}

	public void setEnabled( int controllerId, boolean enabled )
	{
		controllers.set( type.getControllerIndex( controllerId ), enabled );
	}

	public void setEnabledAll( boolean enabled )
	{
		controllers.set( 0, type.getControllerCount(), enabled );
	}

	public void enable( int controllerId )
	{
		setEnabled( controllerId, true );
	}

	public void disable( int controllerId )
	{
		setEnabled( controllerId, false );
	}

	/*
	 * Component Functions 
	 */
	
	public boolean has( int id )
	{
		return type.hasComponent( id );
	}

	public <T> T get( int componentId )
	{
		return (T) components[type.getComponentIndex( componentId )];
	}

	public <T> T get( int componentId, Class<T> castType )
	{
		return (T) components[type.getComponentIndex( componentId )];
	}

	public <T> T getSafe( int componentId, Class<T> castType )
	{
		ComponentType componentType = EntityCore.getComponent( componentId );

		boolean correctType = castType.isAssignableFrom( componentType.type );

		return ( correctType ? get( componentId, castType ) : null );
	}

	public <T> T get( int componentId, T out )
	{
		ComponentType componentType = EntityCore.getComponent( componentId );

		if ( componentType instanceof DynamicComponentType )
		{
			DynamicComponentType<T> dynamicComponentType = (DynamicComponentType<T>) componentType;

			return dynamicComponentType.component.compute( this, out );
		}

		return null;
	}

	public void set( int componentId, Object value )
	{
		components[type.getComponentIndex( componentId )] = value;
	}

	/*
	 * Dynamic Functions 
	 */
	
	public void add( int componentId, Object defaultValue )
	{
		if ( !type.hasComponent( componentId ) )
		{
			type = type.addCustomComponent( componentId );
			components = Arrays.copyOf( components, type.getComponentCount() + 1 );
			set( componentId, defaultValue );
		}
	}

	public void addController( int behaviorId )
	{
		if ( !type.hasController( behaviorId ) )
		{
			type = type.addCustomController( behaviorId );
		}
	}

	public void setView( int viewId )
	{
		if ( type.view != viewId )
		{
			type = type.setCustomView( viewId );
		}
	}

	public Entity clone( boolean deep )
	{
		Object[] copy = new Object[components.length];

		for (int i = 1; i < components.length; i++)
		{
			if ( deep )
			{
				copy[i] = type.cloneComponentAtIndex( i, components[i] );
			}
			else
			{
				copy[i] = components[i];
			}
		}

		return new Entity( type, copy );
	}

	public EntityType getType()
	{
		return type;
	}

	public Object[] getComponents()
	{
		return components;
	}
	
	public BitSet getControllerFlags()
	{
		return controllers;
	}

}

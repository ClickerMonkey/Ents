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
	
	protected boolean visible;
	
	protected boolean enabled;
	

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
		this.expired = false;
		this.visible = true;
		this.enabled = true;
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
	
	/*
	 * View Draw
	 */
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	public void show()
	{
		visible = false;
	}
	
	public void hide()
	{
		visible = false;
	}
	
	public boolean isVisible()
	{
		return visible;
	}
	
	public void draw( Object drawState )
	{
		if ( visible )
		{
			View view = EntityCore.getViewSafe( type.view );

			if ( view != null )
			{
				view.draw( this, drawState );
			}
		}
	}
	
	/*
	 * Controller Update
	 */
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public void enable()
	{
		enabled = true;
	}
	
	public void disable()
	{
		enabled = true;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void update( Object updateState )
	{
		if ( enabled )
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
	}
	
	/*
	 * Controller Functions 
	 */

	public boolean isControllerEnabled( int controllerId )
	{
		return controllers.get( type.getControllerIndex( controllerId ) );
	}

	public void setControllerEnabled( int controllerId, boolean enabled )
	{
		controllers.set( type.getControllerIndex( controllerId ), enabled );
	}

	public void setControllerEnabledAll( boolean enabled )
	{
		controllers.set( 0, type.getControllerCount(), enabled );
	}

	public void enable( int controllerId )
	{
		setControllerEnabled( controllerId, true );
	}

	public void disable( int controllerId )
	{
		setControllerEnabled( controllerId, false );
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

	public void addController( int controllerId )
	{
		if ( !type.hasController( controllerId ) )
		{
			type = type.addCustomController( controllerId );
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
	
	/*
	 * hashCode(), equals() and toString
	 */
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append( '[' );
		
		for (int i = 0; i < type.getComponentCount(); i++)
		{
			if (i > 0 )
			{
				sb.append(',');
			}
			
			ComponentType componentType = EntityCore.getComponent( type.components.ids[i] );
		
			sb.append( componentType.name ) ;
			sb.append( '=' );
			sb.append( components[i] );
		}
		
		sb.append( ']' );
		
		return sb.toString();
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(components);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Entity)) {
			return false;
		}
		
		Entity e = (Entity)obj;
		
		return EntityUtility.equals(type, e.type) &&
			   EntityUtility.equals(components, e.components);
	}

}

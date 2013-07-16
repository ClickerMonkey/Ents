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
	

	public Entity( EntityCore core )
	{
		this( new EntityType( core ), new Object[1] );
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
			ViewBase viewBase = type.core.getViewSafe( type.view );
			
			if ( type.components.bitset.contains( viewBase.required ) && viewBase.view != null )
			{
				viewBase.view.draw( this, drawState );
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
					ControllerBase controllerBase = type.core.getControllerSafe( controllerIds[i] );
					
					if ( type.components.bitset.contains( controllerBase.required ) && controllerBase.controller != null )
					{
						controllerBase.controller.control( this, updateState );
					}
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
	
	public <T> boolean has( Component<T> component )
	{
		return type.hasComponent( component.id );
	}

	public <T> T get( Component<T> component )
	{
		return (T) components[type.getComponentIndex( component.id )];
	}

	public <T> T gets( Component<T> component )
	{
		int index = type.getComponentIndex( component.id );
		
		return ( index == 0 ? null : (T)components[index] );
	}
	
	public <T> T get( ComponentGet<T> component )
	{
		return component.get.get( this );
	}
	
	public <T> T gets( ComponentGet<T> component )
	{
		if ( !type.components.bitset.contains( component.required ) ) 
		{
			return null;
		}
		
		return component.get.get( this );
	}
	
	public <T> T set( ComponentSet<T> component, T target )
	{
		return component.set.set( this, target );
	}
	
	public <T> T sets( ComponentSet<T> component, T target )
	{
		if ( !type.components.bitset.contains( component.required ) ) 
		{
			return null;
		}
		
		return component.set.set( this, target );
	}

	public <T> void set( Component<T> component, T value )
	{
		components[type.getComponentIndex( component.id )] = value;
	}

	public <T> boolean sets( Component<T> component, T value )
	{
		int index = type.getComponentIndex( component.id );
		boolean exists = ( index != 0 );
		if (exists)
		{
			components[index] = value;
		}
		return exists;
	}
	
	/*
	 * Method Functions
	 */
	
	public <R> R execute(Method<R> method, Object ... arguments)
	{
		Method.Execute<R> implementation = type.getMethodImplementation( method.id );
		
		return implementation.execute( this, arguments );
	}

	public <R> R executes(Method<R> method, Object ... arguments)
	{
		Method.Execute<R> implementation = type.getMethodImplementationSafe( method.id );
		
		return ( implementation == null ? null : implementation.execute( this, arguments ) );
	}

	/*
	 * Dynamic Functions 
	 */
	
	public <T> void add( Component<T> component, T defaultValue )
	{
		if ( !type.hasComponent( component.id ) )
		{
			type = type.addCustomComponent( component.id );
			components = Arrays.copyOf( components, type.getComponentCount() + 1 );
			set( component, defaultValue );
		}
	}

	public void addController( int controllerId )
	{
		type = type.addCustomController( controllerId );
	}
	
	public <R> void addMethod( Method<R> method )
	{
		type = type.addCustomMethod( method );
	}
	
	public <R> void setMethod( Method<R> method, Method.Execute<R> execute )
	{
		type = type.setCustomMethod( method, execute );
	}

	public void setView( int viewId )
	{
		if ( type.view != viewId )
		{
			type = type.setCustomView( viewId );
		}
	}
	
	public <T> void alias( Component<T> component, Component<T> alias )
	{
		type = type.setCustomComponentAlias( component, alias );
	}
	
	public void aliasController( int controllerId, int aliasId )
	{
		type = type.setCustomControllerAlias( controllerId, aliasId );
	}
	
	public <R> void aliasMethod( Method<R> method, Method<R> alias )
	{
		type = type.setCustomMethodAlias( method, alias );
	}

	/*
	 * Cloning 
	 */
	
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
			
			ComponentBase componentBase = type.core.getComponent( type.components.ids[i] );
		
			sb.append( componentBase.name ) ;
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

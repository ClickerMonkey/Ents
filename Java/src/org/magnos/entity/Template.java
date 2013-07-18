package org.magnos.entity;

import java.util.Arrays;

public class Template extends Id
{
	public static final int CUSTOM = Integer.MAX_VALUE;
	public static final String CUSTOM_NAME = "custom";

	protected final Template parent;
	
	protected Component<?>[] components;
	protected BitSet componentBitSet;
	protected int[] componentMap;
	
	protected Controller[] controllers;
	protected BitSet controllerBitSet;
	protected int[] controllerMap;
	
	protected TemplateComponent<?>[] handlers = {};
	protected ComponentFactory<?>[] factories = {};
	
	protected View view;
	protected int instances;

	protected Template()
	{
		this( CUSTOM, CUSTOM_NAME, null, new Components(), new Controllers(), null );
	}

	protected Template( int id, String name, Template parent, Components componentSet, Controllers controllerSet, View view )
	{
		super( id, name );
		
		this.parent = parent;
		
		this.components = componentSet.values;
		this.componentMap = EntityUtility.createMap( components );
		this.componentBitSet = new BitSet( components );
		
		this.controllers = controllerSet.values;
		this.controllerMap = EntityUtility.createMap( controllers );
		this.controllerBitSet = new BitSet( controllers );
		
		this.view = view;
		
		ensureFit( EntityUtility.getMaxId( components ) );
		
		for (int i = 0; i < components.length; i++)
		{
			final Component<?> c = components[i];
			
			if ( has( c ) )
			{
				throw new RuntimeException( "A template cannot have the same component more than once!" );
			}
			
			handlers[c.id] = c.add( this );
		}
	}

	protected Template extend( int id, String name )
	{
		return new Template( id, name, this, new Components( components ), new Controllers( controllers ), view );
	}

	protected Object[] createDefaultValues()
	{
		final int n = factories.length;
		final Object[] values = new Object[n];
		
		for (int i = 0; i < n; i++)
		{
			values[i] = factories[i].create();
		}
		
		return values;
	}

	protected void newInstance()
	{
		instances++;
	}

	protected void removeInstance()
	{
		instances--;
	}

	public <T> boolean has( Component<T> component )
	{
		return component.id < componentMap.length && handlers[component.id] != null;
	}

	public <T> boolean hasExact( Component<T> component )
	{
		int i = indexOf( component );
		
		return (i != -1 && components[i] == component);
	}

	public boolean has( Component<?>... components )
	{
		for (Component<?> c : components)
		{
			if ( !has( c ) )
			{
				return false;
			}
		}
		return true;
	}

	protected int indexOf( Component<?> component )
	{
		return component.id >= componentMap.length ? -1 : componentMap[component.id];
	}

	public <T> void add( Component<T> component )
	{
		ensureFit( component.id );
		
		int i = indexOf( component );
		
		if ( i != -1 )
		{
			handlers[i].remove( this );
			components[i] = component;
		}
		else
		{
			i = components.length;
			componentBitSet.set( i );
			components = EntityUtility.append( components, component );
		}
		
		handlers[component.id] = component.add( this );
		componentMap[component.id] = i;
	}

	public <T> void alias( Component<T> component, Component<T> alias )
	{
		ensureFit( alias.id );
		
		handlers[alias.id] = handlers[component.id];
		componentMap[alias.id] = componentMap[component.id];
	}

	public boolean has( Controller controller )
	{
		return controllerBitSet.get( controller.id );
	}

	public boolean hasExact( Controller controller )
	{
		int i = indexOf( controller );
		
		return (i != -1 && controllers[i] == controller);
	}

	public boolean has( Controller... controllers )
	{
		for (Controller c : controllers)
		{
			if ( !has( c ) )
			{
				return false;
			}
		}
		return true;
	}

	protected int indexOf( Controller controller )
	{
		return controller.id >= controllerMap.length ? -1 : controllerMap[controller.id];
	}

	public void add( Controller controller )
	{
		if ( !has( controller ) )
		{
			controllerBitSet.set( controllers.length );
			controllers = EntityUtility.append( controllers, controller );
		}
		else
		{
			controllers[indexOf( controller )] = controller;
		}
	}

	public boolean has( View view )
	{
		return ( this.view.id == view.id );
	}

	public boolean hasExact( View view )
	{
		return this.view == view;
	}

	public void setView( View view )
	{
		this.view = view;
	}

	private void ensureFit( int id )
	{
		if ( handlers.length <= id )
		{
			int componentMapSize = componentMap.length;
			
			handlers = Arrays.copyOf( handlers, id + 1 );
			componentMap = Arrays.copyOf( componentMap, id + 1 );
			
			for (int i = componentMapSize; i <= id; i++)
			{
				componentMap[i] = -1;
			}
		}
	}

	public boolean isCustom()
	{
		return ( id == CUSTOM );
	}

	protected <T> Template addCustomComponent( Component<T> component )
	{
		if ( hasExact( component ) )
		{
			return this;
		}
		
		Template t = getCustomTarget();
		t.add( component );
		
		return t;
	}

	protected <T> Template setCustomAlias( Component<T> component, Component<T> alias )
	{
		Template t = this;
		
		if ( has( component ) )
		{
			if ( handlers[alias.id] != handlers[component.id] )
			{
				t = getCustomTarget();
				t.alias( component, alias );
			}
		}
		
		return t;
	}

	protected Template addCustomController( Controller controller )
	{
		if ( hasExact( controller ) )
		{
			return this;
		}
		
		Template t = getCustomTarget();
		t.add( controller );
		
		return t;
	}

	protected Template setCustomView( View view )
	{
		if ( hasExact( view ) )
		{
			return this;
		}
		
		Template t = getCustomTarget();
		t.setView( view );
		
		return t;
	}

	protected Template getCustomTarget()
	{
		return isCustom() && instances == 1 ? this : extend( CUSTOM, CUSTOM_NAME );
	}
	
}
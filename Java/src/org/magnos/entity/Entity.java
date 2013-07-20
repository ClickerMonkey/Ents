package org.magnos.entity;

import java.util.Arrays;


/**
 * An Entity is a game object that may be drawn, may be updated, has a set of 
 * components, has a set of controllers that modify it's state, and optionally
 * a view which handles the drawing.
 * 
 * @author Philip Diffenderfer
 *
 */
@SuppressWarnings("unchecked")
public class Entity
{

	protected Template template;

	protected Object[] values;

	protected BitSet controllerEnabled;

	protected boolean expired = false;

	protected boolean visible = true;

	protected boolean enabled = true;

	
	public Entity()
	{
		this( EntityCore.newTemplate() );
	}

	public Entity( Template template )
	{
		this( template, template.createDefaultValues() );
	}

	private Entity( Template template, Object[] values )
	{
		this.setTemplate( template );
		this.values = values;
		this.controllerEnabled = new BitSet( template.controllers.length );
		this.setControllerEnabledAll( true );
	}

	private boolean setTemplate( Template newTemplate )
	{
		boolean changed = ( template != newTemplate );

		if ( changed )
		{
			if ( template != null )
			{
				template.removeInstance();
			}

			( template = newTemplate ).newInstance();
		}

		return changed;
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

	public void setVisible( boolean visible )
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
			View view = template.view;

			if ( view != null )
			{
				view.renderer.draw( this, drawState );
			}
		}
	}

	public boolean has( View view )
	{
		return template.has( view );
	}
	
	public boolean hasView()
	{
		return template.hasView();
	}
	
	/*
	 * Controller Update
	 */

	public void setEnabled( boolean enabled )
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
			final Controller[] controllers = template.controllers;

			for (int i = 0; i < controllers.length; i++)
			{
				final Controller c = controllers[i];

				if ( controllerEnabled.get( template.indexOf( c ) ) )
				{
					c.control.control( this, updateState );
				}
			}
		}
	}

	/*
	 * Controller Functions
	 */

	public boolean isControllerEnabled( Controller controller )
	{
		return controllerEnabled.get( template.indexOf( controller ) );
	}

	public void setControllerEnabled( Controller controller, boolean enabled )
	{
		controllerEnabled.set( template.indexOf( controller ), enabled );
	}

	public void setControllerEnabledAll( boolean enabled )
	{
		controllerEnabled.set( 0, template.controllers.length, enabled );
	}

	public void enable( Controller controller )
	{
		setControllerEnabled( controller, true );
	}

	public void disable( Controller controller )
	{
		setControllerEnabled( controller, false );
	}
	
	public boolean has( Controller controller )
	{
		return template.has( controller );
	}

	public boolean hasControllers( BitSet controllers )
	{
		return template.controllerBitSet.contains( controllers );
	}
	
	/*
	 * Component Functions
	 */

	public boolean has( Component<?> component )
	{
		return template.has( component );
	}
	
	public boolean has( Component<?> ... components )
	{
		return template.has( components );
	}

	public boolean has( BitSet components )
	{
		return template.componentBitSet.contains( components );
	}

	public <T> T get( Component<T> component )
	{
		TemplateComponent<T> ch = (TemplateComponent<T>) template.handlers[component.id];
		return ch.get( this );
	}

	public <T> T gets( Component<T> component )
	{
		return template.has( component ) ? get( component ) : null;
	}

	public <T> T gets( Component<T> component, T missingValue )
	{
		return template.has( component ) ? get( component ) : missingValue;
	}

	public <T> void set( Component<T> component, T value )
	{
		TemplateComponent<T> ch = (TemplateComponent<T>) template.handlers[component.id];
		ch.set( this, value );
	}

	public <T> boolean sets( Component<T> component, T value )
	{
		boolean has = template.has( component );
		if ( has )
		{
			set( component, value );
		}
		return has;
	}
	
	public <T> T take( Component<T> component, T target )
	{
		TemplateComponent<T> ch = (TemplateComponent<T>) template.handlers[component.id];
		return ch.take( this, target );
	}
	
	/*
	 * Dynamic Functions
	 */

	public <T> boolean add( Component<T> component )
	{
		boolean newComponent = !template.hasExact( component );

		setTemplate( template.addCustomComponent( component ) );

		if ( newComponent )
		{
			component.postCustomAdd( this );
		}

		return newComponent;
	}

	public <T> boolean add( Component<T> component, T defaultValue )
	{
		boolean added = add( component );

		if ( added )
		{
			set( component, defaultValue );
		}

		return added;
	}

	public <T> boolean put( Component<T> component, T value )
	{
		boolean missing = !template.has( component );
		
		if (missing)
		{
			add(component);
		}
		
		set(component, value);
		
		return missing;
	}

	public void add( Controller controller )
	{
		setTemplate( template.addCustomController( controller ) );
		controllerEnabled.set( controller.id );
	}

	public void setView( View view )
	{
		setTemplate( template.setCustomView( view ) );
	}

	public <T> void alias( Component<T> component, Component<T> alias )
	{
		setTemplate( template.setCustomAlias( component, alias ) );
	}

	/*
	 * Cloning
	 */

	public Entity clone( boolean deep )
	{
		final int valueCount = values.length;
		final Object[] clonedValues = new Object[valueCount];

		if ( deep )
		{
			for (int i = 0; i < valueCount; i++)
			{
				ComponentFactory<Object> factory = (ComponentFactory<Object>) template.factories[i];
				clonedValues[i] = factory.clone( values[i] );
			}
		}
		else
		{
			System.arraycopy( values, 0, clonedValues, 0, valueCount );
		}

		return new Entity( template, clonedValues );
	}
	
	public Entity merge(Entity entity, boolean overwrite)
	{
		Template template = entity.template;
		
		for ( Component<?> component : template.components ) 
		{
			if (overwrite || !has( component ) )
			{
				add( component );
			}
		}
		
		for ( Controller controller : template.controllers )
		{
			if ( overwrite || !has( controller ) )
			{
				add( controller );
			}
		}
		
		if ( overwrite || !hasView() )
		{
			setView( template.view );
		}
		
		return this;
	}

	public Template getTemplate()
	{
		return template;
	}
	
	public boolean isCustom()
	{
		return template.isCustom();
	}

	public Object[] getValues()
	{
		return values;
	}

	public BitSet getControllerFlags()
	{
		return controllerEnabled;
	}

	/*
	 * hashCode(), equals() and toString
	 */
	
	protected void fill(EntityFilter filter)
	{
		filter.prepare(1);
		filter.push(this);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append( '[' );

		for (Component<?> c : template.components)
		{
			if ( sb.length() > 1 )
			{
				sb.append( ',' );
			}

			sb.append( c.name );
			sb.append( '=' );
			sb.append( get( c ) );
		}

		sb.append( ']' );

		return sb.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode( values );
		result = prime * result + ( ( template == null ) ? 0 : template.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( obj == null )
		{
			return false;
		}
		if ( obj == this )
		{
			return true;
		}
		if ( ! ( obj instanceof Entity ) )
		{
			return false;
		}

		Entity e = (Entity) obj;

		return EntityUtility.equals( template, e.template ) &&
			   EntityUtility.equals( values, e.values );
	}
	
}

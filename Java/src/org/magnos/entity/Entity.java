/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package org.magnos.entity;

import org.magnos.entity.util.BitSet;
import org.magnos.entity.util.EntityUtility;


/**
 * An Entity is a game object that may be drawn, may be updated, has a set of
 * components, has a set of controllers that modifies it's state, and optionally
 * a view which handles the drawing. <br/>
 * <br/>
 * An entity by default is not expired, is visible, and is enabled. <br/>
 * <br/>
 * When an entity expires, the object containing the Entity is responsible from
 * removing it from the world. An expired Entity is one that is ready to be
 * entirely forgotten by the world and should not be drawn or updated. However,
 * an Entity could be expired and the {@link #update(Object)} and
 * {@link #draw(Object)} methods may still work, it's the job of the container
 * to check for expiration.
 * 
 * @author Philip Diffenderfer
 * 
 */
@SuppressWarnings ("unchecked" )
public class Entity
{

   public final int id;

   protected Template template;

   protected Object[] values;

   protected BitSet controllerEnabled;

   protected Renderer renderer;

   protected boolean expired = false;

   protected boolean visible = true;

   protected boolean enabled = true;

   /**
    * Instantiates a custom entity without a {@link Template}. This entity will
    * have no components, controller, or view by default.
    */
   public Entity()
   {
      this( EntityCore.newTemplate() );
   }

   public Entity( Component<?>... components )
   {
      this( new Components( components ), Controllers.NONE, View.NONE );
   }

   public Entity( Components componentSet, Controllers controllerSet )
   {
      this( componentSet, controllerSet, View.NONE );
   }

   public Entity( Components componentSet, Controllers controllerSet, View view )
   {
      this( new Template( componentSet, controllerSet, view ) );
   }

   /**
    * Instantiates an Entity given a {@link Template}. This entity will have the
    * Template's components, controllers, and view. <br/>
    * <br/>
    * If a component, controller, or view is dynamically added to this Entity
    * then the given Template will be "extended" in such a way that the parent
    * to the Template of this Entity will be the given template however changes
    * to the resulting Template will not modify it's parent. <br/>
    * <br/>
    * All controllers that exist in the Template are enabled by default on the
    * Entity. In other words, when update is called after Entity creation all
    * controllers will modify the Entity. To control which controllers are
    * enabled use the {@link #setControllerEnabled(Controller, boolean)} method
    * (or any of it's variants). <br/>
    * <br/>
    * Do not modify a Template directly, this should only be done before any
    * Entity is created with it. A problem arises when a distinct component is
    * added to a Template that already has Entity instances... the instances
    * created with the template will appear to have the component but the
    * entities will not actually have the component value.
    * 
    * @param template
    *        The template of the Entity.
    */
   public Entity( Template template )
   {
      this( template, template.createDefaultValues(), template.createRenderer() );
   }

   /**
    * Instantiates an Entity given a template and the Entity's default values.
    * 
    * @param template
    *        The template of the Entity.
    * @param values
    *        The default values of the entity.
    */
   protected Entity( Template template, Object[] values, Renderer renderer )
   {
      this.setTemplate( template );
      this.setRenderer( renderer );
      this.values = values;
      this.controllerEnabled = new BitSet( template.controllers.length, true );
      this.id = EntityCore.register( this );

      template.addtoComponents( this );
   }

   /**
    * Internally sets the Template of the entity notifying the existing Template
    * (if any) that it has one last Entity instance as well as notifying the new
    * Template that it has a new Entity instance.
    * 
    * @param newTemplate
    *        The new template of the Entity.
    * @return True if the template given is different than the existing
    *         template.
    */
   private boolean setTemplate( Template newTemplate )
   {
      boolean changed = (template != newTemplate);

      if (changed)
      {
         if (template != null)
         {
            template.removeInstance( this );
         }

         (template = newTemplate).newInstance( this );
      }

      return changed;
   }

   /**
    * @return True if the Entity has expired, otherwise false.
    */
   public boolean isExpired()
   {
      return expired;
   }

   /**
    * Expires the entity, setting off the flag that let's any container know
    * that this Entity should no longer be held (or drawn and updated) and
    * {@link #delete()} should be invoked.
    */
   public void expire()
   {
      expired = true;
   }

   /**
    * This removes this Entity from the Template and destroys the Entity's
    * Renderer. Once this method is called, this Entity should not by used,
    * all methods will most likely result in a {@link NullPointerException}.
    */
   public boolean delete()
   {
      boolean deletable = (template != null);

      if (deletable)
      {
         EntityCore.unregister( this );

         template.removeFromComponents( this );
         template.removeInstance( this );

         if (renderer != null)
         {
            renderer.destroy( this );
         }

         template = null;
         renderer = null;
         expired = true;
      }

      return deletable;
   }

   /**
    * Sets the visibility of this Entity to the given value.
    * 
    * @param visible
    *        True if the {@link #draw(Object)} method should have any effect,
    *        otherwise false.
    */
   public void setVisible( boolean visible )
   {
      this.visible = visible;
   }

   /**
    * Sets the visibility of this Entity to true. If this entity has a view,
    * {@link Renderer#draw(Entity, Object)} will be called with the drawState
    * passed into draw as well as the reference to this Entity.
    */
   public void show()
   {
      visible = true;
   }

   /**
    * Sets the visibility of this Entity to false. This entity will not be
    * drawn.
    */
   public void hide()
   {
      visible = false;
   }

   /**
    * @return True if the Entity is visible, otherwise false.
    */
   public boolean isVisible()
   {
      return visible;
   }

   /**
    * Draws the Entity if it's visible and has a {@link View}, by passing the
    * drawState and this Entity to it's View.
    * 
    * @param drawState
    *        An object to pass to the view which enables this Entity to be drawn
    *        on the chosen medium.
    */
   public void draw( Object drawState )
   {
      if (visible && renderer != null)
      {
         renderer.drawStart( this, drawState );
         renderer.draw( this, drawState );
         renderer.drawEnd( this, drawState );
      }
   }

   /**
    * Determines whether this Entity has the given view (or an alternative).
    * This returns whether the view passed in has the same ID as the current
    * view of the Entity, it does not check to see if it's literally the exact
    * same view.
    * 
    * @param view
    *        The view to check for.
    * @return True if this Entity has the given view (or an alternative),
    *         otherwise false.
    */
   public boolean has( View view )
   {
      return template.has( view );
   }

   /**
    * @return True if this Entity has a view at all, otherwise false.
    */
   public boolean hasView()
   {
      return template.hasView();
   }

   public boolean hasRenderer()
   {
      return renderer != null;
   }

   public Renderer getRenderer()
   {
      return renderer;
   }

   public View getView()
   {
      return template.getView();
   }

   /**
    * Sets whether this entity is enabled or not. An enabled entity (true) will
    * invoke it's controllers when {@link #update(Object)} is called, while a
    * disabled entity (false) does not. <br/>
    * <br/>
    * Even if the entity is enabled, it's controllers need to be enabled as well
    * using the {@link #setControllerEnabled(Controller, boolean)} method or
    * it's variants.
    * 
    * @param enabled
    *        True if the entity should be enabled, otherwise false.
    */
   public void setEnabled( boolean enabled )
   {
      this.enabled = enabled;
   }

   /**
    * Enables this entity, calling any enabled controllers when
    * {@link #update(Object)} is called.
    */
   public void enable()
   {
      enabled = true;
   }

   /**
    * Disables this entity, {@link #update(Object)} will have no affect.
    */
   public void disable()
   {
      enabled = false;
   }

   /**
    * @return True if the entity is enabled, otherwise false.
    */
   public boolean isEnabled()
   {
      return enabled;
   }

   /**
    * Updates the Entity if it's enabled. To update an entity, all enabled
    * controllers on the Entity are invoked passing the Entity reference and the
    * given updateState.
    * 
    * @param updateState
    *        An object to pass to the controller's control method.
    */
   public void update( Object updateState )
   {
      if (enabled)
      {
         final Controller[] controllers = template.controllers;

         for (int i = 0; i < controllers.length; i++)
         {
            final Controller c = controllers[i];

            if (controllerEnabled.get( template.indexOf( c ) ))
            {
               c.control.control( this, updateState );
            }
         }
      }
   }

   /**
    * 
    * 
    * @param controller
    * @return
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

   public boolean has( Controller... controllers )
   {
      return template.has( controllers );
   }

   public boolean hasControllers( BitSet controllers )
   {
      return template.controllerBitSet.contains( controllers );
   }

   /*
    * Component Functions
    */

   /**
    * Determines whether this entity has the given component (or an
    * alternative).
    * 
    * @param component
    *        The component to test for.
    * @return True if this Entity has the component (or an alternative),
    *         otherwise false.
    */
   public boolean has( Component<?> component )
   {
      return template.has( component );
   }

   /**
    * Determines whether this entity has all of the given components (or their
    * alternatives).
    * 
    * @param components
    *        The set of components to test for.
    * @return True if this entity has all of the components (or their
    *         alternatives), otherwise false.
    */
   public boolean has( Component<?>... components )
   {
      return template.has( components );
   }

   /**
    * Determines whether this entity has all of the given components specified
    * by a BitSet, where the i'th bit in the set is set to true to indicate the
    * component with the ID equaling "i" is present.
    * 
    * @param components
    *        The BitSet of components.
    * @return True if this entity has all of the components specified in the
    *         BitSet, otherwise false.
    */
   public boolean hasComponents( BitSet components )
   {
      return template.componentBitSet.contains( components );
   }

   /**
    * Returns the value of the component on this Entity. This is an unsafe
    * method, meaning if the Entity does not have the component then a
    * {@link NullPointerException} will be thrown. This method is also the
    * fastest way of getting an Entity's component value. <br/>
    * <br/>
    * {@link #gets(Component)} or {@link #gets(Component, Object)} should be
    * used to safely get a value of a component. <br/>
    * <br/>
    * Depending on the component type, the value returned may be a value that is
    * shared between all Entities that have the component, it may be shared
    * between all Entities that have the same Template, it may be a dynamically
    * created value, or it may be an aliased value (existing under a different
    * component in reality).
    * 
    * @param component
    *        The component to get the value of.
    * @return The value of the component on this Entity.
    * @throws NullPointerException
    *         The component does not exist on this Entity.
    */
   public <T> T get( Component<T> component )
   {
      TemplateComponent<T> ch = (TemplateComponent<T>)template.handlers[component.id];
      return ch.get( this );
   }

   /**
    * Returns the value of the component on this Entity or null if this entity
    * does not have the given component. This is a safer, although slightly
    * slower, alternative to {@link #get(Component)}. An alternative method to
    * this is {@link #gets(Component, Object)} which returns a defined value if
    * the Entity doesn't have the component. <br/>
    * <br/>
    * {@link #get(Component)} should be used if you are confident an Entity has
    * the component, or if you want an exception to be thrown to indicate a
    * logical error in your code. <br/>
    * <br/>
    * The return value of null should not be used as an indicator to whether an
    * Entity has a component, since null is a valid component value. <br/>
    * <br/>
    * Depending on the component type, the value returned may be a value that is
    * shared between all Entities that have the component, it may be shared
    * between all Entities that have the same Template, it may be a dynamically
    * created value, or it may be an aliased value (existing under a different
    * component in reality).
    * 
    * @param component
    *        The component to get the value of.
    * @return The value of the component on this Entity or null.
    */
   public <T> T gets( Component<T> component )
   {
      return template.has( component ) ? get( component ) : null;
   }

   /**
    * Returns the value of the component on this Entity or given missingValue if
    * this entity does not have the given component. This is a safer, although
    * slightly slower, alternative to {@link #get(Component)}. An alternative
    * method to this is {@link #gets(Component)} which returns null if the
    * Entity doesn't have the component. <br/>
    * <br/>
    * {@link #get(Component)} should be used if you are confident an Entity has
    * the component, or if you want an exception to be thrown to indicate a
    * logical error in your code. <br/>
    * <br/>
    * The return value matching (by reference) the missingValue passed in should
    * not be used as an indicator to whether an Entity has a component, since
    * missingValue could actually exist on the Entity. <br/>
    * <br/>
    * Depending on the component type, the value returned may be a value that is
    * shared between all Entities that have the component, it may be shared
    * between all Entities that have the same Template, it may be a dynamically
    * created value, or it may be an aliased value (existing under a different
    * component in reality).
    * 
    * @param component
    *        The component to get the value of.
    * @return The value of the component on this Entity or missingValue.
    */
   public <T> T gets( Component<T> component, T missingValue )
   {
      return template.has( component ) ? get( component ) : missingValue;
   }

   /**
    * Sets the value of the component on this Entity. This is an unsafe method,
    * meaning if the Entity does not have the component then a
    * {@link NullPointerException} will be thrown. This method is also the
    * fastest way of setting an Entity's component value. <br/>
    * <br/>
    * {@link #sets(Component, Object)} should be used to safely set the value of
    * a component on the Entity. <br/>
    * <br/>
    * Depending on the component type, the value set may be a value that is
    * shared between all Entities that have the component, it may be shared
    * between all Entities that have the same Template, it may be a dynamically
    * set value, or it may be an aliased value (existing under a different
    * component in reality).
    * 
    * @param component
    *        The component to set the value of.
    * @param value
    *        The new value of the component.
    * @throws NullPointerException
    *         The component does not exist on this Entity.
    */
   public <T> void set( Component<T> component, T value )
   {
      TemplateComponent<T> ch = (TemplateComponent<T>)template.handlers[component.id];
      ch.set( this, value );
   }

   /**
    * Sets the value of the component on this Entity and returns true, or
    * returns false if this Entity does not have the given component. This is a
    * safe yet slower alternative to {@link #set(Component, Object)}, thus will
    * not result in an exception being thrown. <br/>
    * <br/>
    * {@link #set(Component, Object)} should be used if you are confident an
    * Entity has the component, or if you want an exception to be thrown to
    * indicate a logical error in your code. <br/>
    * <br/>
    * Depending on the component type, the value set may be a value that is
    * shared between all Entities that have the component, it may be shared
    * between all Entities that have the same Template, it may be a dynamically
    * set value, or it may be an aliased value (existing under a different
    * component in reality).
    * 
    * @param component
    *        The component to set the value of.
    * @param value
    *        The new value of the component.
    * @return True if the Entity has the component and the value was set,
    *         otherwise false.
    */
   public <T> boolean sets( Component<T> component, T value )
   {
      boolean has = template.has( component );

      if (has)
      {
         set( component, value );
      }

      return has;
   }

   /**
    * Sets target to the value of the given component on this Entity and returns
    * target. This is not a safe method and will result in a
    * {@link NullPointerException} if the entity does not contain the component,
    * however it is the quicker method. <br/>
    * <br/>
    * This is useful when a dynamic component could be in use, depending on the
    * {@link DynamicValue} implementation it may save on unnecessarily creating
    * a short-lived object. <br/>
    * <br/>
    * {@link #takes(Component, Object)} should be used as the safe alternative
    * to this method. <br/>
    * <br/>
    * Not all component types may support taking, therefore a reference to an
    * object other than target may be returned.
    * 
    * @param component
    *        The component to take the value of.
    * @param target
    *        The target value to set and return with the component's value.
    * @return The reference to target, or the reference to another object if
    *         taking is not supported by the component on this Entity.
    * @throws NullPointerException
    *         The component does not exist on this Entity.
    */
   public <T> T take( Component<T> component, T target )
   {
      TemplateComponent<T> ch = (TemplateComponent<T>)template.handlers[component.id];
      return ch.take( this, target );
   }

   /**
    * Sets target to the value of the given component on this Entity and returns
    * target. This is the safe alternative to {@link #take(Component, Object)}
    * and will return null if this Entity does not have the given component. <br/>
    * <br/>
    * This is useful when a dynamic component could be in use, depending on the
    * {@link DynamicValue} implementation it may save on unnecessarily creating
    * a short-lived object. <br/>
    * <br/>
    * {@link #take(Component, Object)} should be used if you are confident an
    * Entity has the component, or if you want an exception to be thrown to
    * indicate a logical error in your code. <br/>
    * <br/>
    * Not all component types may support taking, therefore a reference to an
    * object other than target may be returned.
    * 
    * @param component
    *        The component to take the value of.
    * @param target
    *        The target value to set and return with the component's value.
    * @return The reference to target, null, or the reference to another object
    *         if taking is not supported by the component on this Entity.
    * @throws NullPointerException
    *         The component does not exist on this Entity.
    */
   public <T> T takes( Component<T> component, T target )
   {
      return has( component ) ? take( component, target ) : null;
   }

   /*
    * Dynamic Functions
    */

   public <T> void add( Component<T> component )
   {
      setTemplate( template.addCustomComponent( component, this ) );
   }

   public <T> void add( Component<T> component, T defaultValue )
   {
      add( component );
      set( component, defaultValue );
   }

   public <T> boolean put( Component<T> component, T value )
   {
      boolean missing = !template.has( component );

      if (missing)
      {
         add( component );
      }

      set( component, value );

      return missing;
   }

   public <T> T grab( Component<T> component )
   {
      boolean missing = !template.has( component );

      if (missing)
      {
         add( component );
      }

      return get( component );
   }

   public void add( Controller controller )
   {
      setTemplate( template.addCustomController( controller ) );

      controllerEnabled.set( template.indexOf( controller ) );
   }

   public void setView( View view )
   {
      if (setTemplate( template.setCustomView( view ) ))
      {
         setRenderer( template.createRenderer() );
      }
   }

   public <T> void alias( Component<T> component, Component<T> alias )
   {
      setTemplate( template.setCustomAlias( component, alias ) );
   }

   public void setRenderer( Renderer newRenderer )
   {
      if (renderer != newRenderer)
      {
         if (renderer != null)
         {
            renderer.destroy( this );
         }

         renderer = (newRenderer == null ? null : newRenderer.create( this ));
      }
   }

   /*
    * Cloning
    */

   public Entity clone( boolean deep )
   {
      return cloneState( new Entity( template, template.createClonedValues( values, deep ), renderer ) );
   }

   protected <E extends Entity> E cloneState( E e )
   {
      e.controllerEnabled.clear();
      e.controllerEnabled.or( controllerEnabled );
      e.enabled = enabled;
      e.visible = visible;

      return e;
   }

   public Entity merge( Entity entity, boolean overwrite )
   {
      Template template = entity.template;

      for (Component<?> component : template.components)
      {
         if (overwrite || !has( component ))
         {
            add( component );
         }
      }

      for (Controller controller : template.controllers)
      {
         if (overwrite || !has( controller ))
         {
            add( controller );
         }
      }

      if (overwrite || !hasView())
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

   /**
    * Returns the number of entities within this Entity. This should always be
    * at least one since this Entity is included in the size.
    * 
    * @return The number of entities in this Entity (including this).
    */
   protected int getEntitySize()
   {
      return 1;
   }

   /**
    * Returns the reference to the entity at the given position within this
    * Entity. The index must be greater than or equal to 0 and less than
    * {@link #getEntitySize()}.
    * 
    * @param index
    *        The index of the entity to return.
    * @return The reference to the entity at the given index.
    */
   protected Entity getEntity( int index )
   {
      return this;
   }

   /**
    * Returns the index that when passed into {@link #getEntity(int)} will
    * return the reference to this Entity.
    * 
    * @return The index of this entity.
    */
   protected int getEntityIndex()
   {
      return 0;
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();

      sb.append( '[' );

      if (template != null)
      {
         for (Component<?> c : template.components)
         {
            if (sb.length() > 1)
            {
               sb.append( ',' );
            }

            sb.append( c.name );
            sb.append( '=' );
            sb.append( get( c ) );
         }
      }
      else
      {
         sb.append( "DELETED" );
      }

      sb.append( ']' );

      return sb.toString();
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      
      for (Component<?> c : template.components)
      {
         Object v = get( c );
         
         result = prime * result + (v == null ? 0 : v.hashCode());
      }
      
      return result;
   }

   @Override
   public boolean equals( Object obj )
   {
      if (obj == null || !(obj instanceof Entity))
      {
         return false;
      }
      if (obj == this)
      {
         return true;
      }

      Entity e = (Entity)obj;

      if (!template.componentBitSet.equals( e.template.componentBitSet ))
      {
         return false;
      }

      for (Component<?> c : template.components)
      {
         if (!EntityUtility.equals( get( c ), e.get( c ) ))
         {
            return false;
         }
      }

      return true;
   }

}

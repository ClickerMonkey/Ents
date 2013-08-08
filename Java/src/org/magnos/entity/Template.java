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

import java.util.Arrays;

import org.magnos.entity.util.BitSet;
import org.magnos.entity.util.EntityUtility;


public class Template extends Id
{

   public static final int CUSTOM = Integer.MAX_VALUE;
   public static final String CUSTOM_NAME = "custom";
   public static final Template PARENT_NONE = null;

   protected final Template parent;
   protected final BitSet extensionBitSet;

   protected Component<?>[] components;
   protected BitSet componentBitSet;
   protected int[] componentMap;

   protected Controller[] controllers;
   protected BitSet controllerBitSet;
   protected int[] controllerMap;

   protected TemplateComponent<?>[] handlers = {};
   protected ComponentValueFactory<?>[] factories = {};

   protected View view;
   protected int instances;

   protected Template()
   {
      this( CUSTOM, CUSTOM_NAME, PARENT_NONE, Components.NONE, Controllers.NONE, View.NONE );
   }

   protected Template( Components componentSet, Controllers controllerSet, View view )
   {
      this( CUSTOM, CUSTOM_NAME, PARENT_NONE, componentSet, controllerSet, view );
   }

   protected Template( int id, String name, Template parent, Components componentSet, Controllers controllerSet, View view )
   {
      super( id, name );

      this.parent = parent;
      this.extensionBitSet = getExtensionBitSet( this );

      this.components = componentSet.values;
      this.componentMap = EntityUtility.createMap( components );
      this.componentBitSet = new BitSet( components );

      this.controllers = controllerSet.values;
      this.controllerMap = EntityUtility.createMap( controllers );
      this.controllerBitSet = new BitSet( controllers );

      this.view = view;

      ensureComponentFit( EntityUtility.getMaxId( components ) );

      for (int i = 0; i < components.length; i++)
      {
         final Component<?> c = components[i];

         handlers[c.id] = c.add( this );
      }
   }

   public Template copy()
   {
      return extend( CUSTOM, CUSTOM_NAME );
   }

   public Template merge( Template template, boolean overwrite )
   {
      for (Component<?> c : template.components)
      {
         if (overwrite || !has( c ))
         {
            add( c );
         }
      }

      for (Controller c : template.controllers)
      {
         if (overwrite || !has( c ))
         {
            add( c );
         }
      }

      if (overwrite || !hasView())
      {
         setView( template.view );
      }

      return this;
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

   protected Renderer createRenderer()
   {
      return (view == null ? null : view.renderer);
   }

   @SuppressWarnings ("unchecked" )
   protected Object[] createClonedValues( Object[] values, boolean deep )
   {
      final int valueCount = values.length;
      final Object[] clonedValues = new Object[valueCount];

      if (deep)
      {
         for (int i = 0; i < valueCount; i++)
         {
            ComponentValueFactory<Object> factory = (ComponentValueFactory<Object>)factories[i];
            clonedValues[i] = factory.clone( values[i] );
         }
      }
      else
      {
         System.arraycopy( values, 0, clonedValues, 0, valueCount );
      }

      return clonedValues;
   }

   protected void newInstance( Entity e )
   {
      instances++;
   }

   protected void removeInstance( Entity e )
   {
      instances--;
   }

   protected void addtoComponents( Entity e )
   {
      for (int i = 0; i < components.length; i++)
      {
         handlers[components[i].id].postAdd( e );
      }
   }

   protected void removeFromComponents( Entity e )
   {
      for (int i = 0; i < components.length; i++)
      {
         handlers[components[i].id].preRemove( e );
      }
   }

   public int getInstances()
   {
      return instances;
   }

   @SuppressWarnings ("unchecked" )
   public <T> TemplateComponent<T> getTemplateComponent( Component<T> component )
   {
      return (TemplateComponent<T>)handlers[component.id];
   }

   public <T> boolean has( Component<T> component )
   {
      return componentBitSet.get( component.id );
   }

   public boolean hasComponents( BitSet components )
   {
      return componentBitSet.contains( components );
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
         if (!has( c ))
         {
            return false;
         }
      }
      return true;
   }
   
   protected TemplateComponent<?> getTemplateComponentSafe( Component<?> c )
   {
      return ( c.id >= handlers.length ? null : handlers[c.id] );
   }

   protected int indexOf( Component<?> component )
   {
      return component.id >= componentMap.length ? -1 : componentMap[component.id];
   }
   
   public <T> void add( Component<T> component )
   {
      ensureComponentFit( component.id );

      int i = indexOf( component );

      if (i != -1)
      {
         handlers[component.id].remove( this );
         components[i] = component;
      }
      else
      {
         i = components.length;
         componentBitSet.set( component.id );
         components = EntityUtility.append( components, component );
      }

      handlers[component.id] = component.add( this );
      componentMap[component.id] = i;
   }

   public <T> void addForEntity( Component<T> component, Entity e )
   {
      final int componentId = component.id;
      
      TemplateComponent<?> handler = null;
      
      ensureComponentFit( componentId );

      int i = indexOf( component );

      if (i != -1)
      {
         handler = handlers[componentId];
         handler.preRemove( e );
         handler.remove( this );
         components[i] = component;
      }
      else
      {
         i = components.length;
         componentBitSet.set( componentId );
         components = EntityUtility.append( components, component );
      }

      handler = component.add( this );
      handlers[componentId] = handler;
      componentMap[componentId] = i;
      component.postCustomAdd( e, this, handler );
      handler.postAdd( e );
   }

   public <T> void alias( Component<T> component, Component<T> alias )
   {
      ensureComponentFit( alias.id );

      handlers[alias.id] = handlers[component.id];
      componentMap[alias.id] = componentMap[component.id];
      componentBitSet.set( alias.id );
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
         if (!has( c ))
         {
            return false;
         }
      }
      return true;
   }

   public boolean hasControllers( BitSet controllers )
   {
      return controllerBitSet.contains( controllers );
   }

   protected int indexOf( Controller controller )
   {
      return controller.id >= controllerMap.length ? -1 : controllerMap[controller.id];
   }

   public void add( Controller controller )
   {
      if (!has( controller ))
      {
         ensureControllerFit( controller.id );
         controllerMap[controller.id] = controllers.length;
         controllerBitSet.set( controller.id );
         controllers = EntityUtility.append( controllers, controller );
      }
      else
      {
         controllers[indexOf( controller )] = controller;
      }
   }

   public boolean hasView()
   {
      return (view != null);
   }

   public View getView()
   {
      return view;
   }

   public boolean has( View v )
   {
      return (v == view) || (view != null && v != null && view.id == v.id);
   }

   public boolean hasExact( View view )
   {
      return this.view == view;
   }

   public void setView( View view )
   {
      this.view = view;
   }

   private void ensureComponentFit( int id )
   {
      if (handlers.length <= id)
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

   private void ensureControllerFit( int id )
   {
      if (controllerMap.length <= id)
      {
         int controllerMapSize = controllerMap.length;

         controllerMap = Arrays.copyOf( controllerMap, id + 1 );

         for (int i = controllerMapSize; i <= id; i++)
         {
            controllerMap[i] = -1;
         }
      }
   }

   public boolean isCustom()
   {
      return (id == CUSTOM);
   }

   public boolean isRelative( Template template )
   {
      return template.id != CUSTOM && extensionBitSet.get( template.id );
   }

   public boolean contains( Template template )
   {
      return controllerBitSet.contains( template.controllerBitSet ) &&
         componentBitSet.contains( template.componentBitSet ) &&
         has( template.view );
   }

   protected <T> Template addCustomComponent( Component<T> component, Entity e )
   {
      if (hasExact( component ))
      {
         return this;
      }

      Template t = getCustomTarget();
      t.addForEntity( component, e );

      return t;
   }

   protected <T> Template setCustomAlias( Component<T> component, Component<T> alias )
   {
      Template t = this;

      if (has( component ))
      {
         if (handlers[alias.id] != handlers[component.id])
         {
            t = getCustomTarget();
            t.alias( component, alias );
         }
      }

      return t;
   }

   protected Template addCustomController( Controller controller )
   {
      if (hasExact( controller ))
      {
         return this;
      }

      Template t = getCustomTarget();
      t.add( controller );

      return t;
   }

   protected Template setCustomView( View view )
   {
      if (hasExact( view ))
      {
         return this;
      }

      Template t = getCustomTarget();
      t.setView( view );

      return t;
   }

   protected Template getCustomTarget()
   {
      return isCustom() && instances <= 1 ? this : extend( CUSTOM, CUSTOM_NAME );
   }

   protected static BitSet getExtensionBitSet( Template t )
   {
      BitSet extensions = new BitSet();

      while (t != null)
      {
         if (t.id != CUSTOM)
         {
            extensions.set( t.id );
         }

         t = t.parent;
      }

      return extensions;
   }

}

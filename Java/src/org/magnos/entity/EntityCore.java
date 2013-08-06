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

import org.magnos.entity.util.IndexPool;


public class EntityCore
{

   private static final Controllers CONTROLLERS_NONE = new Controllers();

   private static final Components COMPONENTS_NONE = new Components();

   private static final View VIEW_NONE = null;

   protected static final int DEFAULT_INITIAL_CAPACITY = 64;

   protected static IdContainer<View> views = new IdContainer<View>( DEFAULT_INITIAL_CAPACITY );

   protected static IdContainer<Controller> controllers = new IdContainer<Controller>( DEFAULT_INITIAL_CAPACITY );

   protected static IdContainer<Template> templates = new IdContainer<Template>( DEFAULT_INITIAL_CAPACITY );

   protected static IdContainer<Component<?>> components = new IdContainer<Component<?>>( DEFAULT_INITIAL_CAPACITY );

   protected static IndexPool indices = new IndexPool();

   protected static EntityListener listener;

   protected EntityCore()
   {
   }

   protected static int popId()
   {
      return indices.pop();
   }

   protected static void pushId( int id )
   {
      indices.push( id );
   }

   protected static void register( Entity e )
   {
      if (listener != null)
      {
         listener.onEntityAdd( e );
      }
   }

   protected static void unregister( Entity e )
   {
      if (listener != null)
      {
         listener.onEntityRemove( e );
      }
   }

   public static View newView( String name )
   {
      return newView( name, null );
   }

   public static View newView( String name, Renderer defaultRenderer )
   {
      return registerView( true, views.addDefinition( new View( views.nextId(), name, defaultRenderer ) ) );
   }

   public static void setViewDefault( View view, Renderer defaultRenderer )
   {
      views.getDefinition( view ).renderer = defaultRenderer;
   }

   public static View newViewAlternative( View view, Renderer renderer )
   {
      return registerView( false, views.addInstance( new View( view.id, view.name, renderer ) ) );
   }

   private static View registerView( boolean definition, View view )
   {
      if (listener != null)
      {
         listener.onViewAdd( view, definition );
      }

      return view;
   }

   public static IdContainer<View> getViews()
   {
      return views;
   }

   public static Controller newController( String name )
   {
      return newController( name, null );
   }

   public static Controller newController( String name, Control defaultControl )
   {
      return registerController( true, controllers.addDefinition( new Controller( controllers.nextId(), name, defaultControl ) ) );
   }

   public static void setControllerDefault( Controller controller, Control defaultControl )
   {
      controllers.getDefinition( controller ).control = defaultControl;
   }

   public static Controller newControllerAlternative( Controller controller, Control control )
   {
      return registerController( false, controllers.addInstance( new Controller( controller.id, controller.name, control ) ) );
   }

   private static Controller registerController( boolean definition, Controller controller )
   {
      if (listener != null)
      {
         listener.onControllerAdd( controller, definition );
      }

      return controller;
   }

   public static IdContainer<Controller> getControllers()
   {
      return controllers;
   }

   public static <T> Component<T> newComponent( String name )
   {
      return registerComponent( true, components.addDefinition( new ComponentUndefined<T>( components.nextId(), name ) ) );
   }

   public static <T> Component<T> newComponent( String name, ComponentFactory<T> factory )
   {
      return registerComponent( true, components.addDefinition( new ComponentDistinct<T>( components.nextId(), name, factory ) ) );
   }

   public static <T> Component<T> newComponentAlternative( Component<T> component, ComponentFactory<T> factory )
   {
      return registerComponent( false, components.addInstance( new ComponentDistinct<T>( component.id, component.name, factory ) ) );
   }

   public static <T> Component<T> newComponentShared( String name, ComponentFactory<T> factory )
   {
      return registerComponent( true, components.addDefinition( new ComponentShared<T>( components.nextId(), name, factory ) ) );
   }

   public static <T> Component<T> newComponentSharedAlternative( Component<T> component, ComponentFactory<T> factory )
   {
      return registerComponent( false, components.addInstance( new ComponentShared<T>( component.id, component.name, factory ) ) );
   }

   public static <T> Component<T> newComponentDynamic( String name, DynamicValue<T> dynamic )
   {
      return registerComponent( true, components.addDefinition( new ComponentDynamic<T>( components.nextId(), name, dynamic ) ) );
   }

   public static <T> Component<T> newComponentDynamicAlternative( Component<T> component, DynamicValue<T> dynamic )
   {
      return registerComponent( false, components.addInstance( new ComponentDynamic<T>( component.id, component.name, dynamic ) ) );
   }

   public static <T> Component<T> newComponentGlobal( String name, T constant, boolean settable )
   {
      return registerComponent( true, components.addDefinition( new ComponentGlobal<T>( components.nextId(), name, constant, settable ) ) );
   }

   public static <T> Component<T> newComponentGlobalAlternative( Component<T> component, T constant, boolean settable )
   {
      return registerComponent( false, components.addInstance( new ComponentGlobal<T>( component.id, component.name, constant, settable ) ) );
   }

   public static <T> Component<T> newComponentAlias( Component<T> component, Component<T> alias )
   {
      return registerComponent( false, components.addInstance( new ComponentAlias<T>( alias.id, alias.name, component.id ) ) );
   }

   public static <T> Component<T> newComponentCustom( Component<T> custom )
   {
      return registerComponent( true, components.addDefinition( custom ) );
   }

   public static <T> Component<T> newComponentCustomAlternative( Component<T> custom )
   {
      return registerComponent( false, components.addInstance( custom ) );
   }

   private static <T> Component<T> registerComponent( boolean definition, Component<T> component )
   {
      if (listener != null)
      {
         listener.onComponentAdd( component, definition );
      }

      return component;
   }

   public static IdContainer<Component<?>> getComponents()
   {
      return components;
   }

   public static Template newTemplate()
   {
      return new Template();
   }

   public static Template newTemplate( String name, Components componentSet, Controllers controllerSet, View view )
   {
      return registerTemplate( templates.addDefinition( new Template( templates.nextId(), name, null, componentSet, controllerSet, view ) ) );
   }

   public static Template newTemplate( String name, Components componentSet, Controllers controllerSet )
   {
      return newTemplate( name, componentSet, controllerSet, VIEW_NONE );
   }

   public static Template newTemplate( String name, Components componentSet )
   {
      return newTemplate( name, componentSet, CONTROLLERS_NONE, VIEW_NONE );
   }

   public static Template newTemplate( String name )
   {
      return newTemplate( name, COMPONENTS_NONE, CONTROLLERS_NONE, VIEW_NONE );
   }

   public static Template newTemplate( Template base, String name, Components componentSet, Controllers controllerSet, View view )
   {
      Template t = templates.addDefinition( base.extend( templates.nextId(), name ) );

      for (Component<?> component : componentSet.values)
      {
         t.add( component );
      }

      for (Controller controller : controllerSet.values)
      {
         t.add( controller );
      }

      if (view != null)
      {
         t.setView( view );
      }

      return registerTemplate( t );
   }

   public static Template newTemplate( Template base, String name, Components componentSet, Controllers controllerSet )
   {
      return newTemplate( base, name, componentSet, controllerSet, VIEW_NONE );
   }

   public static Template newTemplate( Template base, String name, Components componentSet )
   {
      return newTemplate( base, name, componentSet, CONTROLLERS_NONE, VIEW_NONE );
   }

   public static Template newTemplate( Template base, String name )
   {
      return newTemplate( base, name, COMPONENTS_NONE, CONTROLLERS_NONE, VIEW_NONE );
   }

   public static Template newTemplate( String name, boolean overwrite, Template... templatesToMerge )
   {
      Template t = templates.addDefinition( templatesToMerge[0].extend( templates.nextId(), name ) );

      for (int i = 1; i < templatesToMerge.length; i++)
      {
         t.merge( templatesToMerge[i], overwrite );
      }

      return registerTemplate( t );
   }

   private static Template registerTemplate( Template template )
   {
      if (listener != null)
      {
         listener.onTemplateAdd( template );
      }

      return template;
   }

   public static IdContainer<Template> getTemplates()
   {
      return templates;
   }

   public static void clear()
   {
      if (listener != null)
      {
         listener.onCoreClear();
      }

      views.clear();
      controllers.clear();
      components.clear();
      templates.clear();
      indices.clear();
   }

}

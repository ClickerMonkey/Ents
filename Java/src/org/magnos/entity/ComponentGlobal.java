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

/**
 * A component that holds a single value that can be shared among any number of
 * Entities. The shared value is optionally modifiable, when enabled if one
 * Entity has the component set to some value all entities sharing the component
 * will see this value.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <T>
 *        The component value type.
 */
class ComponentGlobal<T> extends Component<T>
{

   /**
    * The global value shared between all Entities.
    */
   private T global;

   /**
    * Whether or not the constant is modifiable by
    * {@link Entity#set(Component, Object)}.
    */
   private final boolean settable;

   /**
    * A cached handler.
    */
   private final ComponentGlobalHandler handler;

   /**
    * Instantiates a ComponentGlobal.
    * 
    * @param id
    *        The id of the component.
    * @param name
    *        The name of the component.
    * @param constant
    *        The constant shared between all Entities that have this component.
    * @param settable
    *        Whether or not the constant can be changed when set is called on an
    *        Entity with this component. If true, all entities that have this
    *        component will see a change, otherwise the constant passed in will
    *        be the only value an Entity every sees.
    */
   protected ComponentGlobal( int id, String name, T constant, boolean settable )
   {
      super( id, name );

      this.global = constant;
      this.settable = settable;
      this.handler = new ComponentGlobalHandler();
   }

   @Override
   protected void postCustomAdd( Entity e, Template template, TemplateComponent<?> templateComponent )
   {

   }

   @Override
   protected TemplateComponent<T> add( Template template )
   {
      return handler;
   }

   private class ComponentGlobalHandler implements TemplateComponent<T>
   {

      @Override
      public void set( Entity e, T value )
      {
         if (settable)
         {
            global = value;
         }
      }

      @Override
      public T get( Entity e )
      {
         return global;
      }

      @Override
      public T take( Entity e, T target )
      {
         return global;
      }

      @Override
      public void remove( Template template )
      {

      }
      
      @Override
      public void postAdd( Entity e )
      {
         
      }
      
      @Override
      public void preRemove( Entity e )
      {
         
      }
   }

}

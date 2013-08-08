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
 * A component is added to an {@link Entity} (directly or via a {@link Template}
 * ) and it adds "value" to that Entity. The Entity can then get and set the
 * value of the component.
 * 
 * By default a component has no defined behavior, this is an abstract class
 * that when implemented will receive addition/removal requests to and from
 * Templates as well as set/get/add requests from an Entity.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <T>
 *        The component value type.
 */
public abstract class Component<T> extends Id
{

   /**
    * Instantiates a Component with the given id and name.
    * 
    * @param id
    *        The id of the component.
    * @param name
    *        The name of the component.
    */
   protected Component( int id, String name )
   {
      super( id, name );
   }

   /**
    * When a component is added to a template it returns a
    * {@link TemplateComponent} which is responsible for handling how a
    * components value is gotten or set for an entity. It also handles being
    * removed from the template.
    * 
    * @param template
    *        The template to add a component to.
    * @return A TemplateComponent implementation for this component.
    */
   protected abstract TemplateComponent<T> add( Template template );

   /**
    * The method invoked after a component is successfully dynamically added to
    * an {@link Entity} for the first time.
    * 
    * @param e
    *        The entity that has just had this component added to it.
    * @param template
    *        The template adding this component to the given entity.
    * @param templateComponent
    *        The TemplateComponent created by the {@link #add(Template)} method.
    */
   protected abstract void postCustomAdd( Entity e, Template template, TemplateComponent<?> templateComponent );

}

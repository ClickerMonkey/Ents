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
 * A component that does not have a definition and cannot be directly added to a
 * {@link Template}. This type of component would commonly be used to define
 * sets of components Entities should have in order to operate with some piece
 * of functionality. An entity will have an alternative of this component, but
 * never the component exactly.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <T>
 *        The component value type.
 */
class ComponentUndefined<T> extends Component<T>
{

   /**
    * Instantiates a new ComponentUndefined.
    * 
    * @param id
    *        The id of the component.
    * @param name
    *        The name of the component.
    */
   protected ComponentUndefined( int id, String name )
   {
      super( id, name );
   }

   @Override
   protected void postCustomAdd( Entity e, Template template, TemplateComponent<?> templateComponent )
   {

   }

   @Override
   protected TemplateComponent<T> add( Template template )
   {
      throw new RuntimeException( "An undefined component cannot be added to a Template" );
   }

}

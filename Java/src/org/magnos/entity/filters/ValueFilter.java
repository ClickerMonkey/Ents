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

package org.magnos.entity.filters;

import org.magnos.entity.Component;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityIterator;
import org.magnos.entity.util.EntityUtility;


/**
 * A filter that only returns entities that have a given component and their
 * component has a specific value.
 * 
 * @author Philip Diffenderfer
 * @see EntityIterator
 * 
 */
public class ValueFilter implements EntityFilter
{

   protected Object value;
   protected Component<?> component;

   /**
    * Instantiates a ValueFilter without a component and value. The
    * {@link #set(Component, Object)} method needs to be called, otherwise a
    * {@link NullPointerException} will be thrown.
    */
   public ValueFilter()
   {
   }

   /**
    * Instantiates a ValueFilter.
    * 
    * @param component
    *        The component the entity must have.
    * @param value
    *        The value of the given component the entity must have.
    */
   public <T> ValueFilter( Component<T> component, T value )
   {
      set( component, value );
   }

   /**
    * Resets and returns this filter by specifying the component and value to
    * filter by.
    * 
    * @param component
    *        The component the entity must have.
    * @param value
    *        The value of the given component the entity must have.
    * @return The reference to this filter.
    */
   public <T> ValueFilter set( Component<T> component, T value )
   {
      this.component = component;
      this.value = value;

      return this;
   }

   @Override
   public boolean isValid( Entity e )
   {
      Object entityValue = e.gets( component );

      return EntityUtility.equals( entityValue, value );
   }

}

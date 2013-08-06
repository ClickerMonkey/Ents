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

import org.magnos.entity.Entity;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityIterator;


/**
 * A filter that returns the opposite of what another filter would return.
 * 
 * @author Philip Diffenderfer
 * @see EntityIterator
 * 
 */
public class NotFilter implements EntityFilter
{

   protected EntityFilter filter;

   /**
    * 
    */
   public NotFilter()
   {
   }

   /**
    * 
    * @param filter
    */
   public NotFilter( EntityFilter filter )
   {
      set( filter );
   }

   /**
    * Resets the NotFilter specifying the filter to return the negation
    * (opposite) of.
    * 
    * @param filter
    *        The filter to return the negation (opposite) of.
    * @return The {@link Iterable} negation of the given filter.
    */
   public NotFilter set( EntityFilter filter )
   {
      this.filter = filter;

      return this;
   }

   @Override
   public boolean isValid( Entity e )
   {
      return !filter.isValid( e );
   }

}

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
 * A filter that combines two filters with AND logic.
 * 
 * @author Philip Diffenderfer
 * @see EntityIterator
 * 
 */
public class AndFilter implements EntityFilter
{

   protected EntityFilter first;
   protected EntityFilter second;

   /**
    * 
    */
   public AndFilter()
   {

   }

   /**
    * 
    * @param first
    * @param second
    */
   public AndFilter( EntityFilter first, EntityFilter second )
   {
      set( first, second );
   }

   /**
    * Resets the AndFilter specifying the filters to combine.
    * 
    * @param first
    *        The first filter to check for valid entities.
    * @param second
    *        The second filter to check for valid entities.
    * @return The {@link Iterable} filtered by both filters.
    */
   public AndFilter set( EntityFilter first, EntityFilter second )
   {
      this.first = first;
      this.second = second;

      return this;
   }

   @Override
   public boolean isValid( Entity e )
   {
      return first.isValid( e ) && second.isValid( e );
   }

}

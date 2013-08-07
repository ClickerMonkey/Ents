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
import org.magnos.entity.util.BitSet;


/**
 * A filter that only returns entities that have given components.
 * 
 * @author Philip Diffenderfer
 * @see EntityIterator
 * 
 */
public class ComponentFilter implements EntityFilter
{

   protected BitSet components;

   /**
    * 
    */
   public ComponentFilter()
   {
   }

   /**
    * 
    * @param components
    */
   public ComponentFilter( Component<?>... components )
   {
      set( components );
   }

   /**
    * Resets the ComponentFilter specifying the set of components to filter by.
    * 
    * @param components
    *        The set of components each entity returned by the filter will have.
    * @return The {@link Iterable} filter by components.
    */
   public ComponentFilter set( Component<?>... components )
   {
      this.components = new BitSet( components );

      return this;
   }

   @Override
   public boolean isValid( Entity e )
   {
      return e.hasComponents( components );
   }

}

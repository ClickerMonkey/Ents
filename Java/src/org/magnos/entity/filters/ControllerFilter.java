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

import org.magnos.entity.BitSet;
import org.magnos.entity.Controller;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityChain;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityList;


/**
 * A filter that only returns entities that have given controllers.
 * 
 * @author Philip Diffenderfer
 * @see EntityFilter
 * 
 */
public class ControllerFilter extends EntityFilter
{

   protected BitSet controllers;

   /**
    * Instantiates a new ControllerFilter.
    * 
    * @param defaultFilterCapacity
    *        The default capacity of this filter. The filter works by filling an
    *        array of entities that meet the filtering criteria. If the array is
    *        not large enough it resizes to 150% it's previous size.
    */
   public ControllerFilter( int defaultFilterCapacity )
   {
      super( defaultFilterCapacity );
   }

   /**
    * Resets the ControllerFilter specifying the root entity and the set of
    * controllers to filter by.
    * 
    * @param root
    *        The root entity to filter. This entity is typically an
    *        {@link EntityChain} or {@link EntityList} which both can contain
    *        any number of entities.
    * @param components
    *        The set of controllers each entity returned by the filter will
    *        have.
    * @return The {@link Iterable} filter by controllers.
    */
   public EntityFilter reset( Entity root, Controller... controllers )
   {
      this.controllers = new BitSet( controllers );

      return reset( root );
   }

   @Override
   public boolean isValid( Entity e )
   {
      return e.hasControllers( controllers );
   }

}

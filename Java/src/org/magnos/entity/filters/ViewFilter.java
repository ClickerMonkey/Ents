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
import org.magnos.entity.View;


/**
 * A filter that only returns entities that have a given view.
 * 
 * @author Philip Diffenderfer
 * @see EntityIterator
 * 
 */
public class ViewFilter implements EntityFilter
{

   protected View view;

   /**
    * 
    */
   public ViewFilter()
   {

   }

   /**
    * 
    * @param controllers
    */
   public ViewFilter( View view )
   {
      set( view );
   }

   /**
    * Resets the ControllerFilter specifying the set of controllers to filter
    * by.
    * 
    * @param components
    *        The set of controllers each entity returned by the filter will
    *        have.
    * @return The {@link Iterable} filter by controllers.
    */
   public ViewFilter set( View view )
   {
      this.view = view;

      return this;
   }

   @Override
   public boolean isValid( Entity e )
   {
      return e.has( view );
   }

}

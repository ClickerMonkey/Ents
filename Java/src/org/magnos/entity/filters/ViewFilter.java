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
    * Instantiates a ViewFilter without a view. The {@link #set(View)} method
    * needs to be called, otherwise a {@link NullPointerException} will be
    * thrown.
    */
   public ViewFilter()
   {

   }

   /**
    * Instantiates a ViewFilter.
    * 
    * @param view
    *        The view each entity returned by the filter will have.
    */
   public ViewFilter( View view )
   {
      set( view );
   }

   /**
    * Resets and returns this filter by specifying the view an entity must have.
    * 
    * @param view
    *        The view each entity returned by the filter will have.
    * @return The reference to this filter.
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

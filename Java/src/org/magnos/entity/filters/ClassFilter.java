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
import org.magnos.entity.EntityChain;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityIterator;
import org.magnos.entity.EntityLayers;
import org.magnos.entity.EntityList;


/**
 * A filter that returns Entities of a specific class.
 * 
 * @author Philip Diffenderfer
 * @see EntityIterator
 * 
 */
public class ClassFilter implements EntityFilter
{

   protected Class<? extends Entity> entityClass;

   /**
    * Instantiates a ClassFilter with a class. The {@link #set(Class)} method
    * needs to be called, otherwise the filter will find no entities valid.
    */
   public ClassFilter()
   {
   }

   /**
    * Instantiates a new ClassFilter.
    * 
    * @param entityClass
    *       The class of the entities to return. 
    * @see Entity
    * @see EntityChain
    * @see EntityList
    * @see EntityLayers
    */
   public ClassFilter( Class<? extends Entity> entityClass )
   {
      set( entityClass );
   }

   /**
    * Resets the ClassFilter specifying the Entity class.
    * 
    * @param entityClass
    *        The class of the entities to return.
    * @return The reference to this filter. 
    * @see Entity
    * @see EntityChain
    * @see EntityList
    * @see EntityLayers
    */
   public ClassFilter set( Class<? extends Entity> entityClass )
   {
      this.entityClass = entityClass;

      return this;
   }

   @Override
   public boolean isValid( Entity e )
   {
      return e.getClass() == entityClass;
   }

}

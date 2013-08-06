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
import org.magnos.entity.EntityList;


/**
 * A filter that returns Entities of a specific class.
 * 
 * @author Philip Diffenderfer
 * @see EntityFilter
 * 
 */
public class ClassFilter extends EntityFilter
{

   protected Class<? extends Entity> entityClass;

   /**
    * Resets the ClassFilter specifying the root entity and the Entity class.
    * 
    * @param root
    *        The root entity to filter. This entity is typically an
    *        {@link EntityChain} or {@link EntityList} which both can contain
    *        any number of entities.
    * @param entityClass
    *        The class to filter entities by.
    * @return The {@link Iterable} filtered by class.
    */
   public EntityFilter reset( Entity root, Class<? extends Entity> entityClass )
   {
      return reset( entityClass ).reset( root );
   }

   /**
    * Resets the ClassFilter specifying the Entity class.
    * 
    * @param entityClass
    *        The class to filter entities by.
    * @return The {@link Iterable} filtered by class.
    */
   public EntityFilter reset( Class<? extends Entity> entityClass )
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

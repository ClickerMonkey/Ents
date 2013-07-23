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

import java.util.Arrays;
import java.util.Iterator;


/**
 * An EntityFilter is an {@link Iterable} object that accumulates all Entities
 * that the implementation deems valid into an array which can easily be
 * traversed through an {@link Iterator} or manually through {@link #size()} and
 * {@link #get(int)}.
 * 
 * To populate a filter the reset method needs to be called of the implementing
 * class. This will query the root Entity to add an internal Entities it may
 * have into the filter.
 * 
 * The only current problem with this filtering technique is storing all the
 * valid entities in an internal array (temporarily). That means if there are
 * 10,000 entities and a filter finds all of them valid, it will have an
 * internal array >= 10,000. To get around this problem the stop() method should
 * be used to stop iteration or after iteration to clean up entities referenced
 * in the internal array AND you should also avoid allocating these filters
 * during game play, rather create them in the beginning and reuse them as much
 * as possible.
 * 
 * @author Philip Diffenderfer
 * 
 */
public abstract class EntityFilter implements Iterator<Entity>, Iterable<Entity>
{

   private Entity[] stack;
   private int stackSize;
   private int stackCursor;

   /**
    * Instantiates a new EntityFilter.
    * 
    * @param defaultFilterCapacity
    *        The default capacity of this filter. The filter works by filling an
    *        array of entities that meet the filtering criteria. If the array is
    *        not large enough it resizes to 150% it's previous size.
    */
   public EntityFilter( int defaultFilterCapacity )
   {
      this.stack = new Entity[defaultFilterCapacity];
      this.stackSize = 0;
      this.stackCursor = 0;
   }

   /**
    * Determines whether the given Entity is valid. If true this Entity is added
    * to the internal array and may be iterated over with the
    * {@link #iterator()} method.
    * 
    * @param e
    *        The entity to validate.
    * @return True if the entity belongs in this filter, otherwise false.
    */
   public abstract boolean isValid( Entity e );

   /**
    * The method an entity uses to push itself into a filter.
    * 
    * @param e
    *        The entity to attempt to push.
    */
   protected void push( Entity e )
   {
      if (isValid( e ))
      {
         stack[stackSize++] = e;
      }
   }

   /**
    * Prepares the filter for the likely addition of entityCount entities
    * (before validation and pushing occurs).
    * 
    * @param entityCount
    *        The number of entities that may be pushed into the filter.
    */
   protected void prepare( int entityCount )
   {
      int desired = stackSize + entityCount;

      if (desired > stack.length)
      {
         int suggested = stack.length + (stack.length >> 1);

         stack = Arrays.copyOf( stack, Math.max( suggested, desired ) );
      }
   }

   /**
    * @return The number of entities in the filter.
    */
   public int size()
   {
      return stackSize;
   }

   /**
    * Returns the entity at the given position.
    * 
    * @param i
    *        The index of the entity, this should be >= 0 and < {@link #size()}.
    * @return The entity at the given index.
    */
   public Entity get( int i )
   {
      return stack[i];
   }

   /**
    * Stops the filter by clearing the internal array and setting the size and
    * iteration cursor to zero.
    */
   public void stop()
   {
      while (--stackSize >= 0)
      {
         stack[stackSize] = null;
      }

      stackCursor = stackSize = 0;
   }

   /**
    * Resets this filter by stopping it and filtering it with all valid entities
    * found within the root Entity passed in.
    * 
    * @param root
    *        The root entity to fill this filter from.
    * @return The reference to this filter.
    */
   public EntityFilter reset( Entity root )
   {
      stop();

      root.fill( this );

      return this;
   }

   /**
    * Resets this filter to the beginning of the filtered entities.
    * 
    * @return The reference to this filter.
    */
   public EntityFilter reset()
   {
      stackCursor = 0;

      return this;
   }

   /**
    * Returns the reference to this iterator, used when this filter is used in
    * for-each loops.
    */
   public Iterator<Entity> iterator()
   {
      return this;
   }

   @Override
   public boolean hasNext()
   {
      return (stackCursor < stackSize);
   }

   @Override
   public Entity next()
   {
      return stack[stackCursor++];
   }

   /**
    * Removes the last entity from the filter by expiring it.
    */
   @Override
   public void remove()
   {
      stack[stackCursor - 1].expire();
   }

}

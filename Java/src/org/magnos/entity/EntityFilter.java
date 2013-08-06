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

   private static final int DEFAULT_MAX_DEPTH = 16;

   private Entity root;
   private Entity[] stack;
   private Entity curr;
   private Entity prev;
   private int[] offset;
   private int depth;

   /**
    * 
    */
   public EntityFilter()
   {
      this( DEFAULT_MAX_DEPTH );
   }

   /**
    * Instantiates a new EntityFilter.
    * 
    * @param defaultMaxDepth
    */
   public EntityFilter( int defaultMaxDepth )
   {
      this.stack = new Entity[defaultMaxDepth];
      this.offset = new int[defaultMaxDepth];
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
    * Stops the filter by clearing the internal array and setting the size and
    * iteration cursor to zero.
    */
   public void stop()
   {
      depth = -1;
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
      this.root = root;
      this.reset();

      return this;
   }

   /**
    * Resets this filter to the beginning of the filtered entities.
    * 
    * @return The reference to this filter.
    */
   public EntityFilter reset()
   {
      depth = 0;
      offset[0] = -1;
      stack[0] = root;

      prev = null;
      curr = isValid( root ) ? root : findNext();

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
      return (depth != -1);
   }

   @Override
   public Entity next()
   {
      prev = curr;
      curr = findNext();
      return prev;
   }

   /**
    * Removes the last entity from the filter by expiring it.
    */
   @Override
   public void remove()
   {
      prev.expire();
   }

   /**
    * Finds the next valid entity, returns null if there are no valid entities.
    * 
    * @return The reference to the next valid entity, otherwise false.
    */
   private Entity findNext()
   {
      if (offset[0] == root.getEntitySize())
      {
         return null;
      }

      Entity current = null;
      boolean found = false;

      while (!found)
      {
         current = stack[depth];
         int size = current.getEntitySize();
         int skip = current.getEntityIndex();
         int id = offset[depth] + 1;

         // Search through current entity's children for a valid entity.
         while (id < size && (id == skip || !isValid( current.getEntity( id ) )))
         {
            id++;
         }

         // If the end of the entity has been reached, pop the previous entity off the stack.
         if (id == size)
         {
            depth--;

            // If it's -1 then stop searching.
            if (depth == -1)
            {
               current = null;
               found = true;
            }
         }
         else
         {
            // Update offset in current entity, and grab it.
            offset[depth] = id;
            current = current.getEntity( id );

            // Only traverse entities that contain other entities.
            if (current.getEntitySize() > 1)
            {
               depth++;

               // If next depth has max'd out the stack, increase it.
               if (depth == stack.length)
               {
                  stack = Arrays.copyOf( stack, depth + DEFAULT_MAX_DEPTH );
                  offset = Arrays.copyOf( offset, depth + DEFAULT_MAX_DEPTH );
               }

               // Push entity on stack for traversal
               stack[depth] = current;
               offset[depth] = -1;
            }

            found = true;
         }
      }

      return current;
   }

}

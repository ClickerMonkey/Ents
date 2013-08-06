
package org.magnos.entity.util;

import java.util.Arrays;


/**
 * A utility classes that returns indices (consecutive number of integers
 * starting at zero) through a {@link #pop()} method. Once an index is done
 * being used it is passed to {@link #push(int)} so it can be recycled and
 * reused the next time {@link #pop()} is called. When the last index
 * {@link #pop()}'d is {@link #push(int)}'d it causes the pool to shrink by
 * removing all previous consecutive indices from the recycled index list and
 * updates the maximum index that can be returned (accessible via
 * {@link #maxIndex()}).
 * 
 * @author Philip Diffenderfer
 * 
 */
public class IndexPool
{

   public static final int MIN_CAPACITY = 32;

   private int[] ints;
   private int size;
   private int previousIndex;

   /**
    * Instantiates a new IndexPool.
    */
   public IndexPool()
   {
      this( MIN_CAPACITY );
   }

   /**
    * Instantiates a new IndexPool with a given capacity.
    * 
    * @param initialCapacity
    *        The initial number of indices to store for reuse before the array
    *        used to store recycled indices needs to be resized.
    */
   public IndexPool( int initialCapacity )
   {
      ints = new int[initialCapacity];
      clear();
   }

   /**
    * Retrieves an index from the pool. If a reusable index is stored in the
    * pool, it is used. Otherwise a new index is created.
    * 
    * @return A new index that is not currently in use.
    */
   public int pop()
   {
      return (size == 0 ? ++previousIndex : ints[--size]);
   }

   /**
    * Adds the index back to the pool so it can reused at a later time. Every
    * {@link #pop()}'d index needs to be {@link #push(int)}'d back one once it's
    * done being used.
    * 
    * @param index
    *        The index to push back on the pool.
    * @return True if the pool has shrunk and reusable indices have been removed
    *         from the pool to keep the values returned by {@link #pop()} at their
    *         smallest possible values, otherwise false.
    */
   public boolean push( int index )
   {
      boolean shrank = (index == previousIndex);
      
      // Last index popped? Try shrinking the pool.
      if (shrank)
      {
         shrink();
      }
      else
      {
         // Increase backing pool by 50% if it's full.
         if (size == ints.length)
         {
            ints = Arrays.copyOf( ints, size + (size >> 1) );
         }

         ints[size++] = index;
      }
      
      return shrank;
   }

   /**
    * Clears the IndexPool of all indices and resets the next index to 0.
    */
   public void clear()
   {
      size = 0;
      previousIndex = -1;
   }

   /**
    * The number of recycled indices available to be returned from
    * {@link #pop()} before new indices need to be generated.
    * 
    * @return The size of the pool.
    */
   public int size()
   {
      return size;
   }

   /**
    * The current capacity of the pool of recycled indices. The pool expands and
    * shrinks
    * as indices are {@link #pop()}'d and {@link #push(int)}'d.
    * 
    * @return The current capacity of the pool.
    */
   public int capacity()
   {
      return ints.length;
   }

   /**
    * The maximum active index this pool has returned from {@link #pop()}. An
    * index is not active when it has been {@link #push(int)}'d onto the pool.
    * The maximum active index can shrink when it's {@link #push(int)}'d onto
    * the pool.
    * 
    * @return The maximum active index.
    */
   public int maxIndex()
   {
      return previousIndex;
   }

   /**
    * Method invoked when {@link #push(int)} is called on the last
    * {@link #pop()}'d index. This will traverse through the stack of indices
    * and remove the largest descending consecutive sequence of indices starting
    * with the last index. If it's shrunk such that 75% of the stack is empty,
    * the capacity of the stack is decreased by 25%.
    */
   private void shrink()
   {
      final int greatestIndex = previousIndex - 1;
      int lastIndex = greatestIndex;
      int removeAt = size;

      // Find the largest descending consecutive sequence of indices starting with lastIndex
      // and remove them from the pool by setting their values to -1;
      while (removeAt != -1)
      {
         // Find the index of lastIndex in the pool (or -1 if not found).
         while (--removeAt >= 0 && ints[removeAt] != lastIndex)
            ;

         // If lastIndex was found in the pool...
         if (removeAt != -1)
         {
            // Mark it for removal
            ints[removeAt] = -1;

            // Find the previous index
            lastIndex--;
            removeAt = size;

            // All indices have been popped off the stack.
            if (lastIndex == -1)
            {
               break;
            }
         }
      }

      // If any additional indices have been popped off the stack...
      if (lastIndex != greatestIndex)
      {
         int alive = 0;

         // Overwrite all indices that are marked for removal (-1)
         for (int i = 0; i < size; i++)
         {
            if (ints[i] != -1)
            {
               ints[alive++] = ints[i];
            }
         }

         // Reset size.
         size = alive;

         // Shrink backing array by 25% if 75% is free.
         int capacity25 = ints.length >> 2;
         int desiredCapacity = ints.length - capacity25;

         if (capacity25 >= size && desiredCapacity < MIN_CAPACITY)
         {
            ints = Arrays.copyOf( ints, desiredCapacity );
         }
      }

      previousIndex = lastIndex;
   }

}

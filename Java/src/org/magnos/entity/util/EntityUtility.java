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

package org.magnos.entity.util;

import java.util.Arrays;

import org.magnos.entity.Id;


/**
 * A class with static utility methods.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class EntityUtility
{

   /**
    * Adds the element to the beginning of the array and returns the new array.
    * 
    * @param element
    *        The element to add to the beginning of the array.
    * @param array
    *        The array to add the element to.
    * @return The reference to the new array starting with the element.
    */
   public static <T> T[] prepend( T element, T[] array )
   {
      array = Arrays.copyOf( array, array.length + 1 );
      System.arraycopy( array, 0, array, 1, array.length - 1 );
      array[0] = element;
      return array;
   }

   /**
    * Adds the element to the end of the array and returns a new array.
    * 
    * @param array
    *        The array to add the element to.
    * @param element
    *        The element to add to the end of the array.
    * @return The reference to the new array ending with the element.
    */
   public static <T> T[] append( T[] array, T element )
   {
      array = Arrays.copyOf( array, array.length + 1 );
      array[array.length - 1] = element;
      return array;
   }

   /**
    * Removes the element at the index from the array and returns a new array.
    * 
    * @param array
    *        The array to remove an element from.
    * @param index
    *        The index of the element to remove.
    * @return The reference to a new array without the element at the index.
    */
   public static <T> T[] removeAt( T[] array, int index )
   {
      System.arraycopy( array, index + 1, array, index, array.length - index - 1 );
      return Arrays.copyOf( array, array.length - 1 );
   }

   /**
    * Sequentially searches through the array for an element with the same
    * reference and returns it index. If the element is not found, -1 is
    * returned.
    * 
    * @param array
    *        The array to search through.
    * @param search
    *        The element to search for.
    * @return The first index of the element in the array, unless the element is
    *         not found then -1 is returned.
    */
   public static <T> int indexOfSame( T[] array, T search )
   {
      for (int i = 0; i < array.length; i++)
      {
         if (array[i] == search)
         {
            return i;
         }
      }

      return -1;
   }

   /**
    * Returns the maximum id that exists in the array of {@link Id}s.
    * 
    * @param ids
    *        The {@link Id}s to search through.
    * @return The maximum id, or -1 if the array is empty.
    */
   public static <I extends Id> int getMaxId( I[] ids )
   {
      int max = -1;

      for (int i = 0; i < ids.length; i++)
      {
         max = Math.max( max, ids[i].id );
      }

      return max;
   }

   /**
    * Creates an index map given the array of {@link Id}s. An index map
    * has an index at {@link Id#id} that points to the index of the {@link Id}
    * in the given array. All other indices in the map that don't point to an
    * {@link Id} in the array are -1.
    * 
    * @param ids
    *        The array of {@link Id}s to create a map from.
    * @return An index map for the array of {@link Id}s.
    */
   public static <I extends Id> int[] createMap( I[] ids )
   {
      int[] map = new int[getMaxId( ids ) + 1];

      Arrays.fill( map, -1 );

      for (int i = 0; i < ids.length; i++)
      {
         map[ids[i].id] = i;
      }

      return map;
   }

   /**
    * Determines whether the two objects are equal. Two objects are considered
    * equal if they point to the same exact object (reference comparison), are
    * both null, or are both non-null and {@link Object#equals(Object)} returns
    * true.
    * 
    * @param a
    *        The first object to check for equality.
    * @param b
    *        The second object to check for equality.
    * @return True if the two objects are equal, otherwise false.
    */
   public static boolean equals( Object a, Object b )
   {
      return (a == b || (a != null && b != null && a.equals( b )));
   }

   /**
    * Determines whether two arrays are equal. Two arrays are considered equal
    * if they point to the same exact object (reference comparison), are both
    * null, or both are the same length and have elements that are all equal
    * using the {@link #equals(Object, Object)} method.
    * 
    * @param a
    *        The first array to check for equality.
    * @param b
    *        The second array to check for equality.
    * @return True if the two arrays are equal, otherwise false.
    */
   public static <T> boolean equals( T[] a, T[] b )
   {
      if (a == b)
      {
         return true;
      }
      if (a == null || b == null)
      {
         return false;
      }
      if (a.length != b.length)
      {
         return false;
      }
      for (int i = 0; i < a.length; i++)
      {
         if (!equals( a[i], b[i] ))
         {
            return false;
         }
      }
      return true;
   }

}

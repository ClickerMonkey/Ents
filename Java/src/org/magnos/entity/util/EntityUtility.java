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


public class EntityUtility
{

   public static <T> T[] prepend( T element, T[] array )
   {
      array = Arrays.copyOf( array, array.length + 1 );
      System.arraycopy( array, 0, array, 1, array.length - 1 );
      array[0] = element;
      return array;
   }

   public static <T> T[] append( T[] array, T element )
   {
      array = Arrays.copyOf( array, array.length + 1 );
      array[array.length - 1] = element;
      return array;
   }

   public static <T> T[] removeAt( T[] array, int index )
   {
      System.arraycopy( array, index + 1, array, index, array.length - index - 1 );
      return Arrays.copyOf( array, array.length - 1 );
   }

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

   public static <T> T[] remove( T[] array, T item, int max )
   {
      if (max <= 0)
      {
         return array;
      }

      for (int i = 0; i < array.length; i++)
      {
         if (equals( array[i], item ))
         {
            array = removeAt( array, i-- );

            if (--max <= 0)
            {
               break;
            }
         }
      }

      return array;
   }

   public static <I extends Id> int getMaxId( I[] ids )
   {
      int max = -1;

      for (int i = 0; i < ids.length; i++)
      {
         max = Math.max( max, ids[i].id );
      }

      return max;
   }

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

   public static boolean equals( Object a, Object b )
   {
      return (a == b || (a != null && b != null && a.equals( b )));
   }

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

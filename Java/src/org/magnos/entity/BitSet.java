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

/**
 * An extension to {@link java.util.BitSet} adding new constructors for
 * EntityCore as well as a containment method that doesn't require needless
 * allocation.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class BitSet extends java.util.BitSet
{

   private static final long serialVersionUID = 1L;

   /**
    * Instantiates an empty BitSet.
    */
   public BitSet()
   {
   }

   /**
    * Instantiates a pre-sized BitSet.
    * 
    * @param size
    *        The initial size of the BitSet in bits.
    */
   public BitSet( int size )
   {
      super( size );
   }

   /**
    * Instantiates a BitSet given an array of bit indices to set to true.
    * 
    * @param indices
    *        The bit indices to set to true.
    */
   public BitSet( int... indices )
   {
      super( indices.length );

      for (int i = 0; i < indices.length; i++)
      {
         set( indices[i] );
      }
   }

   /**
    * Instantiates a BitSet that uses an array of Id to set bits true. The bits
    * set to true based on the "id" of the Id passed in.
    * 
    * @param ids
    *        The array of Id.
    */
   public BitSet( Id... ids )
   {
      super( ids.length );

      for (int i = 0; i < ids.length; i++)
      {
         set( ids[i].id );
      }
   }

   /**
    * Returns whether this BitSet has at least all on-bits that the other BitSet
    * has.
    * 
    * @param other
    *        The other BitSet to check for containment.
    * @return True if this BitSet contains true for every bit the other BitSet
    *         does.
    */
   public boolean contains( BitSet other )
   {
      int i = -1;

      while ((i = other.nextSetBit( i + 1 )) >= 0)
      {
         if (!get( i ))
         {
            return false;
         }
      }

      return true;
   }

}

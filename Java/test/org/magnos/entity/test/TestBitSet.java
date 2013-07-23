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

package org.magnos.entity.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.magnos.entity.BitSet;


public class TestBitSet
{

   @Test
   public void testEmptyConstructor()
   {
      BitSet a = new BitSet();

      assertEquals( 64, a.size() ); // Java version starts out with one 64-bit integer
      assertEquals( 0, a.length() );
      assertFalse( a.get( 0 ) );
   }

   @Test
   public void testIntConstructor()
   {
      BitSet a = new BitSet( 0, 1, 4, 5 );

      assertEquals( 64, a.size() ); // Java version uses 64-bit integers
      assertTrue( a.get( 0 ) );
      assertTrue( a.get( 1 ) );
      assertFalse( a.get( 2 ) );
      assertFalse( a.get( 3 ) );
      assertTrue( a.get( 4 ) );
      assertTrue( a.get( 5 ) );
      assertFalse( a.get( 6 ) );
   }

   @Test
   public void testSet()
   {
      BitSet a = new BitSet();

      assertFalse( a.get( 0 ) );

      a.set( 0, true );

      assertTrue( a.get( 0 ) );

      a.set( 0, false );

      assertFalse( a.get( 0 ) );

      a.set( 0 );

      assertTrue( a.get( 0 ) );
      assertFalse( a.get( 345 ) );

      a.set( 345 );

      assertTrue( a.get( 345 ) );
   }

   @Test
   public void testIntersects()
   {
      BitSet a = new BitSet( 1, 2, 4, 5 );
      BitSet b = new BitSet( 1, 6 );
      BitSet c = new BitSet( 0, 3, 6 );

      assertTrue( a.intersects( b ) );
      assertFalse( a.intersects( c ) );
      assertTrue( b.intersects( c ) );
   }

   @Test
   public void testContains()
   {
      BitSet a = new BitSet( 1, 2, 3, 4, 6, 7 );
      BitSet b = new BitSet( 2, 3 );
      BitSet c = new BitSet( 1, 2, 5 );

      assertTrue( a.contains( b ) );
      assertFalse( a.contains( c ) );
      assertFalse( b.contains( a ) );
      assertFalse( c.contains( a ) );
   }

}

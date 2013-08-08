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

package org.magnos.entity.vals;

import org.magnos.entity.ComponentValueFactory;


/**
 * A mutable wrapper around a long that should replace the usage of the
 * {@link java.lang.Long}. Using {@link java.lang.Long} should be avoided, the
 * "invisible" autoboxing and unboxing can cause a significant performance hit
 * in games when used inappropriately.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class LongVal implements ComponentValueFactory<LongVal>
{

   public long v;

   public LongVal()
   {
   }

   public LongVal( long v )
   {
      this.v = v;
   }

   public LongVal( LongVal iv )
   {
      this.v = iv.v;
   }

   @Override
   public LongVal create()
   {
      return new LongVal( v );
   }

   @Override
   public LongVal clone( LongVal value )
   {
      return new LongVal( value );
   }

   @Override
   public LongVal copy( LongVal from, LongVal to )
   {
      to.v = from.v;

      return to;
   }

   @Override
   public int hashCode()
   {
      return (int)(v ^ (v >>> 32));
   }

   @Override
   public boolean equals( Object obj )
   {
      return (obj instanceof LongVal) && ((LongVal)obj).v == v;
   }

   @Override
   public String toString()
   {
      return String.valueOf( v );
   }

}

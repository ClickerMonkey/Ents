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

package org.magnos.entity.test.helper;

import org.magnos.entity.ComponentValueFactory;


public class Vector implements ComponentValueFactory<Vector>
{

   public float x, y;

   public Vector()
   {
   }

   public Vector( Vector v )
   {
      set( v.x, v.y );
   }

   public Vector( float x, float y )
   {
      set( x, y );
   }

   public void set( float vx, float vy )
   {
      x = vx;
      y = vy;
   }

   @Override
   public Vector create()
   {
      return copy( this, new Vector() );
   }

   @Override
   public Vector clone( Vector value )
   {
      return copy( value, new Vector() );
   }

   @Override
   public Vector copy( Vector from, Vector to )
   {
      to.x = from.x;
      to.y = from.y;

      return to;
   }

   public void addsi( Vector v, float s )
   {
      x += v.x * s;
      y += v.y * s;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + Float.floatToIntBits( x );
      result = prime * result + Float.floatToIntBits( y );
      return result;
   }

   @Override
   public boolean equals( Object obj )
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Vector other = (Vector)obj;
      if (Float.floatToIntBits( x ) != Float.floatToIntBits( other.x ))
         return false;
      if (Float.floatToIntBits( y ) != Float.floatToIntBits( other.y ))
         return false;
      return true;
   }

   @Override
   public String toString()
   {
      return "Vector [x=" + x + ", y=" + y + "]";
   }

}

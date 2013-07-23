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

public class EntityChain extends Entity
{

   protected Entity first;
   protected Entity last;
   protected boolean inheritVisible;
   protected boolean inheritEnabled;

   public EntityChain()
   {
      this( EntityCore.newTemplate(), null, null, false, false );
   }

   public EntityChain( Template template )
   {
      this( template, null, null, false, false );
   }

   public EntityChain( Entity first, Entity last, boolean inheritVisible, boolean inheritEnabled )
   {
      this( EntityCore.newTemplate(), first, last, inheritVisible, inheritEnabled );
   }

   public EntityChain( Template template, Entity first, Entity last, boolean inheritVisible, boolean inheritEnabled )
   {
      super( template );

      this.first = first;
      this.last = last;
      this.inheritEnabled = inheritEnabled;
      this.inheritVisible = inheritVisible;
   }

   protected EntityChain( Template template, Object[] values )
   {
      super( template, values );
   }

   @Override
   public void draw( Object drawState )
   {
      if (visible || !inheritVisible)
      {
         if (first != null)
         {
            first.draw( drawState );
         }

         super.draw( drawState );

         if (last != null)
         {
            last.draw( drawState );
         }
      }
   }

   @Override
   public void update( Object updateState )
   {
      if (enabled || !inheritEnabled)
      {
         if (first != null)
         {
            first.update( updateState );
         }

         super.update( updateState );

         if (last != null)
         {
            last.update( updateState );
         }
      }
   }

   @Override
   protected void fill( EntityFilter filter )
   {
      filter.prepare( 3 );

      if (first != null)
      {
         first.fill( filter );
      }

      filter.push( this );

      if (last != null)
      {
         last.fill( filter );
      }
   }

   @Override
   public Entity clone( boolean deep )
   {
      EntityChain clone = new EntityChain( template, template.createClonedValues( values, deep ) );

      if (first != null)
      {
         clone.first = deep ? first.clone( deep ) : first;
      }
      if (last != null)
      {
         clone.last = deep ? last.clone( deep ) : last;
      }

      return clone;
   }

}

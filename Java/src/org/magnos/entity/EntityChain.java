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

   protected EntityChain( Template template, Object[] values, Renderer renderer )
   {
      super( template, values, renderer );
   }

   @Override
   public boolean delete()
   {
      boolean deletable = super.delete();

      if (deletable)
      {
         if (validateFirst())
         {
            first.delete();
         }

         if (validateLast())
         {
            last.delete();
         }
      }

      return deletable;
   }

   @Override
   public void draw( Object drawState )
   {
      if (visible || !inheritVisible)
      {
         if (validateFirst())
         {
            first.draw( drawState );
         }

         super.draw( drawState );
         
         if (validateLast())
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
         if (validateFirst())
         {
            first.update( updateState );
         }

         super.update( updateState );

         if (validateLast())
         {
            last.update( updateState );
         }
      }
   }

   @Override
   protected int getEntitySize()
   {
      final boolean f = (first != null);
      final boolean l = (last != null);

      return (f & l ? 3 : (f | l ? 2 : 1));
   }

   @Override
   protected Entity getEntity( int index )
   {
      final boolean f = (first != null);

      switch (index)
      {
      case 0:
         return (f ? first : this);
      case 1:
         return (f ? this : last);
      case 2:
         return last;
      }

      return this;
   }

   @Override
   protected int getEntityIndex()
   {
      final boolean f = (first != null);

      return (f ? 1 : 0);
   }

   @Override
   public EntityChain clone( boolean deep )
   {
      EntityChain clone = cloneState( new EntityChain( template, template.createClonedValues( values, deep ), renderer ) );

      clone.inheritEnabled = inheritEnabled;
      clone.inheritVisible = inheritVisible;

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

   private boolean validateFirst()
   {
      if (first != null && first.isExpired())
      {
         first.delete();
         first = null;
      }

      return (first != null);
   }

   private boolean validateLast()
   {
      if (last != null && last.isExpired())
      {
         last.delete();
         last = null;
      }

      return (last != null);
   }

   public Entity getFirst()
   {
      return first;
   }

   public void setFirst( Entity first )
   {
      this.first = first;
   }

   public Entity getLast()
   {
      return last;
   }

   public void setLast( Entity last )
   {
      this.last = last;
   }

   public boolean isInheritVisible()
   {
      return inheritVisible;
   }

   public void setInheritVisible( boolean inheritVisible )
   {
      this.inheritVisible = inheritVisible;
   }

   public boolean isInheritEnabled()
   {
      return inheritEnabled;
   }

   public void setInheritEnabled( boolean inheritEnabled )
   {
      this.inheritEnabled = inheritEnabled;
   }

}

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
import java.util.Collection;
import java.util.Iterator;


public class EntityList extends Entity
{

   public static int DEFAULT_CAPACITY = 16;

   protected Entity[] entities = {};
   protected int entityCount = 0;

   public EntityList()
   {
      this( EntityCore.newTemplate(), DEFAULT_CAPACITY );
   }

   public EntityList( int initialCapacity )
   {
      this( EntityCore.newTemplate(), initialCapacity );
   }

   public EntityList( Entity... entities )
   {
      this( EntityCore.newTemplate(), entities.length );

      this.add( entities );
   }

   public EntityList( Template template, Entity... entities )
   {
      this( template, entities.length );

      this.add( entities );
   }

   public EntityList( Template template, int initialCapacity )
   {
      super( template );

      this.entities = new Entity[initialCapacity];
   }

   protected EntityList( Template template, Object[] values )
   {
      super( template, values );
   }

   protected void onEntityAdd( Entity e, int index )
   {
   }

   protected void onEntityRemove( Entity e, int index )
   {
   }

   protected void onEntityUpdated( Entity e, int index, Object updateState )
   {
   }

   public void pad( int count )
   {
      if (entityCount + count >= entities.length)
      {
         int nextCapacity = entities.length + (entities.length >> 1);
         int minimumCapacity = entityCount + count;

         entities = Arrays.copyOf( entities, Math.max( nextCapacity, minimumCapacity ) );
      }
   }

   private void internalAdd( Entity entity )
   {
      entities[entityCount] = entity;

      onEntityAdd( entity, entityCount );

      entityCount++;
   }

   public void add( Entity entity )
   {
      pad( 1 );
      internalAdd( entity );
   }

   public void add( Entity... entityArray )
   {
      pad( entityArray.length );

      for (int i = 0; i < entityArray.length; i++)
      {
         internalAdd( entityArray[i] );
      }
   }

   public void addRange( Entity[] entityArray, int from, int to )
   {
      pad( to - from );

      while (from < to)
      {
         internalAdd( entityArray[from++] );
      }
   }

   public void addAll( EntityList list )
   {
      addRange( list.entities, 0, list.entityCount );
   }

   public void addAll( Collection<Entity> entityCollection )
   {
      pad( entityCollection.size() );

      for (Entity e : entityCollection)
      {
         internalAdd( e );
      }
   }

   public void addAll( Iterator<Entity> iterator )
   {
      while (iterator.hasNext())
      {
         add( iterator.next() );
      }
   }

   public void addAll( Iterable<Entity> iterable )
   {
      addAll( iterable.iterator() );
   }

   public void clean()
   {
      int alive = 0;

      for (int i = 0; i < entityCount; i++)
      {
         final Entity e = entities[i];

         if (e.isExpired())
         {
            onEntityRemove( e, i );
         }
         else
         {
            entities[alive++] = e;
         }
      }

      while (entityCount > alive)
      {
         entities[--entityCount] = null;
      }
   }

   @Override
   public void draw( Object drawState )
   {
      super.draw( drawState );

      for (int i = 0; i < entityCount; i++)
      {
         entities[i].draw( drawState );
      }
   }

   @Override
   public void update( Object updateState )
   {
      super.update( updateState );

      if (isExpired())
      {
         return;
      }

      for (int i = 0; i < entityCount; i++)
      {
         final Entity e = entities[i];

         e.update( updateState );

         onEntityUpdated( e, i, updateState );
      }

      this.clean();
   }

   public int size()
   {
      return entityCount;
   }

   public Entity getEntity( int index )
   {
      return entities[index];
   }

   @Override
   public Entity clone( boolean deep )
   {
      EntityList clone = new EntityList( template, template.createClonedValues( values, deep ) );

      clone.pad( entityCount );

      if (deep)
      {
         for (int i = 0; i < entityCount; i++)
         {
            clone.internalAdd( entities[i].clone( deep ) );
         }
      }
      else
      {
         System.arraycopy( entities, 0, clone.entities, 0, entityCount );

         clone.entityCount = entityCount;
      }

      return clone;
   }

   @Override
   protected void fill( EntityFilter filter )
   {
      filter.prepare( entityCount + 1 );
      filter.push( this );

      for (int i = 0; i < entityCount; i++)
      {
         entities[i].fill( filter );
      }
   }

}

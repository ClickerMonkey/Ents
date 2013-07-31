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
 *              Open Software License (OSL 3.0)
 */

package org.magnos.entity;


/**
 * 
 * @author Philip Diffenderfer
 *
 */
public class EntityLayers extends Entity
{

   protected final EntityList[] layers;
   
   public <E extends Enum<E>> EntityLayers(Class<E> enumClass )
   {
      this( EntityCore.newTemplate(), enumClass.getEnumConstants().length );
   }
   
   public EntityLayers(int layerCount)
   {
      this( EntityCore.newTemplate(), layerCount );
   }
   
   public <E extends Enum<E>> EntityLayers(Template template, Class<E> enumClass )
   {
      this( template, enumClass.getEnumConstants().length );
   }
   
   public EntityLayers(Template template, int layerCount)
   {
      this( template, template.createDefaultValues(), template.createRenderer(), layerCount );
   }
   
   protected EntityLayers( Template template, Object[] values, Renderer renderer, int layerCount )
   {
      super( template, values, renderer );
      
      layers = new EntityList[ layerCount ];
      
      for (int i = 0; i < layerCount; i++)
      {
         layers[i] = new EntityList();
      }
   }
   
   public EntityList layer(int index)
   {
      return layers[index];
   }
   
   public <E extends Enum<E>> EntityList layer(E enumConstant)
   {
      return layers[enumConstant.ordinal()];
   }
   
   public void add(int layerIndex, Entity e)
   {
      layers[layerIndex].add( e );
   }
   
   public <E extends Enum<E>> void add(E layerIndex, Entity e)
   {
      layers[layerIndex.ordinal()].add( e );
   }
   
   public void swap(int i, int j)
   {
      EntityList temp = layers[i];
      layers[i] = layers[j];
      layers[j] = temp;
   }
   
   public <E extends Enum<E>> void swap(E i, E j)
   {
      swap( i.ordinal(), j.ordinal() );
   }

   @Override
   public void draw( Object drawState )
   {
      super.draw( drawState );
      
      for (int i = 0; i < layers.length; i++)
      {
         final EntityList list = layers[i];
         
         if (!list.isExpired())
         {
            list.draw( drawState );
         }
      }
   }

   @Override
   public void update( Object updateState )
   {
      super.update( updateState );
      
      for (int i = 0; i < layers.length; i++)
      {
         final EntityList list = layers[i];
         
         if (!list.isExpired())
         {
            list.update( updateState );
         }
      }
   }

   @Override
   protected void fill( EntityFilter filter )
   {
      filter.prepare( layers.length + 1 );
      
      filter.push( this );
      
      for (int i = 0; i < layers.length; i++)
      {
         layers[i].fill( filter );
      }
   }

   @Override
   public EntityLayers clone( boolean deep )
   {
      EntityLayers clone = new EntityLayers( template, template.createClonedValues( layers, deep ), renderer, layers.length );
      
      clone.controllerEnabled.clear();
      clone.controllerEnabled.or( controllerEnabled );
      clone.enabled = enabled;
      clone.expired = expired;
      clone.visible = visible;
      
      if (deep)
      {
         for (int i = 0; i < layers.length; i++)
         {
            clone.layers[i] = layers[i].clone( deep );
         }
      }
      else
      {
         System.arraycopy( layers, 0, clone.layers, 0, layers.length );
      }
      
      return clone;
   }
   
}

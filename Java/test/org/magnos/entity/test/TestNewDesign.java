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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Test;
import org.magnos.entity.Component;
import org.magnos.entity.ComponentValueFactory;
import org.magnos.entity.Components;
import org.magnos.entity.Controllers;
import org.magnos.entity.DynamicValue;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityCore;
import org.magnos.entity.Renderer;
import org.magnos.entity.Template;
import org.magnos.entity.View;
import org.magnos.entity.test.helper.Bounds;
import org.magnos.entity.test.helper.Vector;
import org.magnos.entity.vals.FloatVal;


public class TestNewDesign
{

   @AfterClass
   public static void afterTest()
   {
      EntityCore.clear();
   }

   public static interface CollisionHandler
   {

      void handleCollision( Entity a, Entity b, float time );
   }

   public static class Shape implements ComponentValueFactory<Shape>
   {

      public Shape create()
      {
         return new Shape();
      }

      public Shape clone( Shape value )
      {
         return new Shape();
      }

      @Override
      public Shape copy( Shape from, Shape to )
      {
         return to;
      }
   }

   public static class CollisionHandlerDefault implements CollisionHandler, ComponentValueFactory<CollisionHandler>
   {

      public CollisionHandler create()
      {
         return this;
      }

      public CollisionHandler clone( CollisionHandler value )
      {
         return this;
      }

      public CollisionHandler copy( CollisionHandler from, CollisionHandler to )
      {
         return to;
      }

      public void handleCollision( Entity a, Entity b, float time )
      {

      }
   }

   public static class ViewDefault implements Renderer
   {
      public void draw( Entity e, Object drawState )
      {

      }

      public Renderer create( Entity e )
      {
         return this;
      }

      public void destroy( Entity e )
      {
         
      }
   }

   @Test
   public void test()
   {
      final Component<Vector> POSITION = EntityCore.newComponent( "position", new Vector() );
      final Component<FloatVal> RADIUS = EntityCore.newComponent( "radius", new FloatVal() );
      final Component<CollisionHandler> COLLISION_HANDLER = EntityCore.newComponent( "collision-callback" ); // by default, there is no collision handler
      final Component<Shape> SHAPE = EntityCore.newComponent( "collision-shape" ); // by default, there is no collision shape on an entity
      final Component<Bounds> BOUNDS = EntityCore.newComponent( "bounds" ); // by default, an entity has a bounds component 

      final Component<CollisionHandler> COLLISION_HANDLER_SHIP = EntityCore.newComponentSharedAlternative( COLLISION_HANDLER, new CollisionHandlerDefault() );
      final Component<CollisionHandler> COLLISION_HANDLER_ASTEROID = EntityCore.newComponentSharedAlternative( COLLISION_HANDLER, new CollisionHandlerDefault() );

      final Component<Shape> SHAPE_SHIP = EntityCore.newComponentSharedAlternative( SHAPE, new Shape() );
      final Component<Shape> SHAPE_ASTEROID = EntityCore.newComponentSharedAlternative( SHAPE, new Shape() );

      final Component<Bounds> BOUNDS_DYNAMIC = EntityCore.newComponentDynamicAlternative( BOUNDS, new DynamicValue<Bounds>() {

         public Bounds get( Entity e )
         {
            return take( e, new Bounds() );
         }

         public void set( Entity e, Bounds target )
         {

         }

         @Override
         public Bounds take( Entity e, Bounds target )
         {
            Vector p = e.get( POSITION );
            float r = e.get( RADIUS ).v;
            target.left = p.x - r;
            target.right = p.x + r;
            target.top = p.y - r;
            target.bottom = p.y + r;
            return target;
         }
      } );

      final View SHIP_VIEW = EntityCore.newView( "ship", new ViewDefault() );
      final View ASTEROID_VIEW = EntityCore.newView( "asteroid", new ViewDefault() );

      final Template SHIP = EntityCore.newTemplate( "ship", new Components( POSITION, RADIUS, COLLISION_HANDLER_SHIP, SHAPE_SHIP, BOUNDS_DYNAMIC ), new Controllers(), SHIP_VIEW );
      final Template ASTEROID = EntityCore.newTemplate( "asteroid", new Components( POSITION, RADIUS, COLLISION_HANDLER_ASTEROID, SHAPE_ASTEROID, BOUNDS_DYNAMIC ), new Controllers(), ASTEROID_VIEW );

      assertTrue( SHIP.has( SHAPE ) );
      assertTrue( SHIP.has( BOUNDS ) );
      assertTrue( SHIP.has( COLLISION_HANDLER ) );
      assertTrue( SHIP.has( POSITION ) );

      assertTrue( ASTEROID.has( SHAPE ) );
      assertTrue( ASTEROID.has( BOUNDS ) );
      assertTrue( ASTEROID.has( COLLISION_HANDLER ) );
      assertTrue( ASTEROID.has( POSITION ) );

      Entity e = new Entity( SHIP );
      e.get( POSITION ).x = 1.0f;
      e.get( POSITION ).y = 2.0f;

      Bounds b = e.get( BOUNDS );

      assertEquals( 1.0f, b.left, 0.00001f );
      assertEquals( 1.0f, b.right, 0.00001f );
      assertEquals( 2.0f, b.top, 0.00001f );
      assertEquals( 2.0f, b.bottom, 0.00001f );

      e.get( RADIUS ).v = 2.0f;
      e.take( BOUNDS, b );

      assertEquals( -1.0f, b.left, 0.00001f );
      assertEquals( 3.0f, b.right, 0.00001f );
      assertEquals( 0.0f, b.top, 0.00001f );
      assertEquals( 4.0f, b.bottom, 0.00001f );

      Entity n = e.clone( true );
      assertEquals( 2.0f, n.get( RADIUS ).v, 0.00001f );
      assertEquals( 1.0f, n.get( POSITION ).x, 0.00001f );
      assertEquals( 2.0f, n.get( POSITION ).y, 0.00001f );
   }

}

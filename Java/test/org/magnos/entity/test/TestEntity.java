
package org.magnos.entity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.AfterClass;
import org.junit.Test;
import org.magnos.entity.Component;
import org.magnos.entity.Components;
import org.magnos.entity.Control;
import org.magnos.entity.Controller;
import org.magnos.entity.Controllers;
import org.magnos.entity.DynamicValue;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityCore;
import org.magnos.entity.Renderer;
import org.magnos.entity.Template;
import org.magnos.entity.View;
import org.magnos.entity.vals.FloatVal;
import org.magnos.entity.vals.IntVal;


public class TestEntity
{

   public static final float EPSILON = 0.000001f;

   @AfterClass
   public static void afterTest()
   {
      EntityCore.clear();
   }

   // @formatter:off
   static DynamicValue<FloatVal> DYNAMIC_CENTER = new DynamicValue<FloatVal>() {
      public FloatVal get( Entity e ) {
         return take( e, new FloatVal() );
      }
      public void set( Entity e, FloatVal value ) {
         FloatVal r = e.get( RIGHT );
         FloatVal l = e.get( LEFT );
         float halfwidth = (r.v - l.v) * 0.5f;
         r.v = value.v + halfwidth;
         l.v = value.v - halfwidth;
      }
      public FloatVal take( Entity e, FloatVal target ) {
         FloatVal r = e.get( RIGHT );
         FloatVal l = e.get( LEFT );
         target.v = (l.v + r.v) * 0.5f;
         return target;
      }
   };

   static Control CONTROL_MOTION = new Control() {
      public void control( Entity e, Object updateState ) {
         float dt = (Float)updateState;
         float dist = e.get( SPEED ).v * dt;
         e.get( LEFT ).v += dist;
         e.get( RIGHT ).v += dist;
      }
   };
   
   static Control CONTROL_UPDATES = new Control() {
      public void control( Entity e, Object updateState ) {
         e.get( UPDATE_COUNT ).v++;
      }
   };
   
   static Renderer RENDERER_EXTENT = new Renderer() {
      public void draw( Entity e, Object drawState ) {
         String graphics = drawState.toString();
         System.out.println( "Drawing extent at {" + e.get( LEFT ) + "->" + e.get( RIGHT ) + "} with " + graphics );
      }
      public void destroy( Entity e ) { }
      public Renderer create( Entity e ) { return this; }
   };
   
   static Renderer RENDERER_DRAWS = new Renderer() {
      public void draw( Entity e, Object drawState ) {
         e.get( DRAW_COUNT ).v++;
      }
      public void destroy( Entity e ) { }
      public Renderer create( Entity e ) { return this; }
   };
   
   static Component<FloatVal>   LEFT            = EntityCore.newComponent( "left", new FloatVal() );
   static Component<FloatVal>   RIGHT           = EntityCore.newComponent( "right", new FloatVal() );
   static Component<FloatVal>   SPEED           = EntityCore.newComponent( "speed", new FloatVal() );
   static Component<FloatVal>   CENTER          = EntityCore.newComponentDynamic( "center", DYNAMIC_CENTER );
   static Controller            MOTION          = EntityCore.newController( "motion", CONTROL_MOTION );
   static Controller            NETWORKING      = EntityCore.newController( "networking" );
   static View                  EXTENT_VIEW     = EntityCore.newView( "extent-view", RENDERER_EXTENT );
   static Template              EXTENT          = EntityCore.newTemplate( "extent", new Components(LEFT, RIGHT, CENTER), new Controllers(MOTION), EXTENT_VIEW );
   
   static Component<IntVal>     DRAW_COUNT      = EntityCore.newComponent( "draw-count", new IntVal() );
   static View                  DRAWS_VIEW      = EntityCore.newView( "draws-view", RENDERER_DRAWS );
   static Template              DRAWS           = EntityCore.newTemplate( "draws", new Components(DRAW_COUNT), Controllers.NONE, DRAWS_VIEW );
   
   static Component<IntVal>     UPDATE_COUNT    = EntityCore.newComponent( "update-count", new IntVal() );
   static Controller            UPDATES_CONTROL = EntityCore.newController( "updates-control", CONTROL_UPDATES );
   static Template              UPDATES         = EntityCore.newTemplate( "updates", new Components(UPDATE_COUNT), new Controllers(UPDATES_CONTROL) );
   
   // @formatter:on

   @Test
   public void testConstructor()
   {
      Entity e = new Entity( EXTENT );

      assertTrue( e.isVisible() );
      assertTrue( e.isEnabled() );
      assertFalse( e.isExpired() );
      assertFalse( e.isCustom() );

      assertTrue( e.has( LEFT ) );
      assertTrue( e.has( RIGHT ) );
      assertTrue( e.has( CENTER ) );
      assertFalse( e.has( SPEED ) );
      assertTrue( e.has( MOTION ) );
      assertTrue( e.has( EXTENT_VIEW ) );

      e.delete();
   }

   @Test
   public void testCustomEntity()
   {
      Entity e = new Entity();

      assertTrue( e.isCustom() );
      assertFalse( e.has( LEFT ) );

      e.add( LEFT );

      assertTrue( e.has( LEFT ) );

      e.get( LEFT ).v = 3.0f;

      assertEquals( 3.0f, e.get( LEFT ).v, EPSILON );

      e.delete();
   }

   @Test
   public void testCustomEntityDefined()
   {
      Entity e = new Entity( LEFT );

      assertTrue( e.has( LEFT ) );
      assertFalse( e.has( RIGHT ) );
      assertFalse( e.has( MOTION ) );

      e.delete();
   }

   @Test
   public void testToString()
   {
      // Non-custom
      Entity e0 = new Entity( EXTENT );
      e0.get( LEFT ).v = 3.0f;
      e0.get( RIGHT ).v = 5.5f;
      assertEquals( "[left=3.0,right=5.5,center=4.25]", e0.toString() );

      // Custom empty
      Entity e1 = new Entity();
      assertEquals( "[]", e1.toString() );

      // Custom non-empty
      Entity e2 = new Entity( LEFT );
      e2.get( LEFT ).v = 2.0f;
      assertEquals( "[left=2.0]", e2.toString() );

      // Custom from template
      Entity e3 = new Entity( EXTENT );
      e3.get( LEFT ).v = 3.0f;
      e3.get( RIGHT ).v = 5.5f;
      e3.grab( SPEED ).v = -1.0f;
      assertEquals( "[left=3.0,right=5.5,center=4.25,speed=-1.0]", e3.toString() );

      e0.delete();
      e1.delete();
      e2.delete();
      e3.delete();
   }

   @Test
   public void testGet()
   {
      Entity e = new Entity( EXTENT );

      FloatVal left = e.get( LEFT );
      FloatVal right = e.get( RIGHT );

      left.v = 3.0f;
      right.v = 5.5f;

      assertSame( left, e.get( LEFT ) );
      assertSame( right, e.get( RIGHT ) );
      assertEquals( 3.0f, e.get( LEFT ).v, EPSILON );
      assertEquals( 5.5f, e.get( RIGHT ).v, EPSILON );

      e.delete();
   }

   @Test
   public void testDynamic()
   {
      Entity e = new Entity( EXTENT );

      e.get( LEFT ).v = 2.0f;
      e.get( RIGHT ).v = 3.0f;

      FloatVal center = e.get( CENTER );

      assertEquals( 2.5f, center.v, EPSILON );

      e.delete();
   }

   @Test
   public void testGetSafe()
   {
      Entity e = new Entity( EXTENT );

      assertFalse( e.has( SPEED ) );

      FloatVal speed0 = e.gets( SPEED );

      assertNull( speed0 );

      FloatVal speed1 = e.gets( SPEED, new FloatVal( Float.NaN ) );

      assertNotNull( speed1 );
      assertTrue( Float.isNaN( speed1.v ) );

      e.delete();
   }

   @Test
   public void testTake()
   {
      final FloatVal left = new FloatVal( 0.0f );
      final FloatVal right = new FloatVal( 0.0f );

      Entity e = new Entity( LEFT );

      e.get( LEFT ).v = 2.0f;

      FloatVal leftTaken = e.takes( LEFT, left );
      assertSame( left, leftTaken );
      assertNotSame( left, e.gets( LEFT ) );
      assertEquals( 2.0f, left.v, EPSILON );

      FloatVal rightTaken = e.takes( RIGHT, right );
      assertNull( rightTaken );
      assertNotSame( right, e.gets( RIGHT ) );
      assertEquals( 0.0f, right.v, EPSILON );

      e.delete();
   }

   @Test
   public void testHasComponents()
   {
      Entity e = new Entity( EXTENT );

      assertTrue( e.has( LEFT ) );
      assertTrue( e.has( RIGHT ) );
      assertTrue( e.has( CENTER ) );
      assertFalse( e.has( SPEED ) );
      assertTrue( e.has( LEFT, RIGHT, CENTER ) );
      assertFalse( e.has( LEFT, RIGHT, CENTER, SPEED ) );

      e.delete();
   }

   @Test
   public void testHasController()
   {
      Entity e = new Entity( EXTENT );

      assertTrue( e.has( MOTION ) );
      assertFalse( e.has( NETWORKING ) );
      assertFalse( e.has( MOTION, NETWORKING ) );

      e.delete();
   }

   @Test
   public void testAdd()
   {
      Entity e = new Entity( new Components( LEFT ), new Controllers( MOTION ) );

      assertTrue( e.has( LEFT ) );
      assertFalse( e.has( RIGHT ) );

      e.get( LEFT ).v = 2.0f;

      assertTrue( e.add( RIGHT ) );
      assertTrue( e.has( RIGHT ) );

      e.get( RIGHT ).v = 3.4f;

      assertEquals( 2.0f, e.get( LEFT ).v, EPSILON );
      assertEquals( 3.4f, e.get( RIGHT ).v, EPSILON );
      assertTrue( e.isCustom() );

      assertFalse( e.add( LEFT ) );
      assertFalse( e.add( RIGHT ) );
      assertTrue( e.add( SPEED ) );

      e.get( SPEED ).v = 3.0f;

      assertEquals( 3.0f, e.get( SPEED ).v, EPSILON );

      e.delete();
   }

   @Test
   public void testExpireAndDelete()
   {
      assertEquals( 0, EXTENT.getInstances() );

      Entity e = new Entity( EXTENT );

      assertEquals( 1, EXTENT.getInstances() );

      assertFalse( e.isExpired() );
      assertNotNull( e.getTemplate() );
      assertNotNull( e.getRenderer() );

      e.expire();

      assertTrue( e.isExpired() );
      
      assertNotNull( e.getTemplate() );
      assertNotNull( e.getRenderer() );
      
      e.delete();
      
      assertNull( e.getTemplate() );
      assertNull( e.getRenderer() );

      assertEquals( 0, EXTENT.getInstances() );

      e.delete();

      assertTrue( e.isExpired() );
      assertNull( e.getTemplate() );
      assertNull( e.getRenderer() );

      assertEquals( 0, EXTENT.getInstances() );
   }

   @Test
   public void testVisible()
   {
      Entity e = new Entity( EXTENT );

      assertTrue( e.isVisible() );

      e.hide();

      assertFalse( e.isVisible() );

      e.show();

      assertTrue( e.isVisible() );

      e.delete();
   }

   @Test
   public void testVisibleDraw()
   {
      Entity e = new Entity( DRAWS );

      assertEquals( 0, e.get( DRAW_COUNT ).v );

      e.draw( null );

      assertEquals( 1, e.get( DRAW_COUNT ).v );

      e.hide();
      e.draw( null );

      assertEquals( 1, e.get( DRAW_COUNT ).v );

      e.show();
      e.draw( null );

      assertEquals( 2, e.get( DRAW_COUNT ).v );

      e.delete();
   }

   @Test
   public void testSetView()
   {
      Entity e = new Entity( DRAWS );

      assertTrue( e.hasView() );
      assertTrue( e.hasRenderer() );
      assertSame( DRAWS_VIEW, e.getView() );
      assertFalse( e.isCustom() );

      e.setView( null );

      assertTrue( e.isCustom() );
      assertFalse( e.hasView() );
      assertFalse( e.hasRenderer() );
      assertNull( e.getView() );
      assertNull( e.getRenderer() );

      e.delete();
   }

   @Test
   public void testSetRenderer()
   {
      Entity e = new Entity();

      assertTrue( e.isCustom() );
      assertFalse( e.hasRenderer() );
      assertNull( e.getRenderer() );

      final AtomicBoolean flagCreated = new AtomicBoolean( false );
      final AtomicBoolean flagDestroyed = new AtomicBoolean( false );

      e.setRenderer( new Renderer() {

         public Renderer create( Entity e )
         {
            flagCreated.set( true );
            return this;
         }

         public void draw( Entity e, Object drawState )
         {

         }

         public void destroy( Entity e )
         {
            flagDestroyed.set( true );
         }
      } );

      assertTrue( flagCreated.get() );
      assertFalse( flagDestroyed.get() );

      assertTrue( e.hasRenderer() );

      e.setRenderer( null );

      assertFalse( e.hasRenderer() );
      assertNull( e.getRenderer() );
      assertTrue( flagDestroyed.get() );

      e.delete();
   }

   @Test
   public void testEnabled()
   {
      Entity e = new Entity( EXTENT );

      assertTrue( e.isEnabled() );

      e.disable();

      assertFalse( e.isEnabled() );

      e.enable();

      assertTrue( e.isEnabled() );

      e.delete();
   }

   @Test
   public void testEnabledUpdate()
   {
      Entity e = new Entity( UPDATES );

      assertEquals( 0, e.get( UPDATE_COUNT ).v );

      e.update( null );

      assertEquals( 1, e.get( UPDATE_COUNT ).v );

      e.disable();
      e.update( null );

      assertEquals( 1, e.get( UPDATE_COUNT ).v );

      e.enable();
      e.update( null );

      assertEquals( 2, e.get( UPDATE_COUNT ).v );

      e.delete();
   }

   @Test
   public void testControllerEnabled()
   {
      Entity e = new Entity( UPDATES );

      assertEquals( 0, e.get( UPDATE_COUNT ).v );

      assertTrue( e.isControllerEnabled( UPDATES_CONTROL ) );

      e.update( null );

      assertEquals( 1, e.get( UPDATE_COUNT ).v );

      e.disable( UPDATES_CONTROL );
      assertFalse( e.isControllerEnabled( UPDATES_CONTROL ) );

      e.update( null );

      assertEquals( 1, e.get( UPDATE_COUNT ).v );

      e.enable( UPDATES_CONTROL );
      e.update( null );

      assertTrue( e.isControllerEnabled( UPDATES_CONTROL ) );

      assertEquals( 2, e.get( UPDATE_COUNT ).v );

      e.delete();
   }

   @Test
   public void testAddController()
   {
      Entity e = new Entity( UPDATE_COUNT );

      assertFalse( e.has( UPDATES_CONTROL ) );
      assertEquals( 0, e.get( UPDATE_COUNT ).v );
      
      e.update( null );
      
      assertEquals( 0, e.get( UPDATE_COUNT ).v );
      
      e.add( UPDATES_CONTROL );

      assertTrue( e.has( UPDATES_CONTROL ) );
      assertTrue( e.isControllerEnabled( UPDATES_CONTROL ) );
      
      e.update( null );
      
      assertEquals( 1, e.get( UPDATE_COUNT ).v );
      
      e.delete();
   }
   
   @Test
   public void testCloneDeep()
   {
      Entity e0 = new Entity( EXTENT );
      e0.get( LEFT ).v = 2.0f;
      e0.get( RIGHT ).v = 3.7f;
      e0.hide();
      
      Entity e1 = e0.clone( true );
      
      assertSame( e0.getTemplate(), e1.getTemplate() );
      assertNotSame( e0.get( LEFT ), e1.get( LEFT ) );
      assertNotSame( e0.get( RIGHT ), e1.get( RIGHT ) );
      assertFalse( e1.isVisible() );
      
      assertEquals( 2.0f, e1.get( LEFT ).v, EPSILON );
      assertEquals( 3.7f, e1.get( RIGHT ).v, EPSILON );
      assertEquals( e0, e1 );
      
      e1.delete();
      e0.delete();
   }

   @Test
   public void testCloneShallow()
   {
      Entity e0 = new Entity( EXTENT );
      e0.get( LEFT ).v = 2.0f;
      e0.get( RIGHT ).v = 3.7f;
      e0.hide();
      
      Entity e1 = e0.clone( false );
      
      assertSame( e0.getTemplate(), e1.getTemplate() );
      assertSame( e0.get( LEFT ), e1.get( LEFT ) );
      assertSame( e0.get( RIGHT ), e1.get( RIGHT ) );
      assertFalse( e1.isVisible() );
      
      assertEquals( 2.0f, e1.get( LEFT ).v, EPSILON );
      assertEquals( 3.7f, e1.get( RIGHT ).v, EPSILON );
      assertEquals( e0, e1 );
      
      e1.delete();
      e0.delete();
   }
   
}

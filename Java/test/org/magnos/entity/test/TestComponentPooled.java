
package org.magnos.entity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.AfterClass;
import org.junit.Test;
import org.magnos.entity.ComponentPooled;
import org.magnos.entity.Entity;
import org.magnos.entity.Ents;
import org.magnos.entity.Template;
import org.magnos.entity.test.helper.Vector;


public class TestComponentPooled
{

   public static final float EPSILON = 0.000001f;

   @AfterClass
   public static void afterTest()
   {
      Ents.clear();
   }

   static ComponentPooled<Vector> POSITION = (ComponentPooled<Vector>)Ents.newComponentPooled( "position", new Vector() );
   static Template POSITIONED = Ents.newTemplate( "positioned", POSITION );

   @Test
   public void testGet()
   {
      Entity e = new Entity( POSITIONED );
      Vector p = e.get( POSITION );

      assertNotNull( p );

      e.delete();
   }

   @Test
   public void testSet()
   {
      Entity e = new Entity( POSITIONED );

      assertEquals( 0, POSITION.pool().size() );

      // overwrite, recycling the original value
      e.set( POSITION, new Vector( 4.0f, 5.0f ) );

      // it has been recycled
      assertEquals( 1, POSITION.pool().size() );

      // delete it to notify the component that the entity is gone and may have a value
      e.delete();

      // pool size doesn't increase since the value on the deleted entity was not created by POSITION
      assertEquals( 1, POSITION.pool().size() );
   }

   @Test
   public void testSuccessiveCreation()
   {
      Entity e0 = new Entity( POSITIONED );
      Vector p0 = e0.get( POSITION );
      p0.set( 0.4f, 0.5f );
      e0.delete();

      Entity e1 = new Entity( POSITIONED );
      Vector p1 = e1.get( POSITION );

      assertSame( p0, p1 );
      assertEquals( 0.0f, p1.x, EPSILON );
      assertEquals( 0.0f, p1.y, EPSILON );

      e1.delete();
   }

   @Test
   public void testSuccessiveClone()
   {
      Entity e0 = new Entity( POSITIONED );
      Vector p0 = e0.get( POSITION );
      p0.set( 0.4f, 0.5f );

      Entity e1 = e0.clone( true );
      Vector p1 = e1.get( POSITION );

      assertNotSame( p0, p1 );
      assertEquals( 0.4f, p1.x, EPSILON );
      assertEquals( 0.5f, p1.y, EPSILON );

      e0.delete();

      Entity e2 = e1.clone( true );
      Vector p2 = e2.get( POSITION );

      assertSame( p0, p2 );
      assertEquals( 0.4f, p2.x, EPSILON );
      assertEquals( 0.5f, p2.y, EPSILON );

      e2.delete();
      e1.delete();
   }

   @Test
   public void testCustom()
   {
      Entity e = new Entity();
      Vector p = e.grab( POSITION );
      p.set( 4.0f, 2.5f );
      e.expire();
   }

}


package org.magnos.entity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import static org.magnos.entity.filters.Filters.*;

import org.junit.AfterClass;
import org.junit.Test;
import org.magnos.entity.Component;
import org.magnos.entity.ComponentFactoryNull;
import org.magnos.entity.Entity;
import org.magnos.entity.Ents;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityIterator;
import org.magnos.entity.EntityList;
import org.magnos.entity.Template;
import org.magnos.entity.test.helper.Vector;
import org.magnos.entity.vals.FloatVal;


public class TestEntityFilter
{

   @AfterClass
   public static void afterTest()
   {
      Ents.clear();
   }

   static Component<String> NAME = Ents.newComponent( "name", new ComponentFactoryNull<String>() );
   static Component<Vector> POSITION = Ents.newComponent( "position", new Vector( 0f, 0f ) );
   static Component<Vector> VELOCITY = Ents.newComponent( "velocity", new Vector( 0f, 0f ) );
   static Component<FloatVal> RADIUS = Ents.newComponent( "radius", new FloatVal( 0f ) );
   static Component<FloatVal> SCALE = Ents.newComponent( "scale", new FloatVal( 0f ) );

   static Template BASIC_OBJECT = Ents.newTemplate( "basic", NAME, POSITION, VELOCITY, RADIUS );
   static Template BASICER_OBJECT = Ents.newTemplate( "basic'er", POSITION, VELOCITY );

   @Test
   public void testDefaultFilter()
   {
      testFilter( all(), 0, 1, 2, 3, 4 );
   }

   @Test
   public void testVisible()
   {
      testFilter( visible(), 0, 1, 3, 4 );
   }

   @Test
   public void testEnabled()
   {
      testFilter( enabled(), 0, 1, 2, 3 );
   }

   @Test
   public void testCustom()
   {
      testFilter( custom(), 0, 3 );
   }

   @Test
   public void testNot()
   {
      testFilter( not(all()) /* nothing */);
      testFilter( not(visible()), 2 );
      testFilter( not(enabled()), 4 );
      testFilter( not(custom()), 1, 2, 4 );
   }

   @Test
   public void testAnd()
   {
      testFilter( and( custom(), visible() ), 0, 3 );
   }

   @Test
   public void testOr()
   {
      testFilter( or( enabled(), visible() ), 0, 1, 2, 3, 4 );
   }

   @Test
   public void testXor()
   {
      testFilter( xor( enabled(), custom() ), 1, 2 );
   }

   @Test
   public void testTemplateExact()
   {
      testFilter( template( BASIC_OBJECT ), 1, 2, 4 );
   }

   @Test
   public void testTemplateRelative()
   {
      testFilter( relative( BASIC_OBJECT ), 1, 2, 3, 4 );
   }

   @Test
   public void testContains()
   {
      testFilter( contains( BASICER_OBJECT ), 1, 2, 3, 4 );
   }

   @Test
   public void testValue()
   {
      testFilter( value( POSITION, new Vector( 5.0f, 3.0f ) ), 4 );
      testFilter( value( VELOCITY, new Vector( 0.0f, 0.0f ) ), 1, 2, 3, 4 );
   }

   @Test
   public void testClass()
   {
      testFilter( clazz( EntityList.class ), 0 );
      testFilter( clazz( Entity.class ), 1, 2, 3, 4 );
   }

   @Test
   public void testComponent()
   {
      testFilter( components( NAME, POSITION ), 1, 2, 3, 4 );
      testFilter( components( NAME ), 0, 1, 2, 3, 4 );
      testFilter( components( SCALE ), 3 );
   }

   private void testFilter( EntityFilter filter, int... valid )
   {
      EntityList e0 = new EntityList();         // entirely custom
      e0.put( NAME, "e0" );

      Entity e1 = new Entity( BASIC_OBJECT );    // expired
      e1.set( NAME, "e1" );
      //    e1.expire();

      Entity e2 = new Entity( BASIC_OBJECT );    // invisible
      e2.set( NAME, "e2" );
      e2.hide();

      Entity e3 = new Entity( BASIC_OBJECT );    // custom from BASIC_OBJET
      e3.set( NAME, "e3" );
      e3.grab( SCALE ).v = 4.0f;

      Entity e4 = new Entity( BASIC_OBJECT );    // disabled
      e4.set( NAME, "e4" );
      e4.get( POSITION ).set( 5.0f, 3.0f );
      e4.disable();

      e0.add( e1, e2, e3, e4 );

      // Filter entities
      Entity[] entities = { e0, e1, e2, e3, e4 };

      int count = 0;

      EntityIterator iterator = new EntityIterator( filter );

      for (Entity entity : iterator.iterate( e0 ))
      {
         assertSame( entity, entities[valid[count++]] );
      }

      assertEquals( count, valid.length );
   }

}

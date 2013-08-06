
package org.magnos.entity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.AfterClass;
import org.junit.Test;
import org.magnos.entity.Component;
import org.magnos.entity.ComponentFactoryNull;
import org.magnos.entity.Components;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityCore;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityIterator;
import org.magnos.entity.EntityList;
import org.magnos.entity.Template;
import org.magnos.entity.filters.AndFilter;
import org.magnos.entity.filters.ClassFilter;
import org.magnos.entity.filters.ComponentFilter;
import org.magnos.entity.filters.CustomFilter;
import org.magnos.entity.filters.DefaultFilter;
import org.magnos.entity.filters.EnabledFilter;
import org.magnos.entity.filters.NotFilter;
import org.magnos.entity.filters.OrFilter;
import org.magnos.entity.filters.TemplateContainsFilter;
import org.magnos.entity.filters.TemplateExactFilter;
import org.magnos.entity.filters.TemplateRelativeFilter;
import org.magnos.entity.filters.ValueFilter;
import org.magnos.entity.filters.VisibleFilter;
import org.magnos.entity.filters.XorFilter;
import org.magnos.entity.test.helper.Vector;
import org.magnos.entity.vals.FloatVal;


public class TestEntityFilter
{

   @AfterClass
   public static void afterTest()
   {
      EntityCore.clear();
   }

   static Component<String> NAME = EntityCore.newComponent( "name", new ComponentFactoryNull<String>() );
   static Component<Vector> POSITION = EntityCore.newComponent( "position", new Vector( 0f, 0f ) );
   static Component<Vector> VELOCITY = EntityCore.newComponent( "velocity", new Vector( 0f, 0f ) );
   static Component<FloatVal> RADIUS = EntityCore.newComponent( "radius", new FloatVal( 0f ) );
   static Component<FloatVal> SCALE = EntityCore.newComponent( "scale", new FloatVal( 0f ) );

   static Template BASIC_OBJECT = EntityCore.newTemplate( "basic", new Components( NAME, POSITION, VELOCITY, RADIUS ) );
   static Template BASICER_OBJECT = EntityCore.newTemplate( "basic'er", new Components( POSITION, VELOCITY ) );
   
   @Test
   public void testDefaultFilter()
   {
      testFilter( DefaultFilter.INSTANCE, 0, 1, 2, 3, 4 );
   }

   @Test
   public void testVisible()
   {
      testFilter( VisibleFilter.INSTANCE, 0, 1, 3, 4 );
   }

   @Test
   public void testEnabled()
   {
      testFilter( EnabledFilter.INSTANCE, 0, 1, 2, 3 );
   }

   @Test
   public void testCustom()
   {
      testFilter( CustomFilter.INSTANCE, 0, 3 );
   }

   @Test
   public void testNot()
   {
      NotFilter filter = new NotFilter();

      filter.set( DefaultFilter.INSTANCE );
      testFilter( filter /* nothing */);

      filter.set( VisibleFilter.INSTANCE );
      testFilter( filter, 2 );

      filter.set( EnabledFilter.INSTANCE );
      testFilter( filter, 4 );

      filter.set( CustomFilter.INSTANCE );
      testFilter( filter, 1, 2, 4 );
   }

   @Test
   public void testAnd()
   {
      AndFilter filter = new AndFilter();

      filter.set( CustomFilter.INSTANCE, VisibleFilter.INSTANCE );
      testFilter( filter, 0, 3 );
   }

   @Test
   public void testOr()
   {
      OrFilter filter = new OrFilter();

      filter.set( EnabledFilter.INSTANCE, VisibleFilter.INSTANCE );
      testFilter( filter, 0, 1, 2, 3, 4 );
   }

   @Test
   public void testXor()
   {
      XorFilter filter = new XorFilter();

      filter.set( EnabledFilter.INSTANCE, CustomFilter.INSTANCE );
      testFilter( filter, 1, 2 );
   }

   @Test
   public void testTemplateExact()
   {
      TemplateExactFilter filter = new TemplateExactFilter();

      filter.set( BASIC_OBJECT );
      testFilter( filter, 1, 2, 4 );
   }

   @Test
   public void testTemplateRelative()
   {
      TemplateRelativeFilter filter = new TemplateRelativeFilter();

      filter.set( BASIC_OBJECT );
      testFilter( filter, 1, 2, 3, 4 );
   }
   
   @Test
   public void testContains()
   {
      TemplateContainsFilter filter = new TemplateContainsFilter();
      
      filter.set( BASICER_OBJECT );
      testFilter( filter, 1, 2, 3, 4 );
   }

   @Test
   public void testValue()
   {
      ValueFilter filter = new ValueFilter();

      filter.set( POSITION, new Vector( 5.0f, 3.0f ) );
      testFilter( filter, 4 );

      filter.set( VELOCITY, new Vector( 0.0f, 0.0f ) );
      testFilter( filter, 1, 2, 3, 4 );
   }

   @Test
   public void testClass()
   {
      ClassFilter filter = new ClassFilter();

      filter.set( EntityList.class );
      testFilter( filter, 0 );

      filter.set( Entity.class );
      testFilter( filter, 1, 2, 3, 4 );
   }

   @Test
   public void testComponent()
   {
      ComponentFilter filter = new ComponentFilter();

      filter.set( NAME, POSITION );
      testFilter( filter, 1, 2, 3, 4 );

      filter.set( NAME );
      testFilter( filter, 0, 1, 2, 3, 4 );

      filter.set( SCALE );
      testFilter( filter, 3 );
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

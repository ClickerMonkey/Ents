
package org.magnos.entity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.AfterClass;
import org.junit.Test;
import org.magnos.entity.Component;
import org.magnos.entity.ComponentFactoryNull;
import org.magnos.entity.Components;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityChain;
import org.magnos.entity.EntityCore;
import org.magnos.entity.EntityFilter;
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


public class TestEntityFilterComplex
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
      testFilter( new DefaultFilter(), 0, 1, 2, 3, 4, 5 );
   }

   @Test
   public void testVisible()
   {
      testFilter( new VisibleFilter(), 0, 1, 2, 4, 5 );
   }

   @Test
   public void testEnabled()
   {
      testFilter( new EnabledFilter(), 0, 1, 2, 3, 4 );
   }

   @Test
   public void testCustom()
   {
      testFilter( new CustomFilter(), 0, 4 );
   }

   @Test
   public void testNot()
   {
      NotFilter filter = new NotFilter();

      filter.reset( new DefaultFilter() );
      testFilter( filter /* nothing */);

      filter.reset( new VisibleFilter() );
      testFilter( filter, 3 );

      filter.reset( new EnabledFilter() );
      testFilter( filter, 5 );

      filter.reset( new CustomFilter() );
      testFilter( filter, 1, 2, 3, 5 );
   }

   @Test
   public void testAnd()
   {
      AndFilter filter = new AndFilter();

      filter.reset( new CustomFilter(), new VisibleFilter() );
      testFilter( filter, 0, 4 );
   }

   @Test
   public void testOr()
   {
      OrFilter filter = new OrFilter();

      filter.reset( new EnabledFilter(), new VisibleFilter() );
      testFilter( filter, 0, 1, 2, 3, 4, 5 );
   }

   @Test
   public void testXor()
   {
      XorFilter filter = new XorFilter();

      filter.reset( new EnabledFilter(), new CustomFilter() );
      testFilter( filter, 1, 2, 3 );
   }

   @Test
   public void testTemplateExact()
   {
      TemplateExactFilter filter = new TemplateExactFilter();

      filter.reset( BASIC_OBJECT );
      testFilter( filter, 1, 2, 3, 5 );
   }

   @Test
   public void testTemplateRelative()
   {
      TemplateRelativeFilter filter = new TemplateRelativeFilter();

      filter.reset( BASIC_OBJECT );
      testFilter( filter, 1, 2, 3, 4, 5 );
   }

   @Test
   public void testContains()
   {
      TemplateContainsFilter filter = new TemplateContainsFilter();

      filter.reset( BASICER_OBJECT );
      testFilter( filter, 1, 2, 3, 4, 5 );
   }

   @Test
   public void testValue()
   {
      ValueFilter filter = new ValueFilter();

      filter.reset( POSITION, new Vector( 5.0f, 3.0f ) );
      testFilter( filter, 5 );

      filter.reset( VELOCITY, new Vector( 0.0f, 0.0f ) );
      testFilter( filter, 1, 2, 3, 4, 5 );
   }

   @Test
   public void testClass()
   {
      ClassFilter filter = new ClassFilter();

      filter.reset( EntityList.class );
      testFilter( filter, 0 );

      filter.reset( Entity.class );
      testFilter( filter, 1, 3, 4, 5 );

      filter.reset( EntityChain.class );
      testFilter( filter, 2 );
   }

   @Test
   public void testComponent()
   {
      ComponentFilter filter = new ComponentFilter();

      filter.reset( NAME, POSITION );
      testFilter( filter, 1, 2, 3, 4, 5 );

      filter.reset( NAME );
      testFilter( filter, 0, 1, 2, 3, 4, 5 );

      filter.reset( SCALE );
      testFilter( filter, 4 );
   }

   private void testFilter( EntityFilter filter, int... valid )
   {
      EntityList e0 = new EntityList();         // entirely custom
      e0.put( NAME, "e0" );

      Entity e1 = new Entity( BASIC_OBJECT );
      e1.set( NAME, "e1" );

      Entity e3 = new Entity( BASIC_OBJECT );    // invisible
      e3.set( NAME, "e3" );
      e3.hide();

      Entity e4 = new Entity( BASIC_OBJECT );    // custom from BASIC_OBJET
      e4.set( NAME, "e4" );
      e4.grab( SCALE ).v = 4.0f;

      Entity e5 = new Entity( BASIC_OBJECT );    // disabled
      e5.set( NAME, "e5" );
      e5.get( POSITION ).set( 5.0f, 3.0f );
      e5.disable();

      Entity e2 = new EntityChain( BASIC_OBJECT, e1, e3, true, true );
      e2.set( NAME, "e2" );

      e0.add( e2, e4, e5 );

      // Filter entities
      Entity[] entities = { e0, e1, e2, e3, e4, e5 };

      int count = 0;

      for (Entity entity : filter.reset( e0 ))
      {
         assertSame( entity, entities[valid[count++]] );
      }

      assertEquals( count, valid.length );
   }

}

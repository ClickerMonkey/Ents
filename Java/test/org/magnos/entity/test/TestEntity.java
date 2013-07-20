package org.magnos.entity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Test;
import org.magnos.entity.Component;
import org.magnos.entity.ComponentFactoryNull;
import org.magnos.entity.Components;
import org.magnos.entity.Controller;
import org.magnos.entity.Controller.Control;
import org.magnos.entity.Controllers;
import org.magnos.entity.DynamicValue;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityCore;
import org.magnos.entity.Template;
import org.magnos.entity.View;
import org.magnos.entity.test.helper.Bounds;
import org.magnos.entity.test.helper.Vector;
import org.magnos.entity.vals.IntVal;

public class TestEntity 
{
	
	@AfterClass
	public static void afterTest()
	{
		EntityCore.clear();
	}
	
	static class TestComponents {
		public static Component<String>		NAME 					= EntityCore.newComponent("name", new ComponentFactoryNull<String>() );
		public static Component<Vector> 	POSITION 				= EntityCore.newComponent("position", new Vector());
		public static Component<Vector> 	VELOCITY 				= EntityCore.newComponent("velocity", new Vector());
		public static Component<IntVal> 		ID 					= EntityCore.newComponent("id", new IntVal());
		public static Component<Vector> 	SPATIAL_POSITION 		= EntityCore.newComponent("spatial position", new Vector());
		public static Component<Vector> 	SPATIAL_VELOCITY 		= EntityCore.newComponent("spatial velocity", new Vector());
		public static Component<Vector>		SPATIAL_POSITION_ALIAS 	= EntityCore.newComponentAlias( POSITION, SPATIAL_POSITION );
		public static Component<Vector>		SPATIAL_VELOCITY_ALIAS 	= EntityCore.newComponentAlias( VELOCITY, SPATIAL_VELOCITY );
		public static Component<Vector> 	SIZE 					= EntityCore.newComponent("size", new Vector());
		public static Component<Bounds> 	BOUNDS 					= EntityCore.newComponentDynamic("bounds", new DynamicValue<Bounds>() {
			public void set( Entity e, Bounds value ) {
				Vector p = e.get(POSITION);
				Vector s = e.get(SIZE);
				p.x = (value.left + value.right) * 0.5f;
				p.y = (value.top + value.bottom) * 0.5f;
				s.x = (value.right - value.left);
				s.y = (value.bottom - value.top);
			}
			public Bounds get( Entity e ) {
				return take( e, new Bounds() );
			}
			@Override
			public Bounds take(Entity e, Bounds target) {
				Vector p = e.get(POSITION);
				Vector s = e.get(SIZE);
				target.left = p.x - s.x * 0.5f;
				target.right = p.x + s.x * 0.5f;
				target.top = p.y - s.y * 0.5f;
				target.bottom = p.y + s.y * 0.5f;
				return target;
			}
		});
		
	}
	
	static class TestViews {
		public static View SPRITE = EntityCore.newView( "sprite" );
	}
	
	static class TestControllers {
		public static Controller PHYSICS = EntityCore.newController( "physics", new Control() { 			
			public void control( Entity e, Object updateState ) {
				e.get( TestComponents.POSITION ).addsi( e.get( TestComponents.VELOCITY), (Float)updateState );
			}
		});
	}
	
	static class TestTemplates {
		public static Template SPRITE = EntityCore.newTemplate( "sprite", 
			/* components  */ new Components(TestComponents.NAME, TestComponents.POSITION, TestComponents.VELOCITY, TestComponents.SIZE, TestComponents.SPATIAL_POSITION_ALIAS, TestComponents.SPATIAL_VELOCITY_ALIAS, TestComponents.BOUNDS), 
			/* controllers */ new Controllers(TestControllers.PHYSICS),
			/*    view     */ TestViews.SPRITE
		);
	}

	@Test
	public void testBasic()
	{
		Entity e = new Entity(TestTemplates.SPRITE);
		e.set(TestComponents.NAME, "Philip Diffenderfer");
		e.get(TestComponents.POSITION).set( 5, 10 );
		e.get(TestComponents.VELOCITY).set( 0, 0 );
		e.get(TestComponents.SIZE).set( 6, 4 );
		
		assertEquals( "Philip Diffenderfer", e.get(TestComponents.NAME) );
		assertFalse( e.has(TestComponents.ID) );
		
		e.add(TestComponents.ID);
		
		assertTrue( e.has(TestComponents.ID) );
		
		e.get(TestComponents.ID).v = 345;
		
		assertEquals( 345, e.get(TestComponents.ID).v );
		
		assertEquals( new Vector(5, 10), e.get(TestComponents.SPATIAL_POSITION) );
		assertSame( e.get(TestComponents.POSITION), e.get(TestComponents.SPATIAL_POSITION) );
		
		Bounds bounds = e.get(TestComponents.BOUNDS);
		assertEquals( 2, bounds.left, 0.00001 );
		assertEquals( 8, bounds.top, 0.00001 );
		assertEquals( 8, bounds.right, 0.00001 );
		assertEquals( 12, bounds.bottom, 0.00001 );
	}
	
}

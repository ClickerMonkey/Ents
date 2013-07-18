package org.magnos.entity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Test;
import org.magnos.entity.Component;
import org.magnos.entity.ComponentFactory;
import org.magnos.entity.Components;
import org.magnos.entity.Controllers;
import org.magnos.entity.DynamicValue;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityCore;
import org.magnos.entity.Template;
import org.magnos.entity.View;
import org.magnos.entity.View.Renderer;
import org.magnos.entity.test.helper.Bounds;
import org.magnos.entity.test.helper.Scalar;
import org.magnos.entity.test.helper.Vector;


public class TestNewDesign 
{
	
	@AfterClass
	public static void afterTest()
	{
		EntityCore.clear();
	}
	
	public static interface CollisionHandler {
		void handleCollision(Entity a, Entity b, float time);
	}
	
	public static class Shape implements ComponentFactory<Shape> {
		public Shape create() {
			return new Shape();
		}
		public Shape clone( Shape value ) {
			return new Shape();
		}
	}
	
	public static class CollisionHandlerDefault implements CollisionHandler, ComponentFactory<CollisionHandler> {
		public CollisionHandler create() {
			return this;
		}
		public CollisionHandler clone( CollisionHandler value ) {
			return this;
		}
		public void handleCollision( Entity a, Entity b, float time ) {
			
		}
	}
	
	public static class ViewDefault implements Renderer {
		public void draw( Entity e, Object drawState ) {
			
		}
	}
	
	@Test
	public void test()
	{
		final Component<Vector> POSITION = EntityCore.newComponent( "position", new Vector() );
		final Component<Scalar> RADIUS = EntityCore.newComponent( "radius", new Scalar() );
		final Component<CollisionHandler> COLLISION_HANDLER = EntityCore.newComponent( "collision-callback" ); // by default, there is no collision handler
		final Component<Shape> SHAPE = EntityCore.newComponent( "collision-shape" ); // by default, there is no collision shape on an entity
		final Component<Bounds> BOUNDS = EntityCore.newComponent( "bounds" ); // by default, an entity has a bounds component 
		
		final Component<CollisionHandler> COLLISION_HANDLER_SHIP = EntityCore.newComponentSharedAlternative( COLLISION_HANDLER, new CollisionHandlerDefault() ); 
		final Component<CollisionHandler> COLLISION_HANDLER_ASTEROID = EntityCore.newComponentSharedAlternative( COLLISION_HANDLER, new CollisionHandlerDefault() );
		
		final Component<Shape> SHAPE_SHIP = EntityCore.newComponentSharedAlternative( SHAPE, new Shape() );
		final Component<Shape> SHAPE_ASTEROID = EntityCore.newComponentSharedAlternative( SHAPE, new Shape() );
		
		final Component<Bounds> BOUNDS_DYNAMIC = EntityCore.newComponentDynamicAlternative( BOUNDS, new DynamicValue<Bounds>() {
			public Bounds get( Entity e ) {
				Bounds b = new Bounds();
				set( e, b );
				return b;
			}
			public void set( Entity e, Bounds target ) {
				Vector p = e.get( POSITION );
				float r = e.get( RADIUS ).x;
				target.left = p.x - r;
				target.right = p.x + r;
				target.top = p.y - r;
				target.bottom = p.y + r;
			}
		});
		
		final View SHIP_VIEW = EntityCore.newView( "ship", new ViewDefault() );
		final View ASTEROID_VIEW = EntityCore.newView( "asteroid", new ViewDefault() );
		
		final Template SHIP = EntityCore.newTemplate( "ship", new Components(POSITION, RADIUS, COLLISION_HANDLER_SHIP, SHAPE_SHIP, BOUNDS_DYNAMIC), new Controllers(), SHIP_VIEW );
		final Template ASTEROID = EntityCore.newTemplate( "asteroid", new Components(POSITION, RADIUS, COLLISION_HANDLER_ASTEROID, SHAPE_ASTEROID, BOUNDS_DYNAMIC), new Controllers(), ASTEROID_VIEW );

		assertTrue( SHIP.has(SHAPE) );
		assertTrue( SHIP.has(BOUNDS) );
		assertTrue( SHIP.has(COLLISION_HANDLER) );
		assertTrue( SHIP.has(POSITION) );
		
		assertTrue( ASTEROID.has(SHAPE) );
		assertTrue( ASTEROID.has(BOUNDS) );
		assertTrue( ASTEROID.has(COLLISION_HANDLER) );
		assertTrue( ASTEROID.has(POSITION) );
		
		Entity e = new Entity( SHIP );
		e.get( POSITION ).x = 1.0f;
		e.get( POSITION ).y = 2.0f;
		
		Bounds b = e.get( BOUNDS );

		assertEquals( 1.0f, b.left, 0.00001f );
		assertEquals( 1.0f, b.right, 0.00001f );
		assertEquals( 2.0f, b.top, 0.00001f );
		assertEquals( 2.0f, b.bottom, 0.00001f );
		
		e.get( RADIUS ).x = 2.0f;
		e.set( BOUNDS, b );
		
		assertEquals(-1.0f, b.left, 0.00001f );
		assertEquals( 3.0f, b.right, 0.00001f );
		assertEquals( 0.0f, b.top, 0.00001f );
		assertEquals( 4.0f, b.bottom, 0.00001f );
		
		Entity n = e.clone( true );
		assertEquals( 2.0f, n.get( RADIUS ).x, 0.00001f );
		assertEquals( 1.0f, n.get( POSITION ).x, 0.00001f );
		assertEquals( 2.0f, n.get( POSITION ).y, 0.00001f );
	}
	
}

package org.magnos.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.awt.Rectangle;

import org.junit.Test;

public class TestEntity 
{
	
	public static final EntityCore core = new EntityCore();
	
	public class Scalar implements ComponentFactory<Scalar>{ 
		float x;
		public Scalar(float x) {
			this.x = x;
		}
		public Scalar create() {
			return new Scalar( x );
		}
		public Scalar clone( Scalar value ) {
			return new Scalar( value.x );
		}
	}
	
	static class PhysicsController implements Controller {
		public void control( Entity e, Object updateState ) {
			float dt = (Float)updateState;
			Point pos = e.get( Components.POSITION );
			Point vel = e.get( Components.VELOCITY );
			pos.x += vel.x * dt;
			pos.y += vel.y * dt;
		}
	}
	
	static class Components {
		public static Component<String>		NAME 				= core.newComponent("name");
		public static Component<Point> 		POSITION 			= core.newComponent("position");
		public static Component<Point> 		VELOCITY 			= core.newComponent("velocity");
		public static Component<Integer> 	ID 					= core.newComponent("id");
		public static Component<Point> 		SPATIAL_POSITION 	= core.newComponent("spatial position");
		public static Component<Point> 		SPATIAL_VELOCITY 	= core.newComponent("spatial velocity");
		public static Component<Point> 		SIZE 				= core.newComponent("size");
		
		public static ComponentSet<Rectangle> BOUNDS = core.newComponentSet("bounds", new BitSet(POSITION, SIZE), new ComponentSet.Set<Rectangle>() {
			public Rectangle set( Entity e, Rectangle target ) {
				Point p = e.get(POSITION);
				Point s = e.get(SIZE);
				target.setFrameFromCenter(p.x, p.y, p.x - s.x * 0.5, p.y - s.y * 0.5);
				return target;
			}
		});
	}
	
	static class Views {
		public static int SPRITE = core.newView();
	}
	
	static class Controllers {
		public static int PHYSICS = core.addController( "physics", new BitSet(Components.POSITION, Components.VELOCITY), new PhysicsController() );
	}
	
	static class Entities {
		public static EntityType SPRITE = core.newEntityType(
			/* components  */ new IdMap(Components.NAME, Components.POSITION, Components.VELOCITY, Components.SIZE), 
			/* controllers */ new IdMap(Controllers.PHYSICS),
			/*   methods   */ new IdMap(),
			/*    view     */ Views.SPRITE
		)
		.setComponentAlias( Components.POSITION, Components.SPATIAL_POSITION )
		.setComponentAlias( Components.VELOCITY, Components.SPATIAL_VELOCITY );
	}

	@Test
	public void testBasic()
	{
		Entity x = new Entity(Entities.SPRITE);
		x.set(Components.NAME, "Philip Diffenderfer");
		x.set(Components.POSITION, new Point(5, 10));
		x.set(Components.VELOCITY, new Point(0, 0));
		x.set(Components.SIZE, new Point(6, 4));
		
		System.out.println( x.get(Components.NAME) );
		
		assertFalse( x.has(Components.ID) );
		
		x.add( Components.ID, 0 );
		
		assertTrue( x.has(Components.ID) );
		
		x.set(Components.ID, 345);
		
		System.out.println(x.get(Components.ID));
		
		// Test Aliasing
		assertEquals( new Point(5, 10), x.get(Components.SPATIAL_POSITION) );
		assertSame( x.get(Components.POSITION), x.get(Components.SPATIAL_POSITION) );
		
		Rectangle bounds = x.set(Components.BOUNDS, new Rectangle());
		assertEquals( 2, bounds.x, 0.00001 );
		assertEquals( 8, bounds.y, 0.00001 );
		assertEquals( 6, bounds.width, 0.00001 );
		assertEquals( 4, bounds.height, 0.00001 );
	}
	
}

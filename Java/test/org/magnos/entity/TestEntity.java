package org.magnos.entity;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Rectangle;

import org.junit.Test;

public class TestEntity 
{
	
	static class DynamicBounds implements DynamicComponent<Rectangle> {
		public Rectangle compute(Entity e, Rectangle out) {
			Point p = e.get(Components.POSITION);
			Point s = e.get(Components.SIZE);
			if (p == null || s == null) {
				return null;
			}
			out.setFrameFromCenter(p.x, p.y, p.x - s.x * 0.5, p.y - s.y * 0.5);
			return out;
		}
	}
	
	static class Components {
		public static int NAME = EntityCore.newComponent("name", String.class);
		public static int POSITION = EntityCore.newComponent("position", Point.class);
		public static int VELOCITY = EntityCore.newComponent("velocity", Point.class);
		public static int ID = EntityCore.newComponent("id", int.class);
		public static int SPATIAL_POSITION = EntityCore.newComponent("spatial position", Point.class);
		public static int SPATIAL_VELOCITY = EntityCore.newComponent("spatial velocity", Point.class);
		public static int SIZE = EntityCore.newComponent("size", Point.class);
		public static int BOUNDS = EntityCore.newDynamicComponent("bounds", Rectangle.class, new DynamicBounds());
	}
	
	static class Entities {
		public static int SPRITE = EntityCore.newEntity(Components.NAME, Components.POSITION, Components.VELOCITY, Components.SIZE); 
	}

	@Test
	public void testBasic()
	{
		EntityType spriteType = EntityCore.getEntityType(Entities.SPRITE);
		spriteType.setAlias(Components.POSITION, Components.SPATIAL_POSITION);
		spriteType.setAlias(Components.VELOCITY, Components.SPATIAL_VELOCITY);
		
		Entity x = new Entity(Entities.SPRITE);
		x.set(Components.NAME, "Philip Diffenderfer");
		x.set(Components.POSITION, new Point(5, 10));
		x.set(Components.VELOCITY, new Point(0, 0));
		x.set(Components.SIZE, new Point(6, 4));
		
		System.out.println( x.get(Components.NAME) );
		System.out.println( x.get(Components.NAME, String.class) );
		
		assertFalse( x.has(Components.ID) );
		
		x.add( Components.ID, 0 );
		
		assertTrue( x.has(Components.ID) );
		
		x.set(Components.ID, 345);
		
		System.out.println(x.get(Components.ID));
		
		// Test Aliasing
		assertEquals( new Point(5, 10), x.get(Components.SPATIAL_POSITION) );
		assertSame( x.get(Components.POSITION), x.get(Components.SPATIAL_POSITION) );
		
		Rectangle bounds = x.get(Components.BOUNDS, new Rectangle());
		assertEquals( 2, bounds.x, 0.00001 );
		assertEquals( 8, bounds.y, 0.00001 );
		assertEquals( 6, bounds.width, 0.00001 );
		assertEquals( 4, bounds.height, 0.00001 );
	}
	
}

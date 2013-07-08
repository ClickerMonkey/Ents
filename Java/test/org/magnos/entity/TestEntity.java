package org.magnos.entity;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class TestEntity 
{
	
	static class Components {
		public static int NAME = EntityCore.newComponent("name", String.class);
		public static int POSITION = EntityCore.newComponent("position", Point.class);
		public static int VELOCITY = EntityCore.newComponent("velocity", Point.class);
		public static int ID = EntityCore.newComponent("id", int.class);
		public static int SPATIAL_POSITION = EntityCore.newComponent("spatial position", Point.class);
		public static int SPATIAL_VELOCITY = EntityCore.newComponent("spatial velocity", Point.class);
	}
	
	static class Entities {
		public static int SPRITE = EntityCore.newEntity(Components.NAME, Components.POSITION, Components.VELOCITY); 
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
	}
	
}

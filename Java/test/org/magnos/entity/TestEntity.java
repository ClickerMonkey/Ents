package org.magnos.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.magnos.entity.helper.Bounds;
import org.magnos.entity.helper.Index;
import org.magnos.entity.helper.Vector;

public class TestEntity 
{
	
	public static final EntityCore core = new EntityCore();
	
	static class Components {
		public static Component<String>		NAME 				= core.newComponent("name");
		public static Component<Vector> 	POSITION 			= core.newComponent("position", new Vector());
		public static Component<Vector> 	VELOCITY 			= core.newComponent("velocity", new Vector());
		public static Component<Index> 		ID 					= core.newComponent("id", new Index( ));
		public static Component<Vector> 	SPATIAL_POSITION 	= core.newComponent("spatial position", new Vector());
		public static Component<Vector> 	SPATIAL_VELOCITY 	= core.newComponent("spatial velocity", new Vector());
		public static Component<Vector> 	SIZE 				= core.newComponent("size", new Vector());
		
		public static ComponentSet<Bounds> BOUNDS = core.newComponentSet("bounds", new BitSet(POSITION, SIZE), new ComponentSet.Set<Bounds>() {
			public Bounds set( Entity e, Bounds target ) {
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
	
	static class Views {
		public static int SPRITE = core.newView();
	}
	
	static class Controllers {
		public static int PHYSICS = core.newController( "physics", new BitSet(Components.POSITION, Components.VELOCITY), new Controller() { 			
			public void control( Entity e, Object updateState ) {
				e.get( Components.POSITION ).addsi( e.get( Components.VELOCITY), (Float)updateState );
			}
		});
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
		Entity e = new Entity(Entities.SPRITE);
		e.set(Components.NAME, "Philip Diffenderfer");
		e.get(Components.POSITION).set( 5, 10 );
		e.get(Components.VELOCITY).set( 0, 0 );
		e.get(Components.SIZE).set( 6, 4 );
		
		assertEquals( "Philip Diffenderfer", e.get(Components.NAME) );
		assertFalse( e.has(Components.ID) );
		
		e.add(Components.ID);
		
		assertTrue( e.has(Components.ID) );
		
		e.get(Components.ID).x = 345;
		
		assertEquals( 345, e.get(Components.ID).x );
		
		assertEquals( new Vector(5, 10), e.get(Components.SPATIAL_POSITION) );
		assertSame( e.get(Components.POSITION), e.get(Components.SPATIAL_POSITION) );
		
		Bounds bounds = e.set(Components.BOUNDS, new Bounds());
		assertEquals( 2, bounds.left, 0.00001 );
		assertEquals( 8, bounds.top, 0.00001 );
		assertEquals( 8, bounds.right, 0.00001 );
		assertEquals( 12, bounds.bottom, 0.00001 );
	}
	
}

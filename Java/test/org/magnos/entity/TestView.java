package org.magnos.entity;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.magnos.entity.helper.Vector;

public class TestView
{

	public static final EntityCore core = new EntityCore();
	
	public static final Component<Vector>	POSITION = core.newComponent( "position", new Vector() );
	public static final int		 			SPRITE_VIEW = core.newView();
	public static final EntityType			SPRITE = core.newEntityType( new IdMap(POSITION), new IdMap(), new IdMap(), SPRITE_VIEW );
	
	@Test
	public void testDraw()
	{
		final AtomicInteger DRAW_COUNTER = new AtomicInteger();

		core.setView( SPRITE_VIEW, "sprite", new BitSet(POSITION), new View() {
			public void draw( Entity entity, Object drawState ) {
				DRAW_COUNTER.incrementAndGet();
			}
		});
		
		Entity e = new Entity( SPRITE );
		
		assertEquals( 0, DRAW_COUNTER.get() );
		
		e.draw( null );
		
		assertEquals( 1, DRAW_COUNTER.get() );
	}
	
}

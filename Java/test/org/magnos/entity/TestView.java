package org.magnos.entity;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.magnos.entity.View.Renderer;
import org.magnos.entity.helper.Vector;

public class TestView
{

	public static final EntityCore core = new EntityCore();
	
	public static final Component<Vector>	POSITION = EntityCore.newComponent( "position", new Vector() );
	public static final View		 		SPRITE_VIEW = EntityCore.newView( "sprite-view" );
	public static final Template			SPRITE = EntityCore.newTemplate( "sprite", new Components(POSITION), new Controllers(), SPRITE_VIEW );
	
	@Test
	public void testDraw()
	{
		final AtomicInteger DRAW_COUNTER = new AtomicInteger();

		EntityCore.setViewDefault( SPRITE_VIEW, new Renderer() {
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

package org.magnos.entity.test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.AfterClass;
import org.junit.Test;
import org.magnos.entity.Component;
import org.magnos.entity.Components;
import org.magnos.entity.Controllers;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityCore;
import org.magnos.entity.Template;
import org.magnos.entity.View;
import org.magnos.entity.View.Renderer;
import org.magnos.entity.test.helper.Vector;

public class TestView
{
	
	@AfterClass
	public static void afterTest()
	{
		EntityCore.clear();
	}

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

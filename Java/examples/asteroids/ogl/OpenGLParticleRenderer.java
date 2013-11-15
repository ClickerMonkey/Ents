
package asteroids.ogl;

import static org.lwjgl.opengl.GL11.*;

import static asteroids.Components.COLOR;
import static asteroids.Components.POSITION;
import static asteroids.Components.RADIUS;

import java.awt.Color;

import org.magnos.entity.Entity;
import org.magnos.entity.Renderer;

import asteroids.Vector;


public class OpenGLParticleRenderer implements Renderer
{

	@Override
	public Renderer create( Entity e )
	{
		return this;
	}

	@Override
	public void begin( Entity e, Object drawState )
	{
		Vector pos = e.get( POSITION );
		float rad = e.get( RADIUS ).v;
		Color clr = e.get( COLOR );

		OpenGLAsteroids.bind( clr );
		glVertex2f( pos.x - rad, pos.y - rad );
		glVertex2f( pos.x + rad, pos.y - rad );
		glVertex2f( pos.x + rad, pos.y + rad );
		glVertex2f( pos.x - rad, pos.y + rad );
	}

	@Override
	public void end( Entity e, Object drawState )
	{

	}

	@Override
	public void destroy( Entity e )
	{

	}

}

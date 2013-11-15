
package asteroids.ogl;

import static org.lwjgl.opengl.GL11.*;

import static asteroids.Components.COLOR;
import static asteroids.Components.POSITION;
import static asteroids.Components.VELOCITY;

import java.awt.Color;

import org.magnos.entity.Entity;
import org.magnos.entity.Renderer;

import asteroids.Vector;


public class OpenGLLaserRenderer implements Renderer
{

    public static final Color TRANSPARENT = new Color( 0, 0, 0, 0 );
    
	public float tailLength;

	public OpenGLLaserRenderer( float tailLength )
	{
		this.tailLength = tailLength;
	}

	@Override
	public Renderer create( Entity e )
	{
		return this;
	}

	@Override
	public void begin( Entity e, Object drawState )
	{
		Vector vel = e.get( VELOCITY );
		Vector pos = e.get( POSITION );
		Color clr = e.get( COLOR );

		float dx = vel.x * tailLength * 0.5f;
		float dy = vel.y * tailLength * 0.5f;
		
		glLineWidth( 3.0f );
		
		glBegin( GL_LINES );
		OpenGLAsteroids.bind( clr );
		glVertex2f( pos.x + dx, pos.y + dy );
		OpenGLAsteroids.bind( TRANSPARENT );
		glVertex2f( pos.x - dx, pos.y - dy );
		glEnd();
		
		glLineWidth( 1.0f );
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

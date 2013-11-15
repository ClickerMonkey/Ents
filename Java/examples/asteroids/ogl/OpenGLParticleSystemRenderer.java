package asteroids.ogl;

import static org.lwjgl.opengl.GL11.*;

import org.magnos.entity.Entity;
import org.magnos.entity.Renderer;


public class OpenGLParticleSystemRenderer implements Renderer
{

	@Override
	public Renderer create( Entity e )
	{
		return this;
	}

	@Override
	public void begin( Entity e, Object drawState )
	{
		glBegin( GL_QUADS );
	}

	@Override
	public void end( Entity e, Object drawState )
	{
		glEnd();
	}

	@Override
	public void destroy( Entity e )
	{
		
	}

}

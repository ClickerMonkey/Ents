package asteroids.ogl;

import static org.lwjgl.opengl.GL11.*;

import static asteroids.Components.ANGLE;
import static asteroids.Components.COLOR;
import static asteroids.Components.POSITION;
import static asteroids.Components.RADIUS;

import java.awt.Color;

import org.magnos.entity.Entity;
import org.magnos.entity.Renderer;

import asteroids.Vector;


public class OpenGLShipRenderer implements Renderer
{

	int callList = -1;
	
	@Override
	public Renderer create( Entity e )
	{
		return this;
	}

	@Override
	public void begin( Entity e, Object drawState )
	{
		Vector pos = e.get( POSITION );
        float ang = e.get( ANGLE ).v;
        float rad = e.get( RADIUS ).v;
        Color clr = e.get( COLOR );
        
        glPushMatrix();
        glTranslatef( pos.x, pos.y, 0.0f );
        glRotatef( (float)Math.toDegrees( ang ), 0, 0, 1 );
        
        if (callList == -1)
        {
        	callList = glGenLists( 1 );
        	
        	glNewList( callList, GL_COMPILE_AND_EXECUTE );
        	OpenGLAsteroids.bind( clr );
            glBegin( GL_LINES );
            glVertex2f( rad, 0 );
            glVertex2f( -rad * 0.8f, rad * 0.6f );
            glVertex2f( rad, 0 );
            glVertex2f( -rad * 0.8f, -rad * 0.6f );
            glVertex2f( -rad * 0.6f, -rad * 0.4f );
            glVertex2f( -rad * 0.6f, rad * 0.4f );
            glEnd();
            glEndList();
        }
        else
        {
        	glCallList( callList );
        }
        
        glPopMatrix();
	}

	@Override
	public void end( Entity e, Object drawState )
	{
		
	}

	@Override
	public void destroy( Entity e )
	{
		if (callList != -1)
		{
			glDeleteLists( callList, 1 );	
			callList = -1;
		}
	}

}

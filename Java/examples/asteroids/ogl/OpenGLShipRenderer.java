/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package asteroids.ogl;

import static asteroids.Components.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import org.magnos.entity.Entity;
import org.magnos.entity.RendererSingle;

import asteroids.Vector;


public class OpenGLShipRenderer extends RendererSingle
{

	int callList = -1;
	
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
	public void destroy( Entity e )
	{
		if (callList != -1)
		{
			glDeleteLists( callList, 1 );	
			callList = -1;
		}
	}

}

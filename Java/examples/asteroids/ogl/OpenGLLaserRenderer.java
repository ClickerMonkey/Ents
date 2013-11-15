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


public class OpenGLLaserRenderer extends RendererSingle
{

    public static final Color TRANSPARENT = new Color( 0, 0, 0, 0 );
    
	public float tailLength;

	public OpenGLLaserRenderer( float tailLength )
	{
		this.tailLength = tailLength;
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

}

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

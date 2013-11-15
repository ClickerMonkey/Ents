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

package asteroids.j2d;

import static asteroids.Components.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import org.magnos.entity.Entity;
import org.magnos.entity.RendererSingle;

import asteroids.Vector;


public class Java2DParticleRenderer extends RendererSingle
{

    public static final Ellipse2D.Float ELLIPSE = new Ellipse2D.Float( -1, -1, 2, 2 );
    
    @Override
    public void begin( Entity e, Object drawState )
    {
        Graphics2D gr = (Graphics2D)drawState;
        Vector pos = e.get( POSITION );
        float rad = e.get( RADIUS ).v;
        Color clr = e.get( COLOR );
        
        ELLIPSE.setFrame( pos.x - rad, pos.y - rad, rad * 2, rad * 2 );
        
        gr.setColor( clr );
        gr.draw( ELLIPSE );
    }
    
}

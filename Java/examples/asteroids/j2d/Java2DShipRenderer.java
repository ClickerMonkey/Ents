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
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import org.magnos.entity.Entity;
import org.magnos.entity.RendererSingle;

import asteroids.Vector;

public class Java2DShipRenderer extends RendererSingle
{

    public static final Line2D.Float LINE = new Line2D.Float( 0, 0, 1, 0 );
    
    @Override
    public void begin( Entity e, Object drawState )
    {
        Graphics2D gr = (Graphics2D)drawState;
        Vector pos = e.get( POSITION );
        float ang = e.get( ANGLE ).v;
        float rad = e.get( RADIUS ).v;
        Color clr = e.get( COLOR );
        
        AffineTransform popped = gr.getTransform();
        gr.translate( pos.x, pos.y );
        gr.rotate( ang );
        gr.setColor( clr );

        LINE.setLine( rad, 0, -rad * 0.8f, rad * 0.6f );
        gr.draw( LINE );
        
        LINE.setLine( rad, 0, -rad * 0.8f, -rad * 0.6f );
        gr.draw( LINE );
        
        LINE.setLine( -rad * 0.6f, -rad * 0.4f, -rad * 0.6f, rad * 0.4f );
        gr.draw( LINE );
        
        gr.setTransform( popped );
    }

}

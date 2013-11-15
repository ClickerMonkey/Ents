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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import org.magnos.entity.Entity;
import org.magnos.entity.RendererSingle;

import asteroids.Vector;


public class Java2DLaserRenderer extends RendererSingle
{

    public static final Line2D.Float LINE = new Line2D.Float();
    public static final Color TRANSPARENT = new Color( 0, 0, 0, 0 );
    public static final Stroke STROKE = new BasicStroke( 3.0f );

    public float tailLength;

    public Java2DLaserRenderer( float tailLength )
    {
        this.tailLength = tailLength;
    }

    @Override
    public void begin( Entity e, Object drawState )
    {
        Graphics2D gr = (Graphics2D)drawState;
        Vector vel = e.get( VELOCITY );
        Vector pos = e.get( POSITION );
        Color clr = e.get( COLOR );

        float dx = vel.x * tailLength * 0.5f;
        float dy = vel.y * tailLength * 0.5f;

        LINE.x1 = pos.x + dx;
        LINE.y1 = pos.y + dy;
        LINE.x2 = pos.x - dx;
        LINE.y2 = pos.y - dy;

        Stroke popped = gr.getStroke();
        gr.setPaint( new GradientPaint( LINE.getP1(), clr, LINE.getP2(), TRANSPARENT ) );
        gr.setStroke( STROKE );
        gr.draw( LINE );
        gr.setStroke( popped );
    }

}

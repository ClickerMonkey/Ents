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
import java.awt.geom.Path2D;
import java.util.Random;

import org.magnos.entity.Entity;
import org.magnos.entity.Renderer;
import org.magnos.entity.RendererSingle;

import asteroids.Vector;


public class Java2DAsteroidRenderer extends RendererSingle
{

    public float dentPercent;
    public int spokeCount;
    public Random random;
    public Path2D.Float path;

    public Java2DAsteroidRenderer( float dentPercent, int spokeCount, Random random )
    {
        this( dentPercent, spokeCount, random, null );
    }

    public Java2DAsteroidRenderer( float dentPercent, int spokeCount, Random random, Path2D.Float path )
    {
        this.dentPercent = dentPercent;
        this.spokeCount = spokeCount;
        this.random = random;
        this.path = path;
    }

    @Override
    public Renderer create( Entity e )
    {
        final float angleIncrease = (float)(Math.PI * 2 / spokeCount);
        float angle = angleIncrease;

        Path2D.Float path = new Path2D.Float();
        path.moveTo( nextSpoke(), 0 );

        for (int i = 1; i < spokeCount; i++)
        {
            float s = nextSpoke();
            path.lineTo( Math.cos( angle ) * s, Math.sin( angle ) * s );
            angle += angleIncrease;
        }

        path.closePath();

        return new Java2DAsteroidRenderer( dentPercent, spokeCount, random, path );
    }

    private float nextSpoke()
    {
        return 1.0f - (random.nextFloat() * dentPercent);
    }

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
        gr.scale( rad, rad );
        gr.scale( 1.1f, 1.1f );
        gr.setColor( clr.darker() );
        gr.fill( path );
        gr.scale( 0.9f, 0.9f );
        gr.setColor( clr );
        gr.fill( path );
        gr.setTransform( popped );
    }

}

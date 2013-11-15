package asteroids.j2d;

import static asteroids.Components.ANGLE;
import static asteroids.Components.COLOR;
import static asteroids.Components.POSITION;
import static asteroids.Components.RADIUS;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import org.magnos.entity.Entity;
import org.magnos.entity.Renderer;

import asteroids.Vector;

public class Java2DShipRenderer implements Renderer
{

    public static final Line2D.Float LINE = new Line2D.Float( 0, 0, 1, 0 );
    
    @Override
    public Renderer create( Entity e )
    {
        return this;
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
        gr.setColor( clr );

        LINE.setLine( rad, 0, -rad * 0.8f, rad * 0.6f );
        gr.draw( LINE );
        
        LINE.setLine( rad, 0, -rad * 0.8f, -rad * 0.6f );
        gr.draw( LINE );
        
        LINE.setLine( -rad * 0.6f, -rad * 0.4f, -rad * 0.6f, rad * 0.4f );
        gr.draw( LINE );
        
        gr.setTransform( popped );
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

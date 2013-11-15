package asteroids.j2d;

import static asteroids.Components.COLOR;
import static asteroids.Components.POSITION;
import static asteroids.Components.RADIUS;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import org.magnos.entity.Entity;
import org.magnos.entity.Renderer;

import asteroids.Vector;


public class Java2DParticleRenderer implements Renderer
{

    public static final Ellipse2D.Float ELLIPSE = new Ellipse2D.Float( -1, -1, 2, 2 );
    
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
        float rad = e.get( RADIUS ).v;
        Color clr = e.get( COLOR );
        
        ELLIPSE.setFrame( pos.x - rad, pos.y - rad, rad * 2, rad * 2 );
        
        gr.setColor( clr );
        gr.draw( ELLIPSE );
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

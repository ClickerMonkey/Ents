package asteroids.j2d;

import static asteroids.Components.ANGLE;
import static asteroids.Components.COLOR;
import static asteroids.Components.POSITION;
import static asteroids.Components.RADIUS;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Random;

import org.magnos.entity.Entity;
import org.magnos.entity.Renderer;

import asteroids.Vector;

public class Java2DAsteroidRenderer implements Renderer
{

    public float dentPercent;
    public int spokeCount;
    public Random random;
    public Path2D.Float path;
    
    public Java2DAsteroidRenderer(float dentPercent, int spokeCount, Random random)
    {
        this( dentPercent, spokeCount, random, null );
    }
    
    public Java2DAsteroidRenderer(float dentPercent, int spokeCount, Random random, Path2D.Float path)
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

    @Override
    public void end( Entity e, Object drawState )
    {
        
    }

    @Override
    public void destroy( Entity e )
    {
        
    }

}

package org.magnos.entity.asteroids.j2d;

import static org.magnos.entity.asteroids.Components.COLOR;
import static org.magnos.entity.asteroids.Components.POSITION;
import static org.magnos.entity.asteroids.Components.VELOCITY;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import org.magnos.entity.Entity;
import org.magnos.entity.Renderer;

import com.gameprogblog.engine.Vector;


public class LaserRenderer implements Renderer
{
    
    public static final Line2D.Float LINE = new Line2D.Float();
    public static final Color TRANSPARENT = new Color( 0, 0, 0, 0 );
    public static final Stroke STROKE = new BasicStroke( 3.0f );
    
    public float tailLength;
    
    public LaserRenderer(float tailLength)
    {
        this.tailLength = tailLength;
    }
    
    @Override
    public Renderer create( Entity e )
    {
        return this;
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

    @Override
    public void end( Entity e, Object drawState )
    {
        
    }   

    @Override
    public void destroy( Entity e )
    {

    }

}

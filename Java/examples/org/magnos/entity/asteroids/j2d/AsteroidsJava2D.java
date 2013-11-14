package org.magnos.entity.asteroids.j2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import org.magnos.entity.Ents;
import org.magnos.entity.asteroids.Asteroids;
import org.magnos.entity.asteroids.Views;

import com.gameprogblog.engine.Game;
import com.gameprogblog.engine.GameLoop;
import com.gameprogblog.engine.GameLoopVariable;
import com.gameprogblog.engine.GameScreen;
import com.gameprogblog.engine.GameState;
import com.gameprogblog.engine.Scene;
import com.gameprogblog.engine.input.GameInput;


public class AsteroidsJava2D implements Game
{
    public static void main( String[] args )
    {
       Game game = new AsteroidsJava2D();
       GameLoop loop = new GameLoopVariable( 0.1f );
       GameScreen screen = new GameScreen( 640, 480, Color.black, true, loop, game );
       GameScreen.showWindow( screen, "Asteroids" );
    }
    
    public static final float SHIP_SPEED = 300;
    public static final Color LEVEL_COLOR = new Color( 255, 255, 255, 200 );
    
    public Random random = new Random();
    public Asteroids a;
    public boolean playing;

    @Override
    public void start( Scene scene )
    {
        Ents.setViewDefault( Views.SHIP, new ShipRenderer() );
        Ents.setViewDefault( Views.ASTEROID, new AsteroidRenderer( 0.3f, 20, random ) );
        Ents.setViewDefault( Views.LASER, new LaserRenderer( 0.1f ) );
        Ents.setViewDefault( Views.PARTICLE, new ParticleRenderer() );
        
        a = new Asteroids();
        a.random = random;
        a.shipBrakes = true;
        a.setSize( scene.width, scene.height );
        a.reset();
        a.spawn( Asteroids.INITIAL_SPAWN_COUNT );
        
        playing = true;
    }

    @Override
    public void input( GameInput input )
    {
        if (input.keyDown[KeyEvent.VK_ESCAPE])
        {
            playing = false;
        }
        
        if (input.keyUp[KeyEvent.VK_R])
        {
            a.setState( Asteroids.GameState.DEATH );
        }
        
        if (input.keyUp[KeyEvent.VK_B])
        {
            a.shipBrakes = !a.shipBrakes;
        }
        
        a.shipForce.x = (input.keyDown[KeyEvent.VK_A] ? -1 : (input.keyDown[KeyEvent.VK_D] ? 1 : 0)) * SHIP_SPEED;
        a.shipForce.y = (input.keyDown[KeyEvent.VK_W] ? -1 : (input.keyDown[KeyEvent.VK_S] ? 1 : 0)) * SHIP_SPEED;
        a.shipLook.set( input.mouseX, input.mouseY );
        a.shooting = (input.keyDown[KeyEvent.VK_SPACE] || input.mouseDownCount > 0);
    }

    @Override
    public void update( GameState state, Scene scene )
    {
        a.update( state.seconds );
    }

    @Override
    public void draw( GameState state, Graphics2D gr, Scene scene )
    {
        a.draw( gr );

        gr.setFont( gr.getFont().deriveFont( 24.0f ) );
        gr.setColor( LEVEL_COLOR );
        gr.drawString( String.format( "%d", Asteroids.INITIAL_SPAWN_COUNT - a.spawnCount ), 10, 22 );
    }

    @Override
    public void destroy()
    {

    }

    @Override
    public boolean isPlaying()
    {
        return playing;
    }

}

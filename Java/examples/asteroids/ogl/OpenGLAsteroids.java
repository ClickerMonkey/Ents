
package asteroids.ogl;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import org.magnos.entity.Ents;

import asteroids.Asteroids;
import asteroids.Views;

/**
 * {@link Asteroids} with OpenGL (LWJGL) graphics.
 * 
 * @author Philip Diffenderfer
 *
 */
public class OpenGLAsteroids
{

	public static void main( String[] args ) throws Exception
	{
		Display.setDisplayMode( new DisplayMode( WIDTH, HEIGHT ) );
		Display.setTitle( "Asteroids" );
		Display.setInitialBackground( 0.0f, 0.0f, 0.0f );
		Display.create();
		
		Keyboard.create();
		Keyboard.enableRepeatEvents( false );
		Mouse.create();
		Mouse.setGrabbed( false );
		
		OpenGLAsteroids game = new OpenGLAsteroids();
		game.run();

		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}

	public static final float SHIP_SPEED = 300;
	public static final Color LEVEL_COLOR = new Color( 255, 255, 255, 200 );
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final float COMPONENT_SCALE = 1.0f / 255.0f;
	
	public Random random = new Random();
	public Asteroids a;
	public boolean playing;
	public boolean paused;

	public boolean[] keyDown = new boolean[ 256 ];
	public boolean[] keyUp = new boolean[ 256 ];
	public OpenGLText text;
	
	public void run()
	{
		start();

		long time = System.nanoTime();

		Display.update();
		
		while (playing && !Display.isCloseRequested())
		{
			long currentTime = System.nanoTime();
			float dt = (currentTime - time) * 0.000000001f;

			input();

			if (!paused)
			{
				update( dt );
			}
			
			glClear( GL_COLOR_BUFFER_BIT );

			draw();

			Display.update();
			
			time = currentTime;
		}
	}

	public void start()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, -1, 1 );
		glMatrixMode(GL_MODELVIEW);
		
		glDisable( GL_TEXTURE_2D );
		glDisable( GL_CULL_FACE );
		glDisable( GL_DEPTH_TEST );
		glEnable( GL_BLEND );
		glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
		glEnable( GL_LINE_SMOOTH );
		glHint( GL_LINE_SMOOTH_HINT,  GL_NICEST );
		
		errorCheck();
		
		Ents.setViewDefault( Views.SHIP, new OpenGLShipRenderer() );
		Ents.setViewDefault( Views.ASTEROID, new OpenGLAsteroidRenderer( 0.3f, 20, random ) );
		Ents.setViewDefault( Views.LASER, new OpenGLLaserRenderer( 0.1f ) );
		Ents.setViewDefault( Views.PARTICLE, new OpenGLParticleRenderer() );
		Ents.setViewDefault( Views.PARTICLE_SYSTEM, new OpenGLParticleSystemRenderer() );
		
		a = new Asteroids();
		a.random = random;
		a.shipBrakes = true;
		a.setSize( WIDTH, HEIGHT );
		a.reset();
		a.spawn( Asteroids.INITIAL_SPAWN_COUNT );

		text = new OpenGLText();
		
		playing = true;
	}

	public void input()
	{
		for (int i = 0; i < 256; i++)
		{
			boolean down = Keyboard.isKeyDown( i ); 
			keyUp[i] = !down && keyDown[i];
			keyDown[i] = down;
		}
		
		if (keyDown[Keyboard.KEY_ESCAPE])
		{
			playing = false;
		}
		
		if (keyUp[Keyboard.KEY_R])
		{
			a.setState( Asteroids.GameState.DEATH );
		}

		if (keyUp[Keyboard.KEY_B])
		{
			a.shipBrakes = !a.shipBrakes;
		}
		
		if (keyUp[Keyboard.KEY_P])
		{
			paused = !paused;
		}

		a.shipForce.x = (keyDown[Keyboard.KEY_A] ? -1 : (keyDown[Keyboard.KEY_D] ? 1 : 0)) * SHIP_SPEED;
		a.shipForce.y = (keyDown[Keyboard.KEY_W] ? -1 : (keyDown[Keyboard.KEY_S] ? 1 : 0)) * SHIP_SPEED;
		a.shipLook.set( Mouse.getX(), HEIGHT - Mouse.getY() );
		a.shooting = (keyDown[Keyboard.KEY_SPACE] || Mouse.isButtonDown( 0 ));
	}

	public void update( float dt )
	{
		a.update( dt );
	}

	public void draw()
	{
		a.draw( null );

		bind( LEVEL_COLOR );
		
		text.drawString( 10, 10, 10, 16, 1.3f, 2.0f, "LEVEL %d", a.spawnCount - Asteroids.INITIAL_SPAWN_COUNT + 1 );
		
		if (paused)
		{
			text.drawString( 210, 220, 30, 42, 1.3f, 4.0f, "PAUSED" );	
		}
		
		errorCheck();
	}

	public void destroy()
	{
		a.delete();
	}
	
	public static void errorCheck()
	{
		int err = GL_NO_ERROR;
		
		while ((err = glGetError()) != GL_NO_ERROR) 
		{
			System.out.println( GLU.gluErrorString( err ) );
		}		
	}
	
	public static void bind( Color c )
	{
		glColor4f( c.getRed() * COMPONENT_SCALE, c.getGreen() * COMPONENT_SCALE, c.getBlue() * COMPONENT_SCALE, c.getAlpha() * COMPONENT_SCALE );
	}
	
}

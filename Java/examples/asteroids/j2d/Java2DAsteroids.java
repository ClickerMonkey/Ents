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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.magnos.entity.Ents;

import asteroids.Asteroids;
import asteroids.Vector;
import asteroids.Views;

/**
 * {@link Asteroids} with Java2D graphics.
 * 
 * @author Philip Diffenderfer
 *
 */
public class Java2DAsteroids extends JPanel implements KeyListener, MouseListener, MouseMotionListener
{

	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args)
	{
		Java2DAsteroids game = new Java2DAsteroids();
		game.setPreferredSize( new Dimension( WIDTH, HEIGHT ) );
		game.setFocusable( true );
		game.addKeyListener( game );
		game.addMouseListener( game );
		game.addMouseMotionListener( game );
		
		JFrame frame = new JFrame( "Asteroids" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.add( game );
		frame.pack();
		frame.setVisible( true );
		
		game.run();
		
		frame.dispose();
	}
	
	public static final float SHIP_SPEED = 300;
	public static final Color LEVEL_COLOR = new Color( 255, 255, 255, 200 );
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	public BufferedImage buffer;
	public Random random = new Random();
	public Asteroids a;
	public boolean playing;
	public boolean paused;
	
	public boolean[] keyDown = new boolean[ 256 ];
	public boolean[] keyUp = new boolean[ 256 ];
	public Vector mouse = new Vector();
	public int mouseDownCount;

	public void start()
	{
		Ents.setViewDefault( Views.SHIP, new Java2DShipRenderer() );
		Ents.setViewDefault( Views.ASTEROID, new Java2DAsteroidRenderer( 0.3f, 20, random ) );
		Ents.setViewDefault( Views.LASER, new Java2DLaserRenderer( 0.1f ) );
		Ents.setViewDefault( Views.PARTICLE, new Java2DParticleRenderer() );
		
		a = new Asteroids();
		a.random = random;
		a.shipBrakes = true;
		a.setSize( WIDTH, HEIGHT );
		a.reset();
		a.spawn( Asteroids.INITIAL_SPAWN_COUNT );
		
		buffer = new BufferedImage( WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR );
		
		playing = true;
	}
	
	public void input()
	{
		if (keyDown[KeyEvent.VK_ESCAPE])
		{
			playing = false;
		}

		if (keyUp[KeyEvent.VK_R])
		{
			a.setState( Asteroids.GameState.DEATH );
		}

		if (keyUp[KeyEvent.VK_B])
		{
			a.shipBrakes = !a.shipBrakes;
		}
		
		if (keyUp[KeyEvent.VK_P])
		{
			paused = !paused;
		}

		a.shipForce.x = (keyDown[KeyEvent.VK_A] ? -1 : (keyDown[KeyEvent.VK_D] ? 1 : 0)) * SHIP_SPEED;
		a.shipForce.y = (keyDown[KeyEvent.VK_W] ? -1 : (keyDown[KeyEvent.VK_S] ? 1 : 0)) * SHIP_SPEED;
		a.shipLook.set( mouse );
		a.shooting = (keyDown[KeyEvent.VK_SPACE] || mouseDownCount > 0);
	}

	public void update( float dt )
	{
		a.update( dt );
	}

	public void draw( Graphics2D gr )
	{
		a.draw( gr );

		gr.setFont( gr.getFont().deriveFont( 24.0f ) );
		gr.setColor( LEVEL_COLOR );
		gr.drawString( String.format( "Level %d", a.spawnCount - Asteroids.INITIAL_SPAWN_COUNT + 1 ), 10, 22 );
		
		if (paused)
		{
			gr.setFont( gr.getFont().deriveFont( 64.0f ) );
			gr.drawString( "PAUSED", 180, 270 );
		}
	}

	public void destroy()
	{
		a.delete();
	}

    public void run()
    {
        start();
        
        long time = System.nanoTime();
        
        while (playing)
        {
            long currentTime = System.nanoTime();
            float dt = (currentTime - time) * 0.000000001f;
            
            input();
            
            for (int i = 0; i < keyUp.length; i++)
            {
                keyUp[i] = false;
            }
            
            if (!paused)
            {
                update( dt );   
            }
            
            Graphics2D g2d = buffer.createGraphics();
            g2d.setColor( Color.black );
            g2d.fillRect( 0, 0, WIDTH, HEIGHT );
            g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            
            draw( g2d );
            
            Graphics gr = getGraphics();
            gr.drawImage( buffer, 0, 0, this );
            
            time = currentTime;
        }
        
        destroy();
    }

	@Override
	public void keyTyped( KeyEvent e )
	{
		
	}

	@Override
	public void keyPressed( KeyEvent e )
	{
		keyDown[ e.getKeyCode() & 0xFF ] = true;
	}

	@Override
	public void keyReleased( KeyEvent e )
	{
		keyDown[ e.getKeyCode() & 0xFF ] = false;
		keyUp[ e.getKeyCode() & 0xFF ] = true;
	}

	@Override
	public void mouseClicked( MouseEvent e )
	{
		
	}

	@Override
	public void mousePressed( MouseEvent e )
	{
		mouseDownCount++;
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
		mouseDownCount--;
	}

	@Override
	public void mouseEntered( MouseEvent e )
	{
		
	}

	@Override
	public void mouseExited( MouseEvent e )
	{
		
	}

	@Override
	public void mouseDragged( MouseEvent e )
	{
		mouse.set( e.getX(), e.getY() );
	}

	@Override
	public void mouseMoved( MouseEvent e )
	{
		mouse.set( e.getX(), e.getY() );
	}

}

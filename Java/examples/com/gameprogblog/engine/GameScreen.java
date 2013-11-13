
package com.gameprogblog.engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.gameprogblog.engine.input.GameInput;


/**
 * 
 * @author Philip Diffenderfer
 * 
 */
public class GameScreen extends JPanel
{

	private static final long serialVersionUID = 1L;

	// The double buffer used to eliminate flickering.
	private BufferedImage buffer;

	// The flag to have antialiasing for drawing (smoother drawing).
	private boolean antialising;

	// The graphics object used for drawing.
	private Graphics2D graphics;

	// The loop that's executed in the game loop.
	private GameLoop loop;

	// The game that's being played.
	private Game game;

	// The state of the game.
	private GameState state;

	// The input of the game.
	private GameInput input;

	// The scene in the game.
	private Scene scene;

	/**
	 * Instantiates a new screen to play a game.
	 * 
	 * @param width
	 *        The width of the screen in pixels.
	 * @param height
	 *        The height of the screen in pixels.
	 * @param antialiasing
	 *        True if drawing should be smooth, otherwise false.
	 * @param loop
	 *        The loop implementation to use.
	 * @param game
	 *        The game to play.
	 */
	public GameScreen( int width, int height, Color backgroundColor, boolean antialiasing, GameLoop loop, Game game )
	{
		Dimension d = new Dimension( width, height );
		this.setSize( d );
		this.setPreferredSize( d );
		this.setFocusable( true );
		this.setBackground( backgroundColor );

		this.antialising = antialiasing;
		this.buffer = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
		this.loop = loop;
		this.game = game;
		this.state = new GameState();
		this.input = new GameInput();
		this.scene = new Scene( 0, 0, width, height );
	}

	/**
	 * Starts the game loop until the game is no longer being played.
	 */
	public void start()
	{
		input.mouseInside = getParent().contains( MouseInfo.getPointerInfo().getLocation() );

		addKeyListener( input );
		addMouseListener( input );
		addMouseMotionListener( input );

		resetGraphics();

		game.start( scene );

		loop.onStart( game, state );

		while (game.isPlaying())
		{
			// If the loop has called the draw method of the game, render the
			// graphics to the screen and then reset them for the next frame.
			if (loop.onLoop( game, state, input, graphics, scene ))
			{
				renderGraphics( getGraphics() );
				resetGraphics();
			}
		}

		game.destroy();

		System.exit( 0 );
	}

	@Override
	public final void paint( Graphics g )
	{
		if (g == null || buffer == null)
		{
			return;
		}

		// Attempt to draw this frame. Occasionally there are random errors.
		try
		{
			resetGraphics();
			renderGraphics( g );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void renderGraphics( Graphics gr )
	{
		gr.drawImage( buffer, 0, 0, this );
		gr.dispose();
	}

	/**
	 * Resets the graphics which get drawn on to prepare for another frame.
	 */
	private void resetGraphics()
	{
		// Get the graphics of the buffer
		graphics = (Graphics2D)buffer.getGraphics();

		// Clear the buffer with the background color
		graphics.setColor( getBackground() );
		graphics.fillRect( 0, 0, getWidth(), getHeight() );

		// If antialiasing is turned on enable it.
		if (antialising)
		{
			graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		}
	}

	/**
	 * Overrides the update method to enable double buffering.
	 */
	@Override
	public final void update( Graphics g )
	{
		paint( g );
	}

	public void setLoop( GameLoop gameLoop )
	{
		if (loop != null)
		{
			gameLoop.onStart( game, state );
		}

		loop = gameLoop;
	}

	/**
	 * Shows the given GameScreen in a window with the given title.
	 * 
	 * @param gameScreen
	 *        The GameScreen to add to the window.
	 * @param title
	 *        The title of the window.
	 */
	public static void showWindow( GameScreen gameScreen, String title )
	{
		if (gameScreen != null)
		{
			JFrame window = new JFrame( title );
			window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			window.add( gameScreen );
			window.pack();
			window.setVisible( true );

			gameScreen.start();
		}
	}

}

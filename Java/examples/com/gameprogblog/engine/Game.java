
package com.gameprogblog.engine;

import java.awt.Graphics2D;

import com.gameprogblog.engine.input.GameInput;


/**
 * A game which is played.
 * 
 * @author Philip Diffenderfer
 */
public interface Game
{

	/**
	 * Called once when the game starts, before any input, update, or draw
	 * occurs.
	 */
	public void start( Scene scene );

	/**
	 * Called before every update with the current state of the input and queues
	 * of key and mouse events that have occurred since this method was last
	 * called.
	 * 
	 * @param input
	 *        The input state of the game.
	 */
	public void input( GameInput input );

	/**
	 * Update the game based on the given state.
	 * 
	 * @param state
	 *        The state of the game.
	 */
	public void update( GameState state, Scene scene );

	/**
	 * Draw the game based on the given state and with the given graphics.
	 * 
	 * @param state
	 *        The state of the game.
	 * @param gr
	 *        The graphics to render to.
	 */
	public void draw( GameState state, Graphics2D gr, Scene scene );

	/**
	 * Called once when the game ends, after all input, update, and draws end.
	 */
	public void destroy();

	/**
	 * Determines whether the game is still being played. If the game is not
	 * being played the game loop will stop and the game will be destroyed.
	 * 
	 * @return True if the game is still being played, false if the game is ready
	 *         to be stopped and destroyed.
	 */
	public boolean isPlaying();

}


package com.gameprogblog.engine;

import java.awt.Graphics2D;

import com.gameprogblog.engine.input.GameInput;


public interface GameLoop
{

	public void onStart( Game game, GameState state );

	public boolean onLoop( Game game, GameState state, GameInput input, Graphics2D gr, Scene scene );
}

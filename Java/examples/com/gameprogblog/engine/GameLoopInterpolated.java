
package com.gameprogblog.engine;

import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;

import com.gameprogblog.engine.input.GameInput;


public class GameLoopInterpolated implements GameLoop
{

	public long frameRate;
	public int maxUpdates;
	public boolean yield = false;
	private long time;

	public GameLoopInterpolated( int maxUpdates, double frameRate )
	{
		this.maxUpdates = maxUpdates;
		this.frameRate = (long)(frameRate * 1000000000L);
	}

	public GameLoopInterpolated( int maxUpdates, long frameRate, TimeUnit timeUnit )
	{
		this( maxUpdates, frameRate, timeUnit, false );
	}

	public GameLoopInterpolated( int maxUpdates, long frameRate, TimeUnit timeUnit, boolean yield )
	{
		this.maxUpdates = maxUpdates;
		this.frameRate = timeUnit.toNanos( frameRate );
		this.yield = yield;
	}

	@Override
	public void onStart( Game game, GameState state )
	{
		state.reset();
		state.setElapsed( frameRate );
	}

	@Override
	public boolean onLoop( Game game, GameState state, GameInput input, Graphics2D gr, Scene scene )
	{
		long nanosElapsed = state.tick();

		time += nanosElapsed;

		int updateCount = 0;

		while (time >= frameRate && updateCount < maxUpdates)
		{
			game.input( input );
			input.clear();

			if (!game.isPlaying())
			{
				return false;
			}

			state.update();
			scene.update();
			game.update( state, scene );
			
			if (!game.isPlaying())
			{
				return false;
			}

			updateCount++;

			time -= frameRate;
		}

		state.interpolate = getStateInterpolation();
		state.forward = state.interpolate * state.seconds;
		state.backward = state.forward - state.seconds;

		state.draw();
		scene.draw( state, gr );
		game.draw( state, gr, scene );

		if (yield)
		{
			long actualTime = time + state.getElapsedSinceTick();
			long remainingTime = frameRate - actualTime;

			if (remainingTime > 0)
			{
				Thread.yield();
			}
		}

		return true;
	}

	public float getStateInterpolation()
	{
		return (float)((double)time / (double)frameRate);
	}

}

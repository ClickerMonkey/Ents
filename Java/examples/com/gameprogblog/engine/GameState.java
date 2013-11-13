
package com.gameprogblog.engine;

import java.util.concurrent.TimeUnit;


public class GameState
{

	public float seconds;
	public long millis;
	public long nanos;
	public long micros;
	public long startTime;
	public long lastTime;
	public long currentTime;
	public float forward = 0;
	public float backward = 0;
	public float interpolate = 0;
	public TimeTracker updateTracker;
	public TimeTracker drawTracker;

	public GameState()
	{
		updateTracker = new TimeTracker( "Updates-per-second: %.1f", 500, TimeUnit.MILLISECONDS );
		drawTracker = new TimeTracker( "Draws-per-second: %.1f", 500, TimeUnit.MILLISECONDS );
	}

	public long reset()
	{
		long resetTime = System.nanoTime();

		startTime = resetTime;
		lastTime = resetTime;
		currentTime = resetTime;

		updateTracker.reset();
		drawTracker.reset();

		return resetTime;
	}

	public long tick()
	{
		lastTime = currentTime;
		currentTime = System.nanoTime();
		return (currentTime - lastTime);
	}

	public long getElapsedSinceTick()
	{
		return (System.nanoTime() - currentTime);
	}

	public void setElapsed( long nanosElapsed )
	{
		nanos = nanosElapsed;
		micros = nanosElapsed / 1000L;
		millis = nanosElapsed / 1000000L;
		seconds = (float)(nanosElapsed * 0.000000001);
	}

	public void update()
	{
		updateTracker.update();
	}

	public void draw()
	{
		drawTracker.update();
	}

}

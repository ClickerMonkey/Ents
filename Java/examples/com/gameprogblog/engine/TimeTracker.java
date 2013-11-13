
package com.gameprogblog.engine;

import java.util.concurrent.TimeUnit;


public class TimeTracker
{

	public long lastTime;
	public long time;
	public long interval;
	public long frames;
	public double rate;
	public String rateFormat;
	public String rateString;
	public char[] rateText;

	public TimeTracker( String rateStringFormat, long refreshInterval, TimeUnit unit )
	{
		rateFormat = rateStringFormat;
		setRefreshInterval( refreshInterval, unit );
	}

	public void setRefreshInterval( long refreshInterval, TimeUnit unit )
	{
		interval = unit.toNanos( refreshInterval );
	}

	public void reset()
	{
		lastTime = System.nanoTime();
		time = 0;
		frames = 0;
		rate = 0;
		updateRateString();
	}

	public void update()
	{
		long currentTime = System.nanoTime();
		long elapsed = (currentTime - lastTime);

		time += elapsed;
		frames++;

		if (time >= interval)
		{
			rate = (frames / (time * 0.000000001));
			updateRateString();

			time -= interval;
			frames = 0;
		}

		lastTime = currentTime;
	}

	private void updateRateString()
	{
		rateString = String.format( rateFormat, rate );
		rateText = rateString.toCharArray();
	}

}

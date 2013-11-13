
package com.gameprogblog.engine;

import java.awt.Graphics2D;

public class Scene
{

	public final Camera camera;
	public int x, y, width, height;

	public Scene( int x, int y, int w, int h )
	{
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.camera = new Camera( w, h );
	}

	public void draw( GameState state, Graphics2D gr )
	{
		camera.draw( state, gr );
	}

	public void update()
	{
		camera.update();
	}

	public Vector getWorldCoordinate( Vector mouse, Vector out )
	{
		return getWorldCoordinate( mouse.x, mouse.y, out );
	}

	public Vector getWorldCoordinate( float mouseX, float mouseY, Vector out )
	{
		out.x = getWorldCoordinateX( mouseX );
		out.y = getWorldCoordinateY( mouseY );

		return out;
	}

	public float getWorldCoordinateX( float mouseX )
	{
		return camera.bounds.left + camera.bounds.getWidth() * (mouseX - x) / width;
	}

	public float getWorldCoordinateY( float mouseY )
	{
		return camera.bounds.top + camera.bounds.getHeight() * (mouseY - y) / height;
	}

}

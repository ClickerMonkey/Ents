
package com.gameprogblog.engine;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


public class Camera
{

	public final Bound2 extents = new Bound2();
	public final Vector center = new Vector();
	public final Vector scale = new Vector( 1.0f, 1.0f );
	public final Bound2 bounds = new Bound2();
	public final Bound2 world = new Bound2();
	public final AffineTransform transform = new AffineTransform();
	public boolean containedInWorld = false;
	private final Vector lastCenter = new Vector();
	public final Vector actualCenter = new Vector();

	// TODO rotation

	public Camera( int width, int height )
	{
		extents.left = extents.right = center.x = width * 0.5f;
		extents.top = extents.bottom = center.y = height * 0.5f;
	}

	public void update()
	{
		lastCenter.set( center );
	}

	public void draw( GameState state, Graphics2D gr )
	{
		actualCenter.x = (center.x - lastCenter.x) * state.interpolate + lastCenter.x;
		actualCenter.y = (center.y - lastCenter.y) * state.interpolate + lastCenter.y;

		updateBounds();

		transform.setToIdentity();
		// TODO rotation
		transform.scale( scale.x, scale.y );
		transform.translate( -actualCenter.x, -actualCenter.y );
		transform.translate( extents.left, extents.top );

		gr.setTransform( transform );
	}

	public void updateBounds()
	{
		bounds.left = (actualCenter.x - extents.left) * scale.x;
		bounds.right = (actualCenter.x + extents.right) * scale.x;
		bounds.top = (actualCenter.y - extents.top) * scale.y;
		bounds.bottom = (actualCenter.y + extents.bottom) * scale.y;

		// TODO rotation

		if (containedInWorld)
		{
			float dy = 0.0f;
			float dx = 0.0f;

			if (bounds.top < world.top)
			{
				dy = world.top - bounds.top;
			}

			if (bounds.bottom > world.bottom)
			{
				dy = world.bottom - bounds.bottom;
			}

			if (bounds.left < world.left)
			{
				dx = world.left - bounds.left;
			}

			if (bounds.right > world.right)
			{
				dx = world.right - bounds.right;
			}

			actualCenter.x += dx * scale.x;
			actualCenter.y += dy * scale.y;
			bounds.translate( dx, dy );
		}
	}

}

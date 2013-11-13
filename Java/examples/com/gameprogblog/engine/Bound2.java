
package com.gameprogblog.engine;

import com.gameprogblog.engine.Vector;

public class Bound2
{

	public float left, right, top, bottom;

	public Bound2()
	{
	}

	public Bound2( Bound2 b )
	{
		set( b );
	}

	public Bound2( float left, float top, float right, float bottom )
	{
		set( left, top, right, bottom );
	}

	public void set( Bound2 b )
	{
		set( b.left, b.top, b.right, b.bottom );
	}

	public void set( float left, float top, float right, float bottom )
	{
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public void union( Bound2 b )
	{
		left = Math.min( left, b.left );
		right = Math.max( right, b.right );
		top = Math.min( top, b.top );
		bottom = Math.max( bottom, b.bottom );
	}

	public boolean intersects( Bound2 b )
	{
		return !(left > b.right || right < b.left || top > b.bottom || bottom < b.top);
	}

	public void set( Vector position, Bound2 extent )
	{
		left = position.x - extent.left;
		right = position.x + extent.right;
		top = position.y - extent.top;
		bottom = position.y + extent.bottom;
	}

	public void grow( float dx, float dy )
	{
		left -= dx;
		right += dx;
		top -= dy;
		bottom += dy;
	}

	public void translate( float dx, float dy )
	{
		left += dx;
		right += dx;
		top += dy;
		bottom += dy;
	}

	public void moveLeft( float newLeft )
	{
		right = newLeft + getWidth();
		left = newLeft;
	}

	public void moveRight( float newRight )
	{
		left = newRight - getWidth();
		right = newRight;
	}

	public void moveTop( float newTop )
	{
		bottom = newTop + getHeight();
		top = newTop;
	}

	public void moveBottom( float newBottom )
	{
		top = newBottom - getHeight();
		bottom = newBottom;
	}

	public float getWidth()
	{
		return (right - left);
	}

	public float getHeight()
	{
		return (bottom - top);
	}

	public float cx()
	{
		return (left + right) * 0.5f;
	}

	public float cy()
	{
		return (top + bottom) * 0.5f;
	}

	public float dx( float d )
	{
		return (right - left) * d + left;
	}

	public float dy( float d )
	{
		return (bottom - top) * d + top;
	}

	public static Bound2 union( Bound2 x, Bound2 y )
	{
		return new Bound2(
			Math.min( x.left, y.left ),
			Math.min( x.top, y.top ),
			Math.max( x.right, y.right ),
			Math.max( x.bottom, y.bottom ) );
	}

	@Override
	public String toString()
	{
		return "Bound2 [left=" + left + ", right=" + right + ", top=" + top + ", bottom=" + bottom + "]";
	}

}

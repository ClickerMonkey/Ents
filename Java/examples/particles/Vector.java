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

package particles;



/**
 * 
 * Operations in this class are in the following format:
 * 
 * <pre>
 *      // The operation is performed and the result is set to the out vector which is then returned.
 *      public Vector operation( <i>parameters</i>, Vector out );
 *      
 *      // The operation is performed and the result is set to this vector which is then returned.
 *      // This is the same as "return operation( <i>parameters</i>, this );"
 *      public Vector operationi( <i>parameters</i>);
 *      
 *      // The operation is performed and the result is set to a new vector which is then returned.
 *      // This is the same as "return operation( <i>parameters</i>, new Vector() );"
 *      public Vector operation( <i>parameters</i> );
 * </pre>
 * 
 * Properties of unit (normalized) vectors:
 * <ol>
 * <li>{@link #length()} and {@link #lengthSq()} return 1.0f</li>
 * <li>{@link #isUnit()} and {@link #isUnit(float)} return true</li>
 * <li>x = cos(A) where A is the angle of the vector (returned by
 * {@link #angle()}).</li>
 * <li>y = sin(A) where A is the angle of the vector (returned by
 * {@link #angle()}).</li>
 * <li>passing in {@link #angle()} to {@link #rotatei(float)} is the same as
 * passing the vector into {@link #rotatei(Vector)} because of the two
 * properties mentioned above except the latter method is quicker since
 * {@link Math#cos(double)} and {@link Math#sin(double)} don't need to be
 * called.</li>
 * <li>It's an efficient way of storing an angle.</li>
 * <li>Can be used as the normal parameter in {@link #reflect(Vector)} methods.</li>
 * </ol>
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Vector implements Attribute<Vector>
{

    /**
     * Returns a vector with all components set to zero. If this is directly
     * modified or passed to a function that may modify it, it will change for
     * all references of this value. This should strictly be used as a constant.
     */
    public static final Vector ZERO = new Vector( 0, 0 );

    /**
     * Returns a vector with all components set to one. If this is directly
     * modified or passed to a function that may modify it, it will change for
     * all references of this value. This should strictly be used as a constant.
     */
    public static final Vector ONE = new Vector( 1, 1 );

    /**
     * Returns a unit vector along the x-axis in the positive direction.
     */
    public static final Vector RIGHT = new Vector( 1, 0 );

    /**
     * Returns a unit vector along the x-axis in the negative direction.
     */
    public static final Vector LEFT = new Vector( 1, 0 );

    /*
     * Returns a unit vector along the y-axis in the positive direction.
     */
    public static final Vector TOP = new Vector( 0, 1 );

    /**
     * Returns a unit vector along the y-axis in the negative direction.
     */
    public static final Vector BOTTOM = new Vector( 0, -1 );

    /**
     * Constant used to fix the angle returned by {@link #angle()} and
     * {@link #angleTo(Vector)}.
     */
    private static final float ANGLE_FIX = (float)(Math.PI * 2.0f);

    /**
     * The x-coordinate of the Vector.
     */
    public float x;

    /**
     * The y-coordinate of the Vector.
     */
    public float y;

    /**
     * Instantiates a new Vector at the origin.
     */
    public Vector()
    {
    }

    /**
     * Instantiates a new Vector at the specified coordinates.
     * 
     * @param x
     *        The initial x-coordinate of the vector.
     * @param y
     *        The initial y-coordinate of the vector.
     */
    public Vector( float x, float y )
    {
        set( x, y );
    }

    /**
     * Instantiates a new Vector based on another Vector.
     * 
     * @param v
     *        The vector to copy x and y coordinates from.
     */
    public Vector( Vector v )
    {
        set( v );
    }

    /**
     * Sets the coordinates of this vector and returns this.
     */
    public Vector set( float x, float y )
    {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Sets the coordinates of this vector and returns this.
     */
    public Vector set( Vector v )
    {
        x = v.x;
        y = v.y;
        return this;
    }

    /**
     * Clears this vector's components by setting them to zero.
     */
    public void clear()
    {
        x = y = 0.0f;
    }

    /**
     * Negates this vector and returns this.
     */
    public Vector negi()
    {
        return neg( this );
    }

    /**
     * Sets out to the negation of this vector and returns out.
     */
    public Vector neg( Vector out )
    {
        out.x = -x;
        out.y = -y;
        return out;
    }

    /**
     * Returns a new vector that is the negation to this vector.
     */
    public Vector neg()
    {
        return neg( new Vector() );
    }

    /**
     * Sets this vector to it's absolute value and returns this.
     */
    public Vector absi()
    {
        return abs( this );
    }

    /**
     * Sets out to the absolute value of this vector and returns out.
     */
    public Vector abs( Vector out )
    {
        out.x = x < 0 ? -x : x;
        out.y = y < 0 ? -y : y;

        return out;
    }

    /**
     * Returns a new vector that is the absolute value of this vector.
     */
    public Vector abs()
    {
        return abs( new Vector() );
    }

    /**
     * Multiplies this vector by s and returns this.
     */
    public Vector muli( float s )
    {
        return mul( s, this );
    }

    /**
     * Sets out to this vector multiplied by s and returns out.
     */
    public Vector mul( float s, Vector out )
    {
        out.x = s * x;
        out.y = s * y;
        return out;
    }

    /**
     * Returns a new vector that is a multiplication of this vector and s.
     */
    public Vector mul( float s )
    {
        return mul( s, new Vector() );
    }

    /**
     * Divides this vector by s and returns this.
     */
    public Vector divi( float s )
    {
        return div( s, this );
    }

    /**
     * Sets out to the division of this vector and s and returns out.
     */
    public Vector div( float s, Vector out )
    {
        if (s != 0.0f)
        {
            s = 1.0f / s;

            out.x = x * s;
            out.y = y * s;
        }
        return out;
    }

    /**
     * Returns a new vector that is a division between this vector and s.
     */
    public Vector div( float s )
    {
        return div( s, new Vector() );
    }

    /**
     * Adds s to this vector and returns this.
     */
    public Vector addi( float s )
    {
        return add( s, this );
    }

    /**
     * Sets out to the sum of this vector and s and returns out.
     */
    public Vector add( float s, Vector out )
    {
        out.x = x + s;
        out.y = y + s;
        return out;
    }

    /**
     * Returns a new vector that is the sum between this vector and s.
     */
    public Vector add( float s )
    {
        return add( s, new Vector() );
    }

    /**
     * Multiplies this vector by v and returns this.
     */
    public Vector muli( Vector v )
    {
        return mul( v, this );
    }

    /**
     * Sets out to the product of this vector and v and returns out.
     */
    public Vector mul( Vector v, Vector out )
    {
        out.x = x * v.x;
        out.y = y * v.y;
        return out;
    }

    /**
     * Returns a new vector that is the product of this vector and v.
     */
    public Vector mul( Vector v )
    {
        return mul( v, new Vector() );
    }

    /**
     * Divides this vector by v and returns this.
     */
    public Vector divi( Vector v )
    {
        return div( v, this );
    }

    /**
     * Sets out to the division of this vector and v and returns out.
     */
    public Vector div( Vector v, Vector out )
    {
        if (v.x != 0.0f)
        {
            out.x = x / v.x;
        }
        if (v.y != 0.0f)
        {
            out.y = y / v.y;
        }
        return out;
    }

    /**
     * Returns a new vector that is the division of this vector by v.
     */
    public Vector div( Vector v )
    {
        return div( v, new Vector() );
    }

    /**
     * Adds v to this vector and returns this.
     */
    public Vector addi( Vector v )
    {
        return add( v, this );
    }

    /**
     * Sets out to the addition of this vector and v and returns out.
     */
    public Vector add( Vector v, Vector out )
    {
        out.x = x + v.x;
        out.y = y + v.y;
        return out;
    }

    /**
     * Returns a new vector that is the addition of this vector and v.
     */
    public Vector add( Vector v )
    {
        return add( v, new Vector() );
    }

    /**
     * Adds v * s to this vector and returns this.
     */
    public Vector addsi( Vector v, float s )
    {
        return adds( v, s, this );
    }

    /**
     * Sets out to the addition of this vector and v * s and returns out.
     */
    public Vector adds( Vector v, float s, Vector out )
    {
        out.x = x + v.x * s;
        out.y = y + v.y * s;
        return out;
    }

    /**
     * Returns a new vector that is the addition of this vector and v * s.
     */
    public Vector adds( Vector v, float s )
    {
        return adds( v, s, new Vector() );
    }

    /**
     * Subtracts v from this vector and returns this.
     */
    public Vector subi( Vector v )
    {
        return sub( v, this );
    }

    /**
     * Sets out to the subtraction of v from this vector and returns out.
     */
    public Vector sub( Vector v, Vector out )
    {
        out.x = x - v.x;
        out.y = y - v.y;
        return out;
    }

    /**
     * Returns a new vector that is the subtraction of v from this vector.
     */
    public Vector sub( Vector v )
    {
        return sub( v, new Vector() );
    }

    /**
     * Sets this to the vector starting at origin and ending at target, and
     * returns this.
     */
    public Vector directi( Vector origin, Vector target )
    {
        return direct( origin, target, this );
    }

    /**
     * Sets out to the vector starting at origin and ending at target, and
     * returns out.
     */
    public Vector direct( Vector origin, Vector target, Vector out )
    {
        out.x = target.x - origin.x;
        out.y = target.y - origin.y;
        return out;
    }

    /**
     * Returns a new vector starting at origin and ending at target.
     */
    public Vector direct( Vector origin, Vector target )
    {
        return direct( origin, target, new Vector() );
    }

    /**
     * Sets this to the vector between start and end based on delta where delta
     * is 0.0 for the start, 0.5 for the middle, and 1.0 for the end, and
     * returns
     * this.
     */
    public Vector interpolatei( Vector start, Vector end, float delta )
    {
        return interpolate( start, end, delta, this );
    }

    /**
     * Sets out to the vector between start and end based on delta where delta
     * is
     * 0.0 for the start, 0.5 for the middle, and 1.0 for the end, and returns
     * out.
     */
    public Vector interpolate( Vector start, Vector end, float delta, Vector out )
    {
        out.x = (end.x - start.x) * delta + start.x;
        out.y = (end.y - start.y) * delta + start.y;

        return out;
    }

    /**
     * Returns a new vector between start and end based on delta where delta is
     * 0.0 for the start, 0.5 for the middle, and 1.0 for the end.
     */
    public Vector interpolate( Vector start, Vector end, float delta )
    {
        return interpolate( start, end, delta, new Vector() );
    }

    /**
     * Sets this to the vector with the given angle in radians with the given
     * magnitude, and returns this.
     */
    public Vector angle( float radians, float magnitude )
    {
        x = (float)Math.cos( radians ) * magnitude;
        y = (float)Math.sin( radians ) * magnitude;

        return this;
    }

    /**
     * Returns the angle in radians of this vector from the x-axis.
     */
    public float angle()
    {
        float a = (float)StrictMath.atan2( y, x );

        if (a < 0)
        {
            a += ANGLE_FIX;
        }

        return a;
    }

    /**
     * Returns the angle in radians that's between this vector and the given
     * vector and the x-axis.
     */
    public float angleTo( Vector to )
    {
        float a = (float)StrictMath.atan2( to.y - y, to.x - x );

        if (a < 0)
        {
            a += ANGLE_FIX;
        }

        return a;
    }

    /**
     * Determines whether this vector is an exact unit vector.
     */
    public boolean isUnit()
    {
        return lengthSq() == 1.0f;
    }

    /**
     * Determines whether this vector is a unit vector within epsilon.
     */
    public boolean isUnit( float epsilon )
    {
        return Math.abs( lengthSq() - 1.0f ) < epsilon;
    }

    /**
     * Returns the squared length of this vector.
     */
    public float lengthSq()
    {
        return x * x + y * y;
    }

    /**
     * Returns the length of this vector.
     */
    public float length()
    {
        return (float)Math.sqrt( x * x + y * y );
    }

    /**
     * Sets the length of this vector and returns the previous length. If this
     * vector has no length, nothing changes.
     */
    public float length( float length )
    {
        float sq = (x * x) + (y * y);
        float actual = length;

        if (sq != 0.0 && sq != length * length)
        {
            actual = (float)Math.sqrt( sq );
            muli( length / actual );
        }

        return actual;
    }

    /**
     * Clamps the length of this vector between a minimum and maximum and
     * returns
     * this Vector. If this vector has no length, nothing changes.
     */
    public Vector clamp( float min, float max )
    {
        float sq = (x * x) + (y * y);

        if (sq != 0)
        {
            if (sq < min * min)
            {
                muli( min / (float)Math.sqrt( sq ) );
            }
            else if (sq > max * max)
            {
                muli( max / (float)Math.sqrt( sq ) );
            }
        }

        return this;
    }

    /**
     * If the length of this Vector is less than min, it's length will be set to
     * min and this will be returned.
     */
    public Vector min( float min )
    {
        float sq = (x * x) + (y * y);

        if (sq != 0 && sq < min * min)
        {
            muli( min / (float)Math.sqrt( sq ) );
        }

        return this;
    }

    /**
     * If the length of this Vector is greater than max, it's length will be set
     * to max and this will be returned.
     */
    public Vector max( float max )
    {
        float sq = (x * x) + (y * y);

        if (sq != 0 && sq > max * max)
        {
            muli( max / (float)Math.sqrt( sq ) );
        }
        return this;
    }

    /**
     * Rotates this vector by the given radians and returns this.
     */
    public Vector rotatei( float radians )
    {
        return rotate( radians, this );
    }

    /**
     * Sets out to this vector rotated by the given radians and returns out.
     */
    public Vector rotate( float radians, Vector out )
    {
        float c = (float)Math.cos( radians );
        float s = (float)Math.sin( radians );

        float xp = x * c - y * s;
        float yp = x * s + y * c;

        out.x = xp;
        out.y = yp;
        return out;
    }

    /**
     * Returns a new vector that is this vector rotated by the given radians.
     */
    public Vector rotate( float radians )
    {
        return rotate( radians, new Vector() );
    }

    /**
     * Rotates this vector by the given unit vector and returns this.
     */
    public Vector rotatei( Vector cossin )
    {
        return rotate( cossin, this );
    }

    /**
     * Sets out to this vector unit vector by the normal and returns out.
     */
    public Vector rotate( Vector cossin, Vector out )
    {
        final float ox = x, oy = y;
        out.x = (cossin.x * ox - cossin.y * oy);
        out.y = (cossin.x * oy + cossin.y * ox);
        return out;
    }

    /**
     * Returns a new vector that is this vector rotated by the unit vector.
     */
    public Vector rotate( Vector cossin )
    {
        return rotate( cossin, new Vector() );
    }

    /**
     * Rotates this vector around the origin the given number of times and
     * returns this.
     */
    public Vector rotate90i( int times )
    {
        return rotate90( times, ZERO, this );
    }

    /**
     * Rotates this vector around the given origin the given number of times and
     * returns this.
     */
    public Vector rotate90i( int times, Vector origin )
    {
        return rotate90( times, origin, this );
    }

    /**
     * Sets out to this vector rotated around the given origin a given number of
     * times and returns out.
     */
    public Vector rotate90( int times, Vector origin, Vector out )
    {
        float dx = x - origin.x;
        float dy = y - origin.y;

        switch (times & 3)
        {
        case 0:
            out.x = x;
            out.y = y;
            break;
        case 1:
            out.x = x - dy;
            out.y = y + dx;
            break;
        case 2:
            out.x = x - dx;
            out.y = y - dy;
            break;
        case 3:
            out.x = x + dy;
            out.y = y - dy;
            break;
        }

        return out;
    }

    /**
     * Returns a new vector rotated around the given origin a given number of
     * times.
     */
    public Vector rotate90( int times )
    {
        return rotate90( times, ZERO, new Vector() );
    }

    /**
     * Returns a new vector rotated around the given origin a given number of
     * times.
     */
    public Vector rotate90( int times, Vector origin )
    {
        return rotate90( times, origin, new Vector() );
    }

    /**
     * Reflects this vector across the normal and returns this.
     */
    public Vector reflecti( Vector normal )
    {
        return reflect( normal, this );
    }

    /**
     * Sets out to this vector reflected across the normal and returns out.
     */
    public Vector reflect( Vector normal, Vector out )
    {
        final float scale = 2 * dot( normal );
        out.x = x - scale * normal.x;
        out.y = y - scale * normal.y;
        return out;
    }

    /**
     * Returns a new vector that is this vector reflected across the normal.
     */
    public Vector reflect( Vector normal )
    {
        return reflect( normal, new Vector() );
    }

    /**
     * Reflects this vector across the normal and returns this.
     */
    public Vector refracti( Vector normal )
    {
        return refract( normal, this );
    }

    /**
     * Sets out to this vector reflected across the normal and returns out.
     */
    public Vector refract( Vector normal, Vector out )
    {
        final float scale = 2 * dot( normal );
        out.x = scale * normal.x - x;
        out.y = scale * normal.y - y;
        return out;
    }

    /**
     * Returns a new vector that is this vector reflected across the normal.
     */
    public Vector refract( Vector normal )
    {
        return refract( normal, new Vector() );
    }

    /**
     * Normalizes this vector into a unit vector and returns the length. A unit
     * vector has a length of 1.0 and the x-component represents cos(A) and the
     * y-component represents sin(A) where A is the angle between this vector
     * and
     * the x-axis.
     */
    public float normalize()
    {
        float m = lengthSq();

        if (m != 0.0f)
        {
            divi( m = (float)Math.sqrt( m ) );
        }

        return m;
    }

    /**
     * Sets this vector to it's normal and returns this.
     */
    public Vector normali()
    {
        return normal( this );
    }

    /**
     * Sets out to the normal of this vector and returns out.
     */
    public Vector normal( Vector out )
    {
        float m = lengthSq();

        out.set( x, y );

        if (m != 0.0)
        {
            out.muli( 1.0f / (float)Math.sqrt( m ) );
        }

        return out;
    }

    /**
     * Returns a new vector which is the normal of this vector.
     */
    public Vector normal()
    {
        return normal( new Vector() );
    }

    /**
     * Sets this vector to the minimum between a and b.
     */
    public Vector mini( Vector a, Vector b )
    {
        return min( a, b, this );
    }

    /**
     * Sets this vector to the maximum between a and b.
     */
    public Vector maxi( Vector a, Vector b )
    {
        return max( a, b, this );
    }

    /**
     * Sets this vector to it's tangent on the left side.
     */
    public Vector lefti()
    {
        return left( this );
    }

    /**
     * Sets out to the tangent of this vector on the left side.
     */
    public Vector left( Vector out )
    {
        float oldx = x;
        out.x = -y;
        out.y = oldx;
        return out;
    }

    /**
     * Returns a new vector that is the tangent of this vector on the left side.
     */
    public Vector left()
    {
        return left( new Vector() );
    }

    /**
     * Sets this vector to it's tangent on the right side.
     */
    public Vector righti()
    {
        return right( this );
    }

    /**
     * Sets out to the tangent of this vector on the right side.
     */
    public Vector right( Vector out )
    {
        float oldx = x;
        out.x = y;
        out.y = -oldx;
        return out;
    }

    /**
     * Returns a new vector that is the tangent of this vector on the right
     * side.
     */
    public Vector right()
    {
        return right( new Vector() );
    }

    /**
     * Returns the dot product between this vector and v.
     */
    public float dot( Vector v )
    {
        return dot( this, v );
    }

    /**
     * Returns the squared distance between this vector and v.
     */
    public float distanceSq( Vector v )
    {
        return distanceSq( this, v );
    }

    /**
     * Returns the distance between this vector and v.
     */
    public float distance( Vector v )
    {
        return distance( this, v );
    }

    /**
     * Sets this vector to the cross between v and a and returns this.
     */
    public Vector cross( Vector v, float a )
    {
        return cross( v, a, this );
    }

    /**
     * Sets this vector to the cross between a and v and returns this.
     */
    public Vector cross( float a, Vector v )
    {
        return cross( a, v, this );
    }

    /**
     * Returns the scalar cross between this vector and v. This is essentially
     * the length of the cross product if this vector were 3d. This can also
     * indicate which way v is facing relative to this vector (left or right).
     */
    public float cross( Vector v )
    {
        return cross( this, v );
    }

    /**
     * Returns the scalar cross between this vector and v. This is essentially
     * the length of the cross product if this vector were 3d. This can also
     * indicate which way v is facing relative to this vector (left or right).
     */
    public float cross( float vx, float vy )
    {
        return cross( x, y, vx, vy );
    }

    /**
     * Returns whether the given vector is perfectly parallel to this vector.
     */
    public boolean isParallel( Vector v )
    {
        return isParallel( v, 0.0f );
    }

    /**
     * Returns whether the given vector is parallel to this vector within
     * epsilon.
     */
    public boolean isParallel( Vector v, float epsilon )
    {
        return Math.abs( cross( v ) ) < epsilon;
    }

    /**
     * Clones this vector.
     */
    public Vector clone()
    {
        return new Vector( x, y );
    }

    /**
     * Determines whether this vector's components are both exactly zero.
     */
    public boolean isZero()
    {
        return (x == 0f && y == 0f);
    }

    /**
     * Determines whether this vector's components are both at least epsilon
     * away
     * from zero.
     */
    public boolean isZero( float epsilon )
    {
        return isEqual( 0, 0, epsilon );
    }

    /**
     * Determines if this vector is equal to v.
     */
    public boolean isEqual( Vector v )
    {
        return (x == v.x && y == v.y);
    }

    /**
     * Determines if this vector is equal to the vector {xx, yy}.
     */
    public boolean isEqual( float xx, float yy )
    {
        return (x == xx && y == yy);
    }

    /**
     * Determines if this vector is equal to v within epsilon.
     */
    public boolean isEqual( Vector v, float epsilon )
    {
        return isEqual( v.x, v.y, epsilon );
    }

    /**
     * Determines if this vector is equal to the vector {xx, yy} within epsilon.
     */
    public boolean isEqual( float xx, float yy, float epsilon )
    {
        return Math.abs( xx - x ) < epsilon && Math.abs( yy - y ) < epsilon;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits( x );
        result = prime * result + Float.floatToIntBits( y );
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Vector other = (Vector)obj;
        if (Float.floatToIntBits( x ) != Float.floatToIntBits( other.x )) return false;
        if (Float.floatToIntBits( y ) != Float.floatToIntBits( other.y )) return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "{" + x + "," + y + "}";
    }

    /**
     * Returns and sets out to the minimum x and y coordinates from a and b.
     */
    public static Vector min( Vector a, Vector b, Vector out )
    {
        out.x = StrictMath.min( a.x, b.x );
        out.y = StrictMath.min( a.y, b.y );
        return out;
    }

    /**
     * Returns and sets out to the maximum x and y coordinates from a and b.
     */
    public static Vector max( Vector a, Vector b, Vector out )
    {
        out.x = StrictMath.max( a.x, b.x );
        out.y = StrictMath.max( a.y, b.y );
        return out;
    }

    /**
     * Return the dot product between the two vectors.
     */
    public static float dot( Vector a, Vector b )
    {
        return a.x * b.x + a.y * b.y;
    }

    /**
     * Return the distance (squared) between the two points.
     */
    public static float distanceSq( Vector a, Vector b )
    {
        float dx = a.x - b.x;
        float dy = a.y - b.y;

        return dx * dx + dy * dy;
    }

    /**
     * Return the distance between the two points.
     */
    public static float distance( Vector a, Vector b )
    {
        float dx = a.x - b.x;
        float dy = a.y - b.y;

        return (float)Math.sqrt( dx * dx + dy * dy );
    }

    /**
     * Returns and sets out to the cross between v and a.
     */
    public static Vector cross( Vector v, float a, Vector out )
    {
        out.x = v.y * a;
        out.y = v.x * -a;
        return out;
    }

    /**
     * Returns and sets out to the cross between a and v.
     */
    public static Vector cross( float a, Vector v, Vector out )
    {
        out.x = v.y * -a;
        out.y = v.x * a;
        return out;
    }

    /**
     * Returns the cross product between the two vectors a and b.
     */
    public static float cross( Vector a, Vector b )
    {
        return a.x * b.y - a.y * b.x;
    }

    /**
     * Returns the cross product between the two vectors {ax, ay} and {bx, by}.
     */
    public static float cross( float ax, float ay, float bx, float by )
    {
        return ax * by - ay * bx;
    }

    /**
     * Returns a vector that represents the given angle, where x = cos(angle)
     * and y = sin(angle).
     */
    public static Vector fromAngle( float angle )
    {
        return new Vector( (float)Math.cos( angle ), (float)Math.sin( angle ) );
    }

    /**
     * Returns a new array of instantiated Vectors of the given length.
     */
    public static Vector[] arrayOf( int length )
    {
        Vector[] array = new Vector[length];

        while (--length >= 0)
        {
            array[length] = new Vector();
        }

        return array;

    }

    @Override
    public Vector create()
    {
        return new Vector();
    }

    @Override
    public Vector clone( Vector value )
    {
        return new Vector( value );
    }

    @Override
    public Vector copy( Vector from, Vector to )
    {
        to.set( from );
        return to;
    }

    @Override
    public Vector scale( float scale )
    {
        muli( scale );
        
        return this;
    }

    @Override
    public Vector add( Vector a, float amount )
    {
        addsi( a, amount );
        
        return this;
    }

}

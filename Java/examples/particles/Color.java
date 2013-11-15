package particles;


public class Color implements Attribute<Color>
{
    
    public static final Color WHITE = new Color( 1.0f, 1.0f, 1.0f, 1.0f );

    public float r, g, b, a;
    
    public Color()
    {
        this( 1.0f, 1.0f, 1.0f, 1.0f );
    }
    
    public Color(float red, float green, float blue)
    {
        this( red, green, blue, 1.0f );
    }
    
    public Color(float red, float green, float blue, float alpha)
    {
        r = red;
        g = green;
        b = blue;
        a = alpha;
    }
    
    @Override
    public Color create()
    {
        return new Color();
    }

    @Override
    public Color clone( Color value )
    {
        return copy( this, new Color() );
    }

    @Override
    public Color copy( Color from, Color to )
    {
        to.r = from.r;
        to.g = from.g;
        to.b = from.b;
        to.a = from.a;
        
        return to;
    }

    @Override
    public Color scale( float scale )
    {
        r *= scale;
        g *= scale;
        b *= scale;
        a *= scale;
        
        return this;
    }

    @Override
    public Color add( Color c, float amount )
    {
        r += c.r * amount;
        g += c.g * amount;
        b += c.b * amount;
        a += c.a * amount;
        
        return this;
    }

    @Override
    public Color set( Color value )
    {
        r = value.r;
        g = value.g;
        b = value.b;
        a = value.a;
        
        return this;
    }
    
    @Override
    public boolean isZero()
    {
        return (r == 0 && g == 0 && b == 0 && a == 0);
    }

}

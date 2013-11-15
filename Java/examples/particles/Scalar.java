package particles;


public class Scalar implements Attribute<Scalar>
{
    
    public static final Scalar ZERO = new Scalar( 0.0f );
    public static final Scalar ONE = new Scalar( 1.0f );
    
    public float v;

    public Scalar()
    {
    }
    
    public Scalar(float v)
    {
        this.v = v;
    }
    
    @Override
    public Scalar create()
    {
        return new Scalar();
    }

    @Override
    public Scalar clone( Scalar value )
    {
        return new Scalar( value.v );
    }

    @Override
    public Scalar copy( Scalar from, Scalar to )
    {
        to.v = from.v;
        
        return to;
    }

    @Override
    public Scalar scale( float scale )
    {
        v *= scale;
        
        return this;
    }

    @Override
    public Scalar add( Scalar a, float amount )
    {
        v += a.v * amount;
        
        return this;
    }

    @Override
    public Scalar set( Scalar value )
    {
        v = value.v;
        
        return this;
    }
    
    @Override
    public boolean isZero()
    {
        return (v == 0);
    }

}


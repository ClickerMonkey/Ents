package asteroids;

import org.magnos.entity.ComponentValueFactory;

/**
 * A component value for Entities that expire after maxAge seconds.
 * 
 * @author Philip Diffenderfer
 *
 */
public class Aged implements ComponentValueFactory<Aged>
{
    public float maxAge;
    public float age;

    public Aged()
    {
    }
    
    public Aged(float maxAge)
    {
        this.maxAge = maxAge;
        this.age = 0.0f;
    }
    
    @Override
    public Aged create()
    {
        return copy( this, new Aged() );
    }
    
    @Override
    public Aged clone( Aged value )
    {
        return copy( value, new Aged() );
    }
    
    @Override
    public Aged copy( Aged from, Aged to )
    {
        to.maxAge = from.maxAge;
        to.age = from.age;
        
        return to;
    }
    
}


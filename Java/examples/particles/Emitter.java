package particles;

import org.magnos.entity.ComponentValueFactory;
import org.magnos.entity.EntityList;
import org.magnos.entity.Template;


public class Emitter implements ComponentValueFactory<Emitter>
{
    
    public static final Emitter FACTORY = new Emitter( null );
    
    public Template particleTemplate;
    public Range burstCountRange;
    public Range burstDelayRange;
    public EntityList particles;
    public EmitterPosition position;
    public EmitterVelocity velocity;
    public float time;
    public float burstDelay;
    public int burstCount;
    
    public Emitter(EntityList particles)
    {
        this.particles = particles;
        this.burstCountRange = new Range();
        this.burstDelayRange = new Range();
    }
    
    @Override
    public Emitter create()
    {
        return new Emitter( new EntityList( Templates.PARTICLE_SYSTEM ) );
    }
    
    @Override
    public Emitter clone( Emitter value )
    {
        return copy( value, create() );
    }
    
    @Override
    public Emitter copy( Emitter from, Emitter to )
    {
        to.particleTemplate = particleTemplate;
        to.burstCountRange.set( burstCountRange );
        to.burstDelayRange.set( burstDelayRange );
        to.position = position;
        to.velocity = velocity;

        return to;
    }
    
}

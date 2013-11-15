package particles;

import static particles.Components.*;

import org.magnos.entity.Control;
import org.magnos.entity.Entity;


public class EmitterControl implements Control
{

    private static final Vector VECTOR_CACHE = new Vector();

    @Override
    public void update( Entity e, Object updateState )
    {
        float dt = ((UpdateState)updateState).dt;
        Emitter emitter = e.get( EMITTER );
        Vector emitterPosition = e.get( POSITION );
        
        emitter.time += dt;
        
        if (emitter.time >= emitter.burstDelay)
        {
            emitter.time -= emitter.burstDelay;
            emitter.burstDelay = emitter.burstDelayRange.randomFloat();
            emitter.burstCount = emitter.burstCountRange.randomInt();
            
            for (int i = 0; i < emitter.burstCount; i++)
            {
                Entity p = new Entity( emitter.particleTemplate );
                emitter.position.setPosition( p, VECTOR_CACHE );
                emitter.velocity.setVelocity( p, VECTOR_CACHE );
                emitter.particles.add( p );
                
                p.get( POSITION ).add( emitterPosition );
            }
        }
    }

}

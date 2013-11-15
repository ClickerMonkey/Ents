package particles;

import org.magnos.entity.Component;
import org.magnos.entity.Control;
import org.magnos.entity.Entity;


public class Controls
{

    public static <T extends Attribute<T>> Control newPhysicsControl(final Component<T> pos, final Component<T> vel, final Component<T> acc)
    {
        return new Control() 
        {
            public void update( Entity e, Object updateState ) 
            {
                float dt = ((UpdateState)updateState).dt;
                
                T position = e.get( pos );
                T velocity = e.get( vel );
                T acceleration = (acc != null ? e.gets( acc ) : null);
                
                if (acceleration != null) {
                    velocity.add( acceleration, dt );
                }
                position.add( velocity, dt );
            }
        };
    }
    
    public static <T extends Attribute<T>> Control newDragControl(final Component<T> posComponent, final Component<T> velComponent, final Component<T> accComponent, final float dragAmount)
    {
        return new Control() 
        {
            public void update( Entity e, Object updateState ) 
            {
                float dt = ((UpdateState)updateState).dt;
                
                T acceleration = (accComponent != null ? e.gets( accComponent ) : null);
                
                if (acceleration == null || acceleration.isZero())
                {
                    e.get( posComponent ).add( e.get( velComponent ), -dt * dragAmount );
                }
            }
        };
    }
    
    public static <T extends Attribute<T>> Control newPathControl(final Component<T> attributeComponent, final Component<Aged> agedComponent, final Component<Path<T>> pathComponent)
    {
        return new Control() 
        {
            public void update( Entity e, Object updateState ) 
            {
                Aged life = e.get( agedComponent );
                e.get( pathComponent ).set( e.get( attributeComponent ), life.age / life.maxAge );
            }
        };
    }
    
}

package org.magnos.entity.asteroids;

import org.magnos.entity.Control;
import org.magnos.entity.Controller;
import org.magnos.entity.Entity;
import org.magnos.entity.Ents;
import org.magnos.entity.vals.FloatVal;

import com.gameprogblog.engine.Vector;

import static org.magnos.entity.asteroids.Components.*;

public class Controllers
{
    
    public static Controller PHYSICS = Ents.newController( "physics" );
    
    public static Controller PHYSICS_ACCEL = Ents.newControllerAlternative( PHYSICS, new Control() {
        public void update( Entity e, Object updateState ) {
            UpdateState state = (UpdateState)updateState;
            Vector pos = e.get( POSITION );
            Vector vel = e.get( VELOCITY );
            Vector acc = e.get( ACCELERATION );
            vel.addsi( acc, state.dt );
            pos.addsi( vel, state.dt );
        }
    });

    public static Controller PHYSICS_NOACCEL = Ents.newControllerAlternative( PHYSICS, new Control() {
        public void update( Entity e, Object updateState ) {
            UpdateState state = (UpdateState)updateState;
            Vector pos = e.get( POSITION );
            Vector vel = e.get( VELOCITY );
            pos.addsi( vel, state.dt );
        }
    });
    
    public static Controller ROTATES = Ents.newController( "rotates", new Control() {
        public void update( Entity e, Object updateState ) {
            UpdateState state = (UpdateState)updateState;
            FloatVal ang = e.get( ANGLE );
            FloatVal vel = e.get( ANGULAR_VELOCITY );
            ang.v += vel.v * state.dt;
        }
    });
    
    public static Controller DRAG = Ents.newController( "ents", new Control() {
        public void update( Entity e, Object updateState ) {
            UpdateState state = (UpdateState)updateState;
            Vector vel = e.get( VELOCITY );
            Vector acc = e.gets( ACCELERATION );
            if (acc == null || acc.isEqual( Vector.ZERO, 0.0001f )) {
                vel.addsi( vel, -state.dt );
            }
        }
    } );
    
    public static Controller AGES = Ents.newController( "aged", new Control() {
        public void update( Entity e, Object updateState ) {
            UpdateState state = (UpdateState)updateState;
            Aged a = e.get( AGE );
            a.age += state.dt;
            if (a.age > a.maxAge) {
                e.expire();
            }
        }
    });
    
}

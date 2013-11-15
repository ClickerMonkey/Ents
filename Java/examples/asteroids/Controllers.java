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

package asteroids;

import static asteroids.Components.*;

import org.magnos.entity.Control;
import org.magnos.entity.Controller;
import org.magnos.entity.Entity;
import org.magnos.entity.Ents;
import org.magnos.entity.vals.FloatVal;

/**
 * {@link Controller}s for {@link Asteroids}.
 * 
 * @author Philip Diffenderfer
 *
 */
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
    
    public static Controller DRAG = Ents.newController( "drag", new Control() {
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

package particles;

import static particles.Components.*;

import org.magnos.entity.Controller;
import org.magnos.entity.Ents;


public class Controllers
{
    public static Controller POSITION_PHYSICS = Ents.newController( "position-physics", Controls.newPhysicsControl( POSITION, VELOCITY, ACCELERATION ) );
    public static Controller SCALE_PHYSICS = Ents.newController( "scale-physics", Controls.newPhysicsControl( SCALE, SCALE_VELOCITY, SCALE_ACCELERATION ) );
    public static Controller ANGLE_PHYSICS = Ents.newController( "angle-physics", Controls.newPhysicsControl( ANGLE, ANGLE_VELOCITY, ANGLE_ACCELERATION ) );
}

package particles;

import org.magnos.entity.Component;
import org.magnos.entity.Ents;


public class Components
{
    public static Component<Vector> POSITION = Ents.newComponent( "position", Vector.ZERO );
    public static Component<Vector> VELOCITY = Ents.newComponent( "velocity", Vector.ZERO );
    public static Component<Vector> ACCELERATION = Ents.newComponent( "acceleration", Vector.ZERO );
    
    public static Component<Vector> SCALE = Ents.newComponent( "scale", Vector.ONE );
    public static Component<Vector> SCALE_VELOCITY = Ents.newComponent( "scale-velocity", Vector.ZERO );
    public static Component<Vector> SCALE_ACCELERATION = Ents.newComponent( "scale-acceleration", Vector.ZERO );
    
    public static Component<Scalar> ANGLE = Ents.newComponent( "angle", Scalar.ZERO );
    public static Component<Scalar> ANGLE_VELOCITY = Ents.newComponent( "angle-velocity", Scalar.ZERO );
    public static Component<Scalar> ANGLE_ACCELERATION = Ents.newComponent( "angle-acceleration", Scalar.ZERO );
    
    public static Component<Scalar> RADIUS = Ents.newComponent( "radius", Scalar.ZERO );
    
    public static Component<Color> COLOR = Ents.newComponent( "color", Color.WHITE );
    
    public static Component<Aged> AGE = Ents.newComponent( "age", Aged.DEAD );
}
package org.magnos.entity.asteroids;

import java.awt.Color;

import org.magnos.entity.Component;
import org.magnos.entity.Ents;
import org.magnos.entity.vals.FloatVal;
import org.magnos.entity.vals.IntVal;

import com.gameprogblog.engine.Vector;


public class Components
{
    public static Component<Vector> POSITION = Ents.newComponent( "position", new Vector() );
    public static Component<Vector> VELOCITY = Ents.newComponent( "velocity", new Vector() );
    public static Component<Vector> ACCELERATION = Ents.newComponent( "acceleration", new Vector() );
    
    public static Component<FloatVal> ANGLE = Ents.newComponent( "angle", new FloatVal() );
    public static Component<FloatVal> ANGULAR_VELOCITY = Ents.newComponent( "angular_velocity", new FloatVal() );
    
    public static Component<FloatVal> RADIUS = Ents.newComponent( "radius", new FloatVal() );
    public static Component<FloatVal> RADIUS_SHARED = Ents.newComponentSharedAlternative( RADIUS, new FloatVal() );
    
    public static Component<Color> COLOR = Ents.newComponent( "color", new ColorValueFactory() );
    public static Component<Color> COLOR_SHARED = Ents.newComponentSharedAlternative( COLOR, new ColorValueFactory() );
    
    public static Component<IntVal> LIVES = Ents.newComponent( "lives", new IntVal() );
    
    public static Component<Aged> AGE = Ents.newComponent( "age", new Aged() );
    
}

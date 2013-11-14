package org.magnos.entity.asteroids;

import org.magnos.entity.Ents;
import org.magnos.entity.Template;
import org.magnos.entity.util.ComponentSet;
import org.magnos.entity.util.ControllerSet;

import static org.magnos.entity.asteroids.Components.*;
import static org.magnos.entity.asteroids.Controllers.*;

public class Templates
{
    
    public static Template ASTEROID = Ents.newTemplate( "asteroid",
        new ComponentSet( POSITION, VELOCITY, RADIUS, ANGLE, ANGULAR_VELOCITY, COLOR, LIVES ),
        new ControllerSet( PHYSICS_NOACCEL, ROTATES ),
        Views.ASTEROID 
    );

    public static Template SHIP = Ents.newTemplate( "ship",
        new ComponentSet( POSITION, VELOCITY, ACCELERATION, ANGLE, RADIUS_SHARED, COLOR ),
        new ControllerSet( PHYSICS_ACCEL, DRAG ),
        Views.SHIP 
    );

    public static Template LASER = Ents.newTemplate( "laser",
        new ComponentSet( POSITION, VELOCITY, RADIUS_SHARED, COLOR_SHARED ),
        new ControllerSet( PHYSICS_NOACCEL ),
        Views.LASER
    );

    public static Template PARTICLE = Ents.newTemplate( "particle",
        new ComponentSet( POSITION, VELOCITY, RADIUS_SHARED, COLOR, AGE ),
        new ControllerSet( PHYSICS_NOACCEL, AGES, DRAG ),
        Views.PARTICLE
    );
    
    public static final int TEMPLATE_COUNT = Ents.getTemplates().size();
    
}

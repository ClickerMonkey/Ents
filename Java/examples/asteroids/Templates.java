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
import static asteroids.Controllers.*;

import org.magnos.entity.Ents;
import org.magnos.entity.Template;
import org.magnos.entity.util.ComponentSet;
import org.magnos.entity.util.ControllerSet;

/**
 * {@link Template}s for {@link Asteroids}.
 * 
 * @author Philip Diffenderfer
 *
 */
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
    
    public static Template PARTICLE_SYSTEM = Ents.newTemplate( "particle-system",
		new ComponentSet(),
		new ControllerSet(),
		Views.PARTICLE_SYSTEM
    );
    
    public static final int TEMPLATE_COUNT = Ents.getTemplates().size();
    
}

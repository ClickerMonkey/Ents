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

import java.awt.Color;

import org.magnos.entity.Component;
import org.magnos.entity.Ents;
import org.magnos.entity.vals.FloatVal;
import org.magnos.entity.vals.IntVal;

/**
 * {@link Component}s for {@link Asteroids}.
 * 
 * @author Philip Diffenderfer
 *
 */
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

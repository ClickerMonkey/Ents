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

import org.magnos.entity.Ents;
import org.magnos.entity.View;


/**
 * {@link View}s for {@link Asteroids}.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Views
{

	public static View ASTEROID = Ents.newView( "asteroid" );
	public static View SHIP = Ents.newView( "ship" );
	public static View LASER = Ents.newView( "laser" );
	public static View PARTICLE = Ents.newView( "particle" );
	public static View PARTICLE_SYSTEM = Ents.newView( "particle-system" );
}

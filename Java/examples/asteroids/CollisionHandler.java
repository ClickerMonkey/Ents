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

import org.magnos.entity.Entity;

/**
 * Handles the collision between two collidable entities. 
 * 
 * @author Philip Diffenderfer
 *
 */
public interface CollisionHandler
{
	
	/**
	 * The subject Entity has collided with the object Entity based on their
	 * position and radius.
	 * 
	 * @param subject
	 * 		The entity that had the object entity collide with it.
	 * @param object
	 * 		The entity that collided with the subject Entity.
	 */
    public void handle(Entity subject, Entity object);
    
}

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

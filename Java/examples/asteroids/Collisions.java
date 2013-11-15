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
import org.magnos.entity.Template;


/**
 * A matrix of {@link CollisionHandler}s for quickly determining how to handle a
 * collision given two {@link Template}s.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Collisions
{

	public static CollisionHandler[][] handlers = new CollisionHandler[Templates.TEMPLATE_COUNT][Templates.TEMPLATE_COUNT];

	/**
	 * Adds a {@link CollisionHandler} between the given subject and object
	 * Templates.
	 * 
	 * @param subject
	 *        The subject template. This determines the subject {@link Entity}
	 *        to pass to {@link CollisionHandler#handle(Entity, Entity)}.
	 * @param object
	 *        The object template. This determines the object {@link Entity} to
	 *        pass to {@link CollisionHandler#handle(Entity, Entity)}.
	 * @param handler
	 *        The handler to return for entities of the given templates.
	 */
	public static void register( Template subject, Template object, CollisionHandler handler )
	{
		handlers[subject.id][object.id] = handler;
		handlers[object.id][subject.id] = reverse( handler );
	}

	/**
	 * Determines a CollisionHandler based on the templates given. If either
	 * template is custom, <code>null</code> is returned.
	 * 
	 * @param subject
	 *        The template of the subject entity.
	 * @param object
	 *        The template of the object entity.
	 * @return The CollisionHandler to use or null if none was registered.
	 */
	public static CollisionHandler getHandler( Template subject, Template object )
	{
		if (subject.isCustom() || object.isCustom())
		{
			return null;
		}

		return handlers[subject.id][object.id];
	}

	/**
	 * A CollisionHandler which swaps object and subject.
	 * 
	 * @param handler
	 *        The CollisionHandler to reverse.
	 * @return A CollisionHandler with swapped object and subject entities.
	 */
	private static CollisionHandler reverse( final CollisionHandler handler )
	{
		return new CollisionHandler() {

			public void handle( Entity subject, Entity object )
			{
				handler.handle( object, subject );
			}
		};
	}

}

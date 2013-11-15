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
 *              Open Software License (OSL 3.0)
 */

package org.magnos.entity;

/**
 * A Renderer is responsible for drawing an entity when the
 * {@link Entity#draw(Object)} method is invoked on a visible Entity. When an
 * Entity is created with a view that has this Renderer, the Renderer of the
 * Entity is set to the Renderer that's returned by {@link #create(Entity)}.
 * When the Entity is deleted or the Renderer changes (due to a view change or
 * a manual Renderer change through {@link Entity#setRenderer(Renderer)}), the
 * {@link #destroy(Entity)} method is invoked. <br/>
 * <br/>
 * {@link #begin(Entity, Object)} and {@link #end(Entity, Object)} are invoked
 * depending on the Entity implementation. If the Entity implementation has a
 * set of entities, most likely {@link #begin(Entity, Object)} will be called,
 * all sub-entities will be drawn, and finally {@link #end(Entity, Object)} will
 * be called. <br/>
 * <br/>
 * It's common for the {@link #create(Entity)} method to return the reference
 * to this Renderer. Renderer implementations that don't have Entity specific
 * rendering information don't need to create a new instance of a Renderer.
 * 
 * @author Philip Diffenderfer
 * 
 */
public interface Renderer
{

    /**
     * Returns a Renderer for the given Entity. This may be a new Renderer if
     * the implementation has Entity specific rendering information, or it may
     * be the reference to this Renderer.
     * 
     * @param e
     *        The entity that requires a Renderer.
     * @return The reference to a Renderer for the given Entity.
     */
    public Renderer create( Entity e );

    /**
     * Invoked at the start of the Entity drawing process.
     * 
     * @param e
     *        The entity to draw.
     * @param drawState
     *        The drawState passed to {@link Entity#draw(Object)}.
     */
    public void begin( Entity e, Object drawState );

    /**
     * Invoked at the end of the Entity drawing process.
     * 
     * @param e
     *        The entity to draw.
     * @param drawState
     *        The drawState passed to {@link Entity#draw(Object)}.
     */
    public void end( Entity e, Object drawState );

    /**
     * Invoked when the renderer of the given Entity is changing or the Entity
     * has expired and has been deleted from it's parent Entity.
     * 
     * @param e
     *        The entity which has this Renderer to destroy.
     */
    public void destroy( Entity e );

    /**
     * Sends a generic message to the renderer. This is typically done by a
     * controller to signal to the Renderer that there has been a component
     * value change for an entity that may affect it.
     * 
     * @param e
     *        The entity changed.
     * @param message
     *        A message the Renderer implementation knows what to do with.
     */
    public void notify( Entity e, int message );

}

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
 * A listener to all major events in the system.
 * 
 * @author Philip Diffenderfer
 * 
 */
public interface EntityListener
{

    /**
     * A method invoked when an entity has just been instantiated and will
     * be assigned the given id. This is invoked from the Entity constructor
     * and not all values will be in the entity (whatever values are added
     * to the entity when {@link TemplateComponent#postAdd(Entity)} is invoked.
     * 
     * @param e
     *        The entity added to the system.
     * @param id
     *        The id of the entity.
     */
    public void onEntityAdd( Entity e, int id );

    /**
     * A method invoked when an entity has just been deleted from the system
     * either through directly calling {@link Entity#delete()} or from a parent
     * entity detecting the entity was expired, and removing and deleting it.
     * 
     * @param e
     *        The entity removed from the system.
     */
    public void onEntityRemove( Entity e );

    /**
     * A method invoked when {@link Ents#clear()} is invoked.
     */
    public void onCoreClear();

    /**
     * A method invoked when a View is added to {@link Ents}.
     * 
     * @param view
     *        The view added.
     * @param definition
     *        Whether this view was a definition or an instance (alternative).
     */
    public void onViewAdd( View view, boolean definition );

    /**
     * A method invoked when a Controller is added to {@link Ents}.
     * 
     * @param controller
     *        The controller added.
     * @param definition
     *        Whether this controller was a definition or an instance
     *        (alternative).
     */
    public void onControllerAdd( Controller controller, boolean definition );

    /**
     * A method invoked when a Component is added to {@link Ents}.
     * 
     * @param component
     *        The component added.
     * @param definition
     *        Whether this component was a definition or an instance
     *        (alternative).
     */
    public void onComponentAdd( Component<?> component, boolean definition );

    /**
     * A method invoked when a Template is added through {@link Ents}.
     * Templates that are dynamically created are not tracked by
     * {@link Ents}, therefore cannot be notified.
     * 
     * @param template
     *        The template added.
     */
    public void onTemplateAdd( Template template );

}

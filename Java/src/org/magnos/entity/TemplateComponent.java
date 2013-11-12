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

package org.magnos.entity;

/**
 * The interface responsible for implementing the
 * {@link Entity#set(Component, Object)}, {@link Entity#sets(Component, Object)}
 * , {@link Entity#get(Component)}, {@link Entity#gets(Component)},
 * {@link Entity#gets(Component, Object)},
 * {@link Entity#take(Component, Object)}, and
 * {@link Entity#takes(Component, Object)} methods.
 * <p>
 * If you want slightly quicker access to getting and setting the components of
 * an Entity you can call these methods directly and pass in the Entity. This is
 * not recommended if there is ever a chance that the Entity's template had this
 * component changed to an alternative.
 * </p>
 * <p>
 * Calling these methods on an Entity that doesn't have the component will have
 * unexpected results and will most likely result in
 * {@link IndexOutOfBoundsException}, {@link NullPointerException}, or
 * {@link ClassCastException}.
 * </p>
 * 
 * @author Philip Diffenderfer
 * 
 * @param <T>
 *        The component value type.
 */
public interface TemplateComponent<T>
{

    /**
     * Sets the value on the Entity.
     * 
     * @param e
     *        The entity to set the value to.
     * @param value
     *        The new value for the component.
     */
    public void set( Entity e, T value );

    /**
     * Gets the value from the Entity.
     * 
     * @param e
     *        The entity to get the value from.
     * @return The reference to the value on the Entity.
     */
    public T get( Entity e );

    /**
     * Sets target to the value on the Entity and returns it.
     * 
     * @param e
     *        The entity to take from.
     * @param target
     *        The target value to set and return.
     * @return The reference to target.
     */
    public T take( Entity e, T target );

    /**
     * Removes this TemplateComponent from the given template. This is for
     * internal use, do not call this directly.
     * 
     * @param template
     *        The template to remove this TemplateComponent from.
     */
    public void remove( Template template );

    /**
     * The method invoked after an Entity is created and is "added" to all
     * of its components.
     * 
     * @param e
     *        The entity that just had this component added to it.
     */
    public void postAdd( Entity e );

    /**
     * The method invoked after an Entity has this component replaced by another
     * with the same ID or when an Entity is deleted. If
     * {@link #postAdd(Entity)} is called this method will be called equally
     * given the user properly
     * deletes their Entities.
     * 
     * @param e
     *        The entity that is having this component be removed.
     */
    public void preRemove( Entity e );

}

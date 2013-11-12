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
 * A component factory is responsible for creating a default value for an
 * {@link Entity}'s component, as well as cloning an Entity's component value to
 * pass along to the cloned Entity.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <T>
 *        The component value type.
 */
public interface ComponentValueFactory<T>
{

    /**
     * Creates a default value for an {@link Entity}'s component.
     * 
     * @return A newly instantiated value.
     */
    public T create();

    /**
     * Takes the given value and returns a new instance that is equivalent.
     * 
     * @param value
     *        A value to clone.
     * @return A newly instantiated value that is equal to the value passed in.
     */
    public T clone( T value );

    /**
     * Sets "to" to the value of "from" and returns the reference to "to".
     * 
     * @param from
     *        The value to copy from.
     * @param target
     *        The value to set to march "from".
     * @return The reference to "to".
     */
    public T copy( T from, T to );

}

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
 * The interface to implement that is called every {@link Entity#update(Object)}
 * invocation on enabled Controllers.
 * 
 * @author Philip Diffenderfer
 * 
 */
public interface Control
{

    /**
     * Executes controller logic on the given Entity with the given updateState
     * (which is defined by the user).
     * 
     * @param e
     *        The entity to control.
     * @param updateState
     *        The update state passed to the Entity.
     */
    public void update( Entity e, Object updateState );

}

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
 * A generic way to identify an object. That object has both a unique
 * identifier, and a unique name.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Id
{

    /**
     * The unique identifier of this object, with respect to the object's type.
     */
    public final int id;

    /**
     * The unique name of this object, with respect to the object's type.
     */
    public final String name;

    /**
     * Instantiates a new Id.
     * 
     * @param id
     *      The unique identifier of this object.
     * @param name  
     *      The unique name of this object.
     */
    public Id( int id, String name )
    {
        this.id = id;
        this.name = name;
    }

}

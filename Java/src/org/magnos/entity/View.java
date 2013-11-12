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
 * A view is an identifiable object for a Renderer.
 * 
 * @author Philip Diffenderfer
 * @see Renderer
 */
public class View extends Id
{

    /**
     * The View for an Entity or Template that doesn't have one.
     */
    public static final View NONE = null;

    /**
     * The default renderer for Entities created with this View (or with
     * a template that has this View).
     */
    public Renderer renderer;

    /**
     * Instantiates a new View.
     * 
     * @param id
     *      The unique identifier of this View.
     * @param name  
     *      The unique name of this View.
     * @param renderer
     *      The default renderer of this View.
     */
    public View( int id, String name, Renderer renderer )
    {
        super( id, name );

        this.renderer = renderer;
    }

}

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

package org.magnos.entity.filters;

import org.magnos.entity.Entity;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityIterator;


/**
 * A filter that only returns Entities that are visible or invisible.
 * 
 * @author Philip Diffenderfer
 * @see EntityIterator
 * 
 */
public class VisibleFilter implements EntityFilter
{

    /**
     * A single instance to a ViewFilter for visible entities.
     */
    public static final VisibleFilter TRUE = new VisibleFilter( true );

    /**
     * A single instance to a ViewFilter for invisible entities.
     */
    public static final VisibleFilter FALSE = new VisibleFilter( false );

    public final boolean visible;
    
    private VisibleFilter(boolean visible)
    {
    	this.visible = visible;
    }

    @Override
    public boolean isValid( Entity e )
    {
        return e.isVisible() == visible;
    }

}

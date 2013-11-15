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

import org.magnos.entity.ComponentValueFactory;

/**
 * A component value for Entities that expire after maxAge seconds.
 * 
 * @author Philip Diffenderfer
 *
 */
public class Aged implements ComponentValueFactory<Aged>
{
    public float maxAge;
    public float age;

    public Aged()
    {
    }
    
    public Aged(float maxAge)
    {
        this.maxAge = maxAge;
        this.age = 0.0f;
    }
    
    @Override
    public Aged create()
    {
        return copy( this, new Aged() );
    }
    
    @Override
    public Aged clone( Aged value )
    {
        return copy( value, new Aged() );
    }
    
    @Override
    public Aged copy( Aged from, Aged to )
    {
        to.maxAge = from.maxAge;
        to.age = from.age;
        
        return to;
    }
    
}


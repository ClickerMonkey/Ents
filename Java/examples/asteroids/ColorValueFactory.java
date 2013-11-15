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

import java.awt.Color;

import org.magnos.entity.ComponentValueFactory;

/**
 * A {@link ComponentValueFactory} for {@link Color}.
 * 
 * @author Philip Diffenderfer
 *
 */
public class ColorValueFactory implements ComponentValueFactory<Color>
{

    @Override
    public Color create()
    {
        return Color.white;
    }

    @Override
    public Color clone( Color value )
    {
        return new Color( value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha() );
    }

    @Override
    public Color copy( Color from, Color to )
    {
        return clone( from );
    }

}

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
 * A {@link ComponentFactory} implementation that provides a null default
 * value for a component and also returns null when given a value to clone.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The component value type.
 */
public class ComponentFactoryNull<T> implements ComponentFactory<T>
{

	@Override
	public T create()
	{
		return null;
	}

	@Override
	public T clone(T value) 
	{
		return null;
	}

	@Override
	public T copy(T from, T to) 
	{
		return to;
	}

}

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

package org.magnos.entity.util;

import org.magnos.entity.Component;
import org.magnos.entity.EntityCore;
import org.magnos.entity.Template;


/**
 * A simple collection of {@link Component}s passed to {@link EntityCore} to
 * define what components a {@link Template} should have. The order of
 * components passed in define the order in which they are added to a Template
 * and subsequently any Entity.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class ComponentSet
{

   public static final ComponentSet NONE = new ComponentSet();

   /**
    * The components passed into the constructor.
    */
   public final Component<?>[] values;

   /**
    * Instantiates a new Components.
    * 
    * @param values
    *        The components that should be added to a template.
    */
   public ComponentSet( Component<?>... values )
   {
      this.values = values;
   }

}

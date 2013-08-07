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
 * A simple collection of {@link Controllers}s passed to {@link EntityCore} to
 * define what controllers a {@link Template} should have. The order of
 * controllers passed in define the order of controller execution for an Entity.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Controllers
{

   public static final Controllers NONE = new Controllers();
   
   /**
    * The controllers passed into the constructor.
    */
   public final Controller[] values;

   /**
    * Instantiates a new Controllers.
    * 
    * @param values
    *        The controllers that should be added to a template.
    */
   public Controllers( Controller... values )
   {
      this.values = values;
   }

}

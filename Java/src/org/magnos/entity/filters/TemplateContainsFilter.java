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
import org.magnos.entity.EntityChain;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.EntityList;
import org.magnos.entity.Template;


/**
 * A filter that returns all Entities that at least have all components,
 * controllers, and view of the given filtering template.
 * 
 * @author Philip Diffenderfer
 * @see EntityFilter
 * 
 */
public class TemplateContainsFilter extends EntityFilter
{

   protected Template template;

   /**
    * Resets the TemplateContainsFilter specifying the root entity and the
    * template used to check for containment.
    * 
    * @param root
    *        The root entity to filter. This entity is typically an
    *        {@link EntityChain} or {@link EntityList} which both can contain
    *        any number of entities.
    * @param template
    *        The filtering template used.
    * @return The {@link Iterable} filtered by
    *         {@link Template#contains(Template)}.
    */
   public EntityFilter reset( Entity root, Template template )
   {
      return reset( template ).reset( root );
   }

   /**
    * Resets the TemplateContainsFilter specifying the template used to check
    * for containment.
    * 
    * @param template
    *        The filtering template used.
    * @return The {@link Iterable} filtered by
    *         {@link Template#contains(Template)}.
    */
   public EntityFilter reset( Template template )
   {
      this.template = template;

      return this;
   }

   @Override
   public boolean isValid( Entity e )
   {
      return e.getTemplate().contains( template );
   }

}

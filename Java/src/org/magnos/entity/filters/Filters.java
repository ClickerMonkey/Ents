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

package org.magnos.entity.filters;

import org.magnos.entity.Component;
import org.magnos.entity.Controller;
import org.magnos.entity.Entity;
import org.magnos.entity.EntityFilter;
import org.magnos.entity.Template;
import org.magnos.entity.View;


/**
 * <p>
 * A utility class that is used to create a complex filter with minimal amount
 * of code while maximizing readability. This class should be statically
 * imported.
 * </p>
 * <b>Example</b>
 * <pre>
 * // (POSITION and VELOCITY) and (not VISIBLE or not ENABLED)
 * EntityFilter filter = and( components( POSITION, VELOCITY ), or( not( visible() ), not( enabled() ) ) );
 * </pre>
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Filters
{

    /**
     * @see AndFilter
     * @see AndFilter#AndFilter(EntityFilter, EntityFilter)
     */
    public static AndFilter and( EntityFilter first, EntityFilter second )
    {
        return new AndFilter( first, second );
    }

    /**
     * @see ClassFilter
     * @see ClassFilter#ClassFilter(Class)
     */
    public static ClassFilter clazz( Class<? extends Entity> clazz )
    {
        return new ClassFilter( clazz );
    }

    /**
     * @see ComponentFilter
     * @see ComponentFilter#ComponentFilter(Component...)
     */
    public static ComponentFilter components( Component<?>... components )
    {
        return new ComponentFilter( components );
    }

    /**
     * @see ControllerFilter
     * @see ControllerFilter#ControllerFilter(Controller...)
     */
    public static ControllerFilter controllers( Controller... controllers )
    {
        return new ControllerFilter( controllers );
    }

    /**
     * @see CustomFilter
     * @see CustomFilter#TRUE
     */
    public static CustomFilter custom()
    {
        return CustomFilter.TRUE;
    }

    /**
     * @see CustomFilter
     * @see CustomFilter#FALSE
     */
    public static CustomFilter templated()
    {
        return CustomFilter.FALSE;
    }

    /**
     * @see DefaultFilter
     * @see DefaultFilter#INSTANCE
     */
    public static DefaultFilter all()
    {
        return DefaultFilter.INSTANCE;
    }

    /**
     * @see EnabledFilter
     * @see EnabledFilter#TRUE
     */
    public static EnabledFilter enabled()
    {
        return EnabledFilter.TRUE;
    }

    /**
     * @see EnabledFilter
     * @see EnabledFilter#FALSE
     */
    public static EnabledFilter disabled()
    {
        return EnabledFilter.FALSE;
    }

    /**
     * @see ExpiredFilter
     * @see ExpiredFilter#TRUE
     */
    public static ExpiredFilter expired()
    {
        return ExpiredFilter.TRUE;
    }

    /**
     * @see ExpiredFilter
     * @see ExpiredFilter#FALSE
     */
    public static ExpiredFilter alive()
    {
        return ExpiredFilter.FALSE;
    }

    /**
     * @see NotFilter
     * @see NotFilter#NotFilter(EntityFilter)
     */
    public static NotFilter not( EntityFilter filter )
    {
        return new NotFilter( filter );
    }

    /**
     * @see OrFilter
     * @see OrFilter#OrFilter(EntityFilter, EntityFilter)
     */
    public static OrFilter or( EntityFilter first, EntityFilter second )
    {
        return new OrFilter( first, second );
    }

    /**
     * @see TemplateContainsFilter
     * @see TemplateContainsFilter#TemplateContainsFilter(Template)
     */
    public static TemplateContainsFilter contains( Template template )
    {
        return new TemplateContainsFilter( template );
    }

    /**
     * @see TemplateExactFilter
     * @see TemplateExactFilter#TemplateExactFilter(Template)
     */
    public static TemplateExactFilter template( Template template )
    {
        return new TemplateExactFilter( template );
    }

    /**
     * @see TemplateRelativeFilter
     * @see TemplateRelativeFilter#TemplateRelativeFilter(Template)
     */
    public static TemplateRelativeFilter relative( Template template )
    {
        return new TemplateRelativeFilter( template );
    }

    /**
     * @see ValueFilter
     * @see ValueFilter#ValueFilter(Component, Object)
     */
    public static <T> ValueFilter value( Component<T> component, T value )
    {
        return new ValueFilter( component, value );
    }

    /**
     * @see ViewFilter
     * @see ViewFilter#ViewFilter(View)
     */
    public static ViewFilter view( View view )
    {
        return new ViewFilter( view );
    }

    /**
     * @see VisibleFilter
     * @see VisibleFilter#TRUE
     */
    public static VisibleFilter visible()
    {
        return VisibleFilter.TRUE;
    }

    /**
     * @see VisibleFilter
     * @see VisibleFilter#FALSE
     */
    public static VisibleFilter invisible()
    {
        return VisibleFilter.FALSE;
    }

    /**
     * @see XorFilter
     * @see XorFilter#XorFilter(EntityFilter, EntityFilter)
     */
    public static XorFilter xor( EntityFilter first, EntityFilter second )
    {
        return new XorFilter( first, second );
    }

}

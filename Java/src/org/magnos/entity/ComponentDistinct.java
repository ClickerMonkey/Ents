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

import java.util.Arrays;

import org.magnos.entity.util.EntityUtility;


/**
 * A component that adds a value directly to the Entity, so that every entity
 * has it's own distinct value. When added to an Entity a factory is invoked to
 * generate a default value, and when an Entity is cloned the factory is also
 * invoked to clone a given value.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <T>
 *        The component value type.
 */
@SuppressWarnings ("unchecked" )
class ComponentDistinct<T> extends Component<T>
{

    /**
     * The factory that creates the default value on the entity and also handles
     * cloning values from Entity to cloned Entity.
     */
    private final ComponentValueFactory<T> factory;

    /**
     * Instantiates a ComponentDistinct.
     * 
     * @param id
     *        The id of the component.
     * @param name
     *        The name of the component.
     * @param factory
     *        The factory used to create and clone values.
     */
    protected ComponentDistinct( int id, String name, ComponentValueFactory<T> factory )
    {
        super( id, name );

        this.factory = factory;
    }

    @Override
    protected void postCustomAdd( Entity e, Template template, TemplateComponent<?> templateComponent )
    {
        ComponentValuedHandler handler = (ComponentValuedHandler)templateComponent;

        if (handler.componentIndex >= e.values.length)
        {
            e.values = Arrays.copyOf( e.values, handler.componentIndex + 1 );
        }

        e.values[handler.componentIndex] = factory.create();
    }

    @Override
    protected TemplateComponent<T> add( Template template )
    {
        final ComponentValueFactory<?>[] factories = template.factories;

        int factoryIndex = EntityUtility.indexOfSame( factories, null );

        if (factoryIndex == -1)
        {
            factoryIndex = factories.length;

            template.factories = EntityUtility.append( factories, factory );
        }
        else
        {
            factories[factoryIndex] = factory;
        }

        return new ComponentValuedHandler( factoryIndex );
    }

    private class ComponentValuedHandler implements TemplateComponent<T>
    {

        private final int componentIndex;

        private ComponentValuedHandler( int componenentIndex )
        {
            this.componentIndex = componenentIndex;
        }

        @Override
        public void set( Entity e, T value )
        {
            e.values[componentIndex] = value;
        }

        @Override
        public T get( Entity e )
        {
            return (T)e.values[componentIndex];
        }

        @Override
        public T take( Entity e, T target )
        {
            return factory.copy( (T)e.values[componentIndex], target );
        }

        @Override
        public void remove( Template template )
        {
            template.factories[componentIndex] = null;
        }

        @Override
        public void postAdd( Entity e )
        {

        }

        @Override
        public void preRemove( Entity e )
        {

        }
    }

}

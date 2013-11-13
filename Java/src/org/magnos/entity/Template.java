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

import org.magnos.entity.util.BitSet;
import org.magnos.entity.util.ComponentSet;
import org.magnos.entity.util.ControllerSet;
import org.magnos.entity.util.EntityUtility;


/**
 * A template carries the definition used when an Entity is created. It adds a
 * set of components, controllers, and a view to the Entity. Templating is used
 * to reduce the memory usage of storing meta-data about components and how
 * values are accessed and modified for an Entity. <br/>
 * <br/>
 * Do not modify a Template directly, this should only be done before any
 * Entity is created with it. A problem arises when a distinct component is
 * added to a Template that already has Entity instances... the instances
 * created with the template will appear to have the component but the
 * entities will not actually have the component value.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Template extends Id
{

    /**
     * The ID of custom templates.
     */
    public static final int CUSTOM = Integer.MAX_VALUE;

    /**
     * The name of custom templates.
     */
    public static final String CUSTOM_NAME = "custom";

    /**
     * The default BitSet which indicates a template is not an extension of
     * another (does not have a parent).
     */
    public static final BitSet PARENT_NONE = new BitSet();

    /**
     * The set of IDs of all templates which this template extended (includes
     * the ID of this template).
     */
    protected final BitSet extendedIds;

    /**
     * The components in this template, a BitSet for quick existential querying,
     * and a map from component id to index into the local components array.
     */
    protected Component<?>[] components;
    protected BitSet componentBitSet;
    protected int[] componentMap;

    /**
     * The controllers in this template, a BitSet for quick existential
     * querying, and a map from controller id to index into the local
     * controllers array.
     */
    protected Controller[] controllers;
    protected BitSet controllerBitSet;
    protected int[] controllerMap;

    /**
     * A flat array of {@link TemplateComponent}s created for every component in
     * this Template (or any component aliased through
     * {@link #alias(Component, Component)}). The elements in this array are
     * indexed based on the component id. Worst case this array is as large as
     * there are number of component definitions in the system.
     */
    protected TemplateComponent<?>[] handlers = {};

    /**
     * An array of factories indexed by the i'th component added to this
     * Template which uses {@link ComponentValueFactory}s.
     */
    protected ComponentValueFactory<?>[] factories = {};

    /**
     * The view of this template (if any).
     */
    protected View view;

    /**
     * The number of entities that exist that use this template.
     */
    protected int instances;

    /**
     * Instantiates a custom template without components, controllers, or a
     * view.
     */
    protected Template()
    {
        this( CUSTOM, CUSTOM_NAME, PARENT_NONE, ComponentSet.NONE, ControllerSet.NONE, View.NONE );
    }

    /**
     * Instantiates a custom template with a set of components, controllers, and
     * a view.
     * 
     * @param componentSet
     *        The components to add to this template.
     * @param controllerSet
     *        The controllers to add to this template.
     * @param view
     *        The view of this template (if any).
     */
    protected Template( ComponentSet componentSet, ControllerSet controllerSet, View view )
    {
        this( CUSTOM, CUSTOM_NAME, PARENT_NONE, componentSet, controllerSet, view );
    }

    /**
     * Instantiates a new Template given all required criteria.
     * 
     * @param id
     *        The id of the template.
     * @param name
     *        The name of the template.
     * @param parentIds
     *        The BitSet which contains IDs of parent templates.
     * @param componentSet
     *        The components to add to this template.
     * @param controllerSet
     *        The controllers to add to this template.
     * @param view
     *        The view of this template (if any).
     */
    protected Template( int id, String name, BitSet parentIds, ComponentSet componentSet, ControllerSet controllerSet, View view )
    {
        super( id, name );

        this.extendedIds = getExtendedIds( parentIds, id );

        this.components = componentSet.values;
        this.componentMap = EntityUtility.createMap( components );
        this.componentBitSet = new BitSet( components );

        this.controllers = controllerSet.values;
        this.controllerMap = EntityUtility.createMap( controllers );
        this.controllerBitSet = new BitSet( controllers );

        this.view = view;

        ensureComponentFit( EntityUtility.getMaxId( components ) );

        for (int i = 0; i < components.length; i++)
        {
            final Component<?> c = components[i];

            handlers[c.id] = c.add( this );
        }
    }

    /**
     * Creates a custom copy of this Template which has this template as a
     * parent.
     * 
     * @return A custom copy of this Template.
     */
    public Template copy()
    {
        return extend( CUSTOM, CUSTOM_NAME );
    }

    /**
     * Merges the given template into this template optionally overriding the
     * components and controllers in this template with the ones in the given
     * template (in the event this template already has a component).
     * 
     * @param template
     *        The template to merge into this one.
     * @param overwrite
     *        Whether the components and controllers that already exist in this
     *        template should be overwritten with the ones specifically in the
     *        given template.
     * @return The reference to this template.
     */
    public Template merge( Template template, boolean overwrite )
    {
        for (Component<?> c : template.components)
        {
            if (overwrite || !has( c ))
            {
                add( c );
            }
        }

        for (Controller c : template.controllers)
        {
            if (overwrite || !has( c ))
            {
                add( c );
            }
        }

        if (overwrite || !hasView())
        {
            setView( template.view );
        }

        return this;
    }

    /**
     * Extends this template and returns a new one.
     * 
     * @param id
     *        The ID of the new template.
     * @param name
     *        The name of the new template.
     * @return The reference to a newly created Template.
     */
    protected Template extend( int id, String name )
    {
        return new Template( id, name, extendedIds, new ComponentSet( components ), new ControllerSet( controllers ), view );
    }

    /**
     * Creates the initial set of distinct values stored in an entity.
     * 
     * @return An array of distinct component values.
     */
    protected Object[] createDefaultValues()
    {
        final int n = factories.length;
        final Object[] values = new Object[n];

        for (int i = 0; i < n; i++)
        {
            values[i] = factories[i].create();
        }

        return values;
    }

    /**
     * @return The {@link Renderer} for this Template.
     */
    protected Renderer createRenderer()
    {
        return (view == null ? null : view.renderer);
    }

    /**
     * Clones the the given values into a new array, optionally performing a
     * deep clone (opposed to a shallow reference-only clone).
     * 
     * @param values
     *        The array of component values to clone.
     * @param deep
     *        True for a deep clone, false for a shallow clone.
     * @return
     * @see Entity#clone(boolean)
     */
    @SuppressWarnings ("unchecked" )
    protected Object[] createClonedValues( Object[] values, boolean deep )
    {
        final int valueCount = values.length;
        final Object[] clonedValues = new Object[valueCount];

        if (deep)
        {
            for (int i = 0; i < valueCount; i++)
            {
                ComponentValueFactory<Object> factory = (ComponentValueFactory<Object>)factories[i];
                clonedValues[i] = factory.clone( values[i] );
            }
        }
        else
        {
            System.arraycopy( values, 0, clonedValues, 0, valueCount );
        }

        return clonedValues;
    }

    /**
     * Increments the number of instances of Entities for this template.
     * 
     * @param e
     *        The entity created with this template.
     */
    protected void newInstance( Entity e )
    {
        instances++;
    }

    /**
     * Decrements the number of instances of Entities for this template. If this
     * is a custom template and the number of instances is zero, this Template
     * is no longer useful.
     * 
     * @param e
     *        The entity removed from this template.
     */
    protected void removeInstance( Entity e )
    {
        instances--;
    }

    /**
     * Notifies all handlers in this Template that the given entity was just
     * created.
     * 
     * @param e
     *        The entity created with this template.
     */
    protected void addToComponents( Entity e )
    {
        for (int i = 0; i < components.length; i++)
        {
            handlers[components[i].id].postAdd( e );
        }
    }

    /**
     * Notifies all handlers in this Template that a given entity is about to
     * remove from this template.
     * 
     * @param e
     *        The entity about to be removed from this Template.
     */
    protected void removeFromComponents( Entity e )
    {
        for (int i = 0; i < components.length; i++)
        {
            handlers[components[i].id].preRemove( e );
        }
    }

    /**
     * @return The number of Entities that currently have this Template.
     */
    public int getInstances()
    {
        return instances;
    }

    /**
     * Returns the {@link TemplateComponent} that exists on this template for
     * the given component. If the given component does not exist in this
     * Template, either null may be returned or an
     * {@link IndexOutOfBoundsException} will be thrown.
     * 
     * @param component
     *        The component to get the {@link TemplateComponent} of.
     * @return The reference to the {@link TemplateComponent} in this Template
     *         for the given component if one exists.
     */
    @SuppressWarnings ("unchecked" )
    public <T> TemplateComponent<T> getTemplateComponent( Component<T> component )
    {
        return (TemplateComponent<T>)handlers[component.id];
    }

    /**
     * Returns whether this Template has the given component (or an
     * alternative).
     * 
     * @param component
     *        The component to test for existence.
     * @return True if the template has the component.
     */
    public <T> boolean has( Component<T> component )
    {
        return componentBitSet.get( component.id );
    }

    /**
     * Returns whether this Template has the given set of components.
     * 
     * @param components
     *        The set of components as a BitSet where a bit at index K points to
     *        component with ID K.
     * @return True if this template has all the components specified.
     */
    public boolean hasComponents( BitSet components )
    {
        return componentBitSet.contains( components );
    }

    /**
     * Returns whether this Template has the given component exactly.
     * 
     * @param component
     *        The component to test for existence.
     * @return True if the template has the exact component.
     */
    public <T> boolean hasExact( Component<T> component )
    {
        int i = indexOf( component );

        return (i != -1 && components[i] == component);
    }

    /**
     * Returns whether this Template has the given set of components (or their
     * alternatives).
     * 
     * @param components
     *        The set of components.
     * @return True if the template has all components specified.
     */
    public boolean has( Component<?>... components )
    {
        for (Component<?> c : components)
        {
            if (!has( c ))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines the index of the given component in the internal components
     * array.
     * 
     * @param component
     *        The component to get the internal index of.
     * @return The index of the component in this Template, or -1.
     */
    protected int indexOf( Component<?> component )
    {
        return component.id >= componentMap.length ? -1 : componentMap[component.id];
    }

    /**
     * Adds the given component to this Template, or overwrites an existing
     * alternative component. This should only be done before this Template
     * has any entities.
     * 
     * @param component
     *        The component to add to this template.
     * @return The reference to this Template.
     */
    public <T> Template add( Component<T> component )
    {
        if (instances != 0)
        {
            throw new RuntimeException( "You cannot directly add components to a Template which was used to create Entities" );
        }

        ensureComponentFit( component.id );

        int i = indexOf( component );

        if (i != -1)
        {
            handlers[component.id].remove( this );
            components[i] = component;
        }
        else
        {
            i = components.length;
            componentBitSet.set( component.id );
            components = EntityUtility.append( components, component );
        }

        handlers[component.id] = component.add( this );
        componentMap[component.id] = i;

        return this;
    }

    /**
     * Safely returns a {@link TemplateComponent} by doing all of the necessary
     * array bounds checking.
     * 
     * @param c
     *        The component to get the {@link TemplateComponent} of.
     * @return The referenc to the {@link TemplateComponent} for the given
     *         component, or null if none exists.
     */
    public TemplateComponent<?> getTemplateComponentSafe( Component<?> c )
    {
        return (c.id >= handlers.length ? null : handlers[c.id]);
    }

    /**
     * Adds to component to this Template for the given entity. If the Template
     * already has the given component it is overwritten (after the existing
     * component is notified of it's removal).
     * 
     * @param component
     *        The component to add to this Template.
     * @param e
     *        The entity that's having the component added.
     */
    protected <T> void addForEntity( Component<T> component, Entity e )
    {
        final int componentId = component.id;

        TemplateComponent<?> handler = null;

        ensureComponentFit( componentId );

        int i = indexOf( component );

        if (i != -1)
        {
            handler = handlers[componentId];
            handler.preRemove( e );
            handler.remove( this );
            components[i] = component;
        }
        else
        {
            i = components.length;
            componentBitSet.set( componentId );
            components = EntityUtility.append( components, component );
        }

        handler = component.add( this );
        handlers[componentId] = handler;
        componentMap[componentId] = i;
        component.postCustomAdd( e, this, handler );
        handler.postAdd( e );
    }

    /**
     * Adds an alias of one component to another.
     * 
     * @param component
     *        The component to alias.
     * @param alias
     *        The aliased component.
     * @return The reference to this Template.
     */
    public <T> Template alias( Component<T> component, Component<T> alias )
    {
        ensureComponentFit( alias.id );

        handlers[alias.id] = handlers[component.id];
        componentMap[alias.id] = componentMap[component.id];
        componentBitSet.set( alias.id );

        return this;
    }

    /**
     * Returns whether this Template has the given controller (or an
     * alternative).
     * 
     * @param controller
     *        The controller to test for existence.
     * @return True if the template has the controller.
     */
    public boolean has( Controller controller )
    {
        return controllerBitSet.get( controller.id );
    }

    /**
     * Returns whether this Template has the given set of controllers.
     * 
     * @param controllers
     *        The set of controllers as a BitSet where a bit at index K points
     *        to controller with ID K.
     * @return True if this template has all the controllers specified.
     */
    public boolean hasControllers( BitSet controllers )
    {
        return controllerBitSet.contains( controllers );
    }

    /**
     * Returns whether this Template has the given controller exactly.
     * 
     * @param controller
     *        The controller to test for existence.
     * @return True if the template has the exact controller.
     */
    public boolean hasExact( Controller controller )
    {
        int i = indexOf( controller );

        return (i != -1 && controllers[i] == controller);
    }

    /**
     * Returns whether this Template has the given set of controllers (or their
     * alternatives).
     * 
     * @param controllers
     *        The set of controllers.
     * @return True if the template has all controllers specified.
     */
    public boolean has( Controller... controllers )
    {
        for (Controller c : controllers)
        {
            if (!has( c ))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines the index of the given controller in the internal controllers
     * array.
     * 
     * @param controller
     *        The controller to get the internal index of.
     * @return The index of the controller in this Template, or -1.
     */
    protected int indexOf( Controller controller )
    {
        return controller.id >= controllerMap.length ? -1 : controllerMap[controller.id];
    }

    /**
     * Adds the given controller to this Template, or overwrites an existing
     * alternative controller. This should only be done before this Template has
     * any entities.
     * 
     * @param controller
     *        The controller to add to this Template.
     * @return The reference to this Template.
     */
    public Template add( Controller controller )
    {
        if (!has( controller ))
        {
            ensureControllerFit( controller.id );
            controllerMap[controller.id] = controllers.length;
            controllerBitSet.set( controller.id );
            controllers = EntityUtility.append( controllers, controller );
        }
        else
        {
            controllers[indexOf( controller )] = controller;
        }

        return this;
    }

    /**
     * @return True if this Template has a non-null view, otherwise false.
     */
    public boolean hasView()
    {
        return (view != null);
    }

    /**
     * @return True if this Template has a non-null view and it has a non-null
     *         renderer, otherwise false.
     */
    public boolean hasRenderer()
    {
        return (view != null && view.renderer != null);
    }

    /**
     * @return The {@link View} of this template (if any).
     */
    public View getView()
    {
        return view;
    }

    /**
     * Determines whether this Template has the given View. If the given view is
     * an exact match to the view of this Template (or it's <code>null</code>
     * and this Template doesn't have a view) or the view of this Template has
     * the same ID as the given view, true is returned.
     * 
     * @param v
     *        The view to check for existence (alternatives count).
     * @return True if this Template has the view (or an alternative), otherwise
     *         false.
     */
    public boolean has( View v )
    {
        return (v == view) || (view != null && v != null && view.id == v.id);
    }

    /**
     * Determines whether the view in this Template is an exact match for the
     * given View. <code>null</code> is an acceptable value to determine whether
     * this template doesn't have a view.
     * 
     * @param view
     *        The view to check for existence.
     * @return True if the Template has the view, otherwise false.
     */
    public boolean hasExact( View view )
    {
        return this.view == view;
    }

    /**
     * Sets the view of this Template. This should be done before entities are
     * created with this Template to avoid entities of the same template with
     * differing views.
     * 
     * @param view
     *        The new view of this Template.
     * @return The reference to this Template.
     */
    public Template setView( View view )
    {
        this.view = view;

        return this;
    }

    /**
     * Ensures the component with the given ID is going to fit in this Template
     * by resizing an necessary structures.
     * 
     * @param id
     *        The ID of the component being added.
     */
    private void ensureComponentFit( int id )
    {
        if (handlers.length <= id)
        {
            int componentMapSize = componentMap.length;

            handlers = Arrays.copyOf( handlers, id + 1 );
            componentMap = Arrays.copyOf( componentMap, id + 1 );

            for (int i = componentMapSize; i <= id; i++)
            {
                componentMap[i] = -1;
            }
        }
    }

    /**
     * Ensures the controller with the given ID is going to git in this Template
     * by resizing necessary structures.
     * 
     * @param id
     *        The ID of the controller being added.
     */
    private void ensureControllerFit( int id )
    {
        if (controllerMap.length <= id)
        {
            int controllerMapSize = controllerMap.length;

            controllerMap = Arrays.copyOf( controllerMap, id + 1 );

            for (int i = controllerMapSize; i <= id; i++)
            {
                controllerMap[i] = -1;
            }
        }
    }

    /**
     * @return True if this template is custom.
     */
    public boolean isCustom()
    {
        return (id == CUSTOM);
    }

    /**
     * Determines if this Template is a relative of the given template. A
     * template is a relative if it is not a custom template and this Template
     * has extended it at some point.
     * 
     * @param template
     *        The template to test for relativity.
     * @return True if the template is a relative, otherwise false.
     */
    public boolean isRelative( Template template )
    {
        return template.id != CUSTOM && extendedIds.get( template.id );
    }

    /**
     * Determines if this Template contains all of the components, controller,
     * and view of the given template. This template can have more, but if it
     * has less then false will be returned.
     * 
     * @param template
     *        The template to check for containment.
     * @return True if this Template contains the given template.
     */
    public boolean contains( Template template )
    {
        return controllerBitSet.contains( template.controllerBitSet ) &&
            componentBitSet.contains( template.componentBitSet ) &&
            has( template.view );
    }

    /**
     * Handles the request to add a component dynamically to the given Entity
     * and returns the new Template for the Entity (or this Template if it has
     * its own).
     * 
     * @param component
     *        The component to add or overwrite an existing alternative with.
     * @param e
     *        The entity adding the component.
     * @return The reference to the new template for the Entity.
     */
    protected <T> Template addCustomComponent( Component<T> component, Entity e )
    {
        if (hasExact( component ))
        {
            return this;
        }

        Template t = getCustomTarget();
        t.addForEntity( component, e );

        return t;
    }

    /**
     * Handles the request to alias a component dynamic for the given Entity and
     * returns the new Template for the Entity (or this Template if it has its
     * own).
     * 
     * @param component
     *        The aliased component.
     * @param alias
     *        The component to alias.
     * @return The reference to the new template for the Entity.
     */
    protected <T> Template setCustomAlias( Component<T> component, Component<T> alias )
    {
        Template t = this;

        if (has( component ))
        {
            if (handlers[alias.id] != handlers[component.id])
            {
                t = getCustomTarget();
                t.alias( component, alias );
            }
        }

        return t;
    }

    /**
     * Handles the request to add a controller dynamically to the given Entity
     * and returns the new Template for the Entity (or this Template if it has
     * its own).
     * 
     * @param controller
     *        The controller to add or overwrite an existing alternative with.
     * @param e
     *        The entity adding the controller.
     * @return The reference to the new template for the Entity.
     */
    protected Template addCustomController( Controller controller )
    {
        if (hasExact( controller ))
        {
            return this;
        }

        Template t = getCustomTarget();
        t.add( controller );

        return t;
    }

    /**
     * Handles the request to set a view dynamically for the given Entity and
     * returns the new Template for the Entity (or this Template if it has its
     * own).
     * 
     * @param view
     *        The view to set or overwrite an existing alternative with.
     * @return The reference to the new template for the Entity.
     */
    protected Template setCustomView( View view )
    {
        if (hasExact( view ))
        {
            return this;
        }

        Template t = getCustomTarget();
        t.setView( view );

        return t;
    }

    /**
     * Returns the Template to customize based on the current state of the
     * template and the number of entity instances using it.
     * 
     * @return A reference to a template to customize.
     */
    protected Template getCustomTarget()
    {
        return isCustom() && instances <= 1 ? this : extend( CUSTOM, CUSTOM_NAME );
    }

    /**
     * Returns a BitSet with the same IDs in parentIds and the specified id (if
     * it's a non-custom one).
     * 
     * @param parentIds
     *        The BitSet of parentIds.
     * @param id
     *        The id of the new template.
     * @return The {@link #extendedIds} for the new Template.
     */
    protected static BitSet getExtendedIds( BitSet parentIds, int id )
    {
        BitSet extendedIds = new BitSet( parentIds );

        if (id != CUSTOM)
        {
            extendedIds.set( id );
        }

        return extendedIds;
    }

}

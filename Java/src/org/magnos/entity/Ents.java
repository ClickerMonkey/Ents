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

import org.magnos.entity.util.ComponentSet;
import org.magnos.entity.util.ControllerSet;
import org.magnos.entity.util.IndexPool;


/**
 * The central class in the system which keeps track of all views, controllers,
 * templates, and components added. The creation of views, controllers,
 * templates, and components must only be done directly through this class. <br/>
 * <br/>
 * An {@link EntityListener} can be set and notified of every event that occurs
 * in the system.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Ents
{

    protected static final int DEFAULT_INITIAL_CAPACITY = 64;

    protected static IdContainer<View> views = new IdContainer<View>( DEFAULT_INITIAL_CAPACITY );

    protected static IdContainer<Controller> controllers = new IdContainer<Controller>( DEFAULT_INITIAL_CAPACITY );

    protected static IdContainer<Template> templates = new IdContainer<Template>( DEFAULT_INITIAL_CAPACITY );

    protected static IdContainer<Component<?>> components = new IdContainer<Component<?>>( DEFAULT_INITIAL_CAPACITY );

    protected static IndexPool indices = new IndexPool();

    protected static EntityListener listener;

    /**
     * Hidden constructor from non-implementing classes.
     */
    protected Ents()
    {
    }

    /**
     * Registers the Entity with Ents by providing it with an ID and notifying
     * the {@link EntityListener} if one exists. This occurs in the root
     * constructor of all entities.
     * 
     * @param e
     *        The entity to register.
     * @return The ID of the entity.
     * @see EntityListener#onEntityAdd(Entity, int)
     */
    protected static int register( Entity e )
    {
        int id = indices.pop();

        if (listener != null)
        {
            listener.onEntityAdd( e, id );
        }

        return id;
    }

    /**
     * Unregisters the entity with Ents by returning it's ID for future use and
     * notifying the {@link EntityListener} if one exists. This occurs when
     * {@link Entity#delete()} is called on an entity.
     * 
     * @param e
     *        The entity to unregister.
     */
    protected static void unregister( Entity e )
    {
        indices.push( e.id );

        if (listener != null)
        {
            listener.onEntityRemove( e );
        }
    }

    /**
     * Adds a new {@link View} definition without a default {@link Renderer}.
     * 
     * @param name
     *        The name of the view.
     * @return The reference to the newly created view.
     */
    public static View newView( String name )
    {
        return newView( name, null );
    }

    /**
     * Adds a new {@link View} definition.
     * 
     * @param name
     *        The name of the view.
     * @param defaultRenderer
     *        The default {@link Renderer} of the view.
     * @return The reference to the newly created view.
     */
    public static View newView( String name, Renderer defaultRenderer )
    {
        return registerView( true, views.addDefinition( new View( views.nextId(), name, defaultRenderer ) ) );
    }

    /**
     * Sets the default {@link Renderer} of the definition of the given
     * {@link View}. If the view given was the definition added, then it sets
     * its renderer. Otherwise it's an alternative and the definition is found
     * and the default renderer is set on that.
     * 
     * @param view
     *        The view to set the default renderer of.
     * @param defaultRenderer
     *        The default renderer of the specified view definition.
     */
    public static void setViewDefault( View view, Renderer defaultRenderer )
    {
        views.getDefinition( view ).renderer = defaultRenderer;
    }

    /**
     * Adds a new {@link View} alternative for the given view with the given
     * {@link Renderer}. An alternative view appears to be the same as the given
     * view, except it may have a different renderer.
     * 
     * @param view
     *        A view to create an alternative for (could be the definition or
     *        another alternative).
     * @param renderer
     *        The renderer of the alternative view.
     * @return The reference to the newly created view.
     */
    public static View newViewAlternative( View view, Renderer renderer )
    {
        return registerView( false, views.addInstance( new View( view.id, view.name, renderer ) ) );
    }

    /**
     * Invokes {@link EntityListener#onViewAdd(View, boolean)} on the listener
     * if it exists and returns the view passed in.
     * 
     * @param definition
     *        Whether or not the view was a view definition or an alternative
     *        view.
     * @param view
     *        The view added.
     * @return The view added.
     */
    private static View registerView( boolean definition, View view )
    {
        if (listener != null)
        {
            listener.onViewAdd( view, definition );
        }

        return view;
    }

    /**
     * Returns the container of all views added. The definitions and
     * alternatives (instances) are available in this container. This container
     * should not be modified directly.
     * 
     * @return The reference to the container with all of the views.
     */
    public static IdContainer<View> getViews()
    {
        return views;
    }

    /**
     * Adds a new {@link Controller} definition without a {@link Control}.
     * 
     * @param name
     *        The name of the controller.
     * @return The reference to the newly created controller.
     */
    public static Controller newController( String name )
    {
        return newController( name, null );
    }

    /**
     * Adds a new {@link Controller} definition.
     * 
     * @param name
     *        The name of the controller.
     * @param defaultControl
     *        The default {@link Control} of the controller.
     * @return The reference to the newly created controller.
     */
    public static Controller newController( String name, Control defaultControl )
    {
        return registerController( true, controllers.addDefinition( new Controller( controllers.nextId(), name, defaultControl ) ) );
    }

    /**
     * Sets the default {@link Control} of the definition of the given
     * {@link Controller}. If the controller given was the definition added,
     * then it sets its control. Otherwise it's an alternative and the
     * definition is found and the default control is set on that.
     * 
     * @param controller
     *        The controller to set the default control of.
     * @param defaultControl
     *        The default control of the specified controller definition.
     */
    public static void setControllerDefault( Controller controller, Control defaultControl )
    {
        controllers.getDefinition( controller ).control = defaultControl;
    }

    /**
     * Adds a new {@link Controller} alternative for the given controller with
     * the given {@link Control}. An alternative controller appears to be the
     * same as the given controller, except it may have a different control.
     * 
     * @param controller
     *        The controller to create an alternative for (could be the
     *        definition or another alternative).
     * @param control
     *        The control of the alternative controller.
     * @return
     */
    public static Controller newControllerAlternative( Controller controller, Control control )
    {
        return registerController( false, controllers.addInstance( new Controller( controller.id, controller.name, control ) ) );
    }

    /**
     * Invokes {@link EntityListener#onControllerAdd(Controller, boolean)} on
     * the listener if it exists and returns the controller passed in.
     * 
     * @param definition
     *        Whether or not the controller was a controller definition or an
     *        alternative controller.
     * @param controller
     *        The controller added.
     * @return The controller added.
     */
    private static Controller registerController( boolean definition, Controller controller )
    {
        if (listener != null)
        {
            listener.onControllerAdd( controller, definition );
        }

        return controller;
    }

    /**
     * Returns the container of all controllers added. The definitions and
     * alternatives (instances) are available in this container. This container
     * should not be modified directly.
     * 
     * @return The reference to the container with all of the controllers.
     */
    public static IdContainer<Controller> getControllers()
    {
        return controllers;
    }

    /**
     * Adds a new undefined {@link Component} definition. The returned component
     * should not be directly given to a {@link Template} or an {@link Entity}
     * directly. It's use is solely for creating alternatives. <br/>
     * <br/>
     * An undefined component is useful when writing a library that expects a
     * set of components. Another person may use that library by definition
     * component alternatives that map to their structure (aliased components
     * are useful here).
     * 
     * @param name
     *        The name of the component.
     * @return The reference to the newly created component.
     * @see ComponentUndefined
     */
    public static <T> Component<T> newComponent( String name )
    {
        return registerComponent( true, components.addDefinition( new ComponentUndefined<T>( components.nextId(), name ) ) );
    }

    /**
     * Adds a new distinct {@link Component} definition. <br/>
     * <br/>
     * A distinct component is useful when you want every entity with the
     * component to have it's own value.
     * 
     * @param name
     *        The name of the component.
     * @param factory
     *        The factory for creating default values, aid in cloning, and
     *        copying one value to another.
     * @return The reference to the newly created component.
     * @see ComponentDistinct
     */
    public static <T> Component<T> newComponent( String name, ComponentValueFactory<T> factory )
    {
        return registerComponent( true, components.addDefinition( new ComponentDistinct<T>( components.nextId(), name, factory ) ) );
    }

    /**
     * Adds a new distinct {@link Component} alternative. <br/>
     * <br/>
     * A distinct component is useful when you want every entity with the
     * component to have it's own value. <br/>
     * <br/>
     * An alternative component appears to be the same as the given component
     * but could be a different implementation. This enables entities to have
     * the same component but the underlying value could be stored or handled
     * differently (distinctly, shared, dynamically created, etc).
     * 
     * @param component
     *        The component to create an alternative for (could be the
     *        definition or another alternative).
     * @param factory
     *        The factory for creating default values, aid in cloning, and
     *        copying one value to another.
     * @return The reference to the newly created component.
     * @see ComponentDistinct
     */
    public static <T> Component<T> newComponentAlternative( Component<T> component, ComponentValueFactory<T> factory )
    {
        return registerComponent( false, components.addInstance( new ComponentDistinct<T>( component.id, component.name, factory ) ) );
    }

    /**
     * Adds a new shared {@link Component} definition. <br/>
     * <br/>
     * A shared component is useful when you want all entities that have the
     * same {@link Template} to share the same value. The best use case for
     * this is when a component is an interface which is notified of events.
     * Based on template, you will want entities to react differently to those
     * events - it would be a waste to store the interface component on all
     * entities.
     * 
     * @param name
     *        The name of the component.
     * @param factory
     *        The factory for creating default values, aid in cloning, and
     *        copying one value to another.
     * @return The reference to the newly created component.
     * @see ComponentShared
     */
    public static <T> Component<T> newComponentShared( String name, ComponentValueFactory<T> factory )
    {
        return registerComponent( true, components.addDefinition( new ComponentShared<T>( components.nextId(), name, factory ) ) );
    }

    /**
     * Adds a new shared {@link Component} alternative. <br/>
     * <br/>
     * A shared component is useful when you want all entities that have the
     * same {@link Template} to share the same value. The best use case for
     * this is when a component is an interface which is notified of events.
     * Based on template, you will want entities to react differently to those
     * events - it would be a waste to store the interface component on all
     * entities. <br/>
     * <br/>
     * An alternative component appears to be the same as the given component
     * but could be a different implementation. This enables entities to have
     * the same component but the underlying value could be stored or handled
     * differently (distinctly, shared, dynamically created, etc).
     * 
     * @param component
     *        The component to create an alternative for (could be the
     *        definition or another alternative).
     * @param factory
     *        The factory for creating default values, aid in cloning, and
     *        copying one value to another.
     * @return The reference to the newly created component.
     * @see ComponentShared
     */
    public static <T> Component<T> newComponentSharedAlternative( Component<T> component, ComponentValueFactory<T> factory )
    {
        return registerComponent( false, components.addInstance( new ComponentShared<T>( component.id, component.name, factory ) ) );
    }

    /**
     * Adds a new pooled {@link Component} definition. <br/>
     * <br/>
     * A pooled component recycles component values from deleted entities. This
     * is most useful when the value of a component is fairly expensive to
     * allocate.
     * 
     * @param name
     *        The name of the component.
     * @param factory
     *        The factory for creating default values, aid in cloning, and
     *        copying one value to another.
     * @return The reference to the newly created component.
     * @see ComponentPooled
     */
    public static <T> Component<T> newComponentPooled( String name, ComponentValueFactory<T> factory )
    {
        return registerComponent( true, components.addDefinition( new ComponentPooled<T>( components.nextId(), name, factory ) ) );
    }

    /**
     * Adds a new pooled {@link Component} alternative. <br/>
     * <br/>
     * A pooled component recycles component values from deleted entities. This
     * is most useful when the value of a component is fairly expensive to
     * allocate. <br/>
     * <br/>
     * An alternative component appears to be the same as the given component
     * but could be a different implementation. This enables entities to have
     * the same component but the underlying value could be stored or handled
     * differently (distinctly, shared, dynamically created, etc).
     * 
     * @param component
     *        The component to create an alternative for (could be the
     *        definition or another alternative).
     * @param factory
     *        The factory for creating default values, aid in cloning, and
     *        copying one value to another.
     * @return The reference to the newly created component.
     * @see ComponentPooled
     */
    public static <T> Component<T> newComponentPooledAlternative( Component<T> component, ComponentValueFactory<T> factory )
    {
        return registerComponent( false, components.addInstance( new ComponentPooled<T>( component.id, component.name, factory ) ) );
    }

    /**
     * Adds a new dynamic {@link Component} definition. <br/>
     * <br/>
     * A dynamic component doesn't store the component value anywhere, it is
     * generated upon request - typically based on components already on the
     * entity.
     * 
     * @param name
     *        The name of the component.
     * @param dynamic
     *        The {@link DynamicValue} implementation for the component.
     * @return The reference to the newly created component.
     * @see ComponentDynamic
     */
    public static <T> Component<T> newComponentDynamic( String name, DynamicValue<T> dynamic )
    {
        return registerComponent( true, components.addDefinition( new ComponentDynamic<T>( components.nextId(), name, dynamic ) ) );
    }

    /**
     * Adds a new dynamic {@link Component} alternative. <br/>
     * <br/>
     * A dynamic component doesn't store the component value anywhere, it is
     * generated upon request - typically based on components already on the
     * entity. <br/>
     * <br/>
     * An alternative component appears to be the same as the given component
     * but could be a different implementation. This enables entities to have
     * the same component but the underlying value could be stored or handled
     * differently (distinctly, shared, dynamically created, etc).
     * 
     * @param component
     *        The component to create an alternative for (could be the
     *        definition or another alternative).
     * @param dynamic
     *        The {@link DynamicValue} implementation for the component.
     * @return The reference to the newly created component.
     * @see ComponentDynamic
     */
    public static <T> Component<T> newComponentDynamicAlternative( Component<T> component, DynamicValue<T> dynamic )
    {
        return registerComponent( false, components.addInstance( new ComponentDynamic<T>( component.id, component.name, dynamic ) ) );
    }

    /**
     * Adds a global {@link Component} definition. <br/>
     * <br/>
     * A global component is one where all entities that have the component
     * share a value. This is also useful when used as an indicator. As an
     * indicator, an {@link EntityIterator} can be used to iterate over all
     * entities with the global component (and it doesn't waste space on every
     * entity).
     * 
     * @param name
     *        The name of the component.
     * @param constant
     *        The global value shared between all entities.
     * @param settable
     *        True if {@link Entity#set(Component, Object)} or
     *        {@link Entity#sets(Component, Object)} will overwrite the value
     *        for all entities, or false if the value is a constant and cannot
     *        be changed.
     * @return The reference to the newly created component.
     * @see ComponentGlobal
     */
    public static <T> Component<T> newComponentGlobal( String name, T constant, boolean settable )
    {
        return registerComponent( true, components.addDefinition( new ComponentGlobal<T>( components.nextId(), name, constant, settable ) ) );
    }

    /**
     * Adds a global {@link Component} alternative. <br/>
     * <br/>
     * A global component is one where all entities that have the component
     * share a value. This is also useful when used as an indicator. As an
     * indicator, an {@link EntityIterator} can be used to iterate over all
     * entities with the global component (and it doesn't waste space on every
     * entity). <br/>
     * <br/>
     * An alternative component appears to be the same as the given component
     * but could be a different implementation. This enables entities to have
     * the same component but the underlying value could be stored or handled
     * differently (distinctly, shared, dynamically created, etc).
     * 
     * @param component
     *        The component to create an alternative for (could be the
     *        definition or another alternative).
     * @param constant
     *        The global value shared between all entities.
     * @param settable
     *        True if {@link Entity#set(Component, Object)} or
     *        {@link Entity#sets(Component, Object)} will overwrite the value
     *        for all entities, or false if the value is a constant and cannot
     *        be changed.
     * @return The reference to the newly created component.
     * @see ComponentGlobal
     */
    public static <T> Component<T> newComponentGlobalAlternative( Component<T> component, T constant, boolean settable )
    {
        return registerComponent( false, components.addInstance( new ComponentGlobal<T>( component.id, component.name, constant, settable ) ) );
    }

    /**
     * Adds an aliased {@link Component} alternative. <br/>
     * <br/>
     * An aliased component is one that is actually another component. An
     * example of this could be position and center. Perhaps they are different
     * for some entities, but there may be some that are the same - that's where
     * aliasing is useful. You can appear to have several components when
     * actually having a single one. It saves memory and the time it would take
     * to copy the values back and forth and keeping them in sync. <br/>
     * <br/>
     * Aliasing is also achieved through
     * {@link Entity#alias(Component, Component)} and
     * {@link Template#alias(Component, Component)} without the need of creating
     * a new component. <br/>
     * <br/>
     * An alternative component appears to be the same as the given component
     * but could be a different implementation. This enables entities to have
     * the same component but the underlying value could be stored or handled
     * differently (distinctly, shared, dynamically created, etc).
     * 
     * @param component
     *        The component to alias.
     * @param alias
     *        The aliased component.
     * @return The reference to the newly created component.
     * @see ComponentAlias
     */
    public static <T> Component<T> newComponentAlias( Component<T> component, Component<T> alias )
    {
        return registerComponent( false, components.addInstance( new ComponentAlias<T>( alias.id, alias.name, component.id ) ) );
    }

    /**
     * Adds a custom {@link Component} definition. <br/>
     * <br/>
     * A custom component is a user-created component implementation. The ID of
     * the component must be the value of {@link IdContainer#nextId()} from
     * {@link #getComponents()} or an exception will be thrown.
     * 
     * @param custom
     *        The custom component definition to add.
     * @return The reference to the given component.
     */
    public static <T> Component<T> newComponentCustom( Component<T> custom )
    {
        return registerComponent( true, components.addDefinition( custom ) );
    }

    /**
     * Adds a custom {@link Component} alternative. <br/>
     * <br/>
     * A custom component is a user-created component implementation. The ID of
     * the component must be the ID of a component that has already been
     * defined. <br/>
     * <br/>
     * An alternative component appears to be the same as the given component
     * but could be a different implementation. This enables entities to have
     * the same component but the underlying value could be stored or handled
     * differently (distinctly, shared, dynamically created, etc).
     * 
     * @param custom
     *        The custom component definition to add.
     * @return The reference to the given component.
     */
    public static <T> Component<T> newComponentCustomAlternative( Component<T> custom )
    {
        return registerComponent( false, components.addInstance( custom ) );
    }

    /**
     * Adds a custom {@link Component} definition.
     * 
     * @param name
     *        The name of the component.
     * @param factory
     *        The {@link ComponentFactory} which creates component
     *        implementations based off of ID and name.
     * @return The reference to the newly created component.
     */
    public static <T> Component<T> newComponentCustom( String name, ComponentFactory<T> factory )
    {
        return registerComponent( true, components.addDefinition( factory.create( components.nextId(), name ) ) );
    }

    /**
     * Adds a custom {@link Component} alternative. <br/>
     * <br/>
     * An alternative component appears to be the same as the given component
     * but could be a different implementation. This enables entities to have
     * the same component but the underlying value could be stored or handled
     * differently (distinctly, shared, dynamically created, etc).
     * 
     * @param component
     *        The component to create an alternative for (could be the
     *        definition or another alternative).
     * @param factory
     *        The {@link ComponentFactory} which creates component
     *        implementations based off of ID and name.
     * @return The reference to the newly created component.
     */
    public static <T> Component<T> newComponentCustomAlternative( Component<T> component, ComponentFactory<T> factory )
    {
        return registerComponent( false, components.addInstance( factory.create( component.id, component.name ) ) );
    }

    /**
     * Invokes {@link EntityListener#onComponentAdd(Component, boolean)} on the
     * listener if it exists and returns the component passed in.
     * 
     * @param definition
     *        Whether or not the controller was a controller definition or an
     *        alternative controller.
     * @param component
     *        The component added.
     * @return The component added.
     */
    private static <T> Component<T> registerComponent( boolean definition, Component<T> component )
    {
        if (listener != null)
        {
            listener.onComponentAdd( component, definition );
        }

        return component;
    }

    /**
     * Returns the container of all components added. The definitions and
     * alternatives (instances) are available in this container. This container
     * should not be modified directly.
     * 
     * @return The reference to the container with all of the components.
     */
    public static IdContainer<Component<?>> getComponents()
    {
        return components;
    }

    /**
     * Creates a new custom {@link Template} that is detached from this class.
     * It is detached in the sense that it is not added to the
     * {@link #getTemplates()} container and the {@link EntityListener} (if it
     * exists) is not notified of it's creation.
     * 
     * @return The reference to the newly created custom Template.
     */
    public static Template newTemplate()
    {
        return new Template();
    }

    /**
     * Adds a new template with the given name, components, controllers, and
     * view.
     * 
     * @param name
     *        The name of the template.
     * @param componentSet
     *        The components to add to the template.
     * @param controllerSet
     *        The controllers to add to the template.
     * @param view
     *        The view of the template.
     * @return The reference to the newly created template.
     */
    public static Template newTemplate( String name, ComponentSet componentSet, ControllerSet controllerSet, View view )
    {
        return registerTemplate( templates.addDefinition( new Template( templates.nextId(), name, null, componentSet, controllerSet, view ) ) );
    }

    /**
     * Adds a new template with the given name, components, and controllers. The
     * returned template will have no view.
     * 
     * @param name
     *        The name of the template.
     * @param componentSet
     *        The components to add to the template.
     * @param controllerSet
     *        The controllers to add to the template.
     * @return The reference to the newly created template.
     */
    public static Template newTemplate( String name, ComponentSet componentSet, ControllerSet controllerSet )
    {
        return newTemplate( name, componentSet, controllerSet, View.NONE );
    }

    /**
     * Adds a new template with the given name and components. The returned
     * template will have no controllers or view.
     * 
     * @param name
     *        The name of the template.
     * @param components
     *        The components to add to the template.
     * @return The reference to the newly created template.
     */
    public static Template newTemplate( String name, Component<?>... components )
    {
        return newTemplate( name, new ComponentSet( components ), ControllerSet.NONE, View.NONE );
    }

    /**
     * Adds a new template with the given name. The returned template will have
     * no components, controllers, or view.
     * 
     * @param name
     *        The name of the template.
     * @return The reference to the newly created template.
     */
    public static Template newTemplate( String name )
    {
        return newTemplate( name, ComponentSet.NONE, ControllerSet.NONE, View.NONE );
    }

    /**
     * Adds a new template that extends a given base template. If the given
     * components, controllers, or view are already on the template then they
     * are overwritten with the components, controllers, and views passed to
     * this method. If the view given is null, it does not change the view of
     * the template.
     * 
     * @param base
     *        The template to extend.
     * @param name
     *        The name of the new template.
     * @param componentSet
     *        The components to ensure existence on the new template.
     * @param controllerSet
     *        The controllers to ensure existence on the new template.
     * @param view
     *        The view to set to the new template (if non-null).
     * @return The reference to the newly created Template.
     */
    public static Template newTemplate( Template base, String name, ComponentSet componentSet, ControllerSet controllerSet, View view )
    {
        Template t = templates.addDefinition( base.extend( templates.nextId(), name ) );

        for (Component<?> component : componentSet.values)
        {
            t.add( component );
        }

        for (Controller controller : controllerSet.values)
        {
            t.add( controller );
        }

        if (view != null)
        {
            t.setView( view );
        }

        return registerTemplate( t );
    }

    /**
     * Adds a new template that extends a given base template. If the given
     * components or controllers are already on the template then they are
     * overwritten with the components and controllers passed to this method.
     * 
     * @param base
     *        The template to extend.
     * @param name
     *        The name of the new template.
     * @param componentSet
     *        The components to ensure existence on the new template.
     * @param controllerSet
     *        The controllers to ensure existence on the new template.
     * @return The reference to the newly created Template.
     */
    public static Template newTemplate( Template base, String name, ComponentSet componentSet, ControllerSet controllerSet )
    {
        return newTemplate( base, name, componentSet, controllerSet, View.NONE );
    }

    /**
     * Adds a new template that extends a given base template. If the given
     * components are already on the template then they are overwritten with the
     * components passed to this method.
     * 
     * @param base
     *        The template to extend.
     * @param name
     *        The name of the new template.
     * @param components
     *        The components to ensure existence on the new template.
     * @return The reference to the newly created Template.
     */
    public static Template newTemplate( Template base, String name, Component<?>... components )
    {
        return newTemplate( base, name, new ComponentSet( components ), ControllerSet.NONE, View.NONE );
    }

    /**
     * Adds a new template that extends a given base template.
     * 
     * @param base
     *        The template to extend.
     * @param name
     *        The name of the new template.
     * @return The reference to the newly created Template.
     */
    public static Template newTemplate( Template base, String name )
    {
        return newTemplate( base, name, ComponentSet.NONE, ControllerSet.NONE, View.NONE );
    }

    /**
     * Adds a new template that is created by sequentially merging the given
     * templates together.
     * 
     * @param name
     *        The name of the new template.
     * @param overwrite
     *        Whether or not components that already exist on the new template
     *        should be overwritten while merging templates into it.
     * @param templatesToMerge
     *        The templates to merge together to create a new template. The new
     *        template returned is an extension of the first template specified
     *        in this array.
     * @return The reference to the newly created Template.
     */
    public static Template newTemplate( String name, boolean overwrite, Template... templatesToMerge )
    {
        Template t = templates.addDefinition( templatesToMerge[0].extend( templates.nextId(), name ) );

        for (int i = 1; i < templatesToMerge.length; i++)
        {
            t.merge( templatesToMerge[i], overwrite );
        }

        return registerTemplate( t );
    }

    /**
     * Invokes {@link EntityListener#onTemplateAdd(Template)} on the listener if
     * it exists and returns the template passed in.
     * 
     * @param template
     *        The template added.
     * @return The template added.
     */
    private static Template registerTemplate( Template template )
    {
        if (listener != null)
        {
            listener.onTemplateAdd( template );
        }

        return template;
    }

    /**
     * Returns the container of all templates added. The definitions and
     * alternatives (instances) are available in this container. This container
     * should not be modified directly.
     * 
     * @return The reference to the container with all of the templates.
     */
    public static IdContainer<Template> getTemplates()
    {
        return templates;
    }

    /**
     * Clears the system of all views, controllers, components, templates, and
     * the listener if it existed.
     */
    public static void clear()
    {
        if (listener != null)
        {
            listener.onCoreClear();
            listener = null;
        }

        views.clear();
        controllers.clear();
        components.clear();
        templates.clear();
        indices.clear();
    }

    /**
     * Sets the {@link EntityListener} which is notified when components,
     * controllers, views, templates, and entities are added to the system.
     * 
     * @param listener
     *        The EntityListener to notify of all events.
     */
    public static void setListener( EntityListener listener )
    {
        Ents.listener = listener;
    }

    /**
     * @return The {@link EntityListener} which is notified when components,
     *         controllers, views, templates, and entities are added to the
     *         system.
     */
    public static EntityListener getListener()
    {
        return listener;
    }

}

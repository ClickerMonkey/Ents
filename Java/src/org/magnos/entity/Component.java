package org.magnos.entity;

/**
 * A component is added to an {@link Entity} (directly or via a 
 * {@link Template}) and it adds "value" to that Entity. The Entity can then
 * get and set the value of the component. 
 * 
 * By default a component has no defined behavior, this is an abstract class
 * that when implemented will receive addition/removal requests to and from 
 * Templates as well as set/get/add requests from an Entity.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The component value type.
 */
public abstract class Component<T> extends Id
{

	/**
	 * Instantiates a Component with the given id and name.
	 * 
	 * @param id
	 * 		The id of the component.
	 * @param name
	 * 		The name of the component.
	 */
	protected Component( int id, String name )
	{
		super( id, name );
	}

	/**
	 * When a component is added to a template it returns a 
	 * {@link TemplateComponent} which is responsible for handling how a
	 * components value is gotten or set for an entity. It also handles being
	 * removed from the template.
	 * 
	 * @param template
	 * 		The template to add a component to.
	 * @return
	 * 		A TemplateComponent implementation for this component.
	 */
	protected abstract TemplateComponent<T> add( Template template );

	/**
	 * The method invoked after a component is successfully dynamically added
	 * to an {@link Entity} for the first time.
	 * 
	 * @param e
	 * 		The entity that has just had this component added to it.
	 */
	protected abstract void postCustomAdd( Entity e );

}
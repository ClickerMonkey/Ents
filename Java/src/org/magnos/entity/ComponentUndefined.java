package org.magnos.entity;

/**
 * A component that does not have a definition and cannot be directly added to
 * a {@link Template}. This type of component would commonly be used to define
 * sets of components Entities should have in order to operate with some piece 
 * of functionality. An entity will have an alternative of this component, but
 * never the component exactly.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The component value type.
 */
class ComponentUndefined<T> extends Component<T>
{

	/**
	 * Instantiates a new ComponentUndefined.
	 * 
	 * @param id
	 * 		The id of the component.
	 * @param name
	 * 		The name of the component.
	 */
	protected ComponentUndefined( int id, String name )
	{
		super( id, name );
	}

	@Override
	protected void postCustomAdd( Entity e )
	{

	}

	@Override
	protected TemplateComponent<T> add( Template template )
	{
		throw new RuntimeException( "An undefined component cannot be added to a Template" );
	}

}
package org.magnos.entity;

/**
 * A component where Entities of the same {@link Template} all share a value.
 * This type of component is good when you have a Template like "ship" and
 * you want all ships to handle collisions the same way (exploding, etc) but
 * it is a waste of space to store the same collision handler on every ship 
 * instance.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The component value type.
 */
class ComponentShared<T> extends Component<T>
{

	/**
	 * The factory that creates the default value on the template.
	 */
	private final ComponentFactory<T> factory;

	/**
	 * Instantiates a new ComponentShared.
	 * 
	 * @param id
	 * 		The id of the component.
	 * @param name
	 * 		The name of the component.
	 * @param factory
	 * 		The factory implementation responsible for creating default values.
	 */
	protected ComponentShared( int id, String name, ComponentFactory<T> factory )
	{
		super( id, name );

		this.factory = factory;
	}

	@Override
	protected void postCustomAdd( Entity e )
	{

	}

	@Override
	protected TemplateComponent<T> add( Template template )
	{
		return new ComponentSharedHandler( factory.create() );
	}

	private class ComponentSharedHandler implements TemplateComponent<T>
	{
		private T value;

		private ComponentSharedHandler( T value )
		{
			this.value = value;
		}

		@Override
		public void set( Entity e, T value )
		{
			this.value = value;
		}

		@Override
		public T get( Entity e )
		{
			return value;
		}

		@Override
		public T take(Entity e, T target) 
		{
			return factory.copy( value, target );
		}

		@Override
		public void remove( Template template )
		{

		}
		
	}

}

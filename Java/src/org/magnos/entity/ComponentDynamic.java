package org.magnos.entity;

/**
 * A component that has a dynamically set and computed value. This is useful
 * when an {@link Entity} has something like position, and you want it to have
 * a center component as well. It would be a waste of space to store the center
 * when you can compute it upon request, or even set the position of the Entity
 * given the center.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The component value type.
 */
class ComponentDynamic<T> extends Component<T>
{

	/**
	 * The interface that handles dynamically computing a value and
	 * dynamically modifying an entity based on some set value.
	 */
	private final DynamicValue<T> dynamic;

	/**
	 * A cached handler.
	 */
	private final ComponentDynamicHandler handler;

	/**
	 * Instantiates a new ComponentDynamic.
	 * 
	 * @param id
	 * 		The id of the component.
	 * @param name
	 * 		The name of the component.
	 * @param dynamic
	 * 		The DynamicValue implementation for this component.
	 */
	protected ComponentDynamic( int id, String name, DynamicValue<T> dynamic )
	{
		super( id, name );

		this.dynamic = dynamic;
		this.handler = new ComponentDynamicHandler();
	}

	@Override
	protected void postCustomAdd( Entity e )
	{

	}

	@Override
	protected TemplateComponent<T> add( Template template )
	{
		return handler;
	}

	private class ComponentDynamicHandler implements TemplateComponent<T>
	{
		
		@Override
		public void set( Entity e, T value )
		{
			dynamic.set( e, value );
		}

		@Override
		public T get( Entity e )
		{
			return dynamic.get( e );
		}

		@Override
		public T take(Entity e, T target) 
		{
			return dynamic.take( e, target );
		}

		@Override
		public void remove( Template template )
		{

		}
		
	}

}
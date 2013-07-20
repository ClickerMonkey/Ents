package org.magnos.entity;

/**
 * A component factory is responsible for creating a default value for
 * an {@link Entity}'s component, as well as cloning an Entity's component
 * value to pass along to the cloned Entity.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The component value type.
 */
public interface ComponentFactory<T>
{
	
	/**
	 * Creates a default value for an {@link Entity}'s component.
	 * 
	 * @return
	 * 		A newly instantiated value.
	 */
	public T create();

	/**
	 * Takes the given value and returns a new instance that is equivalent.
	 * 
	 * @param value
	 * 		A value to clone.
	 * @return
	 * 		A newly instantiated value that is equal to the value passed in.
	 */
	public T clone( T value );
	
	/**
	 * Sets "to" to the value of "from" and returns the reference to "to".
	 * 
	 * @param from
	 * 		The value to copy from.
	 * @param target
	 * 		The value to set to march "from".
	 * @return
	 * 		The reference to "to".
	 */
	public T copy( T from, T to );
	
}

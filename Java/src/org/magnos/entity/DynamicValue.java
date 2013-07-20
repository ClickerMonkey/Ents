package org.magnos.entity;

/**
 * An interface responsible for dynamically generating a value for an 
 * {@link Entity}'s component, as well as modifying the Entity when the
 * {@link Entity#set(Component, Object)} or 
 * {@link Entity#sets(Component, Object)} methods are invoked. Implementations
 * may return cached values to avoid instantiation every time 
 * {@link #get(Entity)} is invoked. If this is done, the user needs to ensure
 * that a returned value is done being used before {@link #get(Entity)} is
 * invoked again on another Entity.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The component value type.
 */
public interface DynamicValue<T>
{
	
	/**
	 * Dynamically computes a value for the given Entity.
	 *  
	 * @param e
	 * 		The entity to compute a value for.
	 * @return
	 * 		The reference to the dynamically computed value.
	 */
	public T get( Entity e );
	
	/**
	 * Dynamically modifies the state of the Entity based on the value
	 * passed in. 
	 * 
	 * @param e
	 * 		The entity to modify.
	 * @param value
	 * 		The value that modifies the state of the entity.
	 */
	public void set( Entity e, T value );
	
	/**
	 * Dynamically computes a value for the given Entity, sets it to the target
	 * value passed in, and returns the reference of target.
	 * 
	 * @param e
	 * 		The entity to compute a value for.
	 * @param target
	 * 		The value to set to the computed value.
	 * @return
	 * 		The reference to the computed value.
	 */
	public T take( Entity e, T target );
	
}
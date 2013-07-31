
package org.magnos.entity;

/**
 * The interface to implement that is called every {@link Entity#update(Object)}
 * invocation.
 * 
 * @author Philip Diffenderfer
 * 
 */
public interface Control
{

   /**
    * Executes controller logic on the given Entity with the given
    * updateState (which is defined by the user).
    * 
    * @param e
    *        The entity to control.
    * @param updateState
    *        The update state passed to the Entity.
    */
   public void control( Entity e, Object updateState );

}

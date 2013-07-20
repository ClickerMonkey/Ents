package org.magnos.entity;

/**
 * A controller is something that updates the state of an {@link Entity} every
 * time {@link Entity#update(Object)} is called. An Entity can have many
 * Controllers at a time, and they are executed in the order that they are
 * added (Controllers added from a {@link Template} reside first in an Entity).
 * <br/><br/>
 * <b>Controller Examples</b>
 * <ul>
 * <li>User Input</li>
 * <li>Physics / Collisions</li>
 * <li>Networking</li>
 * <li>AI (State Machine, Steering Behaviors)</li>
 * <li>Emit Particles</li>
 * <li>Record / Replay Entity</li>
 * </ul>
 * 
 * @author Philip Diffenderfer
 *
 */
public class Controller extends Id
{

	/**
	 * The interface to implement that is called every 
	 * {@link Entity#update(Object)} invocation.
	 * 
	 * @author Philip Diffenderfer
	 *
	 */
	public static interface Control
	{

		/**
		 * Executes controller logic on the given Entity with the given 
		 * updateState (which is defined by the user).
		 * 
		 * @param e
		 * 		The entity to control.
		 * @param updateState
		 * 		The update state passed to the Entity.
		 */
		public void control( Entity e, Object updateState );
		
	}

	/**
	 * The Control implementation to invoke for this Controller.
	 */
	public Control control;

	/**
	 * Instantiates a new Controller.
	 * 
	 * @param id
	 * 		The id of the controller.
	 * @param name
	 * 		The name of the controller.
	 * @param control
	 * 		The {@link Control} implementation.
	 */
	public Controller( int id, String name, Control control )
	{
		super( id, name );

		this.control = control;
	}

}
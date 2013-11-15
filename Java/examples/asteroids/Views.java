
package asteroids;

import org.magnos.entity.Ents;
import org.magnos.entity.View;


/**
 * {@link View}s for {@link Asteroids}.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Views
{

	public static View ASTEROID = Ents.newView( "asteroid" );
	public static View SHIP = Ents.newView( "ship" );
	public static View LASER = Ents.newView( "laser" );
	public static View PARTICLE = Ents.newView( "particle" );
	public static View PARTICLE_SYSTEM = Ents.newView( "particle-system" );
}

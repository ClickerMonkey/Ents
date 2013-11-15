package asteroids;

import static org.magnos.entity.filters.Filters.*;

import org.magnos.entity.EntityFilter;

/**
 * {@link EntityFilter}s for {@link Asteroids}.
 * 
 * @author Philip Diffenderfer
 *
 */
public class Filtering
{
    public static EntityFilter ASTEROIDS = template( Templates.ASTEROID );
    public static EntityFilter WRAPPABLE = and( components( Components.POSITION, Components.RADIUS ), not( expired() ) );
    public static EntityFilter LASERS = template( Templates.LASER );
    public static EntityFilter COLLIDABLE = and( components( Components.POSITION, Components.RADIUS ), not( expired() ) );
}

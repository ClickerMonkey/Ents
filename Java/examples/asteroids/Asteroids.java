/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package asteroids;

import static asteroids.Components.*;

import java.awt.Color;
import java.util.Random;

import javax.swing.Renderer;

import org.magnos.entity.Entity;
import org.magnos.entity.EntityIterator;
import org.magnos.entity.EntityList;
import org.magnos.entity.Ents;
import org.magnos.entity.vals.FloatVal;


/**
 * Asteroids Game developed with {@link Ents}.
 * 
 * @author Philip Diffenderfer
 * @see Views
 * @see Components
 * @see Controllers
 * @see Templates
 */
public class Asteroids
{

	public static final int INITIAL_SPAWN_COUNT = 5;
	public static final float ASTEROID_RADIUS_MIN = 20.0f;
	public static final float ASTEROID_RADIUS_MAX = 50.0f;
	public static final float ASTEROID_ANGULAR_VELOCITY_MAX = 4.0f;
	public static final float ASTEROID_SPEED_MAX = 200.0f;
	public static final int ASTEROID_LIVES = 3;
	public static final float ASTEROID_SPAWN_SHIP_BUFFER = 100.0f;
	public static final float ASTEROID_SPAWN_SHIP_BUFFER_SQ = ASTEROID_SPAWN_SHIP_BUFFER * ASTEROID_SPAWN_SHIP_BUFFER;
	public static final float SHIP_RELOAD = 0.3f;
	public static final float LASER_SPEED = 700.0f;
	public static final float LASER_RADIUS = 5.0f;

	public enum GameState
	{
		PLAYING, DEATH
	}

	public Random random;

	public EntityList entities;
	public EntityList particles;

	public Entity ship;
	public Vector shipForce;
	public Vector shipLook;
	public boolean shipBrakes;
	public boolean shooting;
	public float shootingTime;

	public Vector spaceSize = new Vector();
	public Vector spaceCenter = new Vector();

	public UpdateState updateState = new UpdateState();

	public EntityIterator iterator = new EntityIterator();
	public EntityIterator iteratorInner = new EntityIterator();

	public GameState state;
	public float stateTime;

	public int spawnCount;

	public Asteroids()
	{
		Collisions.register( Templates.ASTEROID, Templates.LASER, new CollisionHandler()
		{
			public void handle( Entity subject, Entity object )
			{
				handleAsteroidDeath( subject );
				handleLaserDeath( object );
			}
		} );

		Collisions.register( Templates.ASTEROID, Templates.SHIP, new CollisionHandler()
		{
			public void handle( Entity subject, Entity object )
			{
				handleAsteroidDeath( subject );
				handleShipDeath();
			}
		} );

		Collisions.register( Templates.ASTEROID, Templates.ASTEROID, new CollisionHandler()
		{
			public void handle( Entity a, Entity b )
			{
				handleBouncyCollision( a, b );
			}
		} );
	}

	public void setSize( int w, int h )
	{
		spaceSize.set( w, h );
		spaceCenter.set( w * 0.5f, h * 0.5f );
	}

	/**
	 * Clears all entities and particles, sets the ship's position to the
	 * center, and sets the {@link GameState} to {@link GameState#PLAYING}.
	 */
	public void reset()
	{
		if (entities != null)
		{
			entities.delete();
		}

		if (particles != null)
		{
			particles.delete();
		}

		entities = new EntityList();
		particles = new EntityList( Templates.PARTICLE_SYSTEM );

		ship = new Entity( Templates.SHIP );
		ship.set( COLOR, Color.white );
		ship.get( POSITION ).set( spaceCenter );
		ship.get( RADIUS ).v = 20.0f;
		shipForce = new Vector();
		shipLook = new Vector( ship.get( POSITION ) );

		entities.add( ship );
		setState( GameState.PLAYING );
	}

	/**
	 * Spawns the given number of asteroids and sets the current spawnCount to
	 * the given asteroidCount;
	 * 
	 * @param asteroidCount
	 *        The number of asteroids to spawn.
	 */
	public void spawn( int asteroidCount )
	{
		spawnCount = asteroidCount;

		while (--asteroidCount >= 0)
		{
			entities.add( newAsteroid() );
		}
	}

	/**
	 * Updates all entities in the Asteroids game.
	 * 
	 * @param dt
	 *        Seconds since the last update.
	 */
	public void update( float dt )
	{
		updateState.dt = dt;

		// update state
		updateState( dt );

		// update ship angle and acceleration
		if (state == GameState.PLAYING)
		{
			updateShipAndShooting( dt );
		}

		// update positions, velocities, angles
		entities.update( updateState );

		// update particles
		particles.update( updateState );

		// kill lasers out of space
		killEscapedLasers();

		// perform wrapping
		wrapEntities();

		// collision detection
		if (state == GameState.PLAYING)
		{
			handleCollisions();
		}
	}

	/**
	 * Updates the state of the game based on the current state, how long it's
	 * been in that state, and based on how many asteroids are left.
	 * 
	 * @param dt
	 *        Seconds since the last update.
	 */
	private void updateState( float dt )
	{
		if (state == GameState.DEATH)
		{
			if (stateTime == 0.0f)
			{
				entities.clear( true );
			}
			else if (stateTime >= 3.0f)
			{
				reset();
				spawn( INITIAL_SPAWN_COUNT );
			}
		}

		if (state == GameState.PLAYING)
		{
			if (iterator.iterate( entities, Filtering.ASTEROIDS ).count() == 0)
			{
				reset();
				spawn( spawnCount + 1 );
			}
		}

		stateTime += dt;
	}

	/**
	 * Updates the ship's angle based on it's position and the specified look-at
	 * vector, it's acceleration based on the specified force, and enables or
	 * disables drag based on whether automatic breaking is enabled. Also
	 * updates shooting.
	 * 
	 * @param dt
	 *        Seconds since the last update.
	 */
	private void updateShipAndShooting( float dt )
	{
		Vector shipPos = ship.get( POSITION );
		FloatVal shipAng = ship.get( ANGLE );

		shipAng.v = (float)Math.atan2( shipLook.y - shipPos.y, shipLook.x - shipPos.x );
		ship.get( ACCELERATION ).set( shipForce );
		ship.setControllerEnabled( Controllers.DRAG, shipBrakes );

		if (shooting)
		{
			shootingTime += dt;

			if (shootingTime >= SHIP_RELOAD)
			{
				shootingTime -= SHIP_RELOAD;
				entities.add( newLaser() );
			}
		}
		else
		{
			shootingTime = SHIP_RELOAD;
		}
	}

	/**
	 * Any laster that's entirely outside of the view, expire it.
	 */
	private void killEscapedLasers()
	{
		for (Entity e : iterator.iterate( entities, Filtering.LASERS ))
		{
			Vector p = e.get( POSITION );
			Vector v = e.get( VELOCITY );
			float left = Math.min( p.x, p.x + v.x );
			float right = Math.max( p.x, p.x + v.x );
			float top = Math.min( p.y, p.y + v.y );
			float bottom = Math.max( p.y, p.y + v.y );

			if (right < 0 || left > spaceSize.x || bottom < 0 || top > spaceSize.y)
			{
				e.expire();
			}
		}
	}

	/**
	 * Any entity with POSITION and RADIUS can be wrapped around space.
	 */
	private void wrapEntities()
	{
		for (Entity e : iterator.iterate( entities, Filtering.WRAPPABLE ))
		{
			Vector p = e.get( POSITION );
			float r = e.get( RADIUS ).v;

			if (p.x < -r) p.x += spaceSize.x + r * 2;
			if (p.x > spaceSize.x + r) p.x -= spaceSize.x + r * 2;
			if (p.y < -r) p.y += spaceSize.y + r * 2;
			if (p.y > spaceSize.y + r) p.y -= spaceSize.y + r * 2;
		}
	}

	/**
	 * Checks for collisions between all collide-able entities.
	 */
	private void handleCollisions()
	{
		for (Entity a : iterator.iterate( entities, Filtering.COLLIDABLE ))
		{
			for (Entity b : iteratorInner.iterate( entities, Filtering.COLLIDABLE ))
			{
				if (a != b)
				{
					Vector ap = a.get( POSITION );
					float ar = a.get( RADIUS ).v;

					Vector bp = b.get( POSITION );
					float br = b.get( RADIUS ).v;

					float radiusSum = ar + br;

					if (ap.distanceSq( bp ) <= radiusSum * radiusSum)
					{
						CollisionHandler handler = Collisions.getHandler( a.getTemplate(), b.getTemplate() );

						if (handler != null)
						{
							handler.handle( a, b );
						}
					}
				}
			}
		}
	}

	/**
	 * Draws all entities and particles.
	 * 
	 * @param drawState
	 *        The drawState to pass to {@link Renderer}s.
	 */
	public void draw( Object drawState )
	{
		entities.draw( drawState );
		particles.draw( drawState );
	}

	/**
	 * Creates a new asteroid randomly placed away from the ship spawn point.
	 * 
	 * @return The reference to the newly created Asteroid.
	 */
	private Entity newAsteroid()
	{
		float r = rnd( ASTEROID_RADIUS_MIN, ASTEROID_RADIUS_MAX );
		float d = rnd( 0, 6.283184f );
		float dx = (float)Math.cos( d );
		float dy = (float)Math.sin( d );

		Entity e = new Entity( Templates.ASTEROID );
		e.set( COLOR, Color.LIGHT_GRAY );
		e.get( RADIUS ).v = r;
		e.get( LIVES ).v = ASTEROID_LIVES;
		e.get( ANGLE ).v = rnd( 0, 6.283184f );
		e.get( ANGULAR_VELOCITY ).v = rnd( -ASTEROID_ANGULAR_VELOCITY_MAX, ASTEROID_ANGULAR_VELOCITY_MAX );
		e.get( VELOCITY ).set( dx * rnd( 0, ASTEROID_SPEED_MAX ), dy * rnd( 0, ASTEROID_SPEED_MAX ) );

		boolean placed = false;
		while (!placed)
		{
			e.get( POSITION ).set( rnd( r, spaceSize.x - r ), rnd( r, spaceSize.y - r ) );

			if (e.get( POSITION ).distanceSq( spaceCenter ) >= ASTEROID_SPAWN_SHIP_BUFFER_SQ)
			{
				placed = true;
			}
		}

		return e;
	}

	/**
	 * Creates a new laser coming from the front of the ship.
	 * 
	 * @return The reference to the newly created Laser.
	 */
	private Entity newLaser()
	{
		Entity e = new Entity( Templates.LASER );
		e.get( POSITION ).set( ship.get( POSITION ) );
		e.get( VELOCITY ).angle( ship.get( ANGLE ).v, LASER_SPEED );
		e.get( RADIUS ).v = LASER_RADIUS;
		e.set( COLOR, Color.red );

		return e;
	}

	/**
	 * Handles what occurs when something collides with a laser.
	 * 
	 * @param a
	 *        The laser that was collided with.
	 */
	private void handleLaserDeath( Entity a )
	{
		explosion( 3, a.get( POSITION ), Color.red, 30.0f, 100.0f, 0.75f, 1.25f );

		a.expire();
	}

	/**
	 * Handles what occurs when something collides with an asteroid.
	 * 
	 * @param a
	 *        The asteroid that was collided with.
	 */
	private void handleAsteroidDeath( Entity a )
	{
		int remainingLives = a.get( LIVES ).v - 1;

		if (remainingLives > 0)
		{
			float scale = (float)remainingLives / ASTEROID_LIVES;

			Entity a0 = newAsteroid();
			Entity a1 = newAsteroid();

			a0.get( LIVES ).v = remainingLives;
			a1.get( LIVES ).v = remainingLives;
			a0.get( RADIUS ).v *= scale;
			a1.get( RADIUS ).v *= scale;
			a1.get( VELOCITY ).negi();
			a0.get( POSITION ).set( a.get( POSITION ) ).addsi( a0.get( VELOCITY ).normal(), a0.get( RADIUS ).v * 0.5f );
			a1.get( POSITION ).set( a.get( POSITION ) ).addsi( a1.get( VELOCITY ).normal(), a1.get( RADIUS ).v * 0.5f );

			entities.add( a0 );
			entities.add( a1 );
		}

		explosion( 20, a.get( POSITION ), Color.gray, 20.0f, 200.0f, 0.5f, 2.0f );

		a.expire();
	}

	/**
	 * Handles what occurs when something collides with an asteroid.
	 */
	private void handleShipDeath()
	{
		explosion( 300, ship.get( POSITION ), Color.white, 0.0f, 200.0f, 0.25f, 3.0f );

		for (Entity e : iterator.iterate( entities, Filtering.ASTEROIDS ))
		{
			explosion( 20, e.get( POSITION ), Color.gray, 20.0f, 200.0f, 0.5f, 2.0f );
		}

		setState( GameState.DEATH );
	}

	/**
	 * Handles what occurs when two entities that don't destroy each other
	 * collide. The radius of the each entity will be used as mass.
	 * 
	 * @param a
	 *        The first entity.
	 * @param b
	 *        The second entity.
	 */
	private void handleBouncyCollision( Entity a, Entity b )
	{
		Vector vel0 = a.get( VELOCITY );
		Vector vel1 = b.get( VELOCITY );
		Vector pos0 = a.get( POSITION );
		Vector pos1 = b.get( POSITION );
		float mass0 = a.get( RADIUS ).v;
		float mass1 = a.get( RADIUS ).v;
		float masssum = 2.0f / (mass0 + mass1);

		float speed0 = vel0.normalize();
		float speed1 = vel1.normalize();

		Vector toward = pos0.sub( pos1 ).normali();

		vel0.set( toward );
		vel0.muli( speed1 * mass0 * masssum );

		vel1.set( toward );
		vel1.muli( -speed0 * mass1 * masssum );
	}

	/**
	 * Creates an explosion of particles in every direction.
	 * 
	 * @see #explosionDirected(int, Vector, Color, float, float, float, float,
	 *      float, float)
	 * @param particleCount
	 *        The number of particles to explode.
	 * @param pos
	 *        The position to explode the particles from.
	 * @param color
	 *        The color of the particles.
	 * @param speedMin
	 *        The minimum possible speed of a particle in pixels-per-second.
	 * @param speedMax
	 *        The maximum possible speed of a particle in pixels-per-second.
	 * @param ageMin
	 *        The minimum possible lifetime of a particle in seconds.
	 * @param ageMax
	 *        The maximum possible lifetime of a particle in seconds.
	 */
	private void explosion( int particleCount, Vector pos, Color color, float speedMin, float speedMax, float ageMin, float ageMax )
	{
		explosionDirected( particleCount, pos, color, speedMin, speedMax, ageMin, ageMax, 0.0f, 6.283184f );
	}

	/**
	 * Creates an explosion of particles.
	 * 
	 * @param particleCount
	 *        The number of particles to explode.
	 * @param pos
	 *        The position to explode the particles from.
	 * @param color
	 *        The color of the particles.
	 * @param speedMin
	 *        The minimum possible speed of a particle in pixels-per-second.
	 * @param speedMax
	 *        The maximum possible speed of a particle in pixels-per-second.
	 * @param ageMin
	 *        The minimum possible lifetime of a particle in seconds.
	 * @param ageMax
	 *        The maximum possible lifetime of a particle in seconds.
	 * @param angleMin
	 *        The minimum angle which determines the direction of velocity.
	 * @param angleMax
	 *        The maximum angle which determines the direction of velocity.
	 */
	private void explosionDirected( int particleCount, Vector pos, Color color, float speedMin, float speedMax, float ageMin, float ageMax, float angleMin, float angleMax )
	{
		for (int i = 0; i < particleCount; i++)
		{
			Entity e = new Entity( Templates.PARTICLE );
			e.get( RADIUS ).v = 1.0f;
			e.get( POSITION ).set( pos );
			e.get( VELOCITY ).angle( rnd( angleMin, angleMax ), rnd( speedMin, speedMax ) );
			e.get( AGE ).maxAge = rnd( ageMin, ageMax );
			e.set( COLOR, color );
			particles.add( e );
		}
	}

	/**
	 * Returns a random number between the given minimum and maximum.
	 * 
	 * @param min
	 *        The minimum value that should be returned (included).
	 * @param max
	 *        The maximum value that should be returned (excluded).
	 * @return The random number [minimum, maximum).
	 */
	private float rnd( float min, float max )
	{
		return random.nextFloat() * (max - min) + min;
	}

	/**
	 * Sets the {@link GameState} of {@link Asteroids} and reset
	 * {@link #stateTime}.
	 * 
	 * @param state
	 *        The state of Asteroids.
	 */
	public void setState( GameState state )
	{
		this.state = state;
		this.stateTime = 0.0f;
	}

	/**
	 * Deletes asteroids by deleting all entities and particles.
	 */
	public void delete()
	{
		entities.delete();
		particles.delete();
	}

}

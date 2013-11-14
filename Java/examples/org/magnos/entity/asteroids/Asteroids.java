package org.magnos.entity.asteroids;

import static org.magnos.entity.asteroids.Components.*;

import java.awt.Color;
import java.util.Random;

import org.magnos.entity.Entity;
import org.magnos.entity.EntityIterator;
import org.magnos.entity.EntityList;
import org.magnos.entity.vals.FloatVal;

import com.gameprogblog.engine.Vector;

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
    }
    
    public void setSize(int w, int h)
    {
        spaceSize.set( w, h );
        spaceCenter.set( w * 0.5f, h * 0.5f );
    }
    
    public void reset()
    {
        entities = new EntityList();
        particles = new EntityList();

        ship = new Entity( Templates.SHIP );
        ship.set( COLOR, Color.white );
        ship.get( POSITION ).set( spaceCenter );
        ship.get( RADIUS ).v = 20.0f;
        shipForce = new Vector();
        shipLook = new Vector( ship.get( POSITION ) );
        
        entities.add( ship );
        setState( GameState.PLAYING );
    }
    
    public void spawn( int asteroidCount )
    {
        spawnCount = asteroidCount;
        
        while (--asteroidCount >= 0)
        {
            entities.add( newAsteroid() );
        }
    }
    
    public void update(float dt)
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
    
    private void wrapEntities()
    {
        for (Entity e : iterator.iterate( entities, Filtering.WRAPPABLE ))
        {
            Vector p = e.get( POSITION );
            float r = e.get( RADIUS ).v;
            
            if (p.x < -r)                   p.x += spaceSize.x + r * 2;
            if (p.x > spaceSize.x + r)      p.x -= spaceSize.x + r * 2;
            if (p.y < -r)                   p.y += spaceSize.y + r * 2;
            if (p.y > spaceSize.y + r)      p.y -= spaceSize.y + r * 2;
        }
    }
    
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
    
    public void draw(Object drawState)
    {
        entities.draw( drawState );
        particles.draw( drawState );
    }
    
    public Entity newAsteroid()
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
            e.get( POSITION ).set( rnd( r, spaceSize.x - r), rnd( r, spaceSize.y - r) );
            
            if (e.get( POSITION ).distanceSq( spaceCenter ) >= ASTEROID_SPAWN_SHIP_BUFFER_SQ) 
            {
                placed = true;
            }
        }
        
        return e;
    }
    
    public Entity newLaser()
    {
        Entity e = new Entity( Templates.LASER );
        e.get( POSITION ).set( ship.get( POSITION ) );
        e.get( VELOCITY ).angle( ship.get( ANGLE ).v, LASER_SPEED );
        e.get( RADIUS ).v = LASER_RADIUS;
        e.set( COLOR, Color.red );
        
        return e;
    }
    
    public void handleLaserDeath( Entity a )
    {
        explosion( 3, a.get( POSITION ), Color.red, 30.0f, 100.0f, 0.75f, 1.25f );
        
        a.expire();
    }
    
    public void handleAsteroidDeath( Entity a )
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
    
    public void handleShipDeath()
    {
        explosion( 300, ship.get( POSITION ), Color.white, 0.0f, 200.0f, 0.25f, 3.0f );
        
        for (Entity e : iterator.iterate( entities, Filtering.ASTEROIDS))
        {
            explosion( 20, e.get( POSITION ), Color.gray, 20.0f, 200.0f, 0.5f, 2.0f );
        }
        
        setState( GameState.DEATH );
    }
    
    private void explosion( int particleCount, Vector pos, Color color, float speedMin, float speedMax, float ageMax, float ageMin )
    {
        explosionDirected( particleCount, pos, color, speedMin, speedMax, ageMax, ageMin, 0.0f, 6.283184f );
    }
    
    private void explosionDirected( int particleCount, Vector pos, Color color, float speedMin, float speedMax, float ageMax, float ageMin, float angleMin, float angleMax )
    {
        for (int i = 0; i < particleCount; i++)
        {
            Entity e = new Entity( Templates.PARTICLE );
            e.get( RADIUS ).v = 1.0f;
            e.get( POSITION ).set( pos );
            e.get( VELOCITY ).angle( rnd( angleMin, angleMax ), rnd( speedMin, speedMax ) );
            e.get( AGE ).maxAge = rnd( ageMax, ageMin );
            e.set( COLOR, color );
            particles.add( e );
        }
    }
    
    public float rnd(float min, float max)
    {
        return random.nextFloat() * (max - min) + min;
    }
    
    public void setState( GameState state )
    {
        this.state = state;
        this.stateTime = 0.0f;
    }
    
}

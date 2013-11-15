Asteroids (with Ents)
==========

#### Play
* [Java2D](https://github.com/ClickerMonkey/Ents/raw/master/Java/build/asteroids-java2d.jar)
* [OpenGL through LWJGL](https://github.com/ClickerMonkey/Ents/raw/master/Java/build/asteroids-lwjgl.jar)

#### Screenshot
![Screenshot](https://raw.github.com/ClickerMonkey/Ents/master/Java/examples/asteroids/screenshot.png)

### Requirements
Asteroids is a pretty well known game, but let's reiterate how we want it to work:
* You control a ship with WASD and the mouse dictates it's orientation.
* Asteroids float around bouncing off of each other, potentially colliding with the ship.
* Clicking or SPACE bar will fire lasers from the ship, destroying asteroids.
* When an asteroid is hit with a laser, it splits into 2 smaller pieces. An asteroid can split 3 times.
* Level 1 has 5 asteroids, and each subsequent level adds another asteroid.
* The game ends and restarts when you die.
* P will pause the game.
* R will restart the game.
* B will toggle automatic breaking (aka friction).
* ESC will close the game.

### Organization
For every game I make with Ents, I like to create the following classes in the root package:
* [Components](Components.java) => contains all component definitions for the game
* [Controllers](Controllers.java) => contains all controller definitions for the game
* [Views](Views.java) => contains all view definitions for the game
* [Templates](Templates.java) => contains all template definitions for the game
* [Filtering](Filtering.java) => contains all EntityFilters for the game

#### Components
```java
Component<Vector> POSITION = Ents.newComponent( "position", new Vector() );
Component<Vector> VELOCITY = Ents.newComponent( "velocity", new Vector() );
Component<Vector> ACCELERATION = Ents.newComponent( "acceleration", new Vector() );
  
Component<FloatVal> ANGLE = Ents.newComponent( "angle", new FloatVal() );
Component<FloatVal> ANGULAR_VELOCITY = Ents.newComponent( "angular_velocity", new FloatVal() );
  
Component<FloatVal> RADIUS = Ents.newComponent( "radius", new FloatVal() );
Component<FloatVal> RADIUS_SHARED = Ents.newComponentSharedAlternative( RADIUS, new FloatVal() );
    
Component<Color> COLOR = Ents.newComponent( "color", new ColorValueFactory() );
Component<Color> COLOR_SHARED = Ents.newComponentSharedAlternative( COLOR, new ColorValueFactory() );
    
Component<IntVal> LIVES = Ents.newComponent( "lives", new IntVal() );

Component<Aged> AGE = Ents.newComponent( "age", new Aged() );
```
As you can see, 2 alternative components were created, `RADIUS_SHARED` and `COLOR_SHARED`. `RADIUS_SHARED` as created as an alternate component definition to `RADIUS` for lasers and the ship, since all lasers will have the same radius they don't need to all have their own unique value! `COLOR_SHARED` was created for lasers since they will all be red.

#### Views
```java
View ASTEROID = Ents.newView( "asteroid" );
View SHIP = Ents.newView( "ship" );
View LASER = Ents.newView( "laser" );
View PARTICLE = Ents.newView( "particle" );
View PARTICLE_SYSTEM = Ents.newView( "particle-system" );
```
The views are empty stubs. Depending on the graphics back-end, the appropriate renderers will be set.

#### Controllers
Lets take our time with the controllers:
```java
Controller PHYSICS = Ents.newController( "physics" );
```
The `PHYSICS` controller is simply a definition, so two alternative physics controllers can be defined (based on whether or not the Entity has acceleration):
```java
Controller PHYSICS_ACCEL = Ents.newControllerAlternative( PHYSICS, new Control() {
    public void update( Entity e, Object updateState ) {
        UpdateState state = (UpdateState)updateState;
        Vector pos = e.get( POSITION );
        Vector vel = e.get( VELOCITY );
        Vector acc = e.get( ACCELERATION );
        vel.addsi( acc, state.dt );
        pos.addsi( vel, state.dt );
    }
});

Controller PHYSICS_NOACCEL = Ents.newControllerAlternative( PHYSICS, new Control() {
    public void update( Entity e, Object updateState ) {
        UpdateState state = (UpdateState)updateState;
        Vector pos = e.get( POSITION );
        Vector vel = e.get( VELOCITY );
        pos.addsi( vel, state.dt );
    }
});
```
The next controller is for asteroids, which have an angle and angular velocity:
```java
Controller ROTATES = Ents.newController( "rotates", new Control() {
    public void update( Entity e, Object updateState ) {
        UpdateState state = (UpdateState)updateState;
        FloatVal ang = e.get( ANGLE );
        FloatVal vel = e.get( ANGULAR_VELOCITY );
        ang.v += vel.v * state.dt;
    }
});
```
The next controller is for particles and when friction/breaking is enabled on the Ship:
```java
Controller DRAG = Ents.newController( "drag", new Control() {
    public void update( Entity e, Object updateState ) {
        UpdateState state = (UpdateState)updateState;
        Vector vel = e.get( VELOCITY );
        Vector acc = e.gets( ACCELERATION );
        if (acc == null || acc.isEqual( Vector.ZERO, 0.0001f )) {
            vel.addsi( vel, -state.dt );
        }
    }
} );
```
And finally, the controller which keeps track of the age of particles, and expires them when they die:
```java
Controller AGES = Ents.newController( "aged", new Control() {
    public void update( Entity e, Object updateState ) {
        UpdateState state = (UpdateState)updateState;
        Aged a = e.get( AGE );
        a.age += state.dt;
        if (a.age > a.maxAge) {
            e.expire();
        }
    }
});
```

Wooh! Well those look useful! I could just slap `ANGLE`, `ANGULAR_VELOCITY` and `ROTATES` on an Entity and it would be set to rotate! Or I could add `AGE` and `AGES` to an entity and after so many seconds it will die.

#### Templates
Now the part where we pull all of it together to define the types of entities in Asteroids:
```java
Template ASTEROID = Ents.newTemplate( "asteroid",
    new ComponentSet( POSITION, VELOCITY, RADIUS, ANGLE, ANGULAR_VELOCITY, COLOR, LIVES ),
    new ControllerSet( PHYSICS_NOACCEL, ROTATES ),
    Views.ASTEROID 
);

Template SHIP = Ents.newTemplate( "ship",
    new ComponentSet( POSITION, VELOCITY, ACCELERATION, ANGLE, RADIUS_SHARED, COLOR ),
    new ControllerSet( PHYSICS_ACCEL, DRAG ),
    Views.SHIP 
);

Template LASER = Ents.newTemplate( "laser",
    new ComponentSet( POSITION, VELOCITY, RADIUS_SHARED, COLOR_SHARED ),
    new ControllerSet( PHYSICS_NOACCEL ),
    Views.LASER
);

Template PARTICLE = Ents.newTemplate( "particle",
    new ComponentSet( POSITION, VELOCITY, RADIUS_SHARED, COLOR, AGE ),
    new ControllerSet( PHYSICS_NOACCEL, AGES, DRAG ),
    Views.PARTICLE
);

Template PARTICLE_SYSTEM = Ents.newTemplate( "particle-system",
    new ComponentSet(),
    new ControllerSet(),
    Views.PARTICLE_SYSTEM
);
```
Go through those carefully, so you know exactly what everything has and how it will behave.

#### Filtering
And here are some simple filters used in the game logic:
```java
EntityFilter ASTEROIDS = template( Templates.ASTEROID );
EntityFilter LASERS = template( Templates.LASER );
EntityFilter WRAPPABLE = and( components( Components.POSITION, Components.RADIUS ), not( expired() ) );
EntityFilter COLLIDABLE = and( components( Components.POSITION, Components.RADIUS ), not( expired() ) );
```
You probably are wondering why `WRAPPABLE` and `COLLIDABLE` are exactly the same... well I like to keep the code as readable as possible so everytime I find myself iterating through entities I create a new filter where the name nicely describes what type of entities I want.

#### Collision Handling
Collision Handling will be done in a very simple matter. Since Templates have an identifier when they are created (starting at 0 and increasing) we can use this to our advantage. We can build a matrix of CollisionHandlers so determining how to handle a collision is as easy plugging in the IDs of the templates of the colliding entities.
The CollisionHandler interface:
```java
public interface CollisionHandler {
   public void handle(Entity subject, Entity object);
}
```

Simple enough right? And the CollisionHandlers are added and accessed through the `Collisions` class which has these methods defined:
```java
public class Collisions {
   public static void register( Template subject, Template object, CollisionHandler handler );
   public static CollisionHandler getHandler( Template subject, Template object );
}
```

### Game Logic
Now onto the actual game!

First, we need to register our CollisionHandlers with the `Collisions` class:
```java
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
```
As you can see when an asteroid and laser collide, they both die. Same goes for an asteroid and a ship. However when we have two asteroids collide, we simply have them bounce off of each other.

#### Update

Lets define what we need to do in here:
* update the state of the game depending on whether the user is PLAYING or has DIED
* update ships angle and acceleration based on user input
* update asteroids, lasers, and the ship
* update particles
* remove all lasers outside of the view
* wrap all entities around the screen if they exit it
* check for collisions, and handle them

And that's all there is to it.

If you want to see how all of it is done, checkout the [Asteroids](Asteroids.java) code. Here's some of the code so you can see how entities can be iterated over:

```java
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
```
And also the wrapping:
```java
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
```

`TODO:` Talk about Java2D and OpenGL back-end implementations.

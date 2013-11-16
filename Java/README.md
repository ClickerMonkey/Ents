Ents for Java
==========

![Stable](http://i4.photobucket.com/albums/y123/Freaklotr4/stage_stable.png)

### Build

* [Ents (latest)](https://github.com/ClickerMonkey/Ents/raw/master/Java/build/Ents-1.1.jar)
* [Ents (latest) with source](https://github.com/ClickerMonkey/Ents/raw/master/Java/build/Ents-1.1-src.jar)

### Complete Examples (+walkthroughs)

* [Asteroids](https://github.com/ClickerMonkey/Ents/tree/master/Java/examples/asteroids) (has Java2D [(jar)](https://github.com/ClickerMonkey/Ents/raw/master/Java/build/asteroids-java2d.jar) and OpenGL [(jar)](https://github.com/ClickerMonkey/Ents/raw/master/Java/build/asteroids-lwjgl.jar) back-ends available)

### Simple Example

```java
Component<Vector> POSITION = Ents.newComponent( "position", new Vector() );
Component<Vector> VELOCITY = Ents.newComponent( "velocity", new Vector() );

Controller PHYSICS = Ents.newController( "physics", new Control() {
  public void update( Entity e, Object updateState ) {
      float dt = (Float)updateState;
      Vector pos = e.get( POSITION );
      Vector vel = e.get( VELOCITY );
      pos.add( vel, dt );
  }
});

Template SPRITE = Ents.newTemplate( "sprite", new ComponentSet( POSITION, VELOCITY ), new ControllerSet( PHYSICS ) );

Entity e = new Entity( SPRITE );
e.get( POSITION ).set( 1.0f, 3.0f );
e.get( VELOCITY ).set( 0.0f, -6.0f );
e.update( 0.5f );

...

Component<Vector> ACCELERATION = Ents.newComponent( "acceleration", new Vector() );

Controller PHYSICS_ACCELERATION = Ents.newControllerAlternative( PHYSICS, new Control() {
  public void update( Entity e, Object updateState ) {
      float dt = (Float)updateState;
      Vector pos = e.get( POSITION );
      Vector vel = e.get( VELOCITY );
      Vector acc = e.get( ACCELERATION );
      vel.add( acc, dt );
      pos.add( vel, dt );
  }
});

...

e.put( ACCELERATION, new Vector (0.0f, -10.0f ) );
e.add( PHYSICS_ACCELERATION ); // overwrite existing PHYSICS controller with this alternative

assert e.has( PHYSICS );

e.update( 0.5f );

```

#### Entity-Component Methods
```java
e.has( POSITION ); // returns true or false depending on whether the Entity has that component
e.get( POSITION ); // gets the value of the component. this assumes the Entity has the component, this is the fastest method for retrieving a component value
e.gets( POSITION ); // gets the value of the component safely, returning null of the Entity doesn't have the component
e.gets( POSITION, ZERO ); // gets the value of the component safely, returning ZERO (or whatever is passed in) if the component is missing
e.set( POSITION, pos ); // sets the value of the component. this assumes the Entity has the component, this is the fastest method for setting a component value
e.sets( POSITION, pos ); // sets the value of the component safely, if the Entity doesn't have the component then this will have no affect and false will be returned.
e.take( POSITION, out ); // sets out to the value of the component in this Entity, this assumes the Entity has the component
e.takes( POSITION, out ); // safely takes the component value and sets it to out and returns out, or returns null of the component doesn't exist
e.add( POSITION ); // adds the given component to the Entity if it doesn't have it already
e.add( POSITION, defaultValue ); // adds the given component to the Entity and then calls set directly afterward
e.put( POSITION, forcedValue ); // sets the component value, adding the component if it doesn't exist already
e.grab( POSITION ); // grabs the component value, adding the component if it doesn't exist
```

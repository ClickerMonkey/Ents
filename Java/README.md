Ents for Java
==========

![Stable](http://i4.photobucket.com/albums/y123/Freaklotr4/stage_stable.png)

### Complete Examples (+walkthroughs)

* [Asteroids](https://github.com/ClickerMonkey/Ents/tree/master/Java/examples/asteroids) (has Java2D and OpenGL back-ends available)

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

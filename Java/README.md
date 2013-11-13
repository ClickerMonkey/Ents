Ents for Java
==========

![Alpha](http://i4.photobucket.com/albums/y123/Freaklotr4/stage_alpha.png)

### Simple Example

```java
Component<Vector> POSITION     = Ents.newComponent( "position", new Vector() );
Component<Vector> VELOCITY     = Ents.newComponent( "velocity", new Vector() );

Controller        PHYSICS      = Ents.newController( "physics", new Control() {
  public void update( Entity e, Object updateState ) {
      float dt = (Float)updateState;
      Vector pos = e.get( POSITION );
      Vector vel = e.get( VELOCITY );
      pos.add( vel, dt );
  }
});

Template          SPRITE       = Ents.newTemplate( "sprite", 
   new ComponentSet( POSITION, VELOCITY ), 
   new ControllerSet( PHYSICS ) );


Entity e = new Entity( SPRITE );
e.get( POSITION ).set( 1.0f, 3.0f );
e.get( VELOCITY ).set( 0.0f, -6.0f );
e.update( 0.5f );

```

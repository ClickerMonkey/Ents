package org.magnos.entity;

public interface Renderer
{
   public Renderer create( Entity e );
   public void drawStart( Entity e, Object drawState );
   public void draw( Entity e, Object drawState );
   public void drawEnd( Entity e, Object drawState );
   public void destroy( Entity e );
}
package org.magnos.entity.asteroids;

import org.magnos.entity.Entity;


public interface CollisionHandler
{
    public void handle(Entity subject, Entity object);
}

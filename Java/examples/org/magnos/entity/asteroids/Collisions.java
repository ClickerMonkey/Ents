package org.magnos.entity.asteroids;

import org.magnos.entity.Entity;
import org.magnos.entity.Template;


public class Collisions
{
    
    public static CollisionHandler[][] handlers = new CollisionHandler[ Templates.TEMPLATE_COUNT ][ Templates.TEMPLATE_COUNT ];
    
    public static void register(Template subject, Template object, CollisionHandler handler)
    {
        handlers[ subject.id ][ object.id ] = handler;
        handlers[ object.id ][ subject.id ] = reverse( handler );
    }
    
    public static CollisionHandler getHandler(Template subject, Template object)
    {
        return handlers[ subject.id ][ object.id ];
    }
    
    private static CollisionHandler reverse(final CollisionHandler handler)
    {
        return new CollisionHandler()
        {
            public void handle( Entity subject, Entity object )
            {
                handler.handle( object, subject );
            }
        };
    }
    
}

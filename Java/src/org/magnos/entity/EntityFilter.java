package org.magnos.entity;

/**
 * 
 * @author Philip Diffenderfer
 *
 */
public interface EntityFilter
{

   /**
    * Determines whether the given Entity is valid.
    * 
    * @param e
    *        The entity to validate.
    * @return True if the entity belongs in this filter, otherwise false.
    */
   public boolean isValid(Entity e);
   
}

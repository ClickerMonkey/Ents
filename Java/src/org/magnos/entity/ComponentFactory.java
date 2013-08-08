
package org.magnos.entity;

/**
 * A factory to create custom Component types.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <T>
 *        The component value type.
 */
public interface ComponentFactory<T>
{

   /**
    * Creates a new Component given the id and name of the component.
    * 
    * @param id
    *        The id of the component.
    * @param name
    *        The name of the component.
    * @return The reference to a newly created component.
    */
   public Component<T> create( int id, String name );

}

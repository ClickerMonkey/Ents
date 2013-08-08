
package org.magnos.entity;

import java.util.Arrays;



/**
 * 
 * @author pdiffenderfer
 * 
 * @param <T>
 */
@SuppressWarnings ("unchecked" )
public class ComponentById<T> extends Component<T>
{

   public static final int DEFAULT_VALUES_SIZE = 16;

   /**
    * 
    */
   private final ComponentByIdHandler handler;

   /**
    * 
    */
   private final ComponentValueFactory<T> factory;

   /**
    * 
    */
   private T[] values;

   /**
    * 
    */
   private Entity[] entity;

   /**
    * 
    * @param id
    * @param name
    * @param factory
    */
   protected ComponentById( int id, String name, ComponentValueFactory<T> factory )
   {
      super( id, name );

      this.factory = factory;
      this.handler = new ComponentByIdHandler();
      this.values = (T[])new Object[DEFAULT_VALUES_SIZE];
      this.entity = new Entity[DEFAULT_VALUES_SIZE];
   }

   @Override
   protected TemplateComponent<T> add( Template template )
   {
      return handler;
   }

   @Override
   protected void postCustomAdd( Entity e, Template template, TemplateComponent<?> templateComponent )
   {

   }

   /**
    * 
    * @author pdiffenderfer
    *
    */
   private class ComponentByIdHandler implements TemplateComponent<T>
   {

      @Override
      public void set( Entity e, T value )
      {
         values[e.id] = value;
      }

      @Override
      public T get( Entity e )
      {
         return values[e.id];
      }

      @Override
      public T take( Entity e, T target )
      {
         return factory.copy( values[e.id], target );
      }

      @Override
      public void remove( Template template )
      {

      }

      @Override
      public void postAdd( Entity e )
      {
         final int valueIndex = e.id;
         final int valueCount = values.length;

         if (valueIndex >= valueCount)
         {
            values = Arrays.copyOf( values, valueIndex + 1 );
            entity = Arrays.copyOf( entity, valueIndex + 1 );
         }

         if (values[valueIndex] == null)
         {
            values[valueIndex] = factory.create();
         }
      }

      @Override
      public void preRemove( Entity e )
      {
      }

   }

}

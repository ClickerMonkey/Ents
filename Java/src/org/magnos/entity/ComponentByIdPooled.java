
package org.magnos.entity;

import java.util.Arrays;

import org.magnos.entity.util.BitSet;


/**
 * 
 * @author pdiffenderfer
 * 
 * @param <T>
 */
@SuppressWarnings ("unchecked" )
public class ComponentByIdPooled<T> extends Component<T>
{

   public static final int DEFAULT_POOL_SIZE = 16;
   public static final int DEFAULT_VALUES_SIZE = 16;

   /**
    * 
    */
   private final ComponentByIdHandler handler;

   /**
    * 
    */
   private final ComponentValuePool<T> pool;

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
    */
   private BitSet recycled = new BitSet();

   protected ComponentByIdPooled( int id, String name, ComponentValueFactory<T> factory )
   {
      super( id, name );

      this.pool = new ComponentValuePool<T>( factory, DEFAULT_POOL_SIZE );
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

   private class ComponentByIdHandler implements TemplateComponent<T>
   {

      @Override
      public void set( Entity e, T value )
      {
         T existingValue = values[e.id];

         if (existingValue != null && existingValue != value && !recycled.get( e.id ))
         {
            pool.push( existingValue );
            recycled.set( e.id, true );
         }

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
         return pool.copy( values[e.id], target );
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

         values[valueIndex] = pool.create();
         recycled.set( valueIndex, false );
      }

      @Override
      public void preRemove( Entity e )
      {
         final int valueIndex = e.id;

         if (!recycled.get( valueIndex ))
         {
            pool.push( values[valueIndex] );
         }

         values[valueIndex] = null;
      }

   }

}


package org.magnos.entity;

public interface EntityListener
{

   public void onEntityAdd( Entity e );

   public void onEntityRemove( Entity e );
   
   public void onCoreClear();
   
   public void onViewAdd( View view, boolean definition );
   
   public void onControllerAdd( Controller controller, boolean definition );
   
   public void onComponentAdd( Component<?> component, boolean definition );
   
   public void onTemplateAdd( Template template );
   
}

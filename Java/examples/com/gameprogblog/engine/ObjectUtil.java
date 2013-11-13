
package com.gameprogblog.engine;

public class ObjectUtil
{

   public static boolean hasValue( Object a )
   {
      return (a != null);
   }

   public static boolean hasValue( String x )
   {
      return (x != null && x.trim().length() > 0);
   }

   public static boolean equals( Object a, Object b )
   {
      return (a == b) || (a != null && b != null && a.equals( b ));
   }

   public static <T extends Comparable<T>> boolean equals( T a, T b )
   {
      return a.compareTo( b ) == 0;
   }

   public static boolean isEquatable( Object subject, Object object )
   {
      return (subject == object) || ((object != null) && subject.getClass() == object.getClass());
   }

   public static int hashCode( Object... values )
   {
      final int prime = 31;
      int result = 1;

      for (Object o : values)
      {
         result = prime * result + ((o == null) ? 0 : o.hashCode());
      }

      return result;
   }

}

package org.magnos.entity.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.magnos.entity.util.IndexPool;


public class TestIndexPool
{

   @Test
   public void testPop()
   {
      IndexPool ip = new IndexPool( 16 );

      assertEquals( 0, ip.size() );
      
      assertEquals( 0, ip.pop() );
      assertEquals( 1, ip.pop() );
      assertEquals( 2, ip.pop() );
   }
   
   @Test
   public void testPushBackwards()
   {
      IndexPool ip = new IndexPool( 16 );
      
      int i0 = ip.pop();
      int i1 = ip.pop();
      int i2 = ip.pop();
      
      assertEquals( 0, ip.size() );
      
      ip.push( i2 );
      
      assertEquals( 0, ip.size() );
      
      ip.push( i1 );
      
      assertEquals( 0, ip.size() );
      
      ip.push( i0 );
      
      assertEquals( 0, ip.size() );
   }
   
   @Test
   public void testPopAfterPushBackwards()
   {
      IndexPool ip = new IndexPool( 16 );
      
      int i0 = ip.pop();
      int i1 = ip.pop();
      int i2 = ip.pop();
      ip.push( i2 );
      ip.push( i1 );
      ip.push( i0 );
      
      assertEquals( 0, ip.pop() );
   }
   
   @Test
   public void testPushForwards()
   {
      IndexPool ip = new IndexPool( 16 );
      
      int i0 = ip.pop();
      int i1 = ip.pop();
      int i2 = ip.pop();
      
      assertEquals( 0, ip.size() );
      
      ip.push( i0 );
      
      assertEquals( 1, ip.size() );
      
      ip.push( i1 );
      
      assertEquals( 2, ip.size() );
      
      ip.push( i2 );
      
      assertEquals( 0, ip.size() );
   }
   
   @Test
   public void testPopAfterPushForwards()
   {
      IndexPool ip = new IndexPool( 16 );
      
      int i0 = ip.pop();
      int i1 = ip.pop();
      int i2 = ip.pop();
      ip.push( i0 );
      ip.push( i1 );
      ip.push( i2 );
      
      assertEquals( 0, ip.pop() );
   }
   
   @Test
   public void testPartialPopAfterPushForwards()
   {
      IndexPool ip = new IndexPool( 16 );
      
      ip.pop();
      ip.pop();
      ip.pop();
      int i3 = ip.pop();
      int i4 = ip.pop();
      int i5 = ip.pop();
      ip.push( i3 );
      ip.push( i4 );
      ip.push( i5 );
      
      assertEquals( i3, ip.pop() );
      assertEquals( i4, ip.pop() );
      assertEquals( i5, ip.pop() );
   }
   
}

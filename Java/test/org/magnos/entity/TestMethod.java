package org.magnos.entity;

import static org.junit.Assert.*;

import org.junit.Test;
import org.magnos.entity.helper.Scalar;

public class TestMethod
{

	public static final EntityCore core = new EntityCore();

	public static final Component<Scalar> LEFT = core.newComponent( "left", new Scalar() );
	public static final Component<Scalar> RIGHT = core.newComponent( "right", new Scalar() );
	
	public static final Method<Void> SHRINK = core.newMethod( "shrink", new BitSet(LEFT, RIGHT), MethodReflection.staticMethod( Void.class, TestMethod.class, "shrink", float.class ) );
	
	public static final EntityType EXTENT = core.newEntityType( new IdMap(LEFT, RIGHT), new IdMap(), new IdMap(SHRINK), View.NONE );
	
	public static void shrink( Entity e, float amount )
	{
		float l = e.get(LEFT).x;
		float r = e.get(RIGHT).x;
		float center = (l + r) * 0.5f;
		float gap = (r - l) * 0.5f;
		
		e.get(LEFT).x = center - gap * amount;
		e.get(RIGHT).x = center + gap * amount;
	}
	
	@Test
	public void testMethod()
	{
		Entity e = new Entity( EXTENT );
		
		e.get(LEFT).x = 4.0f;
		e.get(RIGHT).x = 10.0f;
		
		e.execute( SHRINK, 0.5f );
		
		assertEquals( 5.5f, e.get(LEFT).x, 0.00001f );
		assertEquals( 8.5f, e.get(RIGHT).x, 0.00001f );
	}
	
}

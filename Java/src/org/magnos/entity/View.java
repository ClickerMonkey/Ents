package org.magnos.entity;

public interface View
{
	public static final int NONE = -1;
	
	public void draw( Entity entity, Object drawState );
}

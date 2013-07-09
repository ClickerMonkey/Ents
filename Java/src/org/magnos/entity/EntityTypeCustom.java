package org.magnos.entity;

public class EntityTypeCustom extends EntityType
{

	protected EntityTypeCustom( int id, EntityType parent, IdMap components, IdMap behaviors, int view )
	{
		super( id, parent, components, behaviors, view );
	}

	public boolean isCustom()
	{
		return true;
	}

	public EntityType addCustomComponent( int id )
	{
		add( id );

		return this;
	}

	public EntityType addCustomController( int behaviorId )
	{
		addController( behaviorId );

		return this;
	}

	public EntityType setCustomView( int viewId )
	{
		setView( viewId );

		return this;
	}

}

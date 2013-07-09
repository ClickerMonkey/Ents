package org.magnos.entity;

import java.util.Arrays;

public final class IdMap
{

	public int[] ids;

	public int[] map;

	public IdMap( int... ids )
	{
		this( ids, buildMap( ids ) );
	}

	private IdMap( int[] ids, int[] map )
	{
		this.ids = ids;
		this.map = map;
	}

	public boolean has( int id )
	{
		return ( id < map.length && map[id] >= 0 );
	}

	public boolean hasAll( int... ids )
	{
		for (int i = 0; i < ids.length; i++)
		{
			if ( !has( ids[i] ) )
			{
				return false;
			}
		}

		return true;
	}

	public void alias( int id, int aliasId )
	{
		if ( has( id ) )
		{
			map( aliasId, map[id] );
		}
	}

	public void add( int id )
	{
		int index = ids.length;

		ids = Arrays.copyOf( ids, index + 1 );
		ids[index] = id;

		map( id, index );
	}

	private void map( int id, int index )
	{
		if ( id >= map.length )
		{
			map = Arrays.copyOf( map, id + 1 );
		}

		map[id] = index;
	}

	public int getIndex( int id )
	{
		return map[id];
	}

	public int getIndexSafe( int id )
	{
		return ( id < map.length && id >= 0 ? map[id] : -1 );
	}

	public int size()
	{
		return ids.length;
	}

	public IdMap getCopy()
	{
		return new IdMap( Arrays.copyOf( ids, ids.length ), Arrays.copyOf( map, map.length ) );
	}

	public static int[] buildMap( int[] ids )
	{
		int max = 0;

		for (int i = 0; i < ids.length; i++)
		{
			max = Math.max( max, ids[i] );
		}

		int[] map = new int[max + 1];

		Arrays.fill( map, -1 );

		for (int i = 0; i < ids.length; i++)
		{
			map[ids[i]] = i;
		}

		return map;
	}

}

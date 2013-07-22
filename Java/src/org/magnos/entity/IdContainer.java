/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package org.magnos.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IdContainer<I extends Id>
{

	protected final List<I> definitions;
	protected final Map<String, I> names;
	protected final List<List<I>> instances;

	protected IdContainer( int initialCapacity )
	{
		definitions = new ArrayList<I>( initialCapacity );
		names = new HashMap<String, I>( initialCapacity );
		instances = new ArrayList<List<I>>( initialCapacity );
	}

	protected <D extends I> D addDefinition( D element )
	{
		if ( names.containsKey( element.name ) )
		{
			throw new RuntimeException( String.format( "Definition of %s with name '%s' and ID %d cannot be added, it has the same name as another %s", element.getClass().getSimpleName(), element.name, element.id, element.getClass().getSimpleName() ) );
		}

		if ( definitions.size() != element.id )
		{
			throw new RuntimeException( String.format( "Definition of %s with name '%s' and ID %d cannot be added, the ID of the element must be retrieved by calling nextId() on this container. The alternative is you meant to add an instance and not a definition.", element.getClass().getSimpleName(), element.name, element.id ) );
		}

		definitions.add( element );
		names.put( element.name, element );
		instances.add( new ArrayList<I>() );
		instances.get( element.id ).add( element );

		return element;
	}

	protected <D extends I> D addInstance( D element )
	{
		if ( element.id >= instances.size() )
		{
			throw new RuntimeException( String.format( "Instance of %s with name '%s' and ID %d could not be added, a definition with a matching ID has not been added.", element.getClass().getSimpleName(), element.name, element.id ) );
		}

		instances.get( element.id ).add( element );

		return element;
	}

	public int nextId()
	{
		return definitions.size();
	}

	public int size()
	{
		return definitions.size();
	}

	public List<I> getDefinitions()
	{
		return definitions;
	}

	public List<List<I>> getInstances()
	{
		return instances;
	}

	public Map<String, I> getNameMap()
	{
		return names;
	}
	
	public Set<String> getNames()
	{
		return names.keySet();
	}

	protected void clear()
	{
		definitions.clear();
		names.clear();
		instances.clear();
	}

	public I getDefinition( String name )
	{
		return names.get( name );
	}

	public I getDefinition( int id )
	{
		return ( id >= 0 && id < definitions.size() ? definitions.get( id ) : null );
	}

	public I getDefinition( I id )
	{
		return definitions.get( id.id );
	}

	public List<I> getInstances( String name )
	{
		return names.containsKey( name ) ? instances.get( names.get( name ).id ) : null;
	}

	public List<I> getInstances( int id )
	{
		return ( id >= 0 && id < definitions.size() ? instances.get( id ) : null );
	}

	public List<I> getInstances( I id )
	{
		return instances.get( id.id );
	}

}

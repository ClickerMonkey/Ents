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

/**
 * A container that has {@link Id}s of a specific type. The container is special 
 * because Id's can be added first as a definition and then later alternative
 * instances can be added.
 * 
 * @author Philip Diffenderfer
 *
 * @param <I>
 */
public class IdContainer<I extends Id>
{

   protected final List<I> definitions;
   protected final Map<String, I> names;
   protected final List<List<I>> instances;

   /**
    * Instantiates a new IdContainer with the given initial capacity.
    * 
    * @param initialCapacity
    *        The initial expected number of {@link Id}s for this container.
    */
   protected IdContainer( int initialCapacity )
   {
      definitions = new ArrayList<I>( initialCapacity );
      names = new HashMap<String, I>( initialCapacity );
      instances = new ArrayList<List<I>>( initialCapacity );
   }

   /**
    * Adds a definition to this container. The definition's ID must be the last
    * return value of {@link #nextId()} and there cannot be a definition in this
    * container with the same name. The definition given is also added as the
    * first instance of the definition.
    * 
    * @param element
    *       The definition to add.
    * @return The reference to the definition added.
    */
   protected <D extends I> D addDefinition( D element )
   {
      if (names.containsKey( element.name ))
      {
         throw new RuntimeException( String.format( "Definition of %s with name '%s' and ID %d cannot be added, it has the same name as another %s", element.getClass().getSimpleName(), element.name, element.id, element.getClass().getSimpleName() ) );
      }

      if (definitions.size() != element.id)
      {
         throw new RuntimeException( String.format( "Definition of %s with name '%s' and ID %d cannot be added, the ID of the element must be retrieved by calling nextId() on this container. The alternative is you meant to add an instance and not a definition.", element.getClass().getSimpleName(), element.name, element.id ) );
      }

      definitions.add( element );
      names.put( element.name, element );
      instances.add( new ArrayList<I>() );
      instances.get( element.id ).add( element );

      return element;
   }

   /**
    * Adds an instance to this container. The definition of the instance must
    * already be added (a definition and an instance have the same 
    * {@link Id#id}).
    * 
    * @param element
    *       The instance to add.
    * @return The reference to the instance added.
    */
   protected <D extends I> D addInstance( D element )
   {
      if (element.id >= instances.size())
      {
         throw new RuntimeException( String.format( "Instance of %s with name '%s' and ID %d could not be added, a definition with a matching ID has not been added.", element.getClass().getSimpleName(), element.name, element.id ) );
      }

      instances.get( element.id ).add( element );

      return element;
   }

   /**
    * The id of the next definition if one were to be added through
    * {@link #addDefinition(Id)}.
    * 
    * @return The next id.
    */
   public int nextId()
   {
      return definitions.size();
   }

   /**
    * @return The number of definitions in this container.
    */
   public int size()
   {
      return definitions.size();
   }

   /**
    * @return The reference to the list of definitions in this container. This 
    *         should not be modified.
    */
   public List<I> getDefinitions()
   {
      return definitions;
   }

   /**
    * @return The list of instances in this container. The i'th element in the 
    *         list returned is the set if instances for the definition with the
    *         Id of i. This should not be modified.
    */
   public List<List<I>> getInstances()
   {
      return instances;
   }

   /**
    * @return The reference to definitions mapped by their name. This should 
    *         not be modified.
    */
   public Map<String, I> getNameMap()
   {
      return names;
   }

   /**
    * @return The names of all definitions in this container. This should not 
    *         be modified.
    */
   public Set<String> getNames()
   {
      return names.keySet();
   }

   /**
    * Removes all definitions and instances from this container.
    */
   protected void clear()
   {
      definitions.clear();
      names.clear();
      instances.clear();
   }

   /**
    * Finds the definition with the given name.
    * 
    * @param name
    *       The name of the definition to return.
    * @return The definition found, or null if no definition existed.
    */
   public I getDefinition( String name )
   {
      return names.get( name );
   }

   /**
    * Finds the definition with the given id.
    * 
    * @param id
    *       The id of the definition to return.
    * @return The definition found, or null if no definition existed.
    */
   public I getDefinition( int id )
   {
      return (id >= 0 && id < definitions.size() ? definitions.get( id ) : null);
   }

   /**
    * Finds the definition which has the same Id of the given object.
    * 
    * @param id
    *       The id to find the definition of.
    * @return The definition found, or null if no definition existed.
    */
   public I getDefinition( I id )
   {
      return (id.id >= 0 && id.id < definitions.size() ? definitions.get( id.id ) : null);
   }

   /**
    * Returns the instances for the definition with the given name. The list of
    * instances also contains the original definition added (first element). 
    * The returned list should not be modified.
    * 
    * @param name
    *       The name of the definition.
    * @return The reference to the instances or null if a definition with the
    *         given name exists.
    */
   public List<I> getInstances( String name )
   {
      return names.containsKey( name ) ? instances.get( names.get( name ).id ) : null;
   }

   /**
    * Returns the instances for the definition with the given id. The list of
    * instances also contains the original definition added (first element). 
    * The returned list should not be modified.
    * 
    * @param id
    *       The id of the definition.
    * @return The reference to the instances or null if a definition with the
    *         given name exists.
    */
   public List<I> getInstances( int id )
   {
      return (id >= 0 && id < definitions.size() ? instances.get( id ) : null);
   }

   /**
    * Returns the instances for the definition with the given id. The list of
    * instances also contains the original definition added (first element). 
    * The returned list should not be modified.
    * 
    * @param id
    *       The id of the definition.
    * @return The reference to the instances or null if a definition with the
    *         given name exists.
    */
   public List<I> getInstances( I id )
   {
      return instances.get( id.id );
   }

}

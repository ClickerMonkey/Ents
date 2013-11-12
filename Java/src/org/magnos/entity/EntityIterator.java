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

import java.util.Arrays;
import java.util.Iterator;


/**
 * An EntityIterator is an {@link Iterable} object that iterates over all nested
 * entities from a root entity. A nested entity is an entity which exists in
 * another.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class EntityIterator implements Iterator<Entity>, Iterable<Entity>
{

    private static final int DEFAULT_MAX_DEPTH = 16;

    private Entity root;
    private Entity[] stack;
    private Entity curr;
    private Entity prev;
    private int[] offset;
    private int depth;
    private EntityFilter filter;

    /**
     * Instantiates a new EntityIterator without a filter and root Entity.
     */
    public EntityIterator()
    {
        this( DEFAULT_MAX_DEPTH );
    }

    /**
     * Instantiates a new EntityIterator with the given filter.
     * 
     * @param filter
     *        The filter of the iterator.
     * @see #filter(EntityFilter)
     */
    public EntityIterator( EntityFilter filter )
    {
        this( DEFAULT_MAX_DEPTH );

        filter( filter );
    }

    /**
     * Instantiates a new EntityIterator with a root Entity and filter.
     * The EntityIterator is now ready to be iterated.
     * 
     * @param root
     *        The root entity to iterate.
     * @param filter
     *        The filter of the entity.
     * @see #iterate(Entity, EntityFilter)
     */
    public EntityIterator( Entity root, EntityFilter filter )
    {
        this( DEFAULT_MAX_DEPTH );

        iterate( root, filter );
    }

    /**
     * Instantiates a new EntityIterator.
     * 
     * @param defaultMaxDepth
     *        An EntityIterator works by popping Entities on a stack that have
     *        sub-entities and iterating through them. The defaultMaxDepth
     *        indicates the initial capacity of that stack before the stack
     *        needs to resize.
     */
    public EntityIterator( int defaultMaxDepth )
    {
        this.stack = new Entity[defaultMaxDepth];
        this.offset = new int[defaultMaxDepth];
    }

    /**
     * Stops the iterator, making the next call to {@link #hasNext()} return
     * false.
     */
    public void stop()
    {
        depth = -1;
    }

    /**
     * Resets this iterator by stopping it and setting the root entity to the
     * one given.
     * 
     * @param root
     *        The root entity to iterator through.
     * @return The reference to this iterator.
     */
    public EntityIterator iterate( Entity root )
    {
        this.root = root;
        this.reset();

        return this;
    }

    /**
     * Resets this iterator by stopping it and setting the root entity to the
     * one given as well as overwriting the filter to use.
     * 
     * @param root
     *        The root entity to iterator through.
     * @param filter
     *        The new filter for this iterator.
     * @return The reference to this iterator.
     */
    public EntityIterator iterate( Entity root, EntityFilter filter )
    {
        this.root = root;
        this.filter = filter;
        this.reset();

        return this;
    }

    /**
     * Sets the filter to be used which determines which entities are valid to
     * iterate over. This is typically done before iteration through this method
     * or through {@link #iterate(Entity, EntityFilter)} but can also be done
     * mid iteration to change which entities are considered valid.
     * 
     * @param filter
     *        The new filter for this iterator.
     * @return The reference to this iterator.
     */
    public EntityIterator filter( EntityFilter filter )
    {
        this.filter = filter;

        return this;
    }

    /**
     * Resets this filter to the beginning. If this EntityIterator does not
     * have a filter, a {@link NullPointerException} is thrown.
     * 
     * @return The reference to this iterator.
     */
    public EntityIterator reset()
    {
        if (filter == null)
        {
            throw new NullPointerException( "A filter is required to iterate." );
        }

        depth = 0;
        offset[0] = -1;
        stack[0] = root;

        prev = null;
        curr = findNext();

        return this;
    }

    /**
     * Returns the reference to this iterator, used when in for-each loops.
     */
    public Iterator<Entity> iterator()
    {
        return this;
    }

    @Override
    public boolean hasNext()
    {
        return (depth != -1);
    }

    @Override
    public Entity next()
    {
        prev = curr;
        curr = findNext();
        return prev;
    }

    /**
     * Removes the last entity from the iterator by expiring it.
     */
    @Override
    public void remove()
    {
        prev.expire();
    }

    /**
     * Finds the next valid entity, returns null if there are no valid entities.
     * 
     * @return The reference to the next valid entity, otherwise false.
     */
    private Entity findNext()
    {
        if (offset[0] == root.getEntitySize())
        {
            return null;
        }

        Entity current = null;
        boolean found = false;

        while (!found)
        {
            current = stack[depth];
            int size = current.getEntitySize();
            int skip = current.getEntityIndex();
            int index = ++offset[depth];

            // If the end of the entity has been reached, pop the previous entity off the stack.
            if (index == size)
            {
                depth--;

                // If it's -1 then stop searching.
                if (depth == -1)
                {
                    current = null;
                    found = true;
                }
            }
            else
            {
                current = current.getEntity( index );

                // Only traverse entities that contain other entities.
                if (current.getEntitySize() > 1 && index != skip)
                {
                    depth++;

                    // If next depth has max'd out the stack, increase it.
                    if (depth == stack.length)
                    {
                        stack = Arrays.copyOf( stack, depth + DEFAULT_MAX_DEPTH );
                        offset = Arrays.copyOf( offset, depth + DEFAULT_MAX_DEPTH );
                    }

                    // Push entity on stack for traversal
                    stack[depth] = current;
                    offset[depth] = -1;
                }
                else if (filter.isValid( current ))
                {
                    found = true;
                }
            }
        }

        return current;
    }

}

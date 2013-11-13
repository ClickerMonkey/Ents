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
import java.util.Collection;
import java.util.Iterator;


/**
 * An Entity which contains an array of Entities. An EntityList can remove and
 * delete expired entities from the internal array every time the
 * {@link #clean()} or {@link #update(Object)} methods are called. An EntityList
 * is the first Entity returned when iterated over, while it's children follow.
 * The child Entity in the list are drawn between
 * {@link Renderer#begin(Entity, Object)} and
 * {@link Renderer#end(Entity, Object)} when {@link #draw(Object)} is invoked.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class EntityList extends Entity
{

    /**
     * The default size of the backing array of Entities. The backing array will
     * automatically grow when it needs to and only shrinks when
     * {@link #shrink()} is invoked.
     */
    public static int DEFAULT_CAPACITY = 16;

    /**
     * The backing array of entities.
     */
    protected Entity[] entities = {};

    /**
     * The number of live entities in the list.
     */
    protected int entityCount = 0;

    /**
     * @see #setInheritVisible(boolean)
     */
    protected boolean inheritVisible;

    /**
     * @see #setInheritEnabled(boolean)
     */
    protected boolean inheritEnabled;

    /**
     * Instantiates a custom EntityList that by default does not have
     * components, controllers, or a view. The child entities of this
     * EntityList do not inherit the visibility and enabled from this
     * EntityList.
     * 
     * @see Entity#Entity()
     */
    public EntityList()
    {
        this( Ents.newTemplate(), DEFAULT_CAPACITY );
    }

    /**
     * Instantiates a custom EntityList with a given initial capacity in child
     * entities that by default does not have components, controllers, or a
     * view. The child entities of this EntityList do not inherit the visibility
     * and enabled from this EntityList.
     * 
     * @param initialCapacity
     *        The initial capacity of this EntityList. This represents the
     *        number of entities that can be added to this EntityList before the
     *        backing array has to be resized.
     * @see Entity#Entity()
     */
    public EntityList( int initialCapacity )
    {
        this( Ents.newTemplate(), initialCapacity );
    }

    /**
     * Instantiates a custom EntityList populated with the given array of
     * entities that by default does not have components, controllers, or a
     * view. The child entities of this EntityList do not inherit the visibility
     * and enabled from this EntityList.
     * 
     * @param entities
     *        The initial set of entities in this EntityList.
     * @see Entity#Entity()
     */
    public EntityList( Entity... entities )
    {
        this( Ents.newTemplate(), entities.length );

        this.add( entities );
    }

    /**
     * Instantiates an EntityList given a {@link Template} and array of
     * child entities. This entity will have the Template's components,
     * controllers, and view. The child entities of this EntityList do not
     * inherit the visibility and enabled from this EntityList. <br/>
     * <br/>
     * All controllers that exist in the Template are enabled by default on the
     * EntityList. In other words, when update is called after Entity creation
     * all controllers will modify the Entity. To control which controllers are
     * enabled use the {@link #setControllerEnabled(Controller, boolean)} method
     * (or any of it's variants).
     * 
     * @param template
     *        The template of the EntityList.
     * @param entities
     *        The initial set of entities in this EntityList.
     * @see Entity#Entity(Template)
     */
    public EntityList( Template template, Entity... entities )
    {
        this( template, entities.length );

        this.add( entities );
    }


    /**
     * Instantiates an EntityList given a {@link Template} and initial
     * entity capacity. This entity will have the Template's components,
     * controllers, and view. The child entities of this EntityList do not
     * inherit the visibility and enabled from this EntityList. <br/>
     * <br/>
     * All controllers that exist in the Template are enabled by default on the
     * EntityList. In other words, when update is called after Entity creation
     * all controllers will modify the Entity. To control which controllers are
     * enabled use the {@link #setControllerEnabled(Controller, boolean)} method
     * (or any of it's variants).
     * 
     * @param template
     *        The template of the EntityList.
     * @param initialCapacity
     *        The initial capacity of this EntityList. This represents the
     *        number of entities that can be added to this EntityList before the
     *        backing array has to be resized.
     * @see Entity#Entity(Template)
     */
    public EntityList( Template template, int initialCapacity )
    {
        super( template );

        this.entities = new Entity[initialCapacity];
    }

    /**
     * A constructor for the clone method.
     * 
     * @param template
     *        The template of the clone.
     * @param values
     *        The values of the clone.
     * @param renderer
     *        The renderer of the clone.
     */
    protected EntityList( Template template, Object[] values, Renderer renderer )
    {
        super( template, values, renderer );
    }

    /**
     * A method invoked every time an Entity is added to this EntityList.
     * 
     * @param e
     *        The entity added to this EntityList.
     * @param index
     *        The index of the entity (and the current size of the list before
     *        the addition).
     */
    protected void onEntityAdd( Entity e, int index )
    {
    }

    /**
     * A method invoked every time an expired Entity is found and is about to be
     * deleted (Entity{@link #delete()}).
     * 
     * @param e
     *        The entity removed.
     * @param index
     *        The index of the entity removed in the backing array.
     */
    protected void onEntityRemove( Entity e, int index )
    {
    }

    /**
     * A method invoked every time an Entity is updated.
     * 
     * @param e
     *        The entity updated.
     * @param index
     *        The index of the entity in the backing array.
     * @param updateState
     *        The updateState passed from {@link #update(Object)}.
     */
    protected void onEntityUpdated( Entity e, int index, Object updateState )
    {
    }

    /**
     * Ensures the free space in the backing array is able to fit the given
     * number of entities.
     * 
     * @param count
     *        The number of entities about to be added to this EntityList.
     */
    public void pad( int count )
    {
        if (entityCount + count >= entities.length)
        {
            int nextCapacity = entities.length + (entities.length >> 1);
            int minimumCapacity = entityCount + count;

            entities = Arrays.copyOf( entities, Math.max( nextCapacity, minimumCapacity ) );
        }
    }

    /**
     * Adds the entity to the backing array. This does not check to see if
     * there's room, {@link #pad(int)} must be called before hand with the
     * expected number of {@link #internalAdd(Entity)} invocations. This also
     * handles invoking {@link #onEntityAdd(Entity, int)} on the entity passed
     * in.
     * 
     * @param entity
     *        The entity to add to this EntityList.
     */
    private void internalAdd( Entity entity )
    {
        entities[entityCount] = entity;

        onEntityAdd( entity, entityCount );

        entityCount++;
    }

    /**
     * Adds a single entity to this EntityList.
     * 
     * @param entity
     *        The entity to add.
     */
    public void add( Entity entity )
    {
        pad( 1 );
        internalAdd( entity );
    }

    /**
     * Adds an array of entities to this EntityList.
     * 
     * @param entityArray
     *        The array of entities to add.
     */
    public void add( Entity... entityArray )
    {
        pad( entityArray.length );

        for (int i = 0; i < entityArray.length; i++)
        {
            internalAdd( entityArray[i] );
        }
    }

    /**
     * Adds a range of entities from the given array into this EntityList.
     * 
     * @param entityArray
     *        The array of entities to take from.
     * @param from
     *        The index of the first entity to be taken (inclusive).
     * @param to
     *        The index of the last entity to be taken (exclusive).
     */
    public void addRange( Entity[] entityArray, int from, int to )
    {
        pad( to - from );

        while (from < to)
        {
            internalAdd( entityArray[from++] );
        }
    }

    /**
     * Adds all entities in the given EntityList to this EntityList.
     * 
     * @param list
     *        The EntityList to add to this EntityList.
     */
    public void addAll( EntityList list )
    {
        addRange( list.entities, 0, list.entityCount );
    }

    /**
     * Adds all entities in the collection to this EntityList.
     * 
     * @param entityCollection
     *        The collection to take entities from to add to this EntityList.
     */
    public void addAll( Collection<Entity> entityCollection )
    {
        pad( entityCollection.size() );

        for (Entity e : entityCollection)
        {
            internalAdd( e );
        }
    }

    /**
     * Adds all entities in the iterator to this EntityList.
     * 
     * @param iterator
     *        The iterator to iterate through.
     */
    public void addAll( Iterator<Entity> iterator )
    {
        while (iterator.hasNext())
        {
            add( iterator.next() );
        }
    }

    /**
     * Adds all entities in the iterable to this EntityList.
     * 
     * @param iterable
     *        The iterable to iterate through.
     */
    public void addAll( Iterable<Entity> iterable )
    {
        addAll( iterable.iterator() );
    }

    /**
     * Removes all expired entities from this EntityList and calls
     * {@link Entity#delete()} on them.
     */
    public void clean()
    {
        int alive = 0;

        for (int i = 0; i < entityCount; i++)
        {
            final Entity e = entities[i];

            if (e.isExpired())
            {
                onEntityRemove( e, i );

                e.delete();
            }
            else
            {
                entities[alive++] = e;
            }
        }

        while (entityCount > alive)
        {
            entities[--entityCount] = null;
        }
    }

    /**
     * Shrinks the backing array of this EntityList to have the same length as
     * {@link #size()}. This should be called infrequently to avoid constantly
     * resizing/allocating the backing array. An ideal method would wait some
     * length of time and if the free space in the EntityList has maintained
     * a certain amount that entire time, shrink the EntityList.
     */
    public void shrink()
    {
        entities = Arrays.copyOf( entities, entityCount );
    }

    @Override
    public boolean delete()
    {
        boolean deletable = super.delete();

        if (deletable)
        {
            for (int i = 0; i < entityCount; i++)
            {
                entities[i].delete();
            }
        }

        return deletable;
    }

    @Override
    public void draw( Object drawState )
    {
        if (visible || !inheritVisible)
        {
            final boolean draw = (visible && renderer != null);

            if (draw)
            {
                renderer.begin( this, drawState );
            }

            for (int i = 0; i < entityCount; i++)
            {
                final Entity e = entities[i];

                if (!e.isExpired())
                {
                    entities[i].draw( drawState );
                }
            }

            if (draw)
            {
                renderer.end( this, drawState );
            }
        }
    }

    @Override
    public void update( Object updateState )
    {
        if (enabled || !inheritEnabled)
        {
            super.update( updateState );

            if (isExpired())
            {
                return;
            }

            for (int i = 0; i < entityCount; i++)
            {
                final Entity e = entities[i];

                if (!e.isExpired())
                {
                    e.update( updateState );

                    onEntityUpdated( e, i, updateState );
                }
            }

            this.clean();
        }
    }

    /**
     * The number of live entities currently in this EntityList. A live entity
     * is one that has not expired (since the last {@link #update(Object)} or
     * {@link #clean()} invocation).
     * 
     * @return The number of live entities in this EntityList.
     */
    public int size()
    {
        return entityCount;
    }

    /**
     * @return The total amount of space in the backing array to store entities.
     */
    public int capacity()
    {
        return entities.length;
    }

    /**
     * Gets the Entity at the given index. A check is not done here to ensure
     * index is positive and less than {@link #size()}.
     * 
     * @param index
     *        The index of the entity.
     * @return The reference of the entity at the given index.
     */
    public Entity at( int index )
    {
        return entities[index];
    }

    /**
     * @return True if visibility of the child entities are inherited from this
     *         EntityList.
     * @see #setInheritVisible(boolean)
     */
    public boolean isInheritVisible()
    {
        return inheritVisible;
    }

    /**
     * Sets whether or not the visibility of the child entities is dependent on
     * the visibility of this EntityList. If this is true and this EntityList is
     * not visibility, the child entities will not be drawn.
     * 
     * @param inheritVisible
     *        True if the visibility of the child entities is inherited.
     */
    public void setInheritVisible( boolean inheritVisible )
    {
        this.inheritVisible = inheritVisible;
    }

    /**
     * @return True if enabled of the child entities are inherited from this
     *         EntityList.
     * @see #setInheritEnabled(boolean)
     */
    public boolean isInheritEnabled()
    {
        return inheritEnabled;
    }

    /**
     * Sets whether or not the enabled of the child entities is dependent on the
     * enabled of this EntityList. If this is true and this EntityList is not
     * enabled, the child entities will not be updated.
     * 
     * @param inheritEnabled
     *        True if the enabled of the child entities is inherited.
     */
    public void setInheritEnabled( boolean inheritEnabled )
    {
        this.inheritEnabled = inheritEnabled;
    }

    @Override
    public EntityList clone( boolean deep )
    {
        EntityList clone = cloneState( new EntityList( template, template.createClonedValues( values, deep ), renderer ) );

        clone.inheritEnabled = inheritEnabled;
        clone.inheritVisible = inheritVisible;
        clone.pad( entityCount );

        for (int i = 0; i < entityCount; i++)
        {
            clone.internalAdd( entities[i].clone( deep ) );
        }

        return clone;
    }

    @Override
    protected int getEntitySize()
    {
        return entityCount + 1;
    }

    @Override
    protected Entity getEntity( int index )
    {
        return (index == 0 ? this : entities[index - 1]);
    }

}

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

/**
 * A chain of three entities. One entity before this one, this entity, and one
 * entity after. <br/>
 * <br/>
 * If an entity expires in the chain, it is deleted and removed from the chain
 * automatically whenever {@link #delete()}, {@link #draw(Object)}, or
 * {@link #update(Object)} are invoked.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class EntityChain extends Entity
{

    /**
     * The first Entity in the chain to update and draw.
     */
    protected Entity first;

    /**
     * The last Entity in the chain to update and draw.
     */
    protected Entity last;

    /**
     * @see #setInheritVisible(boolean)
     */
    protected boolean inheritVisible;

    /**
     * @see #setInheritEnabled(boolean)
     */
    protected boolean inheritEnabled;

    /**
     * Instantiates a custom EntityChain that by default does not have
     * components, controllers, or a view. The first and last entities attached
     * to this EntityChain do not inherit the visibility and enabled from
     * this EntityChain.
     * 
     * @see Entity#Entity()
     */
    public EntityChain()
    {
        this( Ents.newTemplate(), null, null, false, false );
    }

    /**
     * Instantiates an EntityChain given a {@link Template}. This entity will
     * have the Template's components, controllers, and view. The first and
     * last entities attached to this EntityChain do not inherit the visibility
     * and enabled from this EntityChain. <br/>
     * <br/>
     * All controllers that exist in the Template are enabled by default on the
     * EntityChain. In other words, when update is called after Entity creation
     * all controllers will modify the Entity. To control which controllers are
     * enabled use the {@link #setControllerEnabled(Controller, boolean)} method
     * (or any of it's variants).
     * 
     * @param template
     *        The template of the EntityChain.
     * @see Entity#Entity(Template)
     */
    public EntityChain( Template template )
    {
        this( template, null, null, false, false );
    }

    /**
     * Instantiates a custom EntityChain that by default does not have
     * components, controllers, or a view. The first and last entities in the
     * EntityChain are given, as well as whether they inherit this
     * EntityChain's visibility and enabled.
     * 
     * @param first
     *        The first Entity in the chain.
     * @param last
     *        The last (third) Entity in the chain.
     * @param inheritVisible
     *        True if the visibility of the first and last entities is
     *        inherited.
     * @param inheritEnabled
     *        True if the enabled of the first and last entities is inherited.
     * @see Entity#Entity()
     * @see #setInheritEnabled(boolean)
     * @see #setInheritVisible(boolean)
     */
    public EntityChain( Entity first, Entity last, boolean inheritVisible, boolean inheritEnabled )
    {
        this( Ents.newTemplate(), first, last, inheritVisible, inheritEnabled );
    }

    /**
     * Instantiates an EntityChain given a {@link Template}. This entity will
     * have the Template's components, controllers, and view. The first and
     * last entities in the EntityChain are given, as well as whether they
     * inherit this EntityChain's visibility and enabled. <br/>
     * <br/>
     * All controllers that exist in the Template are enabled by default on the
     * EntityChain. In other words, when update is called after Entity creation
     * all controllers will modify the Entity. To control which controllers are
     * enabled use the {@link #setControllerEnabled(Controller, boolean)} method
     * (or any of it's variants).
     * 
     * @param template
     *        The template of the EntityChain.
     * @param first
     *        The first Entity in the chain.
     * @param last
     *        The last (third) Entity in the chain.
     * @param inheritVisible
     *        True if the visibility of the first and last entities is
     *        inherited.
     * @param inheritEnabled
     *        True if the enabled of the first and last entities is inherited.
     * @see Entity#Entity(Template)
     * @see #setInheritEnabled(boolean)
     * @see #setInheritVisible(boolean)
     */
    public EntityChain( Template template, Entity first, Entity last, boolean inheritVisible, boolean inheritEnabled )
    {
        super( template );

        this.first = first;
        this.last = last;
        this.inheritEnabled = inheritEnabled;
        this.inheritVisible = inheritVisible;
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
    protected EntityChain( Template template, Object[] values, Renderer renderer )
    {
        super( template, values, renderer );
    }

    @Override
    public boolean delete()
    {
        boolean deletable = super.delete();

        if (deletable)
        {
            if (validateFirst())
            {
                first.delete();
            }

            if (validateLast())
            {
                last.delete();
            }
        }

        return deletable;
    }

    @Override
    public void draw( Object drawState )
    {
        if (visible || !inheritVisible)
        {
            if (validateFirst())
            {
                first.draw( drawState );
            }

            super.draw( drawState );

            if (validateLast())
            {
                last.draw( drawState );
            }
        }
    }

    @Override
    public void update( Object updateState )
    {
        if (enabled || !inheritEnabled)
        {
            if (validateFirst())
            {
                first.update( updateState );
            }

            super.update( updateState );

            if (validateLast())
            {
                last.update( updateState );
            }
        }
    }

    @Override
    protected int getEntitySize()
    {
        final boolean f = (first != null);
        final boolean l = (last != null);

        return (f & l ? 3 : (f | l ? 2 : 1));
    }

    @Override
    protected Entity getEntity( int index )
    {
        final boolean f = (first != null);

        switch (index)
        {
        case 0:
            return (f ? first : this);
        case 1:
            return (f ? this : last);
        case 2:
            return last;
        }

        return this;
    }

    @Override
    protected int getEntityIndex()
    {
        final boolean f = (first != null);

        return (f ? 1 : 0);
    }

    @Override
    public EntityChain clone( boolean deep )
    {
        EntityChain clone = cloneState( new EntityChain( template, template.createClonedValues( values, deep ), renderer ) );

        clone.inheritEnabled = inheritEnabled;
        clone.inheritVisible = inheritVisible;

        if (first != null)
        {
            clone.first = deep ? first.clone( deep ) : first;
        }
        if (last != null)
        {
            clone.last = deep ? last.clone( deep ) : last;
        }

        return clone;
    }

    /**
     * Returns whether the first entity is non-null and not expired. If the
     * entity as expired, this method deletes it and sets it to null.
     * 
     * @return True if the first Entity is non-null and not expired.
     */
    private boolean validateFirst()
    {
        if (first != null && first.isExpired())
        {
            first.delete();
            first = null;
        }

        return (first != null);
    }

    /**
     * Returns whether the last entity is non-null and not expired. If the
     * entity as expired, this method deletes it and sets it to null.
     * 
     * @return True if the last Entity is non-null and not expired.
     */
    private boolean validateLast()
    {
        if (last != null && last.isExpired())
        {
            last.delete();
            last = null;
        }

        return (last != null);
    }

    /**
     * @return The reference to the first Entity.
     */
    public Entity getFirst()
    {
        return first;
    }

    /**
     * Sets the first Entity.
     * 
     * @param first
     *        The first Entity in the chain.
     */
    public void setFirst( Entity first )
    {
        this.first = first;
    }

    /**
     * @return The reference to the last Entity.
     */
    public Entity getLast()
    {
        return last;
    }

    /**
     * Sets the last Entity.
     * 
     * @param last
     *        The last Entity in the chain.
     */
    public void setLast( Entity last )
    {
        this.last = last;
    }

    /**
     * @return True if visibility of this EntityChain is inherited by first and
     *         last.
     * @see #setInheritVisible(boolean)
     */
    public boolean isInheritVisible()
    {
        return inheritVisible;
    }

    /**
     * Sets whether or not the visibility of the first and last entities is
     * dependent on the visibility of this EntityChain's. If this is true and
     * this EntityChain is not visible, first and last will not be drawn.
     * 
     * @param inheritVisible
     *        True if the visibility of the first and last entities is
     *        inherited.
     */
    public void setInheritVisible( boolean inheritVisible )
    {
        this.inheritVisible = inheritVisible;
    }

    /**
     * @return True if enabled of this EntityChain is inherited by first and
     *         last.
     * @see #setInheritEnabled(boolean)
     */
    public boolean isInheritEnabled()
    {
        return inheritEnabled;
    }

    /**
     * Sets whether or not the enabled of the first and last entities is
     * dependent on the enabled of this EntityChain's. If this is true and
     * this EntityChain is not enabled, first and last will not be updated.
     * 
     * @param inheritEnabled
     *        True if the enabled of the first and last entities is inherited.
     */
    public void setInheritEnabled( boolean inheritEnabled )
    {
        this.inheritEnabled = inheritEnabled;
    }

}

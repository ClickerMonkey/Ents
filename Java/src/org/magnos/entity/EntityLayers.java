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
 *              Open Software License (OSL 3.0)
 */

package org.magnos.entity;

/**
 * An Entity which contains layers of {@link EntityList}s. The layers are used
 * to control the order of updating and drawing entities when
 * {@link EntityLayers#update(Object)} and {@link EntityLayers#draw(Object)} are
 * invoked. Layers can be accessed or added to by index or an enum (where
 * {@link Enum#ordinal()} is used as the index).
 * 
 * @author Philip Diffenderfer
 * 
 */
public class EntityLayers extends Entity
{

    protected final EntityList[] layers;

    /**
     * Instantiates a custom EntityLayers with the same number of layers as
     * there are constants in the given enum that by default does not have
     * components, controllers, or a view.
     * 
     * @param enumClass
     *        The enum that will be used for accessing layers.
     */
    public <E extends Enum<E>> EntityLayers( Class<E> enumClass )
    {
        this( Ents.newTemplate(), enumClass.getEnumConstants().length );
    }

    /**
     * Instantiates a custom EntityLayers with the given number of layers that
     * by default does not have components, controllers, or a view.
     * 
     * @param layerCount
     *        The number of layers.
     */
    public EntityLayers( int layerCount )
    {
        this( Ents.newTemplate(), layerCount );
    }

    /**
     * Instantiates an EntityList given a {@link Template} with the same number
     * of layers as there are constants in the given enum. <br/>
     * <br/>
     * All controllers that exist in the Template are enabled by default on the
     * EntityList. In other words, when update is called after Entity creation
     * all controllers will modify the Entity. To control which controllers are
     * enabled use the {@link #setControllerEnabled(Controller, boolean)} method
     * (or any of it's variants).
     * 
     * @param template
     *        The template of the EntityLayers.
     * @param enumClass
     *        The enum that will be used for accessing layers.
     */
    public <E extends Enum<E>> EntityLayers( Template template, Class<E> enumClass )
    {
        this( template, enumClass.getEnumConstants().length );
    }

    /**
     * Instantiates an EntityList given a {@link Template} with the given number
     * of layers. <br/>
     * <br/>
     * All controllers that exist in the Template are enabled by default on the
     * EntityList. In other words, when update is called after Entity creation
     * all controllers will modify the Entity. To control which controllers are
     * enabled use the {@link #setControllerEnabled(Controller, boolean)} method
     * (or any of it's variants).
     * 
     * @param template
     *        The template of the EntityLayers.
     * @param layerCount
     *        The number of layers.
     */
    public EntityLayers( Template template, int layerCount )
    {
        this( template, template.createDefaultValues(), template.createRenderer(), layerCount );
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
     * @param layerCount
     *        The number of layers in the clone.
     */
    protected EntityLayers( Template template, Object[] values, Renderer renderer, int layerCount )
    {
        super( template, values, renderer );

        layers = new EntityList[layerCount];

        for (int i = 0; i < layerCount; i++)
        {
            layers[i] = new EntityList();
        }
    }

    @Override
    public boolean delete()
    {
        boolean deletable = super.delete();

        if (deletable)
        {
            for (int i = 0; i < layers.length; i++)
            {
                layers[i].delete();
            }
        }

        return deletable;
    }

    /**
     * Gets the layer at the given index. This does not do a check to see if
     * index is greater than or equal to zero or less than the number of layers
     * in this EntityLayers.
     * 
     * @param index
     *        The index of the layer.
     * @return The reference to the layer at the given index.
     */
    public EntityList layer( int index )
    {
        return layers[index];
    }

    /**
     * Gets the layer at the index of the given constant (based on
     * {@link Enum#ordinal()}). This does not check to see if the enum given is
     * valid for accessing layers (the enum has the same number of constants as
     * this EntityLayers has layers).
     * 
     * @param enumConstant
     *        The enum that will be used for accessing layers.
     * @return The reference to the layer at the given enum constant ordinal.
     */
    public <E extends Enum<E>> EntityList layer( E enumConstant )
    {
        return layers[enumConstant.ordinal()];
    }

    /**
     * Adds a single entity to the given layer. This does not do a check to see
     * if index is greater than or equal to zero or less than the number of
     * layers in this EntityLayers.
     * 
     * @param layerIndex
     *        The index of the layer.
     * @param e
     *        The entity to add.
     */
    public void add( int layerIndex, Entity e )
    {
        layers[layerIndex].add( e );
    }

    /**
     * Adds a single entity to the given layer. This does not check to see if
     * the enum given is valid for accessing layers (the enum has the same
     * number of constants as this EntityLayers has layers).
     * 
     * @param layerIndex
     *        The enum that will be used for accessing layers.
     * @param e
     *        The entity to add.
     */
    public <E extends Enum<E>> void add( E layerIndex, Entity e )
    {
        layers[layerIndex.ordinal()].add( e );
    }

    /**
     * Swaps the two layers at the given indices. This is used to change the
     * order of updating and drawing between the layers. This does not do a
     * check to see if the indices are greater than or equal to zero or less
     * than the number of layers in this EntityLayers.
     * 
     * @param i
     *        The index of the first layer.
     * @param j
     *        The index of the second layer.
     */
    public void swap( int i, int j )
    {
        EntityList temp = layers[i];
        layers[i] = layers[j];
        layers[j] = temp;
    }

    /**
     * Swaps the two layers at the given indices. This is used to change the
     * order of updating and drawing between the layers. This does not check to
     * see if the enumerations given are valid for accessing layers (the enum
     * has the same number of constants as this EntityLayers has layers).
     * 
     * @param i
     *        The enum of the first layer.
     * @param j
     *        The enum of the second layer.
     */
    public <E extends Enum<E>> void swap( E i, E j )
    {
        swap( i.ordinal(), j.ordinal() );
    }

    @Override
    public void draw( Object drawState )
    {
        final boolean draw = (visible && renderer != null);

        if (draw)
        {
            renderer.begin( this, drawState );
        }

        for (int i = 0; i < layers.length; i++)
        {
            final EntityList list = layers[i];

            if (!list.isExpired())
            {
                list.draw( drawState );
            }
        }

        if (draw)
        {
            renderer.end( this, drawState );
        }
    }

    @Override
    public void update( Object updateState )
    {
        super.update( updateState );

        for (int i = 0; i < layers.length; i++)
        {
            final EntityList list = layers[i];

            if (!list.isExpired())
            {
                list.update( updateState );
            }
            else
            {
                list.delete();
            }
        }
    }

    @Override
    protected int getEntitySize()
    {
        return layers.length + 1;
    }

    @Override
    protected Entity getEntity( int index )
    {
        return (index == 0 ? this : layers[index - 1]);
    }

    @Override
    public EntityLayers clone( boolean deep )
    {
        EntityLayers clone = cloneState( new EntityLayers( template, template.createClonedValues( layers, deep ), renderer, layers.length ) );

        if (deep)
        {
            for (int i = 0; i < layers.length; i++)
            {
                clone.layers[i] = layers[i].clone( deep );
            }
        }
        else
        {
            System.arraycopy( layers, 0, clone.layers, 0, layers.length );
        }

        return clone;
    }

}

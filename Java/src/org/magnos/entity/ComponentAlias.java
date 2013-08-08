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
 * A component that is an alias to another component. This is used when there 
 * are components of the same type representing essentially the same thing. 
 * When one component value is requested from an entity, the component that
 * is aliased is actually returned. This saves on wasted space and having
 * duplicate values on an Entity that would need to be synchronized.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The component value type.
 */
class ComponentAlias<T> extends Component<T>
{
	
	/**
	 * The id of the component this component is an alias to.
	 */
	private final int componentId;

	/**
	 * Instantiates a new ComponentAlias.
	 * 
	 * @param id
	 * 		The id of the component.
	 * @param name
	 * 		The name of the component.
	 * @param componentId
	 * 		The id of the component being aliased.
	 */
	protected ComponentAlias( int id, String name, int componentId )
	{
		super( id, name );

		this.componentId = componentId;
	}

	@Override
	protected void postCustomAdd( Entity e )
	{

	}

	@Override
	protected TemplateComponent<T> add( Template template )
	{
		return new ComponentAliasHandler( template );
	}

	@SuppressWarnings("unchecked")
	private class ComponentAliasHandler implements TemplateComponent<T>
	{
		private final Template template;

		private ComponentAliasHandler( Template template )
		{
			this.template = template;
		}
		
		@Override
		public void set( Entity e, T value )
		{
			TemplateComponent<T> handler = (TemplateComponent<T>) template.handlers[componentId];
			handler.set( e, value );
		}

		@Override
		public T get( Entity e )
		{
			TemplateComponent<T> handler = (TemplateComponent<T>) template.handlers[componentId];
			return handler.get( e );
		}
		
		@Override
		public T take( Entity e, T target )
		{
			TemplateComponent<T> handler = (TemplateComponent<T>) template.handlers[componentId];
			return handler.take( e, target );
		}

		@Override
		public void remove( Template template )
		{

		}
		
		@Override
		public void postAdd( Entity e )
		{
		   
		}
		
		@Override
		public void preRemove( Entity e )
		{
		   
		}
	}

}
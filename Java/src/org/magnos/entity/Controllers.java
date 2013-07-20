package org.magnos.entity;

/**
 * A simple collection of {@link Controllers}s passed to {@link EntityCore} to 
 * define what controllers a {@link Template} should have. The order of 
 * controllers passed in define the order of controller execution for an 
 * Entity.
 *  
 * @author Philip Diffenderfer
 *
 */
public class Controllers
{

	/**
	 * The controllers passed into the constructor.
	 */
	public final Controller[] values;

	/**
	 * Instantiates a new Controllers.
	 * 
	 * @param values
	 * 		The controllers that should be added to a template.
	 */
	public Controllers( Controller... values )
	{
		this.values = values;
	}

}
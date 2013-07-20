package org.magnos.entity;

/**
 * A simple collection of {@link Component}s passed to {@link EntityCore} to 
 * define what components a {@link Template} should have. The order of 
 * components passed in define the order in which they are added to a Template
 * and subsequently any Entity.
 *  
 * @author Philip Diffenderfer
 *
 */
public class Components
{
	
	/**
	 * The components passed into the constructor.
	 */
	public final Component<?>[] values;

	/**
	 * Instantiates a new Components.
	 * 
	 * @param values
	 * 		The components that should be added to a template.
	 */
	public Components( Component<?>... values )
	{
		this.values = values;
	}

}
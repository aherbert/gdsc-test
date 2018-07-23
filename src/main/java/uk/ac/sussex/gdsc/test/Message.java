/*-
 * #%L
 * Genome Damage and Stability Centre Test Package
 *
 * The GDSC Test package contains code for use with the JUnit test framework.
 * %%
 * Copyright (C) 2018 Alex Herbert
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package uk.ac.sussex.gdsc.test;

/**
 * Provide messages for logging. To be used when providing the arguments for the message is computationally intense.
 */
public abstract class Message
{
	/** The format. */
	final String format;

	/**
	 * Instantiates a new message.
	 *
	 * @param format
	 *            the format
	 */
	public Message(String format)
	{
		this.format = format;
	}

	/**
	 * Helper method to wrap the variable length arguments list for use in {@link #getArgs()}.
	 *
	 * @param args
	 *            the arguments for the message
	 * @return the arguments for the message as an array
	 */
	final public static Object[] wrap(Object... args)
	{
		return args;
	}

	/**
	 * Gets the arguments for the message.
	 *
	 * @return the arguments
	 */
	public abstract Object[] getArgs();
}

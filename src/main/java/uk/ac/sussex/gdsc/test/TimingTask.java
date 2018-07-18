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
 * Defines a task to run.
 */
public interface TimingTask
{
	/**
	 * Gets the number of tasks.
	 *
	 * @return the number of tasks
	 */
	public int getSize();

	/**
	 * Gets the task data for given task.
	 * <p>
	 * If the data is destructively modified by the {@link #run(Object)} method then this should be new data.
	 *
	 * @param i
	 *            the task index
	 * @return the data
	 */
	public Object getData(int i);

	/**
	 * Run the task.
	 *
	 * @param data
	 *            the task data
	 * @return the result
	 */
	public Object run(Object data);

	/**
	 * The task name.
	 *
	 * @return the name
	 */
	public String getName();

	/**
	 * Check the result produced by the given task.
	 * <p>
	 * It is left to the implementation to decide how to handle incorrect results, e.g. throw an exception, log an
	 * error, etc.
	 *
	 * @param i
	 *            the task index
	 * @param result
	 *            the result
	 */
	public void check(int i, Object result);
}
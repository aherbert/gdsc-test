/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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

package uk.ac.sussex.gdsc.test.utils;

/**
 * Defines a task to run.
 */
public interface TimingTask {
  /**
   * Gets the number of tasks.
   *
   * @return the number of tasks
   */
  int getSize();

  /**
   * Gets the task data for given task.
   *
   * <p>If the data is destructively modified by the {@link #run(Object)} method then this should be
   * new data.
   *
   * @param index the task index
   * @return the data
   */
  Object getData(int index);

  /**
   * Run the task.
   *
   * @param data the task data
   * @return the result
   */
  Object run(Object data);

  /**
   * The task name.
   *
   * @return the name
   */
  String getName();

  /**
   * Check the result produced by the given task.
   *
   * @param index the task index
   * @param result the result
   */
  void check(int index, Object result);
}

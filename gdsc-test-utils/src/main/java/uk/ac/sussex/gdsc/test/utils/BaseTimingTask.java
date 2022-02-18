/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 - 2020 Alex Herbert
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
public abstract class BaseTimingTask implements TimingTask {
  /** The name of the task. */
  protected String name;

  /**
   * Creates a new base timing task.
   *
   * @param name the name
   */
  public BaseTimingTask(String name) {
    this.name = name;
  }

  /*
   * (non-Javadoc)
   *
   * @see gdsc.core.test.TimingTask#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This base class does nothing so that extending classes can override if necessary to validate
   * results.
   *
   * @see uk.ac.sussex.gdsc.test.utils.TimingTask#check(int, java.lang.Object)
   */
  @Override
  public void check(int index, Object result) {
    // Do nothing
  }
}

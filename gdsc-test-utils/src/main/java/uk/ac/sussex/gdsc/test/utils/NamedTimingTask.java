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

import java.util.function.Supplier;

/**
 * Defines a named task that was run.
 *
 * <p>This class is used for reporting and has no effect when passed to
 * {@link TimingService#execute(TimingTask)}.
 */
public class NamedTimingTask extends BaseTimingTask {

  /** Constant for no supplier. */
  private static final Supplier<String> NO_SUPPLIER =  null;

  /** The name supplier. */
  private Supplier<String> nameSupplier;

  /**
   * Instantiates a new named timing task.
   *
   * @param name the name
   */
  public NamedTimingTask(String name) {
    super(name);
  }

  /**
   * Instantiates a new named timing task.
   *
   * @param name the name
   */
  public NamedTimingTask(Supplier<String> name) {
    super("");
    this.nameSupplier = name;
  }

  @Override
  public String getName() {
    // Format the name on demand
    final Supplier<String> nameSupplier = this.nameSupplier;
    if (nameSupplier != null) {
      name = nameSupplier.get();
      this.nameSupplier = NO_SUPPLIER;
    }
    return super.getName();
  }

  @Override
  public int getSize() {
    return 0;
  }

  /**
   * This should not be called as the task has no data.
   *
   * @param index the index
   * @return the data
   * @throws IllegalStateException If the method is called
   * @see uk.ac.sussex.gdsc.test.utils.TimingTask#getData(int)
   */
  @Override
  public Object getData(int index) {
    throw new IllegalStateException("This task has no data");
  }

  /**
   * This should not be called as the task has no data.
   *
   * @param data the data
   * @return the data
   * @throws IllegalStateException If the method is called
   * @see uk.ac.sussex.gdsc.test.utils.TimingTask#run(java.lang.Object)
   */
  @Override
  public Object run(Object data) {
    throw new IllegalStateException("This task has no data");
  }
}

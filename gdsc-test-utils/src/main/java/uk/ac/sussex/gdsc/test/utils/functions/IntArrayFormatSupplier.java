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

package uk.ac.sussex.gdsc.test.utils.functions;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Supply a formatted message using the {@link String#format(String, Object...)} function with an
 * array of {@code int} arguments.
 *
 * <p>This class allows setting primitive values which are then boxed for the
 * {@link String#format(String, Object...)} function if necessary.
 */
public class IntArrayFormatSupplier extends ArrayFormatSupplier {

  private final int[] values;

  /**
   * Constructs a new instance of this class.
   *
   * @param format the format (assumed to support the class {@link #getArgs()} function)
   * @param size the size
   * @throws IllegalArgumentException If the format is null or empty, or size if not strictly
   *         positive
   */
  public IntArrayFormatSupplier(String format, int size) throws IllegalArgumentException {
    super(format, size);
    values = new int[size];
  }

  /**
   * Sets the value.
   *
   * @param index the index
   * @param value the value
   * @return the supplier
   */
  public Supplier<String> set(int index, int value) {
    values[index] = value;
    return this;
  }

  /**
   * Gets the value.
   *
   * @param index the index
   * @return the value
   */
  public int get(int index) {
    return values[index];
  }

  @Override
  public Object[] getArgs() {
    return Arrays.stream(values).boxed().toArray();
  }
}

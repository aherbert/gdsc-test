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

package uk.ac.sussex.gdsc.test.utils.functions;

/**
 * Supply a formatted message using the {@link String#format(String, Object...)} function with an
 * array of object arguments.
 */
public class ObjectArrayFormatSupplier extends ArrayFormatSupplier {

  /** The values. */
  private final Object[] values;

  /**
   * Constructs a new instance of this class.
   *
   * @param format the format (assumed to support the class {@link #getArgs()} function)
   * @param size the size
   * @throws IllegalArgumentException If the format is null or empty, or size if not strictly
   *         positive
   */
  public ObjectArrayFormatSupplier(String format, int size) {
    super(format, size);
    values = new Object[size];
  }

  /**
   * Sets the value.
   *
   * @param index the index
   * @param value the value
   * @return the supplier
   */
  public ObjectArrayFormatSupplier set(int index, Object value) {
    values[index] = value;
    return this;
  }

  /**
   * Gets the value.
   *
   * @param index the index
   * @return the value
   */
  public Object get(int index) {
    return values[index];
  }

  @Override
  public Object[] getArgs() {
    return values.clone();
  }
}

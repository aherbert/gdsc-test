/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 - 2025 Alex Herbert
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package uk.ac.sussex.gdsc.test.utils.functions;

import java.util.Arrays;

/**
 * Supply a formatted message using the {@link String#format(String, Object...)} function with an
 * array of {@code int} arguments.
 *
 * <p>This class allows setting primitive values which are then boxed for the
 * {@link String#format(String, Object...)} function if necessary.
 */
public class IntArrayFormatSupplier extends ArrayFormatSupplier {

  /** The values. */
  private final int[] values;

  /**
   * Constructs a new instance of this class.
   *
   * @param format the format (assumed to support the class {@link #getArgs()} function)
   * @param size the size
   * @throws IllegalArgumentException if the format is null or empty, or size if not strictly
   *         positive
   */
  public IntArrayFormatSupplier(String format, int size) {
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
  public IntArrayFormatSupplier set(int index, int value) {
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

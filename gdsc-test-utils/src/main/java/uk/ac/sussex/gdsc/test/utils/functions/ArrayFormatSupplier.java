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

/**
 * Supply a formatted message using the {@link String#format(String, Object...)} function with an
 * array of arguments.
 *
 * <p>This is a base class to allow dynamic update of a message supplier for use in messages.
 */
public abstract class ArrayFormatSupplier extends FormatSupplier {

  /** The size. */
  private final int size;

  /**
   * Constructs a new instance of this class.
   *
   * @param format the format
   * @param size the size
   * @throws IllegalArgumentException if the format is null or empty, or size if not strictly
   *         positive
   */
  public ArrayFormatSupplier(String format, int size) {
    super(format);
    if (size <= 0) {
      throw new IllegalArgumentException(size + " <= 0");
    }
    this.size = size;
  }

  /**
   * Gets the size of the array.
   *
   * @return the size
   */
  public int getSize() {
    return size;
  }
}

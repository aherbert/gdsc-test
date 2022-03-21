/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 - 2022 Alex Herbert
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

import java.util.function.Supplier;

/**
 * Supply a formatted message using the {@link String#format(String, Object...)} function.
 *
 * <p>This is a base class to allow dynamic update of a message supplier for use in messages.
 */
public abstract class FormatSupplier implements Supplier<String> {

  /** The message format. */
  private final String format;

  /**
   * Constructs a new instance of this class.
   *
   * @param format the format
   * @throws IllegalArgumentException If the format is null or empty
   */
  public FormatSupplier(String format) {
    if (format == null || format.length() == 0) {
      throw new IllegalArgumentException("Format is null or empty");
    }
    this.format = format;
  }

  /**
   * Gets a message. The message consists of:
   *
   * <pre>
   * String.format(format, getArgs());
   * </pre>
   *
   * @see java.util.function.Supplier#get()
   * @see #getArgs()
   */
  @Override
  public String get() {
    return String.format(format, getArgs());
  }

  /**
   * Gets the arguments for the {@link String#format(String, Object...)} function.
   *
   * @return the arguments
   */
  public abstract Object[] getArgs();
}

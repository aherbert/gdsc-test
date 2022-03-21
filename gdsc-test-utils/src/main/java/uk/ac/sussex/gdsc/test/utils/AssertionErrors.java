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

package uk.ac.sussex.gdsc.test.utils;

import java.util.function.Supplier;

/**
 * Contains utility functions for {@link AssertionError}.
 */
public final class AssertionErrors {
  /**
   * Do not allow public construction.
   */
  private AssertionErrors() {}

  /**
   * Wraps an assertion error with a new error that has a formatted message appended to the input
   * error's message.
   *
   * @param error The error.
   * @param messageSupplier The message supplier.
   * @throws AssertionError The wrapped assertion error.
   */
  public static void appendMessage(AssertionError error, Supplier<String> messageSupplier)
      throws AssertionError {
    final String msg = error.getMessage();
    if (msg == null || msg.length() == 0) {
      throw new AssertionError(messageSupplier.get(), error);
    }
    throw new AssertionError(msg + " " + messageSupplier.get(), error);
  }

  /**
   * Wraps an assertion error with a new error that has a formatted message prepended to the input
   * error's message.
   *
   * @param error The error.
   * @param messageSupplier The message supplier.
   * @throws AssertionError The wrapped assertion error.
   */
  public static void prependMessage(AssertionError error, Supplier<String> messageSupplier)
      throws AssertionError {
    final String msg = error.getMessage();
    if (msg == null || msg.length() == 0) {
      throw new AssertionError(messageSupplier.get(), error);
    }
    throw new AssertionError(messageSupplier.get() + " " + msg, error);
  }
}

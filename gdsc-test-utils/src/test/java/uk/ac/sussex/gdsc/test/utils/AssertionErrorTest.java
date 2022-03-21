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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class AssertionErrorTest {
  /** The supplied message. */
  private final Supplier<String> messageSupplier = () -> {
    return "Lambda message";
  };

  @Test
  void testAppendMessage() {
    try {
      AssertionErrors.appendMessage(new AssertionError(), messageSupplier);
    } catch (final AssertionError ex) {
      // Null is ignored
      Assertions.assertEquals(ex.getMessage(), messageSupplier.get());
    }
    try {
      AssertionErrors.appendMessage(new AssertionError(""), messageSupplier);
    } catch (final AssertionError ex) {
      // Empty string is ignored
      Assertions.assertEquals(ex.getMessage(), messageSupplier.get());
    }
    try {
      AssertionErrors.appendMessage(new AssertionError("Something bad"),
          messageSupplier);
    } catch (final AssertionError ex) {
      // Empty string is ignored
      Assertions.assertEquals(ex.getMessage(), "Something bad " + messageSupplier.get());
    }
  }

  @Test
  void testPrependMessage() {
    try {
      AssertionErrors.prependMessage(new AssertionError(), messageSupplier);
    } catch (final AssertionError ex) {
      // Null is ignored
      Assertions.assertEquals(ex.getMessage(), messageSupplier.get());
    }
    try {
      AssertionErrors.prependMessage(new AssertionError(""), messageSupplier);
    } catch (final AssertionError ex) {
      // Empty string is ignored
      Assertions.assertEquals(ex.getMessage(), messageSupplier.get());
    }
    try {
      AssertionErrors.prependMessage(new AssertionError("Something bad"), messageSupplier);
    } catch (final AssertionError ex) {
      // Empty string is ignored
      Assertions.assertEquals(ex.getMessage(), messageSupplier.get() + " Something bad");
    }
  }
}

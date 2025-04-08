/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with JUnit 5.
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

package uk.ac.sussex.gdsc.test.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.ac.sussex.gdsc.test.utils.AssertionErrorCounter;

/**
 * This tests the {@link AssertionErrorCounter} can be used when JUnit 5 is the source of
 * {@link AssertionError}.
 */
@SuppressWarnings("javadoc")
class AssertionErrorCounterTest {
  @ParameterizedTest
  @CsvSource({
  // @formatter:off
      "1, 0, true",
      "1, 1, false",
      "1, 1, true",
      "1, 2, false",
      "1, 2, true",
      "2, 0, true",
      "2, 1, false",
      "2, 1, true",
      "2, 2, false",
      "2, 2, true",
      // @formatter:on
  })
  void runTestAssert(int size, int limit, boolean exceed) {
    final AssertionErrorCounter fc = new AssertionErrorCounter(limit, size);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < limit; j++) {
        fc.run(i, () -> Assertions.fail("Throw an error"));
      }
    }
    if (exceed) {
      for (int i = 0; i < size; i++) {
        final int index = i;
        Assertions.assertThrows(AssertionError.class, () -> {
          fc.run(index, () -> Assertions.fail("Throw an error"));
        });
      }
    }
  }
}

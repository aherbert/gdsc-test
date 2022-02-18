/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with JUnit 5.
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

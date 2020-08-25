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
import org.junit.jupiter.api.Test;
import uk.ac.sussex.gdsc.test.utils.TestCounter;

/**
 * This tests the {@link TestCounter} can be used when JUnit 5 is the source of
 * {@link AssertionError}.
 */
@SuppressWarnings("javadoc")
class TestCounterTest {
  private static void runTestAssert(int size, int limit, boolean exceed) {
    final TestCounter fc = new TestCounter(limit, size);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < limit; j++) {
        fc.run(i, () -> Assertions.fail("Throw an error"));
      }
    }
    if (exceed) {
      for (int i = 0; i < size; i++) {
        fc.run(i, () -> Assertions.fail("Throw an error"));
      }
    }
  }

  private static void runTestCase(int size, int limit, boolean exceed) {
    final TestCounter fc = new TestCounter(limit, size);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < limit; j++) {
        fc.run(i, () -> false, () -> Assertions.fail("Throw an error"));
      }
    }
    if (exceed) {
      for (int i = 0; i < size; i++) {
        fc.run(i, () -> false, () -> Assertions.fail("Throw an error"));
      }
    }
  }

  @Test
  void singleTestCaseFail1Limit0ThrowsWhenNoAsserionErrorFunction() {
    final TestCounter fc = new TestCounter(0);
    Assertions.assertThrows(AssertionError.class, () -> {
      fc.run(() -> false, () -> {
        // EMPTY FUNCTION! It should throw an assertion error
      });
    });
  }

  // XXX Copy from here
  @Test
  void singleTestAssertLimit0Fail1Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestAssert(1, 0, true);
    });
  }

  @Test
  void singleTestAssertLimit1Fail1Allowed() {
    runTestAssert(1, 1, false);
  }

  @Test
  void singleTestAssertLimit1Fail2Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestAssert(1, 1, true);
    });
  }

  @Test
  void singleTestAssertLimit2Fail2Allowed() {
    runTestAssert(1, 2, false);
  }

  @Test
  void singleTestAssertLimit2Fail3Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestAssert(1, 2, true);
    });
  }

  @Test
  void multiTestAssertLimit0Fail1Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestAssert(2, 0, true);
    });
  }

  @Test
  void multiTestAssertLimit1Fail1Allowed() {
    runTestAssert(2, 1, false);
  }

  @Test
  void multiTestAssertLimit1Fail2Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestAssert(2, 1, true);
    });
  }

  @Test
  void multiTestAssertLimit2Fail2Allowed() {
    runTestAssert(2, 2, false);
  }

  @Test
  void multiTestAssertLimit2Fail3Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestAssert(2, 2, true);
    });
  }

  // XXX Copy to here
  @Test
  void singleTestCaseLimit0Fail1Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestCase(1, 0, true);
    });
  }

  @Test
  void singleTestCaseLimit1Fail1Allowed() {
    runTestCase(1, 1, false);
  }

  @Test
  void singleTestCaseLimit1Fail2Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestCase(1, 1, true);
    });
  }

  @Test
  void singleTestCaseLimit2Fail2Allowed() {
    runTestCase(1, 2, false);
  }

  @Test
  void singleTestCaseLimit2Fail3Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestCase(1, 2, true);
    });
  }

  @Test
  void multiTestCaseLimit0Fail1Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestCase(2, 0, true);
    });
  }

  @Test
  void multiTestCaseLimit1Fail1Allowed() {
    runTestCase(2, 1, false);
  }

  @Test
  void multiTestCaseLimit1Fail2Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestCase(2, 1, true);
    });
  }

  @Test
  void multiTestCaseLimit2Fail2Allowed() {
    runTestCase(2, 2, false);
  }

  @Test
  void multiTestCaseLimit2Fail3Throws() {
    Assertions.assertThrows(AssertionError.class, () -> {
      runTestCase(2, 2, true);
    });
  }
}

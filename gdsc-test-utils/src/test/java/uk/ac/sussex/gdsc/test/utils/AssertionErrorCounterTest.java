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

package uk.ac.sussex.gdsc.test.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class AssertionErrorCounterTest {
  private static final TestAssertion NO_ERROR = () -> {
    // No-op
  };
  private static final TestAssertion ERROR = () -> {
    throw new AssertionError();
  };

  @Test
  void testClassJavadoc() {
    AssertionErrorCounter c = new AssertionErrorCounter(2);
    c.run(() -> {
      assert 1 == 0;
    });
    c.run(() -> {
      assert 1 == 1;
    });
    c.run(() -> {
      assert 1 == 2;
    });
    // Throws an assertion error
    Assertions.assertThrows(AssertionError.class, () -> {
      c.run(() -> {
        assert 1 == 3;
      });
    });
  }

  @Test
  void canComputeFailureLimit() {
    final int size = 345;
    final double fraction = 0.789;
    final int e = (int) Math.floor(size * fraction);
    Assertions.assertEquals(e, AssertionErrorCounter.computeFailureLimit(size, fraction));
    // Edge cases
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      AssertionErrorCounter.computeFailureLimit(0, fraction);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      AssertionErrorCounter.computeFailureLimit(size, -0.1);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      AssertionErrorCounter.computeFailureLimit(size, 1.1);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      AssertionErrorCounter.computeFailureLimit(size, Double.NaN);
    });
  }

  @Test
  void canConstruct() {
    final int failLimit = 1;
    final int size = 1;
    Assertions.assertNotNull(new AssertionErrorCounter(failLimit));
    Assertions.assertNotNull(new AssertionErrorCounter(failLimit + 5));
    Assertions.assertNotNull(new AssertionErrorCounter(failLimit, size));
    Assertions.assertNotNull(new AssertionErrorCounter(failLimit, size + 5));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      final int size0 = 0;
      @SuppressWarnings("unused")
      final AssertionErrorCounter tc = new AssertionErrorCounter(failLimit, size0);
    });
  }

  @Test
  void badIndexThrows() {
    // All the following methods simulate a pass
    final AssertionErrorCounter tc1 = new AssertionErrorCounter(1);
    tc1.run(NO_ERROR);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      tc1.run(-1, NO_ERROR);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      tc1.run(1, NO_ERROR);
    });
    final AssertionErrorCounter tc2 = new AssertionErrorCounter(1, 2);
    tc2.run(0, NO_ERROR);
    tc2.run(1, NO_ERROR);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      tc2.run(-1, NO_ERROR);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      tc2.run(2, NO_ERROR);
    });
  }

  @Test
  void canRunTests() {
    final int failLimit = 0;
    final int size = 2;
    // All the following methods simulate a pass
    final AssertionErrorCounter tc1 = new AssertionErrorCounter(failLimit, size);
    tc1.run(NO_ERROR);
    final AssertionErrorCounter tc2 = new AssertionErrorCounter(failLimit, size);
    tc2.run(1, NO_ERROR);
  }

  @Test
  void canThrowAssertionError() {
    final int failLimit = 0;
    final int size = 2;
    // All the following methods simulate a failure
    Assertions.assertThrows(AssertionError.class, () -> {
      final AssertionErrorCounter tc = new AssertionErrorCounter(failLimit, size);
      tc.run(ERROR);
    });
    Assertions.assertThrows(AssertionError.class, () -> {
      final AssertionErrorCounter tc = new AssertionErrorCounter(failLimit, size);
      tc.run(1, ERROR);
    });
  }

  @Test
  void canSwallowAssertionError() {
    final int failLimit = 1;
    final int size = 2;
    // All the following methods simulate a failure.
    // Both indices can fail and it should be OK.
    final AssertionErrorCounter tc1 = new AssertionErrorCounter(failLimit, size);
    tc1.run(ERROR);
    tc1.run(1, ERROR);
  }
}

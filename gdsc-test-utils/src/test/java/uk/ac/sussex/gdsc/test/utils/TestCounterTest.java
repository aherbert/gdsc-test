/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 Alex Herbert
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

package uk.ac.sussex.gdsc.test.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class TestCounterTest {
  @Test
  public void canComputeFailureLimit() {
    final int size = 345;
    final double fraction = 0.789;
    final int e = (int) Math.floor(size * fraction);
    Assertions.assertEquals(e, TestCounter.computeFailureLimit(size, fraction));
    // Edge cases
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      TestCounter.computeFailureLimit(0, fraction);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      TestCounter.computeFailureLimit(size, -0.1);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      TestCounter.computeFailureLimit(size, 1.1);
    });
  }

  @Test
  public void canConstruct() {
    final int failLimit = 1;
    final int size = 1;
    Assertions.assertNotNull(new TestCounter(failLimit));
    Assertions.assertNotNull(new TestCounter(failLimit + 5));
    Assertions.assertNotNull(new TestCounter(failLimit, size));
    Assertions.assertNotNull(new TestCounter(failLimit, size + 5));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      final int size0 = 0;
      @SuppressWarnings("unused")
      final TestCounter tc = new TestCounter(failLimit, size0);
    });
  }

  @Test
  public void canRunTests() {
    final int failLimit = 0;
    final int size = 2;
    // All the following methods simulate a pass
    final TestCounter tc1 = new TestCounter(failLimit, size);
    tc1.run(() -> {
      // No failure
    });
    final TestCounter tc2 = new TestCounter(failLimit, size);
    tc2.run(1, () -> {
      // No failure
    });
    final TestCounter tc3 = new TestCounter(failLimit, size);
    tc3.run(() -> true, () -> {
      throw new AssertionError();
    });
    final TestCounter tc4 = new TestCounter(failLimit, size);
    tc4.run(1, () -> true, () -> {
      throw new AssertionError();
    });
  }

  @Test
  public void canThrowAssertionError() {
    final int failLimit = 0;
    final int size = 2;
    // All the following methods simulate a failure
    Assertions.assertThrows(AssertionError.class, () -> {
      final TestCounter tc = new TestCounter(failLimit, size);
      tc.run(() -> {
        throw new AssertionError();
      });
    });
    Assertions.assertThrows(AssertionError.class, () -> {
      final TestCounter tc = new TestCounter(failLimit, size);
      tc.run(1, () -> {
        throw new AssertionError();
      });
    });
    Assertions.assertThrows(AssertionError.class, () -> {
      final TestCounter tc = new TestCounter(failLimit, size);
      tc.run(() -> false, () -> {
        throw new AssertionError();
      });
    });
    Assertions.assertThrows(AssertionError.class, () -> {
      final TestCounter tc = new TestCounter(failLimit, size);
      tc.run(1, () -> false, () -> {
        throw new AssertionError();
      });
    });
  }

  @Test
  public void canSwallowAssertionError() {
    final int failLimit = 1;
    final int size = 2;
    // All the following methods simulate a failure.
    // Both indices can fail and it should be OK.
    final TestCounter tc1 = new TestCounter(failLimit, size);
    tc1.run(() -> {
      throw new AssertionError();
    });
    tc1.run(1, () -> {
      throw new AssertionError();
    });
    final TestCounter tc2 = new TestCounter(failLimit, size);
    tc2.run(() -> false, () -> {
      throw new AssertionError();
    });
    tc2.run(1, () -> false, () -> {
      throw new AssertionError();
    });
  }

  @Test
  public void canThrowDefaultAssertionErrorForTestAssertion() {
    final int failLimit = 0;
    final int size = 2;
    final TestCounter tc = new TestCounter(failLimit, size);
    Assertions.assertThrows(AssertionError.class, () -> {
      tc.run(1, () -> false, () -> {
        // Do nothing. The TestCounter will generate a default error.
      });
    });
  }
}

/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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

package uk.ac.sussex.gdsc.test.utils;

/**
 * Runs test assertions and counts any thrown {@link AssertionError}s. If the limit is exceeded then
 * the last generated error is thrown. Any other runtime exception will not be suppressed.
 *
 * <p>Use this class to fail tests that accumulate too many errors during random repeats, e.g.
 * {@code > 5} out of {@code 100}.
 * 
 * <p>The counter can count failures for tests identified by index allowing suppression of failures
 * in multiple tests execute in parallel. If any single test index exceeds the limit then the
 * assertion error will be thrown.
 *
 * <p>The class can be used with lambda functions, e.g.
 *
 * <pre>
 * TestCounter c = new TestCounter(2);
 * c.run(() -&gt; {
 *   assert 1 == 0;
 * });
 * c.run(() -&gt; {
 *   assert 1 == 1;
 * });
 * c.run(() -&gt; {
 *   assert 1 == 2;
 * });
 * // Throws an assertion error
 * c.run(() -&gt; {
 *   assert 1 == 3;
 * });
 * </pre>
 */
public class AssertionErrorCounter {
  /** The failure limit. */
  private final int failureLimit;

  /** The failures. */
  private final int[] failures;

  /**
   * Compute the failure limit.
   *
   * <pre>
   * return (int) Math.floor(size * fraction);
   * </pre>
   *
   * @param size the number of repeats
   * @param fraction the fraction of repeats that fail that will trigger an error
   * @return the failure limit
   * @throws IllegalArgumentException if fraction is not in the range 0-1 or size is not positive
   */
  public static int computeFailureLimit(int size, double fraction) {
    if (size <= 0) {
      throw new IllegalArgumentException("Size must be strictly positive: " + size);
    }
    if (!(fraction >= 0 && fraction <= 1)) {
      throw new IllegalArgumentException("Fraction must be in the range 0-1");
    }
    return (int) Math.floor(size * fraction);
  }

  /**
   * Creates a new counter.
   *
   * @param failureLimit the failure limit that will generate an AssertionError to be thrown
   */
  public AssertionErrorCounter(int failureLimit) {
    this(failureLimit, 1);
  }

  /**
   * Creates a new counter with a specified number of different tests to monitor.
   *
   * @param failureLimit the failure limit that will generate an {@link AssertionError} to be thrown
   * @param size the number of different tests to be addressed by index in
   *        {@link #run(int, TestAssertion)}
   */
  public AssertionErrorCounter(int failureLimit, int size) {
    this.failureLimit = failureLimit;
    if (size <= 0) {
      throw new IllegalArgumentException("Size must be strictly positive: " + size);
    }
    failures = new int[size];
  }

  /**
   * Run the test (assuming test index 0).
   *
   * @param test the test
   * @throws AssertionError the assertion error if the failure limit has been exceeded
   */
  public void run(TestAssertion test) {
    runTest(0, test);
  }

  /**
   * Run the test.
   *
   * @param index the test index
   * @param test the test
   * @throws IllegalArgumentException If the test index is invalid
   * @throws AssertionError the assertion error if the failure limit has been exceeded
   */
  public void run(int index, TestAssertion test) {
    if (index < 0 || index >= failures.length) {
      throw new IllegalArgumentException("Invalid index : " + index + " / " + failures.length);
    }
    runTest(index, test);
  }

  /**
   * Run the test.
   *
   * @param index the test index
   * @param test the test
   * @throws AssertionError the assertion error if the failure limit has been exceeded
   */
  private void runTest(int index, TestAssertion test) {
    try {
      test.test();
    } catch (final AssertionError ex) {
      if (failures[index] >= failureLimit) {
        throw ex;
      }
      failures[index]++;
    }
  }
}

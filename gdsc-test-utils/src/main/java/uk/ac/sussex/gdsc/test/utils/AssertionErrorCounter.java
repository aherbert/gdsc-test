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
   * @throws IllegalArgumentException if fraction is not in the range [0, 1] or size is not positive
   */
  public static int computeFailureLimit(int size, double fraction) {
    if (size <= 0) {
      throw new IllegalArgumentException("Size must be strictly positive: " + size);
    }
    if (!(fraction >= 0 && fraction <= 1)) {
      throw new IllegalArgumentException("Fraction must be in the range [0, 1]");
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
   * @throws IllegalArgumentException if the test index is invalid
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

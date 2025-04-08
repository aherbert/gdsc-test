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

/**
 * The test complexity. Lower complexity tests are assumed to be faster.
 */
public enum TestComplexity {
  /** No complexity. */
  NONE(0),
  /** Low complexity. */
  LOW(1000),
  /** Medium complexity. */
  MEDIUM(2000),
  /** High complexity. */
  HIGH(3000),
  /** Very high complexity. */
  VERY_HIGH(4000),
  /** Maximum. Used to run any test that checks complexity settings */
  MAXIMUM(Integer.MAX_VALUE);

  /** The value. */
  private final int value;

  /**
   * Create a new test complexity.
   *
   * @param value the value
   */
  TestComplexity(int value) {
    this.value = value;
  }

  /**
   * Gets the value.
   *
   * @return the value
   */
  public int getValue() {
    return value;
  }
}

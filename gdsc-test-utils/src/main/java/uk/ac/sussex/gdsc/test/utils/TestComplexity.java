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

/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with JUnit 5.
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

package uk.ac.sussex.gdsc.test.junit5;

import uk.ac.sussex.gdsc.test.utils.SeedUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Provides random seeds for a repeated test.
 *
 * <p>This class is immutable.
 */
public class RandomSeed implements Serializable {
  /**
   * The serial version ID.
   */
  private static final long serialVersionUID = -7778354090180601732L;

  /** The seed. */
  private final byte[] seed;

  /** The current repetition. */
  private final int currentRepetition;

  /** The total repetitions. */
  private final int totalRepetitions;

  /** The hash code. */
  private int hash;

  /** The seed as a long. */
  private long seedAsLong;

  /**
   * Instantiates a new random seed.
   *
   * @param seed the seed
   * @param currentRepetition the current repetition
   * @param totalRepetitions the total repetitions
   * @throws IllegalArgumentException If {@code totalRepitions < 1} or {@code currentRepetition < 1}
   *         or {@code currentRepetition > totalRepetitions}
   */
  public RandomSeed(byte[] seed, int currentRepetition, int totalRepetitions)
      throws IllegalArgumentException {
    this.seed = Objects.requireNonNull(seed, "The seed must not be null").clone();
    if (totalRepetitions <= 0) {
      throw new IllegalArgumentException("Total repetitions must be strictly positive");
    }
    if (currentRepetition <= 0) {
      throw new IllegalArgumentException("Current repetition must be strictly positive");
    }
    if (currentRepetition > totalRepetitions) {
      throw new IllegalArgumentException("Current repetition is above total repetitions");
    }
    this.currentRepetition = currentRepetition;
    this.totalRepetitions = totalRepetitions;
  }

  /**
   * Gets the seed.
   *
   * @return the seed
   */
  public byte[] getSeed() {
    return seed.clone();
  }

  /**
   * Gets the seed as a single long value.
   *
   * <p>The seed is created using all of the information in the byte seed.
   *
   * @return the seed as a long
   */
  public long getSeedAsLong() {
    long result = seedAsLong;
    if (result == 0) {
      result = SeedUtils.makeLong(seed);
      seedAsLong = result;
    }
    return result;
  }

  /**
   * Gets the current repetition.
   *
   * @return the current repetition
   */
  public int getCurrentRepetition() {
    return currentRepetition;
  }

  /**
   * Gets the total repetitions.
   *
   * @return the total repetitions
   */
  public int getTotalRepetitions() {
    return totalRepetitions;
  }

  /**
   * Returns a hash code value for the object.
   *
   * <p>The hashcode is computed using the components of the seed that contribute to randomness. The
   * repetition counts are ignored.
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int result = hash;
    if (result == 0) {
      // Starting at 1 ensures* a non-zero result when the seed is zero length,
      // or full of empty (zero) bytes. It also makes different length zero byte
      // arrays have a different hash code.
      // *Note: A zero could occur by randomness.
      result = 1;
      for (final byte bi : seed) {
        result = 31 * result + bi;
      }
      hash = result;
    }
    return result;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Indicates whether some other object is "equal to" this one.
   *
   * <p>Equality is computed using the components of the seed that contribute to randomness. The
   * repetition counts are ignored.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    // self check
    if (this == obj) {
      return true;
    }
    // null check
    if (obj == null) {
      return false;
    }
    // type check and cast
    if (getClass() != obj.getClass()) {
      return false;
    }
    // field comparison
    return equalBytes((RandomSeed) obj);
  }

  /**
   * Check equality using only the seed bytes.
   *
   * @param otherSeed the other seed
   * @return true, if successful
   */
  public boolean equalBytes(RandomSeed otherSeed) {
    return equalBytes(otherSeed.seed);
  }

  /**
   * Check equality using only the seed bytes.
   *
   * @param bytes the bytes
   * @return true, if successful
   */
  public boolean equalBytes(byte[] bytes) {
    return equalBytes(seed, bytes);
  }

  /**
   * Returns <tt>true</tt> if the two specified arrays of bytes are <i>equal</i> to one another. Two
   * arrays are considered equal if both arrays contain the same number of elements, and all
   * corresponding pairs of elements in the two arrays are equal. In other words, two arrays are
   * equal if they contain the same elements in the same order.
   *
   * @param b1 one array to be tested for equality
   * @param b2 the other array to be tested for equality
   * @return <tt>true</tt> if the two arrays are equal
   */
  static boolean equalBytes(byte[] b1, byte[] b2) {
    // Copied from Arrays.equals but removed the check for null arrays.
    // The seed constructor ensures the arrays are not null and are final.
    final int length = b1.length;
    if (b2.length != length) {
      return false;
    }

    for (int i = 0; i < length; i++) {
      if (b1[i] != b2[i]) {
        return false;
      }
    }

    return true;
  }
}

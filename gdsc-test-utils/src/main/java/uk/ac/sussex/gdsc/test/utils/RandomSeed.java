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

package uk.ac.sussex.gdsc.test.utils;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * Provides random seeds for a test.
 *
 * <p>This class is immutable.
 */
public final class RandomSeed implements Serializable, Supplier<byte[]>, LongSupplier {
  /**
   * The serial version ID.
   */
  private static final long serialVersionUID = -7778354090180601732L;

  /** The seed. */
  private final byte[] seed;

  /** The hash code. */
  private int hash;

  /** The seed as a long. */
  private long seedAsLong;

  /**
   * Creates a new random seed.
   *
   * @param seed the seed
   */
  private RandomSeed(byte[] seed) {
    this.seed = Objects.requireNonNull(seed, "The seed must not be null").clone();
  }

  /**
   * Creates a new random seed.
   *
   * @param seed the seed
   * @return the random seed
   */
  public static RandomSeed of(byte[] seed) {
    return new RandomSeed(seed);
  }

  /**
   * Gets the seed.
   *
   * @return the seed
   */
  @Override
  public byte[] get() {
    return seed.clone();
  }

  /**
   * Gets the seed as a single long value.
   *
   * <p>The seed is created using all of the information in the byte seed.
   *
   * @return the seed as a long
   */
  @Override
  public long getAsLong() {
    long result = seedAsLong;
    if (result == 0) {
      result = RandomSeeds.makeLong(seed);
      seedAsLong = result;
    }
    return result;
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
   * <p>Equality is computed using the seed bytes.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    // self check
    if (this == obj) {
      return true;
    }
    // null check and type check (this class is final so no sub-classes are possible)
    if (!(obj instanceof RandomSeed)) {
      return false;
    }
    // field comparison
    return equalBytes(seed, ((RandomSeed) obj).seed);
  }

  /**
   * Check equality using only the seed bytes.
   *
   * <p>Note: This random seed will not have a null array. If the argument bytes are null then this
   * is equal only if the random seed bytes are zero length.
   *
   * @param bytes the bytes
   * @return true, if successful
   */
  public boolean equalBytes(byte[] bytes) {
    if (bytes == null) {
      return seed.length == 0;
    }
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

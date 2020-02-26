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

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import uk.ac.sussex.gdsc.test.utils.SeedUtils;

@SuppressWarnings("javadoc")
public class RandomSeedTest {
  @SuppressWarnings("unused")
  @Test
  public void testConstructor() {
    final byte[] seed = {1, 2, 3, 4, 5};
    final int currentRepetition = 1678;
    final int totalRepetitions = 579797;

    final RandomSeed rs = new RandomSeed(seed, currentRepetition, totalRepetitions);
    Assertions.assertNotSame(seed, rs.getSeed(), "Seed reference");
    Assertions.assertArrayEquals(seed, rs.getSeed(), "Seed");
    Assertions.assertEquals(currentRepetition, rs.getCurrentRepetition(), "Current repetition");
    Assertions.assertEquals(totalRepetitions, rs.getTotalRepetitions(), "Total repetitions");

    Assertions.assertThrows(NullPointerException.class, () -> {
      new RandomSeed(null, currentRepetition, totalRepetitions);
    }, "Null seed");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new RandomSeed(seed, 0, totalRepetitions);
    }, "Zero current repetition");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new RandomSeed(seed, 1, 0);
    }, "Zero total repetitions");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new RandomSeed(seed, totalRepetitions + 1, totalRepetitions);
    }, "Current repetitions above total repetitions");
  }

  @Test
  public void testEquals() {
    final byte[] seed = {1, 2, 3, 4, 5};
    final int currentRepetition = 1678;
    final int totalRepetitions = 579797;

    final RandomSeed rs1 = new RandomSeed(seed, currentRepetition, totalRepetitions);
    Assertions.assertEquals(rs1, rs1, "Not equals to the same seed");
    Assertions.assertEquals(rs1.hashCode(), rs1.hashCode(), "Hashcode not same with the same seed");
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertEquals(rs1, null);
    }, "Equals null");
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertEquals(rs1, new Object());
    }, "Equals another object");

    // Same seed
    final RandomSeed rs2 = new RandomSeed(seed, currentRepetition, totalRepetitions);
    Assertions.assertEquals(rs1, rs2, "Not equals to a duplicate seed");
    Assertions.assertEquals(rs1.hashCode(), rs2.hashCode(),
        "Hashcode not same as a duplicate seed");

    // Different seed
    seed[0]++;
    final RandomSeed rs3 = new RandomSeed(seed, currentRepetition, totalRepetitions);
    Assertions.assertNotEquals(rs1, rs3, "Equals a different seed");
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertEquals(rs1.hashCode(), rs3.hashCode());
    }, "Hashcode same as a different seed");
  }

  @Test
  public void testHashCode() {
    final int currentRepetition = 1678;
    final int totalRepetitions = 579797;

    // Zero length seed
    final RandomSeed rs1 = new RandomSeed(new byte[0], currentRepetition, totalRepetitions);
    Assertions.assertFalse(rs1.hashCode() == 0, "Hashcode is zero for zero length seed");

    // Length 1 seed
    final RandomSeed rs2 = new RandomSeed(new byte[1], currentRepetition, totalRepetitions);
    Assertions.assertFalse(rs2.hashCode() == 0, "Hashcode is zero for length 1 seed");

    Assertions.assertFalse(rs1.hashCode() == rs2.hashCode(),
        "Hashcode is same for different length zero filled seeds");
  }

  @Test
  public void testGetSeedAsLong() {
    final int currentRepetition = 1;
    final int totalRepetitions = 1;

    // The long value should be the same a long converted to a byte array
    final UniformRandomProvider rng = RandomSource.create(RandomSource.SPLIT_MIX_64);
    long value = rng.nextLong();
    for (int i = 0; i < 5; i++) {
      RandomSeed rs =
          new RandomSeed(SeedUtils.makeByteArray(value), currentRepetition, totalRepetitions);
      Assertions.assertEquals(value, rs.getSeedAsLong(),
          "Single long converted to byte[] doesn't match");
      // Test the cache of the value
      Assertions.assertEquals(rs.getSeedAsLong(), rs.getSeedAsLong(),
          "Seed as long value doesn't match itself");
      long value2 = value;
      value = rng.nextLong();
      rs = new RandomSeed(SeedUtils.makeByteArray(value, value2), currentRepetition,
          totalRepetitions);
      Assertions.assertNotEquals(value, rs.getSeedAsLong(),
          "Two longs converted to byte[] matches the first long value");
    }
  }
}

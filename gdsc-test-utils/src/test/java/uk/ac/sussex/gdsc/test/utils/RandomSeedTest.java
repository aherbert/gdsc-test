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

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

@SuppressWarnings("javadoc")
class RandomSeedTest {
  @SuppressWarnings("unused")
  @Test
  void testConstructor() {
    final byte[] seed = {1, 2, 3, 4, 5};
    final int currentRepetition = 1678;
    final int totalRepetitions = 579797;

    final RandomSeed rs = RandomSeed.of(seed);
    Assertions.assertNotSame(seed, rs.get(), "Seed reference");
    Assertions.assertArrayEquals(seed, rs.get(), "Seed");

    Assertions.assertThrows(NullPointerException.class, () -> {
      RandomSeed.of(null);
    }, "Null seed");
  }

  @Test
  void testEquals() {
    final byte[] seed = {1, 2, 3, 4, 5};

    final RandomSeed rs1 = RandomSeed.of(seed);
    Assertions.assertEquals(rs1, rs1, "Not equals to the same seed");
    Assertions.assertEquals(rs1.hashCode(), rs1.hashCode(), "Hashcode not same with the same seed");
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertEquals(rs1, null);
    }, "Equals null");
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertEquals(rs1, new Object());
    }, "Equals another object");

    // Same seed
    final RandomSeed rs2 = RandomSeed.of(seed);
    Assertions.assertEquals(rs1, rs2, "Not equals to a duplicate seed");
    Assertions.assertEquals(rs1.hashCode(), rs2.hashCode(),
        "Hashcode not same as a duplicate seed");

    // Different seed
    seed[0]++;
    final RandomSeed rs3 = RandomSeed.of(seed);
    Assertions.assertNotEquals(rs1, rs3, "Equals a different seed");
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertEquals(rs1.hashCode(), rs3.hashCode());
    }, "Hashcode same as a different seed");
  }

  @Test
  void testEqualBytes() {
    final byte[] seed0 = {};
    final byte[] seed1 = {1, 2, 3, 4, 5};
    final RandomSeed rs0 = RandomSeed.of(seed0);
    final RandomSeed rs1 = RandomSeed.of(seed1);
    Assertions.assertTrue(rs0.equalBytes(seed0));
    Assertions.assertFalse(rs0.equalBytes(seed1));
    Assertions.assertTrue(rs0.equalBytes(null));
    Assertions.assertFalse(rs1.equalBytes(seed0));
    Assertions.assertTrue(rs1.equalBytes(seed1));
    Assertions.assertFalse(rs1.equalBytes(null));
  }


  @Test
  void testHashCode() {
    // Zero length seed
    final RandomSeed rs1 = RandomSeed.of(new byte[0]);
    Assertions.assertFalse(rs1.hashCode() == 0, "Hashcode is zero for zero length seed");

    // Length 1 seed
    final RandomSeed rs2 = RandomSeed.of(new byte[1]);
    Assertions.assertFalse(rs2.hashCode() == 0, "Hashcode is zero for length 1 seed");

    Assertions.assertFalse(rs1.hashCode() == rs2.hashCode(),
        "Hashcode is same for different length zero filled seeds");
  }

  @Test
  void testgetAsLong() {
    // The long value should be the same a long converted to a byte array
    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();
    long value = rng.nextLong();
    for (int i = 0; i < 5; i++) {
      RandomSeed rs = RandomSeed.of(SeedUtils.makeByteArray(value));
      Assertions.assertEquals(value, rs.getAsLong(),
          "Single long converted to byte[] doesn't match");
      // Test the cache of the value
      Assertions.assertEquals(rs.getAsLong(), rs.getAsLong(),
          "Seed as long value doesn't match itself");
      long value2 = value;
      value = rng.nextLong();
      rs = RandomSeed.of(SeedUtils.makeByteArray(value, value2));
      Assertions.assertNotEquals(value, rs.getAsLong(),
          "Two longs converted to byte[] matches the first long value");
    }
  }
}

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
      RandomSeed rs = RandomSeed.of(RandomSeeds.makeByteArray(value));
      Assertions.assertEquals(value, rs.getAsLong(),
          "Single long converted to byte[] doesn't match");
      // Test the cache of the value
      Assertions.assertEquals(rs.getAsLong(), rs.getAsLong(),
          "Seed as long value doesn't match itself");
      long value2 = value;
      value = rng.nextLong();
      rs = RandomSeed.of(RandomSeeds.makeByteArray(value, value2));
      Assertions.assertNotEquals(value, rs.getAsLong(),
          "Two longs converted to byte[] matches the first long value");
    }
  }
}

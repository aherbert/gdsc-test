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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class RandomSeedsTest {
  @Test
  void testZeroBytes() {
    Assertions.assertTrue(RandomSeeds.zeroBytes(null), "Null should be empty");
    Assertions.assertTrue(RandomSeeds.zeroBytes(new byte[0]), "byte[0] should be empty");
    Assertions.assertTrue(RandomSeeds.zeroBytes(new byte[10]), "byte[10] should be empty");
    Assertions.assertFalse(RandomSeeds.zeroBytes(new byte[] {1, 2, 3}),
        "filled byte[] should not be empty");
  }

  @Test
  void testNullOrEmpty() {
    Assertions.assertTrue(RandomSeeds.nullOrEmpty(null), "Null should be null or empty");
    Assertions.assertTrue(RandomSeeds.nullOrEmpty(new byte[0]), "byte[0] should be null or empty");
    Assertions.assertFalse(RandomSeeds.nullOrEmpty(new byte[10]),
        "byte[10] should be not null or empty");
  }

  @Test
  void testGenerateSeed() {
    final int size = 17;
    final byte[] seed1 = RandomSeeds.generateSeed(size);
    final byte[] seed2 = RandomSeeds.generateSeed(size);
    testSeeds(size, seed1, seed2);
  }

  @Test
  void testGenerateNonSecureSeed() {
    final int size = 17;
    final byte[] seed1 = RandomSeeds.generateSeed(size, false);
    final byte[] seed2 = RandomSeeds.generateSeed(size, false);
    testSeeds(size, seed1, seed2);
  }

  private static void testSeeds(int size, byte[] seed1, byte[] seed2) {
    Assertions.assertEquals(size, seed1.length, "Seed 1 length");
    Assertions.assertEquals(size, seed2.length, "Seed 2 length");
    Assertions.assertThrows(AssertionError.class, () -> {
      Assertions.assertArrayEquals(seed1, seed2);
    }, "Seeds should not be equal");
  }

  @Test
  void testMakeByteArrayFromLong() throws IOException {
    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();
    final int size = 5;
    final long[] values = new long[size];
    for (int i = 0; i < size; i++) {
      values[i] = rng.nextLong();
    }

    // Write to a byte array
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (DataOutputStream out = new DataOutputStream(baos)) {
      for (int i = 0; i < size; i++) {
        out.writeLong(values[i]);
      }
    }

    final byte[] expected = baos.toByteArray();
    final byte[] actual = RandomSeeds.makeByteArray(values);

    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  void testMakeByteArrayFromInt() throws IOException {
    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();
    final int size = 5;
    final int[] values = new int[size];
    for (int i = 0; i < size; i++) {
      values[i] = rng.nextInt();
    }

    // Write to a byte array
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (DataOutputStream out = new DataOutputStream(baos)) {
      for (int i = 0; i < size; i++) {
        out.writeInt(values[i]);
      }
    }

    final byte[] expected = baos.toByteArray();
    final byte[] actual = RandomSeeds.makeByteArray(values);

    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  void testMakeLongArray() {
    // This test assumes that the method to make the byte array works.
    // It is tested verses the DataOutputStream in a separate test.

    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();
    final int size = 5;
    final long[] values = new long[size];
    for (int i = 0; i < size; i++) {
      values[i] = rng.nextLong();
    }

    byte[] bytes = RandomSeeds.makeByteArray(values);
    long[] actual = RandomSeeds.makeLongArray(bytes);

    Assertions.assertArrayEquals(values, actual, "Full size array is different");

    // Test truncation
    long mask = 0xFFFFFFFFFFFFFFFFL;
    final int end = size - 1;
    for (int i = 1; i < Long.BYTES; i++) {
      // Remove the final byte
      bytes = Arrays.copyOf(bytes, bytes.length - 1);
      // Remove the bits from the final value
      mask <<= Byte.SIZE;
      values[end] &= mask;

      actual = RandomSeeds.makeLongArray(bytes);
      final int nBytes = i;
      Assertions.assertArrayEquals(values, actual,
          () -> nBytes + " byte trucated array is different");
    }
  }

  @Test
  void testMakeIntArray() {
    // This test assumes that the method to make the byte array works.
    // It is tested verses the DataOutputStream in a separate test.

    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();
    final int size = 5;
    final int[] values = new int[size];
    for (int i = 0; i < size; i++) {
      values[i] = rng.nextInt();
    }

    byte[] bytes = RandomSeeds.makeByteArray(values);
    int[] actual = RandomSeeds.makeIntArray(bytes);

    Assertions.assertArrayEquals(values, actual, "Full size array is different");

    // Test truncation
    long mask = 0xFFFFFFFF;
    final int end = size - 1;
    for (int i = 1; i < Integer.BYTES; i++) {
      // Remove the final byte
      bytes = Arrays.copyOf(bytes, bytes.length - 1);
      // Remove the bits from the final value
      mask <<= Byte.SIZE;
      values[end] &= mask;

      actual = RandomSeeds.makeIntArray(bytes);
      final int nBytes = i;
      Assertions.assertArrayEquals(values, actual,
          () -> nBytes + " byte trucated array is different");
    }
  }

  @Test
  void testMakeLong() {
    Assertions.assertEquals(0L, RandomSeeds.makeLong(), "No bytes should be zero");

    // Check it is most significant first
    final byte allBits = (byte) 0xFF;
    final byte zeroBits = 0;
    long expected = 0xFF00000000000000L;

    Assertions.assertEquals(expected, RandomSeeds.makeLong(allBits), "1 byte input (0xFF)");
    Assertions.assertEquals(expected, RandomSeeds.makeLong(allBits, zeroBits),
        "2 byte input (0xFF00)");

    expected >>>= Byte.SIZE;
    Assertions.assertEquals(expected, RandomSeeds.makeLong(zeroBits, allBits),
        "2 byte input (0x00FF)");

    // Test a full size long (8 bytes)
    expected = 0xFFL;
    Assertions.assertEquals(expected, RandomSeeds.makeLong(zeroBits, zeroBits, zeroBits, zeroBits,
        zeroBits, zeroBits, zeroBits, allBits), "8 byte input (0x00000000000000FF)");
  }
}

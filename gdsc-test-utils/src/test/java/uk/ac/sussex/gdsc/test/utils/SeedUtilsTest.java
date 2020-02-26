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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class SeedUtilsTest {
  @Test
  public void testZeroBytes() {
    Assertions.assertTrue(SeedUtils.zeroBytes(null), "Null should be empty");
    Assertions.assertTrue(SeedUtils.zeroBytes(new byte[0]), "byte[0] should be empty");
    Assertions.assertTrue(SeedUtils.zeroBytes(new byte[10]), "byte[10] should be empty");
    Assertions.assertFalse(SeedUtils.zeroBytes(new byte[] {1, 2, 3}),
        "filled byte[] should not be empty");
  }

  @Test
  public void testNullOrEmpty() {
    Assertions.assertTrue(SeedUtils.nullOrEmpty(null), "Null should be null or empty");
    Assertions.assertTrue(SeedUtils.nullOrEmpty(new byte[0]), "byte[0] should be null or empty");
    Assertions.assertFalse(SeedUtils.nullOrEmpty(new byte[10]),
        "byte[10] should be not null or empty");
  }

  @Test
  public void testGenerateSeed() {
    final int size = 17;
    final byte[] seed1 = SeedUtils.generateSeed(size);
    final byte[] seed2 = SeedUtils.generateSeed(size);
    testSeeds(size, seed1, seed2);
  }

  @Test
  public void testGenerateNonSecureSeed() {
    final int size = 17;
    final byte[] seed1 = SeedUtils.generateSeed(size, false);
    final byte[] seed2 = SeedUtils.generateSeed(size, false);
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
  public void testMakeByteArrayFromLong() throws IOException {
    final UniformRandomProvider rng = RandomSource.create(RandomSource.SPLIT_MIX_64);
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
    final byte[] actual = SeedUtils.makeByteArray(values);

    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testMakeByteArrayFromInt() throws IOException {
    final UniformRandomProvider rng = RandomSource.create(RandomSource.SPLIT_MIX_64);
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
    final byte[] actual = SeedUtils.makeByteArray(values);

    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  public void testMakeLongArray() {
    // This test assumes that the method to make the byte array works.
    // It is tested verses the DataOutputStream in a separate test.

    final UniformRandomProvider rng = RandomSource.create(RandomSource.SPLIT_MIX_64);
    final int size = 5;
    final long[] values = new long[size];
    for (int i = 0; i < size; i++) {
      values[i] = rng.nextLong();
    }

    byte[] bytes = SeedUtils.makeByteArray(values);
    long[] actual = SeedUtils.makeLongArray(bytes);

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

      actual = SeedUtils.makeLongArray(bytes);
      final int nBytes = i;
      Assertions.assertArrayEquals(values, actual,
          () -> nBytes + " byte trucated array is different");
    }
  }

  @Test
  public void testMakeIntArray() {
    // This test assumes that the method to make the byte array works.
    // It is tested verses the DataOutputStream in a separate test.

    final UniformRandomProvider rng = RandomSource.create(RandomSource.SPLIT_MIX_64);
    final int size = 5;
    final int[] values = new int[size];
    for (int i = 0; i < size; i++) {
      values[i] = rng.nextInt();
    }

    byte[] bytes = SeedUtils.makeByteArray(values);
    int[] actual = SeedUtils.makeIntArray(bytes);

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

      actual = SeedUtils.makeIntArray(bytes);
      final int nBytes = i;
      Assertions.assertArrayEquals(values, actual,
          () -> nBytes + " byte trucated array is different");
    }
  }

  @Test
  public void testMakeLong() {
    Assertions.assertEquals(0L, SeedUtils.makeLong(), "No bytes should be zero");

    // Check it is most significant first
    final byte allBits = (byte) 0xFF;
    final byte zeroBits = 0;
    long expected = 0xFF00000000000000L;

    Assertions.assertEquals(expected, SeedUtils.makeLong(allBits), "1 byte input (0xFF)");
    Assertions.assertEquals(expected, SeedUtils.makeLong(allBits, zeroBits),
        "2 byte input (0xFF00)");

    expected >>>= Byte.SIZE;
    Assertions.assertEquals(expected, SeedUtils.makeLong(zeroBits, allBits),
        "2 byte input (0x00FF)");

    // Test a full size long (8 bytes)
    expected = 0xFFL;
    Assertions.assertEquals(expected, SeedUtils.makeLong(zeroBits, zeroBits, zeroBits, zeroBits,
        zeroBits, zeroBits, zeroBits, allBits), "8 byte input (0x00000000000000FF)");
  }
}

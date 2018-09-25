/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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
package uk.ac.sussex.gdsc.test.utils;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class HexUtilsTest {

  @Test
  public void testEncodeWithBadInput() {
    final String EMPTY = "";
    Assertions.assertEquals(EMPTY, HexUtils.encodeHexString(null), "Null input");
    Assertions.assertEquals(EMPTY, HexUtils.encodeHexString(new byte[0]), "Empty input");
  }

  @Test
  public void testDecodeWithBadInput() {
    final byte[] EMPTY = new byte[0];
    Assertions.assertArrayEquals(EMPTY, HexUtils.decodeHex(null), "Null input");
    Assertions.assertArrayEquals(EMPTY, HexUtils.decodeHex(""), "Empty input");
    Assertions.assertArrayEquals(EMPTY, HexUtils.decodeHex("j"), "Bad single chaarcter");
    Assertions.assertArrayEquals(EMPTY, HexUtils.decodeHex("abcsfp678"),
        "Bad single character in larger string");
  }

  @Test
  public void testEncode() {
    UniformRandomProvider rng = RandomSource.create(RandomSource.MWC_256);
    final boolean toLowerCase = true;
    for (int i = 1; i < 20; i++) {
      final byte[] bytes = new byte[i];
      for (int j = 0; j < 5; j++) {
        rng.nextBytes(bytes);
        String expected = Hex.encodeHexString(bytes, toLowerCase);
        String actual = HexUtils.encodeHexString(bytes);
        Assertions.assertEquals(expected, actual, "Bad encoding");
      }
    }
  }

  @Test
  public void testDecode() {
    UniformRandomProvider rng = RandomSource.create(RandomSource.MWC_256);
    final boolean toLowerCase = true;
    /** Output Hex characters. */
    final char[] HEX_DIGITS =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    for (int i = 1; i < 20; i++) {
      final byte[] bytes = new byte[i];
      for (int j = 0; j < 5; j++) {
        rng.nextBytes(bytes);
        String hex = Hex.encodeHexString(bytes, toLowerCase);
        byte[] actual = HexUtils.decodeHex(hex);
        Assertions.assertArrayEquals(bytes, actual, "Bad decoding");

        // Test with odd length string. It should be the same as if it had a '0' on the end.
        StringBuilder sb = new StringBuilder(hex);
        sb.append(HEX_DIGITS[rng.nextInt(16)]);

        byte[] padded = HexUtils.decodeHex(sb);
        sb.append('0');
        byte[] unpadded = HexUtils.decodeHex(sb);
        Assertions.assertArrayEquals(padded, unpadded, "Bad padding");

        // Check against original
        byte[] clipped = Arrays.copyOf(padded, bytes.length);
        Assertions.assertArrayEquals(bytes, clipped, "Bad decoding after padding");
      }
    }
  }
}

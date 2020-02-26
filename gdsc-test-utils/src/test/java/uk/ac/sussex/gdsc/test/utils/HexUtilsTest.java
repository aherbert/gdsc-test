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
    final String empty = "";
    Assertions.assertEquals(empty, HexUtils.encodeHexString(null), "Null input");
    Assertions.assertEquals(empty, HexUtils.encodeHexString(new byte[0]), "Empty input");
  }

  @Test
  public void testDecodeWithBadInput() {
    final byte[] empty = new byte[0];
    Assertions.assertArrayEquals(empty, HexUtils.decodeHex(null), "Null input");
    Assertions.assertArrayEquals(empty, HexUtils.decodeHex(""), "Empty input");
    Assertions.assertArrayEquals(empty, HexUtils.decodeHex("j"), "Bad single chaarcter");
    Assertions.assertArrayEquals(empty, HexUtils.decodeHex("abcsfp678"),
        "Bad single character in larger string");
  }

  @Test
  public void testEncode() {
    final UniformRandomProvider rng = RandomSource.create(RandomSource.MWC_256);
    final boolean toLowerCase = true;
    for (int i = 1; i < 20; i++) {
      final byte[] bytes = new byte[i];
      for (int j = 0; j < 5; j++) {
        rng.nextBytes(bytes);
        final String expected = Hex.encodeHexString(bytes, toLowerCase);
        final String actual = HexUtils.encodeHexString(bytes);
        Assertions.assertEquals(expected, actual, "Bad encoding");
      }
    }
  }

  @Test
  public void testDecode() {
    final UniformRandomProvider rng = RandomSource.create(RandomSource.MWC_256);
    final boolean toLowerCase = true;
    // Output Hex characters.
    final char[] hexDigits =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    for (int i = 1; i < 20; i++) {
      final byte[] bytes = new byte[i];
      for (int j = 0; j < 5; j++) {
        rng.nextBytes(bytes);
        final String hex = Hex.encodeHexString(bytes, toLowerCase);
        final byte[] actual = HexUtils.decodeHex(hex);
        Assertions.assertArrayEquals(bytes, actual, "Bad decoding");

        // Test with odd length string. It should be the same as if it had a '0' on the end.
        final StringBuilder sb = new StringBuilder(hex);
        sb.append(hexDigits[rng.nextInt(16)]);

        final byte[] padded = HexUtils.decodeHex(sb);
        sb.append('0');
        final byte[] unpadded = HexUtils.decodeHex(sb);
        Assertions.assertArrayEquals(padded, unpadded, "Bad padding");

        // Check against original
        final byte[] clipped = Arrays.copyOf(padded, bytes.length);
        Assertions.assertArrayEquals(bytes, clipped, "Bad decoding after padding");
      }
    }
  }
}

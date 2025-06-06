/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 - 2025 Alex Herbert
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

import java.util.Arrays;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class HexTest {

  @Test
  void testEncodeWithBadInput() {
    final String empty = "";
    Assertions.assertEquals(empty, Hex.encodeAsString(null), "Null input");
    Assertions.assertEquals(empty, Hex.encodeAsString(new byte[0]), "Empty input");
  }

  @Test
  void testDecodeWithBadInput() {
    final byte[] empty = new byte[0];
    Assertions.assertArrayEquals(empty, Hex.decode(null), "Null input");
    Assertions.assertArrayEquals(empty, Hex.decode(""), "Empty input");
    Assertions.assertArrayEquals(empty, Hex.decode("j"), "Bad single character");
    Assertions.assertArrayEquals(empty, Hex.decode("/0123456789ABCDEFabcdef"), "Bad char");
    Assertions.assertArrayEquals(empty, Hex.decode("0123456789:ABCDEFabcdef"), "Bar char");
    Assertions.assertArrayEquals(empty, Hex.decode("0123456789@ABCDEFabcdef"), "Bar char");
    Assertions.assertArrayEquals(empty, Hex.decode("0123456789ABCDEFGabcdef"), "Bar char");
    Assertions.assertArrayEquals(empty, Hex.decode("0123456789ABCDEF`abcdef"), "Bar char");
    Assertions.assertArrayEquals(empty, Hex.decode("0123456789ABCDEFabcdefg"), "Bar char");
  }

  @Test
  void testEncode() {
    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();
    final boolean toLowerCase = true;
    for (int i = 1; i < 20; i++) {
      final byte[] bytes = new byte[i];
      for (int j = 0; j < 5; j++) {
        rng.nextBytes(bytes);
        final String expected =
            org.apache.commons.codec.binary.Hex.encodeHexString(bytes, toLowerCase);
        final String actual = Hex.encodeAsString(bytes);
        Assertions.assertEquals(expected, actual, "Bad encoding");
      }
    }
  }

  @Test
  void testDecode() {
    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();
    final boolean toLowerCase = true;
    // Output Hex characters.
    final char[] hexDigits =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    for (int i = 1; i < 20; i++) {
      final byte[] bytes = new byte[i];
      for (int j = 0; j < 5; j++) {
        rng.nextBytes(bytes);
        final String hex = org.apache.commons.codec.binary.Hex.encodeHexString(bytes, toLowerCase);
        final byte[] actual = Hex.decode(hex);
        Assertions.assertArrayEquals(bytes, actual, "Bad decoding");

        // Test with odd length string. It should be the same as if it had a '0' on the end.
        final StringBuilder sb = new StringBuilder(hex);
        sb.append(hexDigits[rng.nextInt(16)]);

        final byte[] padded = Hex.decode(sb);
        sb.append('0');
        final byte[] unpadded = Hex.decode(sb);
        Assertions.assertArrayEquals(padded, unpadded, "Bad padding");

        // Check against original
        final byte[] clipped = Arrays.copyOf(padded, bytes.length);
        Assertions.assertArrayEquals(bytes, clipped, "Bad decoding after padding");
      }
    }
  }
}

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

import java.util.Arrays;
import java.util.stream.DoubleStream;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class ByteScramblerTest {

  @Test
  void testScramble() {
    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();

    // Get a seed size that is not the block size of the 128-bit scrambler
    final byte[] bytes = new byte[17];
    rng.nextBytes(bytes);
    final ByteScrambler bs = ByteScrambler.getByteScrambler(bytes);

    final byte[] next1 = bs.scramble();
    final byte[] next2 = bs.scramble();

    Assertions.assertEquals(bytes.length, next1.length);
    Assertions.assertEquals(bytes.length, next2.length);

    Assertions.assertFalse(Arrays.equals(bytes, next1),
        "Seed bytes and first scramble are the same");
    Assertions.assertFalse(Arrays.equals(bytes, next2),
        "Seed bytes and second scramble are the same");
    Assertions.assertFalse(Arrays.equals(next1, next2), "First and second scramble are the same");
  }

  @Test
  void testBitScramblerIsUniform() {
    // Fixed seed for test stability
    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create(68689L);
    // Scramble some random bytes
    final byte[] bytes = new byte[128];
    rng.nextBytes(bytes);
    final ByteScrambler bs = ByteScrambler.getByteScrambler(bytes);
    final long[] histogram = new long[256];
    for (int i = 0; i < 50; i++) {
      // Compute observed frequencies
      for (final byte bi : bs.scramble()) {
        histogram[bi & 0xff]++;
      }
    }
    // Do a chi-square test
    final ChiSquareTest chiSq = new ChiSquareTest();
    final double[] expected = DoubleStream.generate(() -> 1.0 / 256).limit(256).toArray();
    final double pValue = chiSq.chiSquareTest(expected, histogram);
    Assertions.assertFalse(pValue < 0.01);
  }
}

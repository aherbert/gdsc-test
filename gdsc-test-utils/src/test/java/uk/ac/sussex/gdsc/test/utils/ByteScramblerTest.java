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
import java.util.stream.DoubleStream;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.apache.commons.statistics.inference.ChiSquareTest;
import org.apache.commons.statistics.inference.SignificanceResult;
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
    final double[] expected = DoubleStream.generate(() -> 1.0 / 256).limit(256).toArray();
    final SignificanceResult r = ChiSquareTest.withDefaults().test(expected, histogram);
    Assertions.assertFalse(r.reject(0.01), () -> "p-value = " + r.getPValue());
  }
}

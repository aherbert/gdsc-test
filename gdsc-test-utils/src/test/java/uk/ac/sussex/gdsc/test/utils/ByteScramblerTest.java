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

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class ByteScramblerTest {

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithBadAlgorithm() {
    Assertions.assertThrows(NoSuchAlgorithmException.class, () -> {
      new ByteScrambler(new byte[32], "Bad algorithm");
    });
  }

  @Test
  public void testScramble() {
    final UniformRandomProvider rng = RandomSource.create(RandomSource.SPLIT_MIX_64);

    // Get a seed size that is not the block size of MD5 = 16 bytes
    final byte[] bytes = new byte[16 + 1];
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
}

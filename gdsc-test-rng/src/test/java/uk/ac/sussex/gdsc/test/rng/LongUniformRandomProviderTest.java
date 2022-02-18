/*-
 * #%L
 * Genome Damage and Stability Centre Test RNG
 *
 * Contains utilities for use with Commons RNG for random tests.
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

package uk.ac.sussex.gdsc.test.rng;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.LongSupplier;
import org.apache.commons.rng.RandomProviderState;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.core.source32.IntProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test {@link LongUniformRandomProvider} using the Commons RNG base implementation.
 */
@SuppressWarnings("javadoc")
class LongUniformRandomProviderTest {
  private final static int N = 1000;

  /**
   * Creates two RNGs. The first is the Commons RNG implementation.
   * The second is the same 64-bit source of randomness providing the bits
   * for the {@link LongUniformRandomProvider}.
   *
   * @return the uniform random providers
   */
  private static UniformRandomProvider[] createRngs() {
    final long seed0 = ThreadLocalRandom.current().nextLong();
    final long seed1 = seed0 + 627384217384238449L;
    final UniformRandomProvider rng1 =
        RandomSource.XO_RO_SHI_RO_128_PP.create(new long[] {seed0, seed1});
    final UniformRandomProvider rng2 =
        RandomSource.XO_RO_SHI_RO_128_PP.create(new long[] {seed0, seed1});
    final LongUniformRandomProvider rng = new LongUniformRandomProvider() {
      @Override
      public long nextLong() {
        return rng2.nextLong();
      }

      @Override
      public RandomProviderState saveState() {
        return null;
      }

      @Override
      public void restoreState(RandomProviderState state) {}
    };
    return new UniformRandomProvider[] {rng1, rng};
  }

  /**
   * Convert the upper 32-bits of a 64-bit provider into an int provider.
   *
   * @param rng the rng
   * @return the uniform random provider
   */
  private static UniformRandomProvider toIntProvider(UniformRandomProvider rng) {
    return new IntProvider() {
      @Override
      public int next() {
        return (int) (rng.nextLong() >>> 32);
      }
    };
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 13, 42})
  void testNextBytes(int n) {
    final UniformRandomProvider[] rngs = createRngs();
    final UniformRandomProvider rng1 = rngs[0];
    final UniformRandomProvider rng2 = rngs[1];
    final byte[] b1 = new byte[n];
    final byte[] b2 = new byte[n];
    for (int i = 0; i < N; i++) {
      rng1.nextBytes(b1);
      rng2.nextBytes(b2);
      Assertions.assertArrayEquals(b1, b2);
    }
  }

  @ParameterizedTest
  @CsvSource({"42, 0, 10", "32, 5, 11", "32, 13, 19",})
  void testNextBytesInRange(int n, int start, int len) {
    final UniformRandomProvider[] rngs = createRngs();
    final UniformRandomProvider rng1 = rngs[0];
    final UniformRandomProvider rng2 = rngs[1];
    final byte[] b1 = new byte[n];
    final byte[] b2 = new byte[n];
    for (int i = 0; i < N; i++) {
      rng1.nextBytes(b1, start, len);
      rng2.nextBytes(b2, start, len);
      Assertions.assertArrayEquals(b1, b2);
    }
  }

  @RepeatedTest(value = 3)
  void testNextIntIsUpper64Bits() {
    final UniformRandomProvider[] rngs = createRngs();
    final UniformRandomProvider rng1 = toIntProvider(rngs[0]);
    final UniformRandomProvider rng2 = rngs[1];
    for (int i = 0; i < N; i++) {
      Assertions.assertEquals(rng1.nextInt(), rng2.nextInt());
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 256, 13, 162176276})
  void testNextIntInRange(int n) {
    final UniformRandomProvider[] rngs = createRngs();
    final UniformRandomProvider rng1 = toIntProvider(rngs[0]);
    final UniformRandomProvider rng2 = rngs[1];
    for (int i = 0; i < N; i++) {
      Assertions.assertEquals(rng1.nextInt(n), rng2.nextInt(n));
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1, -13})
  void testNextIntInRangeThrows(int n) {
    final LongUniformRandomProvider rng = new LongUniformRandomProvider() {
      @Override
      public long nextLong() {
        return 42;
      }

      @Override
      public RandomProviderState saveState() {
        return null;
      }

      @Override
      public void restoreState(RandomProviderState state) {}
    };
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.nextInt(n));
  }

  @ParameterizedTest
  @ValueSource(longs = {2, 256, 13, 162176276, 8638763767234L})
  void testNextLongInRange(long n) {
    final UniformRandomProvider[] rngs = createRngs();
    final UniformRandomProvider rng1 = rngs[0];
    final UniformRandomProvider rng2 = rngs[1];
    // Use masking for powers of 2
    final LongSupplier next =
        (n & (n - 1)) == 0 ? () -> rng1.nextLong() & (n - 1) : () -> rng1.nextLong(n);
    for (int i = 0; i < N; i++) {
      Assertions.assertEquals(next.getAsLong(), rng2.nextLong(n));
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1, -13})
  void testNextLongInRangeThrows(int n) {
    final LongUniformRandomProvider rng = new LongUniformRandomProvider() {
      @Override
      public long nextLong() {
        return 42;
      }

      @Override
      public RandomProviderState saveState() {
        return null;
      }

      @Override
      public void restoreState(RandomProviderState state) {}
    };
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.nextInt(n));
  }

  @RepeatedTest(value = 3)
  void testNextBooleanIsSignTest() {
    final UniformRandomProvider[] rngs = createRngs();
    final UniformRandomProvider rng1 = rngs[0];
    final UniformRandomProvider rng2 = rngs[1];
    for (int i = 0; i < N; i++) {
      Assertions.assertEquals(rng1.nextLong() < 0, rng2.nextBoolean());
    }
  }

  @RepeatedTest(value = 3)
  void testNextFloat() {
    final UniformRandomProvider[] rngs = createRngs();
    final UniformRandomProvider rng1 = toIntProvider(rngs[0]);
    final UniformRandomProvider rng2 = rngs[1];
    for (int i = 0; i < N; i++) {
      Assertions.assertEquals(rng1.nextFloat(), rng2.nextFloat());
    }
  }

  @RepeatedTest(value = 3)
  void testNextDouble() {
    final UniformRandomProvider[] rngs = createRngs();
    final UniformRandomProvider rng1 = rngs[0];
    final UniformRandomProvider rng2 = rngs[1];
    for (int i = 0; i < N; i++) {
      Assertions.assertEquals(rng1.nextDouble(), rng2.nextDouble());
    }
  }
}

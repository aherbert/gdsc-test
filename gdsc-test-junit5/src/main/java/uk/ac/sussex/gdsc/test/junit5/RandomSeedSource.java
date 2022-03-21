/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with JUnit 5.
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

package uk.ac.sussex.gdsc.test.junit5;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import uk.ac.sussex.gdsc.test.utils.ByteScrambler;
import uk.ac.sussex.gdsc.test.utils.RandomSeed;
import uk.ac.sussex.gdsc.test.utils.TestSettings;

/**
 * Provides random seeds as a {@link RandomSeed} for use as an {@link ArgumentsSource}.
 *
 * <p>The starting seed and number of repeats are obtained from runtime system properties.
 *
 * @see TestSettings
 */
public class RandomSeedSource implements ArgumentsProvider {

  /**
   * A reference to the last seed sequence.
   *
   * <p>In the common use case the system settings will never change and this can be cached for
   * reuse.
   */
  private static AtomicReference<SeedSequence> lastSeedSequence = new AtomicReference<>();

  /**
   * Encapsulate the data required for a sequence of random seeds as an arguments source.
   *
   * <p>Package scope for testing.
   */
  static class SeedSequence {
    /** The constant 1. */
    private static final int ONE = 1;

    /** The random seed. */
    private final RandomSeed randomSeed;

    /** The number of repeats. */
    private final int repeats;

    /** The arguments. */
    private final Arguments[] arguments;

    /**
     * Create a new instance.
     *
     * @param seed the seed
     * @param repeats the repeats
     */
    SeedSequence(byte[] seed, int repeats) {
      // Store the seed
      this.randomSeed = RandomSeed.of(seed);
      this.repeats = repeats;

      // Create the sequence of random seeds for an arguments source
      arguments = new Arguments[repeats];
      // Reuse the seed for the first entry
      arguments[0] = Arguments.of(randomSeed);

      if (repeats > ONE) {
        // Generate new seeds using a byte scrambler.
        // This is expensive so the immutable seeds are cached.
        // In the common use case the seed and repeats are set by system properties
        // and never changed so the generation occurs only once.
        final ByteScrambler scrambler = ByteScrambler.getByteScrambler(seed);
        for (int i = 1; i < repeats; i++) {
          arguments[i] = Arguments.of(RandomSeed.of(scrambler.scramble()));
        }
      }
    }

    /**
     * Test if this matches the given seed sequence data.
     *
     * @param seed the seed
     * @param repeats the repeats
     * @return true if a match
     */
    boolean matches(byte[] seed, int repeats) {
      return this.repeats == repeats && randomSeed.equalBytes(seed);
    }

    /**
     * Provide the arguments.
     *
     * @return the stream of arguments
     */
    Stream<Arguments> provideArguments() {
      return (arguments.length == 1) ? Stream.of(arguments[0]) : Stream.of(arguments);
    }
  }

  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext arg0) {
    return getSeedSequence().provideArguments();
  }

  /**
   * Gets the seed sequence.
   *
   * <p>This re-uses the last sequence if the seed is unchanged.
   *
   * <p>The seed properties are obtained from the test settings.
   *
   * @return the seed sequence
   */
  private static SeedSequence getSeedSequence() {
    // The seed or repeats may change so get them
    final byte[] seed = TestSettings.getSeed();
    final int repeats = TestSettings.getRepeats();
    return getSeedSequence(seed, repeats);
  }

  /**
   * Gets the seed sequence.
   *
   * <p>This re-uses the last sequence if the seed is unchanged.
   *
   * <p>Package scope for testing.
   *
   * @param seed the seed
   * @param repeats the repeats
   * @return the seed sequence
   */
  static SeedSequence getSeedSequence(byte[] seed, int repeats) {

    // In the common use case the settings will never change so check if
    // the seed can be reused.
    SeedSequence seedSequence = lastSeedSequence.get();
    if (sameSeed(seedSequence, seed, repeats)) {
      return seedSequence;
    }

    // Create the first seed.
    seedSequence = new SeedSequence(seed, repeats);
    lastSeedSequence.set(seedSequence);
    return seedSequence;
  }

  /**
   * Check if this is the same seed.
   *
   * @param seedSequence the seed sequence
   * @param seed the seed
   * @param repeats the repeats
   * @return true, if the same
   */
  private static boolean sameSeed(SeedSequence seedSequence, byte[] seed, int repeats) {
    return (seedSequence != null && seedSequence.matches(seed, repeats));
  }
}

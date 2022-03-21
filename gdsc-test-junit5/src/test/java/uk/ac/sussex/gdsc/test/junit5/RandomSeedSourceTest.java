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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import uk.ac.sussex.gdsc.test.junit5.RandomSeedSource.SeedSequence;
import uk.ac.sussex.gdsc.test.utils.RandomSeed;
import uk.ac.sussex.gdsc.test.utils.RandomSeeds;

@SuppressWarnings("javadoc")
class RandomSeedSourceTest {

  @Test
  void testSeedSequenceMatches() {
    // It doesn't matter what the initial seed is
    byte[] seed = {1, 2, 3, 4};
    int repeats = 1;
    SeedSequence sequence = RandomSeedSource.getSeedSequence(seed, repeats);

    Assertions.assertTrue(sequence.matches(seed, repeats),
        "Doesn't match the same seed and repeats");
    Assertions.assertFalse(sequence.matches(new byte[1], repeats), "Matches different seed");
    Assertions.assertFalse(sequence.matches(seed, repeats + 1), "Matches different repeats");
  }

  @Test
  void testGetSeedSequenceWithSingleRepeat() {
    // It doesn't matter what the initial seed is
    byte[] seed = RandomSeeds.makeByteArray(System.currentTimeMillis());
    int repeats = 1;
    SeedSequence sequence = RandomSeedSource.getSeedSequence(seed, repeats);

    Arguments[] arguments1 = sequence.provideArguments().toArray(Arguments[]::new);
    Assertions.assertEquals(repeats, arguments1.length, "Arguments1 length");

    checkArguments(arguments1, seed, repeats);

    // Second time should be the same seed
    Arguments[] arguments2 = sequence.provideArguments().toArray(Arguments[]::new);
    Assertions.assertEquals(repeats, arguments2.length, "Arguments2 length");

    RandomSeed randomSeed1 = getRandomSeed(arguments1, 0);
    RandomSeed randomSeed2 = getRandomSeed(arguments2, 0);
    Assertions.assertSame(randomSeed1, randomSeed2, "Not the same seed");
  }

  private static void checkArguments(Arguments[] arguments, byte[] seed, int repeats) {
    Assertions.assertEquals(repeats, arguments.length, "Number of arguments");

    RandomSeed randomSeed = getRandomSeed(arguments, 0);
    Assertions.assertArrayEquals(seed, randomSeed.get(), "Initial random seed bytes are different");

    // Check the rest
    for (int i = 1; i < arguments.length; i++) {
      final int repetition = i + 1;
      randomSeed = getRandomSeed(arguments, i);
      Assertions.assertFalse(randomSeed.equalBytes(seed),
          () -> repetition + " random seed bytes are the same");
    }
  }

  private static RandomSeed getRandomSeed(Arguments[] arguments, int index) {
    return (RandomSeed) arguments[index].get()[0];
  }

  @Test
  void testGetSeedSequenceWithMultiRepeat() {
    // It doesn't matter what the initial seed is
    byte[] seed = RandomSeeds.makeByteArray(System.currentTimeMillis());
    int repeats = 2;
    SeedSequence sequence = RandomSeedSource.getSeedSequence(seed, repeats);

    Arguments[] arguments1 = sequence.provideArguments().toArray(Arguments[]::new);
    Assertions.assertEquals(repeats, arguments1.length, "Arguments1 length");

    checkArguments(arguments1, seed, repeats);

    // Second time should be the same seeds
    Arguments[] arguments2 = sequence.provideArguments().toArray(Arguments[]::new);
    Assertions.assertEquals(repeats, arguments2.length, "Arguments2 length");

    for (int i = 0; i < repeats; i++) {
      RandomSeed randomSeed1 = getRandomSeed(arguments1, i);
      RandomSeed randomSeed2 = getRandomSeed(arguments2, i);
      final int repetition = i + 1;
      Assertions.assertSame(randomSeed1, randomSeed2, () -> "Not the same seed @ " + repetition);
    }
  }
}

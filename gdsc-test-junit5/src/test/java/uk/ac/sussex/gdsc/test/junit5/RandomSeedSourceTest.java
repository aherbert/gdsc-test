/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with JUnit 5.
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

package uk.ac.sussex.gdsc.test.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import uk.ac.sussex.gdsc.test.junit5.RandomSeedSource.SeedSequence;
import uk.ac.sussex.gdsc.test.utils.SeedUtils;

@SuppressWarnings("javadoc")
public class RandomSeedSourceTest {

  @Test
  public void testSeedSequenceMatches() {
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
  public void testGetSeedSequenceWithSingleRepeat() {
    // It doesn't matter what the initial seed is
    byte[] seed = SeedUtils.makeByteArray(System.currentTimeMillis());
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
    RandomSeed randomSeed = getRandomSeed(arguments, 0);
    Assertions.assertArrayEquals(seed, randomSeed.getSeed(),
        "Initial random seed bytes are different");
    Assertions.assertEquals(1, randomSeed.getCurrentRepetition(),
        "Initial random seed current repetition");
    Assertions.assertEquals(repeats, randomSeed.getTotalRepetitions(),
        "Initial random seed total repetitions");

    // Check the rest
    for (int i = 1; i < arguments.length; i++) {
      final int repetition = i + 1;
      randomSeed = getRandomSeed(arguments, i);
      Assertions.assertFalse(randomSeed.equalBytes(seed),
          () -> repetition + " random seed bytes are the same");
      Assertions.assertEquals(repetition, randomSeed.getCurrentRepetition(),
          () -> repetition + " random seed current repetition");
      Assertions.assertEquals(repeats, randomSeed.getTotalRepetitions(),
          () -> repetition + " random seed total repetitions");
    }
  }

  private static RandomSeed getRandomSeed(Arguments[] arguments, int index) {
    return (RandomSeed) arguments[index].get()[0];
  }

  @Test
  public void testGetSeedSequenceWithMultiRepeat() {
    // It doesn't matter what the initial seed is
    byte[] seed = SeedUtils.makeByteArray(System.currentTimeMillis());
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

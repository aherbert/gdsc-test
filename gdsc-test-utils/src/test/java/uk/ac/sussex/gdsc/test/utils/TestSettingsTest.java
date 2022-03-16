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

import uk.ac.sussex.gdsc.test.utils.TestLogging.TestLevel;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class TestSettingsTest {
  private static Logger logger;

  @BeforeAll
  public static void beforeAll() {
    logger = Logger.getLogger(TestSettingsTest.class.getName());
  }

  @AfterAll
  public static void afterAll() {
    logger = null;
  }

  @Test
  void testGetSeed() {
    // Disable logging the seed
    final Logger logger = Logger.getLogger(TestSettings.class.getName());
    Level level = logger.getLevel();
    logger.setLevel(Level.OFF);

    try {
      // Check the seed is random if not set as a parameter
      final byte[] seed = TestSettings.getSeed();
      if (System.getProperty(TestSettings.PROPERTY_RANDOM_SEED) == null) {
        Assertions.assertNotNull(seed, "Seed should be generated");
      }
      final byte[] seed2 = TestSettings.getSeed();
      Assertions.assertArrayEquals(seed, seed2, "Seed should be constant");
      Assertions.assertNotSame(seed, seed2, "Seed should be a new array");
      logger.log(TestLevel.TEST_INFO,
          () -> String.format("TestSettings Seed = %s", Hex.encodeAsString(seed)));

      // Test setting the seed to null
      TestSettings.setSeed(null);
      final byte[] seed3 = TestSettings.getSeed();
      Assertions.assertThrows(AssertionError.class, () -> {
        Assertions.assertArrayEquals(seed, seed3);
      }, "Seed should be different after setting to null");

      // Test setting the seed to empty
      TestSettings.setSeed(new byte[0]);
      final byte[] seed4 = TestSettings.getSeed();
      Assertions.assertThrows(AssertionError.class, () -> {
        Assertions.assertArrayEquals(seed, seed4);
      }, "Seed should be different after setting to empty");

      // Test setting the seed to empty
      byte[] zeroSeed = new byte[32];
      TestSettings.setSeed(zeroSeed);
      final byte[] seed5 = TestSettings.getSeed();
      Assertions.assertArrayEquals(zeroSeed, seed5, "Zero filled seed is not supported");
      Assertions.assertNotSame(zeroSeed, seed5, "Zero filled seed is copied by reference");

      // Restore
      TestSettings.setSeed(seed);

    } finally {
      logger.setLevel(level);
    }
  }

  @Test
  void testGetRepeats() {
    // Check the repeats is 1 if not set as a parameter
    final int repeats = TestSettings.getRepeats();
    if (TestSettings.getProperty(TestSettings.PROPERTY_RANDOM_REPEATS, 0) == 0) {
      Assertions.assertEquals(1, repeats);
    }
    logger.log(TestLevel.TEST_INFO, () -> String.format("TestSettings Repeats = %d", repeats));
  }

  @Test
  void testGetComplexity() {
    // Currently no restrictions on complexity
    final int complexity = TestSettings.getTestComplexity();
    logger.log(TestLevel.TEST_INFO,
        () -> String.format("TestSettings Test Complexity = %d", complexity));
  }

  @Test
  void testGetPropertyAsInt() {
    final String key = "A long key that should be really, really unique";
    System.clearProperty(key);
    final int defaultValue = -6765757;
    Assertions.assertEquals(defaultValue, TestSettings.getProperty(key, defaultValue));
    System.setProperty(key, "xx");
    Assertions.assertEquals(defaultValue, TestSettings.getProperty(key, defaultValue));
    System.setProperty(key, "1");
    Assertions.assertEquals(1, TestSettings.getProperty(key, defaultValue));
    System.clearProperty(key);
  }

  @Test
  void testGetPropertyAsLong() {
    final String key = "A long key that should be really, really unique";
    System.clearProperty(key);
    final long defaultValue = -6765757676567L;
    Assertions.assertEquals(defaultValue, TestSettings.getProperty(key, defaultValue));
    System.setProperty(key, "xx");
    Assertions.assertEquals(defaultValue, TestSettings.getProperty(key, defaultValue));
    System.setProperty(key, "1");
    Assertions.assertEquals(1, TestSettings.getProperty(key, defaultValue));
    System.clearProperty(key);
  }

  @Test
  void testGetPropertyAsByteArray() {
    final String key = "A long key that should be really, really unique";
    System.clearProperty(key);
    final byte[] defaultValue = new byte[] {1, 23, 4};
    Assertions.assertArrayEquals(defaultValue, TestSettings.getProperty(key, defaultValue));

    // Ignore seeds with no information
    for (String seed : new String[] {"", " ", "  ", "xx"}) {
      System.setProperty(key, seed);
      Assertions.assertArrayEquals(defaultValue, TestSettings.getProperty(key, defaultValue),
          () -> "Seed is " + seed);
    }

    // Decode seeds with information, even if it is zero
    for (String seed : new String[] {"0", "0000", "1", "1234567890abcdef"}) {
      System.setProperty(key, seed);
      final byte[] expected = Hex.decode(seed);
      Assertions.assertArrayEquals(expected, TestSettings.getProperty(key, defaultValue),
          () -> "Seed is " + seed);
    }
    System.clearProperty(key);
  }

  @Test
  void testAllowTestComplexity() {
    final int complexity = TestSettings.getTestComplexity();
    for (final TestComplexity tc : TestComplexity.values()) {
      Assertions.assertEquals(tc.getValue() <= complexity, TestSettings.allow(tc));
    }
  }
}

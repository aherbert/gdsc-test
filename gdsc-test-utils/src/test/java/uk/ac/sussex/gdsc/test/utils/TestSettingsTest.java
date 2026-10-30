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

import uk.ac.sussex.gdsc.test.utils.TestLogging.TestLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
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
  void testGetSeedInParallel() throws Exception {
    // Disable logging the seed
    final Logger logger = Logger.getLogger(TestSettings.class.getName());
    final Level level = logger.getLevel();
    logger.setLevel(Level.OFF);

    final byte[] seed = TestSettings.getSeed();
    final int threads = 4;
    final ExecutorService executor = Executors.newFixedThreadPool(threads);
    try {
      for (int repeat = 0; repeat < 200; repeat++) {
        // Generate a new seed on the first call to getSeed
        TestSettings.setSeed(null);
        final CountDownLatch start = new CountDownLatch(1);
        final List<Future<byte[]>> futures = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
          futures.add(executor.submit(() -> {
            start.await();
            return TestSettings.getSeed();
          }));
        }
        start.countDown();
        final byte[] expected = futures.get(0).get();
        for (final Future<byte[]> f : futures) {
          Assertions.assertArrayEquals(expected, f.get(), "All threads should use the same seed");
        }
        Assertions.assertArrayEquals(expected, TestSettings.getSeed(), "Seed should be constant");
      }
    } finally {
      executor.shutdown();
      // Restore
      TestSettings.setSeed(seed);
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
    try {
      assertNoWarning(() -> Assertions.assertEquals(defaultValue,
          TestSettings.getProperty(key, defaultValue)));
      System.setProperty(key, "xx");
      assertWarning(key, () -> Assertions.assertEquals(defaultValue,
          TestSettings.getProperty(key, defaultValue)));
      System.setProperty(key, "1");
      assertNoWarning(
          () -> Assertions.assertEquals(1, TestSettings.getProperty(key, defaultValue)));
    } finally {
      System.clearProperty(key);
    }
  }

  @Test
  void testGetPropertyAsLong() {
    final String key = "A long key that should be really, really unique";
    System.clearProperty(key);
    final long defaultValue = -6765757676567L;
    try {
      assertNoWarning(() -> Assertions.assertEquals(defaultValue,
          TestSettings.getProperty(key, defaultValue)));
      System.setProperty(key, "xx");
      assertWarning(key, () -> Assertions.assertEquals(defaultValue,
          TestSettings.getProperty(key, defaultValue)));
      System.setProperty(key, "1");
      assertNoWarning(
          () -> Assertions.assertEquals(1, TestSettings.getProperty(key, defaultValue)));
    } finally {
      System.clearProperty(key);
    }
  }

  @Test
  void testGetPropertyAsByteArray() {
    final String key = "A long key that should be really, really unique";
    System.clearProperty(key);
    final byte[] defaultValue = new byte[] {1, 23, 4};
    try {
      assertNoWarning(() -> Assertions.assertArrayEquals(defaultValue,
          TestSettings.getProperty(key, defaultValue)));

      // Ignore seeds with no information and log a warning
      for (String seed : new String[] {"", " ", "  ", "xx", "0x12"}) {
        System.setProperty(key, seed);
        assertWarning(key, () -> Assertions.assertArrayEquals(defaultValue,
            TestSettings.getProperty(key, defaultValue), () -> "Seed is " + seed));
      }

      // Decode seeds with information, even if it is zero
      for (String seed : new String[] {"0", "0000", "1", "1234567890abcdef"}) {
        System.setProperty(key, seed);
        final byte[] expected = Hex.decode(seed);
        assertNoWarning(() -> Assertions.assertArrayEquals(expected,
            TestSettings.getProperty(key, defaultValue), () -> "Seed is " + seed));
      }
    } finally {
      System.clearProperty(key);
    }
  }

  /**
   * Assert the action logs a single warning from {@link TestSettings} containing the property
   * name.
   *
   * @param name the property name
   * @param action the action
   */
  private static void assertWarning(String name, Runnable action) {
    final List<LogRecord> records = captureLogRecords(action);
    Assertions.assertEquals(1, records.size());
    Assertions.assertEquals(Level.WARNING, records.get(0).getLevel());
    Assertions.assertTrue(records.get(0).getMessage().contains(name));
  }

  /**
   * Assert the action logs nothing from {@link TestSettings}.
   *
   * @param action the action
   */
  private static void assertNoWarning(Runnable action) {
    Assertions.assertEquals(0, captureLogRecords(action).size());
  }

  /**
   * Capture the records logged by {@link TestSettings} when running the action.
   *
   * @param action the action
   * @return the records
   */
  private static List<LogRecord> captureLogRecords(Runnable action) {
    final List<LogRecord> records = new ArrayList<>();
    final Handler handler = new Handler() {
      @Override
      public void publish(LogRecord record) {
        records.add(record);
      }

      @Override
      public void flush() {
        // Do nothing
      }

      @Override
      public void close() {
        // Do nothing
      }
    };
    final Logger settingsLogger = Logger.getLogger(TestSettings.class.getName());
    settingsLogger.addHandler(handler);
    try {
      action.run();
    } finally {
      settingsLogger.removeHandler(handler);
    }
    return records;
  }

  @Test
  void testAllowTestComplexity() {
    final int complexity = TestSettings.getTestComplexity();
    for (final TestComplexity tc : TestComplexity.values()) {
      Assertions.assertEquals(tc.getValue() <= complexity, TestSettings.allow(tc));
    }
  }
}

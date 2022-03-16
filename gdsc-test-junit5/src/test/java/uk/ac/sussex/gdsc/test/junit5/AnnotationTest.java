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

import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import uk.ac.sussex.gdsc.test.utils.Hex;
import uk.ac.sussex.gdsc.test.utils.RandomSeed;
import uk.ac.sussex.gdsc.test.utils.TestLogUtils.TestLevel;

@SuppressWarnings("javadoc")
class AnnotationTest {
  private static Logger logger;

  @BeforeAll
  public static void beforeAll() {
    logger = Logger.getLogger(AnnotationTest.class.getName());
  }

  @AfterAll
  public static void afterAll() {
    logger = null;
  }

  @SpeedTag
  @Test
  void canAnnotateSpeedTest(TestInfo info) {
    Assertions.assertTrue(info.getTags().contains("speed"));
  }

  @RandomTag
  @Test
  void canAnnotateRandomTest(TestInfo info) {
    Assertions.assertTrue(info.getTags().contains("random"));
  }

  @RandomSpeedTag
  @Test
  void canAnnotateRandomSpeedTest(TestInfo info) {
    Assertions.assertTrue(info.getTags().contains("speed"));
    Assertions.assertTrue(info.getTags().contains("random"));
  }

  @Test
  @EnabledIfEnvironmentVariable(matches = "CI", named = "ENV")
  void canAnnotateEnableIfEnvironmentVariable(TestInfo info) {
    logger.log(TestLevel.TEST_INFO, info.getTestMethod().get().getName());
  }

  @ParameterizedTest
  @ArgumentsSource(RandomSeedSource.class)
  void canDynamicallyProvideSeedsFromRandomSeedSource(RandomSeed seed, TestInfo info) {
    logger.log(TestLevel.TEST_INFO, () -> String.format("%s seed = %d",
        info.getTestMethod().get().getName(), seed.getAsLong()));
  }

  @SeededTest
  void canAnnotateSeededTest(RandomSeed seed, TestInfo info) {
    logger.log(TestLevel.TEST_INFO, () -> String.format("%s seed = %s",
        info.getTestMethod().get().getName(), Hex.encodeAsString(seed.get())));
  }

  @Test
  @DisabledIfHeadless
  void canDisableIfHeadless(TestInfo info) {
    logger.log(TestLevel.TEST_INFO, () -> String.format("%s: headless=%b",
        info.getTestMethod().get().getName(), java.awt.GraphicsEnvironment.isHeadless()));
    Assertions.assertFalse(java.awt.GraphicsEnvironment.isHeadless());
  }

  @Test
  @EnabledIfHeadless
  void canEnableIfHeadless(TestInfo info) {
    logger.log(TestLevel.TEST_INFO, () -> String.format("%s: headless=%b",
        info.getTestMethod().get().getName(), java.awt.GraphicsEnvironment.isHeadless()));
    Assertions.assertTrue(java.awt.GraphicsEnvironment.isHeadless());
  }
}

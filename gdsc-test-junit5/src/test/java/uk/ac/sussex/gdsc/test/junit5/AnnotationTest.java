/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with JUnit 5.
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
import uk.ac.sussex.gdsc.test.utils.TestLogging.TestLevel;

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

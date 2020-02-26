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
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import uk.ac.sussex.gdsc.test.utils.HexUtils;
import uk.ac.sussex.gdsc.test.utils.TestLogUtils.TestLevel;

@SuppressWarnings("javadoc")
public class AnnotationTest {
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
  public void canAnnotateSpeedTest(TestInfo info) {
    Assertions.assertTrue(info.getTags().contains("speed"));
  }

  @RandomTag
  @Test
  public void canAnnotateRandomTest(TestInfo info) {
    Assertions.assertTrue(info.getTags().contains("random"));
  }

  @RandomSpeedTag
  @Test
  public void canAnnotateRandomSpeedTest(TestInfo info) {
    Assertions.assertTrue(info.getTags().contains("speed"));
    Assertions.assertTrue(info.getTags().contains("random"));
  }

  @Test
  @EnabledIf("'CI' == systemEnvironment.get('ENV')")
  public void canAnnotateEnableIf(TestInfo info) {
    logger.log(TestLevel.TEST_INFO, info.getTestMethod().get().getName());
  }

  @ParameterizedTest
  @ArgumentsSource(RandomSeedSource.class)
  public void canDynamicallyProvideSeedsFromRandomSeedSource(RandomSeed seed, TestInfo info) {
    logger.log(TestLevel.TEST_INFO,
        () -> String.format("%s seed = %d (%d/%d)", info.getTestMethod().get().getName(),
            seed.getSeed(), seed.getCurrentRepetition(), seed.getTotalRepetitions()));
  }

  @SeededTest
  public void canAnnotateSeededTest(RandomSeed seed, TestInfo info) {
    logger.log(TestLevel.TEST_INFO,
        () -> String.format("%s seed = %s (%d/%d)", info.getTestMethod().get().getName(),
            HexUtils.encodeHexString(seed.getSeed()), seed.getCurrentRepetition(),
            seed.getTotalRepetitions()));
  }
}

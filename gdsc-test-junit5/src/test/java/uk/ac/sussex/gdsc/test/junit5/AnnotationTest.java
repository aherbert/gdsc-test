/*-
 * #%L
 * Genome Damage and Stability Centre Test Package
 *
 * The GDSC Test package contains code for use with the JUnit test framework.
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
package uk.ac.sussex.gdsc.test.junit5;

import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("javadoc")
public class AnnotationTest
{
    private static Logger logger;

    @BeforeAll
    public static void beforeAll()
    {
        logger = Logger.getLogger(AnnotationTest.class.getName());
    }

    @AfterAll
    public static void afterAll()
    {
        logger = null;
    }

    @SpeedTag
    @Test
    public void canAnnotateSpeedTest(TestInfo info)
    {
        //logger.info(info.getTestMethod().get().getName());
        Assertions.assertTrue(info.getTags().contains("speed"));
    }

    @RandomTag
    @Test
    public void canAnnotateRandomTest(TestInfo info)
    {
        //logger.info(info.getTestMethod().get().getName());
        Assertions.assertTrue(info.getTags().contains("random"));
    }

    @RandomSpeedTag
    @Test
    public void canAnnotateRandomSpeedTest(TestInfo info)
    {
        //logger.info(info.getTestMethod().get().getName());
        Assertions.assertTrue(info.getTags().contains("speed"));
        Assertions.assertTrue(info.getTags().contains("random"));
    }

    // This is just an example
    //@RepeatedTest(value = 3)
    public void canAnnotateRepeatedTest(TestInfo info, RepetitionInfo repInfo)
    {
        Assertions.assertEquals(3, repInfo.getTotalRepetitions());
        logger.info(() -> String.format("%s (%d/%d)", info.getTestMethod().get().getName(),
                repInfo.getCurrentRepetition(), repInfo.getTotalRepetitions()));
    }

    @Test
    @EnabledIf("'CI' == systemEnvironment.get('ENV')")
    public void canAnnotateEnableIfCI(TestInfo info)
    {
        logger.info(info.getTestMethod().get().getName());
    }

    // This is just an example
    //@TestFactory
    public Stream<DynamicTest> canDynamicallyRepeatTests(TestInfo info)
    {
        return IntStream.iterate(0, n -> n + 1).limit(2)
                .mapToObj(n -> DynamicTest.dynamicTest("test" + n, () -> testToBeRepeated(info, n)));
    }

    private static void testToBeRepeated(TestInfo info, int n)
    {
        logger.info(() -> String.format("%s (%d)", info.getTestMethod().get().getName(), n));
    }

    // This is just an example
    //@ParameterizedTest
    @MethodSource(value = "createSeeds")
    public void canDynamicallyProvideSeeds(int seed)
    {
        logger.info(() -> String.format("Dynamic seed = %d", seed));
    }

    @SuppressWarnings("unused")
    private static Stream<Integer> createSeeds()
    {
        return IntStream.iterate(0, n -> n + 1).limit(2).mapToObj(n -> n);
    }

    @ParameterizedTest
    @ArgumentsSource(RandomSeedSource.class)
    public void canDynamicallyProvideSeedsFromRandomSeedSource(RandomSeed seed, TestInfo info)
    {
        logger.info(() -> String.format("%s seed = %d (%d/%d)", info.getTestMethod().get().getName(), seed.getSeed(),
                seed.getCurrentRepetition(), seed.getTotalRepetitions()));
    }

    @SeededTest
    public void canAnnotateSeededTest(RandomSeed seed, TestInfo info)
    {
        logger.info(() -> String.format("%s seed = %d (%d/%d)", info.getTestMethod().get().getName(), seed.getSeed(),
                seed.getCurrentRepetition(), seed.getTotalRepetitions()));
    }
}

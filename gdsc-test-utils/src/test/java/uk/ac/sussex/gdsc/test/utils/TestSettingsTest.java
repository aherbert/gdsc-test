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
package uk.ac.sussex.gdsc.test.utils;

import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class TestSettingsTest {
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
    public void canPrintSettings() {
        long seed = TestSettings.getSeed();
        // Check the seed is random if not set as a parameter
        if (System.getProperty(TestSettings.PROPERTY_RANDOM_SEED) != null)
            Assertions.assertNotEquals(0, seed);
        logger.info(() -> String.format("TestSettings Seed = %d", seed));
        logger.info(() -> String.format("TestSettings Test Complexity = %d", TestSettings.getTestComplexity()));
    }

    @Test
    public void canSystemProperty() {
        final String key = "A long key that should be really, really unique";
        System.clearProperty(key);
        final int iValue = -6765757;
        Assertions.assertEquals(iValue, TestSettings.getProperty(key, iValue));
        System.setProperty(key, "xx");
        Assertions.assertEquals(iValue, TestSettings.getProperty(key, iValue));
        System.setProperty(key, "1");
        Assertions.assertEquals(1, TestSettings.getProperty(key, iValue));
        System.clearProperty(key);
        final long lValue = -6765757676567L;
        Assertions.assertEquals(lValue, TestSettings.getProperty(key, lValue));
        System.setProperty(key, "xx");
        Assertions.assertEquals(lValue, TestSettings.getProperty(key, lValue));
        System.setProperty(key, "1");
        Assertions.assertEquals(1, TestSettings.getProperty(key, lValue));
    }
}

/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with JUnit 5.
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

@SuppressWarnings("javadoc")
public class RandomSeedTest
{
    @SuppressWarnings("unused")
    @Test
    public void canConstruct()
    {
        long seed = 67868L;
        int currentRepetition = 1678;
        int totalRepetitions = 579797;
        RandomSeed rs = new RandomSeed(seed, currentRepetition, totalRepetitions);
        Assertions.assertNotNull(rs);
        Assertions.assertEquals(seed, rs.getSeed());
        Assertions.assertEquals(currentRepetition, rs.getCurrentRepetition());
        Assertions.assertEquals(totalRepetitions, rs.getTotalRepetitions());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomSeed(seed, 0, totalRepetitions);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomSeed(seed, 1, 0);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomSeed(seed, totalRepetitions + 1, totalRepetitions);
        });
    }

    @Test
    public void canEquals()
    {
        long seed = 67868L;
        int currentRepetition = 1678;
        int totalRepetitions = 579797;
        RandomSeed rs1 = new RandomSeed(seed, currentRepetition, totalRepetitions);
        Assertions.assertNotNull(rs1);
        Assertions.assertEquals(rs1, rs1);
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            Assertions.assertEquals(rs1, null);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            Assertions.assertEquals(rs1, new Object());
        });

        // Same seed
        RandomSeed rs2 = new RandomSeed(seed, currentRepetition, totalRepetitions);
        Assertions.assertNotNull(rs2);
        Assertions.assertNotSame(rs1, rs2);
        Assertions.assertEquals(rs1, rs2);
        Assertions.assertEquals(rs1.hashCode(), rs2.hashCode());

        // Different
        RandomSeed rs3 = new RandomSeed(seed + 1, currentRepetition, totalRepetitions);
        Assertions.assertNotNull(rs3);
        Assertions.assertNotNull(rs2);
        Assertions.assertNotEquals(rs1, rs3);
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            Assertions.assertEquals(rs1.hashCode(), rs3.hashCode());
        });
    }
}

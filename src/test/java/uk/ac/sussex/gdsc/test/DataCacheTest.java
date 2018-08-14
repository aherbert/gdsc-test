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
package uk.ac.sussex.gdsc.test;

import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class DataCacheTest {
    private static class TestProvider implements Function<Integer, Integer> {
        int calls = 0;

        @Override
        public Integer apply(Integer t) {
            calls++;
            return new Integer(t + 1);
        }
    }

    @Test
    public void canCacheData() {
        int value;
        TestProvider provider = new TestProvider();
        DataCache<Integer, Integer> cache = new DataCache<>();
        value = cache.getOrComputeIfAbsent(1, provider);
        Assertions.assertEquals(2, value);
        Assertions.assertEquals(1, provider.calls);
        value = cache.getOrComputeIfAbsent(1, provider);
        Assertions.assertEquals(2, value);
        Assertions.assertEquals(1, provider.calls); // Not called again
        cache.clear();
        value = cache.getOrComputeIfAbsent(1, provider);
        Assertions.assertEquals(2, value);
        Assertions.assertEquals(2, provider.calls); // Called again
    }
}

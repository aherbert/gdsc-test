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

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.ac.sussex.gdsc.test.utils.NamedTimingTask;
import uk.ac.sussex.gdsc.test.utils.TimingResult;
import uk.ac.sussex.gdsc.test.utils.TimingTask;

@SuppressWarnings("javadoc")
public class TimingResultTest {
    @Test
    public void canConstruct() {
        final String name = "name";
        TimingResult r = new TimingResult(name, 2, 1);
        Assertions.assertEquals(name, r.getTask().getName());
        Assertions.assertEquals(2, r.getSize());
        Assertions.assertEquals(1, r.getMin());
        Assertions.assertEquals(1.5, r.getMean());
        Assertions.assertArrayEquals(new long[] { 2, 1 }, r.getTimes());
        final TimingTask task = new NamedTimingTask("name2");
        r = new TimingResult(task, 3);
        Assertions.assertEquals(task.getName(), r.getTask().getName());
        Assertions.assertEquals(1, r.getSize());
        Assertions.assertEquals(3, r.getMin());
        Assertions.assertEquals(3, r.getMean());
        Assertions.assertArrayEquals(new long[] { 3 }, r.getTimes());
        final Supplier<String> supplier = () -> String.format("Name = %s", "this");
        r = new TimingResult(supplier, 10, 20);
        Assertions.assertEquals(supplier.get(), r.getTask().getName());
        Assertions.assertEquals(2, r.getSize());
        Assertions.assertEquals(10, r.getMin());
        Assertions.assertEquals(15, r.getMean());
        Assertions.assertArrayEquals(new long[] { 10, 20 }, r.getTimes());
    }

    @Test
    public void canConstructEmpty() {
        final String name = "name";
        TimingResult r = new TimingResult(name);
        Assertions.assertEquals(name, r.getTask().getName());
        Assertions.assertEquals(0, r.getSize());
        Assertions.assertEquals(0, r.getMin());
        Assertions.assertEquals(0, r.getMean());
        Assertions.assertArrayEquals(new long[0], r.getTimes());
        final TimingTask task = new NamedTimingTask("name2");
        r = new TimingResult(task);
        Assertions.assertEquals(task.getName(), r.getTask().getName());
        Assertions.assertEquals(0, r.getSize());
        Assertions.assertEquals(0, r.getMin());
        Assertions.assertEquals(0, r.getMean());
        Assertions.assertArrayEquals(new long[0], r.getTimes());
        final Supplier<String> supplier = () -> String.format("Name = %s", "this");
        r = new TimingResult(supplier);
        Assertions.assertEquals(supplier.get(), r.getTask().getName());
        Assertions.assertEquals(0, r.getSize());
        Assertions.assertEquals(0, r.getMin());
        Assertions.assertEquals(0, r.getMean());
        Assertions.assertArrayEquals(new long[0], r.getTimes());
    }
}

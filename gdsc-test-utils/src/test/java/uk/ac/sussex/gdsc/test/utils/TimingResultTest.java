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

import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class TimingResultTest {
  @Test
  public void canConstruct() {
    final String name = "name";
    TimingResult tr = new TimingResult(name, 2, 1);
    Assertions.assertEquals(name, tr.getTask().getName());
    Assertions.assertEquals(2, tr.getSize());
    Assertions.assertEquals(1, tr.getMin());
    Assertions.assertEquals(1.5, tr.getMean());
    Assertions.assertArrayEquals(new long[] {2, 1}, tr.getTimes());
    final TimingTask task = new NamedTimingTask("name2");
    tr = new TimingResult(task, 3);
    Assertions.assertEquals(task.getName(), tr.getTask().getName());
    Assertions.assertEquals(1, tr.getSize());
    Assertions.assertEquals(3, tr.getMin());
    Assertions.assertEquals(3, tr.getMean());
    Assertions.assertArrayEquals(new long[] {3}, tr.getTimes());
    final Supplier<String> supplier = () -> String.format("Name = %s", "this");
    tr = new TimingResult(supplier, 10, 20);
    Assertions.assertEquals(supplier.get(), tr.getTask().getName());
    Assertions.assertEquals(2, tr.getSize());
    Assertions.assertEquals(10, tr.getMin());
    Assertions.assertEquals(15, tr.getMean());
    Assertions.assertArrayEquals(new long[] {10, 20}, tr.getTimes());
  }

  @Test
  public void canConstructEmpty() {
    final String name = "name";
    TimingResult tr = new TimingResult(name);
    Assertions.assertEquals(name, tr.getTask().getName());
    Assertions.assertEquals(0, tr.getSize());
    Assertions.assertEquals(0, tr.getMin());
    Assertions.assertEquals(0, tr.getMean());
    Assertions.assertArrayEquals(new long[0], tr.getTimes());
    final TimingTask task = new NamedTimingTask("name2");
    tr = new TimingResult(task);
    Assertions.assertEquals(task.getName(), tr.getTask().getName());
    Assertions.assertEquals(0, tr.getSize());
    Assertions.assertEquals(0, tr.getMin());
    Assertions.assertEquals(0, tr.getMean());
    Assertions.assertArrayEquals(new long[0], tr.getTimes());
    final Supplier<String> supplier = () -> String.format("Name = %s", "this");
    tr = new TimingResult(supplier);
    Assertions.assertEquals(supplier.get(), tr.getTask().getName());
    Assertions.assertEquals(0, tr.getSize());
    Assertions.assertEquals(0, tr.getMin());
    Assertions.assertEquals(0, tr.getMean());
    Assertions.assertArrayEquals(new long[0], tr.getTimes());
  }
}

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
class NamedTimingTaskTest {
  @Test
  void canConstruct() {
    final String name = "name";
    final TimingTask task = new NamedTimingTask(name);
    Assertions.assertEquals(name, task.getName());
    Assertions.assertEquals(0, task.getSize());
    Assertions.assertThrows(IllegalStateException.class, () -> {
      task.getData(0);
    });
    Assertions.assertThrows(IllegalStateException.class, () -> {
      task.run(null);
    });
    final Supplier<String> supplier = () -> String.format("Name = %s", "this");
    final TimingTask task2 = new NamedTimingTask(supplier);
    Assertions.assertEquals(supplier.get(), task2.getName());
    Assertions.assertEquals(0, task2.getSize());
    Assertions.assertThrows(IllegalStateException.class, () -> {
      task2.getData(0);
    });
  }
}

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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class BaseTimingTaskTest {
  @Test
  void canConstruct() {
    final String name = "name";
    final TimingTask<Integer, Double> task = new BaseTimingTask<Integer, Double>(name) {
      @Override
      public int getSize() {
        return 0;
      }

      @Override
      public Integer getData(int index) {
        return null;
      }

      @Override
      public Double run(Integer data) {
        return null;
      }
    };
    Assertions.assertEquals(name, task.getName());
    Assertions.assertEquals(0, task.getSize());
    // Just test the check method can be called.
    // It is an empty implementation in the abstract class.
    task.check(0, null);
  }
}

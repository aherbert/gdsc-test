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

/**
 * Class to store results of running a timing task.
 *
 * @param <D> type of the data
 * @param <R> type of the result
 */
public class TimingResult<D, R> {

  /** The task. */
  private final TimingTask<D, R> task;

  /** The times. */
  private final long[] times;

  /**
   * Creates a new timing result.
   *
   * @param task the task
   * @param times the times
   */
  public TimingResult(TimingTask<D, R> task, long... times) {
    this(task, times, true);
  }

  /**
   * Creates a new timing result.
   *
   * <p>This is package level constructor that optionally takes over the input times array.
   *
   * @param task the task
   * @param times the times
   * @param cloneArray the clone array
   */
  TimingResult(TimingTask<D, R> task, long[] times, boolean cloneArray) {
    this.task = task;
    this.times = (cloneArray) ? times.clone() : times;
  }

  /**
   * Creates a new timing result.
   *
   * <p>The result will be created with a dummy implementation of the {@link TimingTask} interface.
   *
   * @param name the name
   * @param times the times
   */
  public TimingResult(String name, long... times) {
    this.task = new NamedTimingTask<>(name);
    this.times = times.clone();
  }

  /**
   * Creates a new timing result.
   *
   * <p>The result will be created with a dummy implementation of the {@link TimingTask} interface.
   *
   * @param name the name
   * @param times the times
   */
  public TimingResult(Supplier<String> name, long... times) {
    this.task = new NamedTimingTask<>(name);
    this.times = times.clone();
  }

  /**
   * Gets the task.
   *
   * @return the task
   */
  public TimingTask<D, R> getTask() {
    return task;
  }

  /**
   * Gets the times.
   *
   * @return the times for running the timing task
   */
  public long[] getTimes() {
    return times.clone();
  }

  /**
   * Gets the number of times.
   *
   * @return the size
   */
  public int getSize() {
    return times.length;
  }

  /**
   * Gets the minimum time. Returns 0 if there are no times.
   *
   * @return the minimum time
   */
  public long getMin() {
    if (times.length == 0) {
      return 0;
    }
    long min = Long.MAX_VALUE;
    for (final long time : times) {
      if (min > time) {
        min = time;
      }
    }
    return min;
  }

  /**
   * Gets the mean time. Returns 0 if there are no times.
   *
   * @return the mean time.
   */
  public double getMean() {
    if (times.length == 0) {
      return 0;
    }
    long sum = 0;
    for (final long time : times) {
      sum += time;
    }
    return (double) sum / times.length;
  }
}

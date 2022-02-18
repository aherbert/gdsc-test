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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to run timing tasks.
 *
 * <h2>Note</h2>
 *
 * <p>The service provides the functionality to run tasks, verify the results and report on the time
 * for repeat execution. This timing service is not suitable for detailed performance analysis.
 * Benchmarking should be done using suitable benchmarking and profiling tools, for example the Java
 * Microbenchmark Harness (JMH).
 *
 * @param <D> type of the data
 * @param <R> type of the result
 * @see <a href="https://openjdk.java.net/projects/code-tools/jmh/">Java Microbenchmark Harness
 *      (JMH)</a>
 */
public class TimingService<D, R> {
  /**
   * The new line used in the report. Equal to {@code System.getProperty("line.separator")}.
   */
  public static final String NEW_LINE = System.lineSeparator();

  /** The number of timing runs. */
  private int runs;

  /** The results. */
  private final List<TimingResult<D, R>> results = new ArrayList<>();

  /**
   * Gets the number of timing results.
   *
   * @return The number of timing results
   */
  public int getSize() {
    return results.size();
  }

  /**
   * Clear the results.
   */
  public void clearResults() {
    results.clear();
  }

  /**
   * Get the timing result.
   *
   * <p>If the index is negative then count back from the end of the results list.
   *
   * @param index The index
   * @return the timing result
   */
  public TimingResult<D, R> get(int index) {
    if (index < 0) {
      return results.get(results.size() + index);
    }
    return results.get(index);
  }

  /**
   * Create a new timing service with the default number of runs.
   */
  public TimingService() {
    this(5);
  }

  /**
   * Create a new timing service with the specified number of runs.
   *
   * @param runs the number of timing runs
   */
  public TimingService(int runs) {
    setRuns(runs);
  }

  /**
   * Sets the runs.
   *
   * @param runs the number of timing runs
   */
  public void setRuns(int runs) {
    this.runs = runs;
  }

  /**
   * Gets the number of timing runs.
   *
   * @return the runs
   */
  public int getRuns() {
    return runs;
  }

  /**
   * Execute the timing task.
   *
   * @param task the task
   * @return the timing result
   */
  public TimingResult<D, R> execute(TimingTask<D, R> task) {
    return execute(task, false);
  }

  /**
   * Execute the timing task.
   *
   * @param task the task
   * @param check set to true to validate result with the check method
   * @return the timing result
   */
  @SuppressWarnings("unchecked")
  public TimingResult<D, R> execute(TimingTask<D, R> task, boolean check) {
    final int size = task.getSize();
    final long[] times = new long[runs];

    // Store the result
    final Object[] result = new Object[size];
    final Object[] data = new Object[size];
    for (int run = 0; run < runs; run++) {
      for (int i = 0; i < size; i++) {
        data[i] = task.getData(i);
      }
      final long start = System.nanoTime();
      for (int i = 0; i < size; i++) {
        result[i] = task.run((D) data[i]);
      }
      times[run] = System.nanoTime() - start;
    }
    if (check) {
      for (int i = 0; i < size; i++) {
        task.check(i, (R) result[i]);
      }
    }
    final TimingResult<D, R> r = new TimingResult<>(task, times, false);
    results.add(r);
    return r;
  }

  /**
   * Report the timing results to the output.
   *
   * @param out the output
   */
  public void report(PrintStream out) {
    report(out, results);
  }

  /**
   * Report the last N timing results to the output.
   *
   * @param out the output
   * @param lastN the last N
   */
  public void report(PrintStream out, int lastN) {
    if (lastN <= 0 || results.isEmpty()) {
      return;
    }
    final int to = results.size();
    final int from = to - lastN;
    if (from <= 0) {
      // Report all results
      report(out);
      return;
    }
    report(out, results.subList(from, to));
  }

  /**
   * Report the timing results to the output.
   *
   * @param <D> type of the data
   * @param <R> type of the result
   * @param out the output
   * @param results the results
   */
  public static <D, R> void report(PrintStream out, List<TimingResult<D, R>> results) {
    if (results == null || results.isEmpty()) {
      return;
    }

    final int size = results.size();
    final double[] avs = new double[size];
    final long[] mins = new long[size];

    int width = 0;
    int i = 0;
    for (final TimingResult<?, ?> r : results) {
      final int l = r.getTask().getName().length();
      if (width < l) {
        width = l;
      }

      mins[i] = r.getMin();
      avs[i] = r.getMean();
      i++;
    }
    final String format =
        String.format("%%-%ds : %%15d (%%8.3f)%%c: %%15f (%%8.3f)%%c" + NEW_LINE, width);

    // Find the fastest
    final long min = min(mins);
    final double av = min(avs);

    i = 0;
    for (final TimingResult<?, ?> r : results) {
      // Results relative to the first result
      // Mark the fastest
      final char mc = (mins[i] == min) ? '*' : ' ';
      final char ac = (avs[i] == av) ? '*' : ' ';
      out.printf(format, r.getTask().getName(), mins[i], (double) mins[i] / mins[0], mc, avs[i],
          avs[i] / avs[0], ac);
    }
  }

  /**
   * Get a report with the timing results.
   *
   * <p>A leading new line is prefixed to the report and no final trailing new line is used.
   *
   * @return the report
   */
  public String getReport() {
    return getReport(Integer.MAX_VALUE, true);
  }

  /**
   * Get a report with the timing results.
   *
   * <p>A leading new line is optionally prefixed to the report and no final trailing new line is
   * used.
   *
   * @param leadingNewLine Set to true to add a leading new line
   * @return the report
   */
  public String getReport(boolean leadingNewLine) {
    return getReport(Integer.MAX_VALUE, leadingNewLine);
  }

  /**
   * Get a report with the last n timing results.
   *
   * <p>A leading new line is prefixed to the report and no final trailing new line is used.
   *
   * @param lastN the last N
   * @return the report
   */
  public String getReport(int lastN) {
    return getReport(lastN, true);
  }

  /**
   * Get a report with the last N timing results.
   *
   * <p>A leading new line is optionally prefixed to the report and no final trailing new line is
   * used.
   *
   * @param lastN the last N
   * @param leadingNewLine Set to true to add a leading new line
   * @return the report
   */
  public String getReport(int lastN, boolean leadingNewLine) {
    if (lastN <= 0) {
      return "";
    }
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintStream ps = new PrintStream(baos, false, StandardCharsets.UTF_8.name())) {
      if (leadingNewLine) {
        ps.println();
      }
      report(ps, lastN);
      ps.close();
      return getReport(baos);
    } catch (final UnsupportedEncodingException ex) {
      // This should never happen since the encoding is a requirement of the JVM
      throw new IllegalStateException("UTF-8 is not supported!", ex);
    }
  }

  private static String getReport(ByteArrayOutputStream baos) {
    final String text = new String(baos.toByteArray(), StandardCharsets.UTF_8);
    // Remove the new line at the end
    final int i = text.lastIndexOf(NEW_LINE);
    return (i < 0) ? text : text.substring(0, i);
  }

  private static long min(long[] mins) {
    long min = mins[0];
    for (int i = 1; i < mins.length; i++) {
      min = Math.min(min, mins[i]);
    }
    return min;
  }

  private static double min(double[] mins) {
    double min = mins[0];
    for (int i = 1; i < mins.length; i++) {
      min = Math.min(min, mins[i]);
    }
    return min;
  }

  /**
   * Repeat all the tasks.
   *
   * @return the number repeated
   */
  public int repeat() {
    return repeat(results.size());
  }

  /**
   * Repeat all the tasks up to the given size.
   *
   * @param size the size
   * @return the number repeated
   */
  public int repeat(int size) {
    for (int i = 0; i < size; i++) {
      execute(results.get(i).getTask());
    }
    return size;
  }

  /**
   * Run each task and call the check method.
   */
  public void check() {
    final int size = results.size();
    for (int i = 0; i < size; i++) {
      check(results.get(i).getTask());
    }
  }

  /**
   * Run the task and call the check method on the results.
   *
   * @param <D> type of the data
   * @param <R> type of the result
   * @param task the task
   */
  public static <D, R> void check(TimingTask<D, R> task) {
    final int size = task.getSize();
    for (int i = 0; i < size; i++) {
      final R result = task.run(task.getData(i));
      task.check(i, result);
    }
  }
}

/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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

import java.io.IOException;
import java.io.Serializable;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Class used to log messages.
 */
public final class TestLog {
  /**
   * Extend {@link Level} to add levels for test logging.
   */
  public static class TestLevel extends Level {
    /**
     * Additional level for logging test failures.
     *
     * <p>Equivalent level to {@link Level#SEVERE}.
     */
    public static final Level TEST_FAILURE = new TestLevel("TEST FAILURE", SEVERE.intValue());

    /**
     * Additional level for logging test warnings.
     *
     * <p>Equivalent level to {@link Level#WARNING}.
     */
    public static final Level TEST_WARNING = new TestLevel("TEST WARNING", WARNING.intValue());

    /**
     * Additional level for logging test information.
     *
     * <p>Equivalent level to {@link Level#FINE}.
     */
    public static final Level TEST_INFO = new TestLevel("TEST INFO", FINE.intValue());

    /**
     * Additional level for logging test debugging.
     *
     * <p>Equivalent level to {@link Level#FINER}.
     */
    public static final Level TEST_DEBUG = new TestLevel("TEST DEBUG", FINER.intValue());

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     *
     * @param name Name
     * @param value Value
     */
    public TestLevel(String name, int value) {
      super(name, value);
    }
  }

  /**
   * Extend {@link java.util.logging.LogRecord} to add support for a {@code Supplier<String>} or
   * formatted message.
   */
  private static class TestLogRecord extends LogRecord {
    /** The serial version ID. */
    private static final long serialVersionUID = 1L;
    /** The constant for no supplier. */
    private static final Supplier<String> NO_SUPPLIER = null;
    /** The constant for no format. */
    private static final String NO_FORMAT = null;
    /** The constant for no arguments. */
    private static final Object[] NO_ARGS = null;

    /**
     * The message supplier.
     *
     * <p>This is not serialised so is marked as transient.
     */
    private transient Supplier<String> msgSupplier;

    /**
     * The format string for variable arguments.
     *
     * <p>This is serialised.
     */
    private String format;

    /**
     * The arguments to the format string.
     *
     * <p>This is serialised.
     */
    private Object[] args;

    /**
     * Instantiates a new test log record.
     *
     * @param level the level
     * @param message the message
     */
    TestLogRecord(Level level, Supplier<String> message) {
      super(level, "");
      this.msgSupplier = message;
    }

    /**
     * Instantiates a new test log record.
     *
     * @param level the level
     * @param format the format
     * @param args the arguments (stored by reference)
     */
    TestLogRecord(Level level, String format, Object... args) {
      super(level, "");
      this.format = format;
      this.args = args;
    }

    /**
     * Instantiates a new test log record.
     *
     * @param level the level
     * @param thrown a throwable associated with the log event
     */
    TestLogRecord(Level level, Throwable thrown) {
      super(level, thrown.getMessage());
      setThrown(thrown);
    }

    @Override
    public String getMessage() {
      // First call to this class will create the message.
      // This should only be used when the message is logged.
      if (format != null) {
        setMessage(String.format(format, args));
      } else if (msgSupplier != null) {
        setMessage(msgSupplier.get());
      }
      return super.getMessage();
    }

    @Override
    public void setMessage(String message) {
      // Clear the formatted messages
      format = NO_FORMAT;
      msgSupplier = NO_SUPPLIER;
      super.setMessage(message);
    }

    /**
     * Write the object.
     *
     * <p>This is here to support {@link Serializable}.
     *
     * @param out the object output stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
      // The message supplier cannot be serialised so convert to a format string
      // and empty arguments
      if (msgSupplier != null) {
        format = msgSupplier.get();
        args = NO_ARGS; // These can be null
        msgSupplier = NO_SUPPLIER;
      }
      out.defaultWriteObject();
    }
  }

  /**
   * Do not allow public construction.
   */
  private TestLog() {}

  /**
   * Gets the code point (ClassName:MethodName:LineNumber) marking the position where this method
   * was called.
   *
   * @return the code point
   */
  public static String getCodePoint() {
    final StackTraceElement element = getStackTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(1);
    return getCodePoint(element);
  }

  /**
   * Gets the code point (ClassName:MethodName:LineNumber) from the stack trace element.
   *
   * <p>Returns an empty string if the element is null avoiding a NullPointerException if the
   * element is not available.
   *
   * @param element the stack trace element
   * @return the code point (or empty string if the element is null)
   */
  public static String getCodePoint(StackTraceElement element) {
    if (element == null) {
      return "";
    }
    return String.format("%s:%s:%d:", element.getClassName(), element.getMethodName(),
        element.getLineNumber());
  }

  /**
   * Gets the stack trace element marking the position where this method was called.
   *
   * @return the stack trace element
   */
  public static StackTraceElement getStackTraceElement() {
    return getStackTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(1);
  }

  /**
   * Gets the stack trace element marking the position a set count of elements before where this
   * method was called.
   *
   * @param count the count
   * @return the stack trace element
   * @throws IllegalArgumentException If {@code count < 0}
   */
  public static StackTraceElement getStackTraceElement(int count) throws IllegalArgumentException {
    if (count < 0) {
      throw new IllegalArgumentException("Count must be positive: " + count);
    }
    return getStackTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(count + 1);
  }

  /**
   * Gets the stack trace element marking the call to this method in the stack trace. Optionally
   * offset this by a count of elements to locate a position before this method in the stack trace.
   *
   * @param count the count ({@code >=0})
   * @return the stack trace element
   */
  private static StackTraceElement getStackTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(
      int count) {
    // Based on
    // https://stackoverflow.com/questions/17473148/dynamically-get-the-current-line-number/17473358
    int remaining = count;
    boolean thisMethod = false;
    for (final StackTraceElement element : Thread.currentThread().getStackTrace()) {
      if (thisMethod) {
        if (remaining == 0) {
          return element;
        }
        remaining--;
      } else if (element.getMethodName()
          .equals("getStackTraceElement_499ad503_0184_4099_bf36_65c73b4932d3")) {
        thisMethod = true;
      }
    }
    return null;
  }

  ////////////////////////////////////////////////////
  // Helper functionality for using java.util.logging
  ////////////////////////////////////////////////////

  /**
   * Gets the record to log the message.
   *
   * <p>The message will use lazy construction, only generating the string if passed to a logger
   * that is active at the specified level.
   *
   * @param level the level
   * @param format the format
   * @param args the args
   * @return the log record
   */
  public static LogRecord getRecord(Level level, String format, Object... args) {
    return new TestLogRecord(level, format, args);
  }

  /**
   * Gets the record to log the message.
   *
   * <p>The message will use lazy construction, only generating the string if passed to a logger
   * that is active at the specified level.
   *
   * @param level the level
   * @param message the message
   * @return the log record
   */
  public static LogRecord getRecord(Level level, Supplier<String> message) {
    return new TestLogRecord(level, message);
  }

  /**
   * Gets the record to log the object using {@code String.valueOf(object)}.
   *
   * <p>The message will use lazy construction, only generating the string if passed to a logger
   * that is active at the specified level.
   *
   * @param level the level
   * @param object the object
   * @return the log record
   */
  public static LogRecord getRecord(Level level, Object object) {
    return new TestLogRecord(level, () -> String.valueOf(object));
  }

  /**
   * Gets the record to log the test failed. The message will be written at the
   * {@link TestLevel#TEST_FAILURE} level.
   *
   * <p>This is a helper method for tests that may not always pass so they are easily traced in the
   * log.
   *
   * @param message the message
   * @return the log record
   */
  public static LogRecord getFailRecord(String message) {
    return new LogRecord(TestLevel.TEST_FAILURE, message);
  }

  /**
   * Gets the record to log the test result. The message will be written at the
   * {@link TestLevel#TEST_FAILURE} level.
   *
   * <p>This is a helper method for tests that may not always pass so they are easily traced in the
   * log.
   *
   * <p>The message will use lazy construction, only generating the string if passed to a logger
   * that is active at the specified level.
   *
   * @param format the format
   * @param args the args
   * @return the log record
   */
  public static LogRecord getFailRecord(String format, Object... args) {
    return new TestLogRecord(TestLevel.TEST_FAILURE, format, args);
  }

  /**
   * Gets the record to log the test result. The message will be written at the
   * {@link TestLevel#TEST_FAILURE} level.
   *
   * <p>This is a helper method for tests that may not always pass so they are easily traced in the
   * log.
   *
   * <p>The message will use lazy construction, only generating the string if passed to a logger
   * that is active at the specified level.
   *
   * @param message the message
   * @return the log record
   */
  public static LogRecord getFailRecord(Supplier<String> message) {
    return new TestLogRecord(TestLevel.TEST_FAILURE, message);
  }

  /**
   * Gets the record to log the test result. The message will be written at the
   * {@link TestLevel#TEST_FAILURE} level.
   *
   * <p>This is a helper method for tests that may not always pass so they are easily traced in the
   * log.
   *
   * @param thrown a throwable associated with the test result
   * @return the log record
   */
  public static LogRecord getFailRecord(Throwable thrown) {
    return new TestLogRecord(TestLevel.TEST_FAILURE, thrown);
  }

  /**
   * Gets the record to log the test result. <ul> <li>If true the record will be created at the
   * {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at the
   * {@link TestLevel#TEST_FAILURE} level. </ul>
   *
   * <p>This is a helper method for tests that may not always pass so they are easily traced in the
   * log.
   *
   * @param result the result
   * @param message the message
   * @return the log record
   */
  public static LogRecord getResultRecord(boolean result, String message) {
    final Level l = (result) ? TestLevel.TEST_INFO : TestLevel.TEST_FAILURE;
    return new LogRecord(l, message);
  }

  /**
   * Gets the record to log the test result. <ul> <li>If true the record will be created at the
   * {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at the
   * {@link TestLevel#TEST_FAILURE} level. </ul>
   *
   * <p>This is a helper method for tests that may not always pass so they are easily traced in the
   * log.
   *
   * <p>The message will use lazy construction, only generating the string if passed to a logger
   * that is active at the specified level.
   *
   * @param result the result
   * @param format the format
   * @param args the args
   * @return the log record
   */
  public static LogRecord getResultRecord(boolean result, String format, Object... args) {
    final Level l = (result) ? TestLevel.TEST_INFO : TestLevel.TEST_FAILURE;
    return new TestLogRecord(l, format, args);
  }

  /**
   * Gets the record to log the test result. <ul> <li>If true the record will be created at the
   * {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at the
   * {@link TestLevel#TEST_FAILURE} level. </ul>
   *
   * <p>This is a helper method for tests that may not always pass so they are easily traced in the
   * log.
   *
   * <p>The message will use lazy construction, only generating the string if passed to a logger
   * that is active at the specified level.
   *
   * @param result the result
   * @param message the message
   * @return the log record
   */
  public static LogRecord getResultRecord(boolean result, Supplier<String> message) {
    final Level l = (result) ? TestLevel.TEST_INFO : TestLevel.TEST_FAILURE;
    return new TestLogRecord(l, message);
  }

  /**
   * Gets the record to log the test stage result. <ul> <li>If true the record will be created at
   * the {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at the
   * {@link TestLevel#TEST_WARNING} level. </ul>
   *
   * <p>This is a helper method for tests that may not always pass so they are easily traced in the
   * log.
   *
   * @param result the result
   * @param message the message
   * @return the log record
   */
  public static LogRecord getStageResultRecord(boolean result, String message) {
    final Level l = (result) ? TestLevel.TEST_INFO : TestLevel.TEST_WARNING;
    return new LogRecord(l, message);
  }

  /**
   * Gets the record to log the test stage result. <ul> <li>If true the record will be created at
   * the {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at the
   * {@link TestLevel#TEST_WARNING} level. </ul>
   *
   * <p>This is a helper method for tests that may not always pass so they are easily traced in the
   * log.
   *
   * <p>The message will use lazy construction, only generating the string if passed to a logger
   * that is active at the specified level.
   *
   * @param result the result
   * @param format the format
   * @param args the args
   * @return the log record
   */
  public static LogRecord getStageResultRecord(boolean result, String format, Object... args) {
    final Level l = (result) ? TestLevel.TEST_INFO : TestLevel.TEST_WARNING;
    return new TestLogRecord(l, format, args);
  }

  /**
   * Gets the record to log the test stage result. <ul> <li>If true the record will be created at
   * the {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at the
   * {@link TestLevel#TEST_WARNING} level. </ul>
   *
   * <p>This is a helper method for tests that may not always pass so they are easily traced in the
   * log.
   *
   * <p>The message will use lazy construction, only generating the string if passed to a logger
   * that is active at the specified level.
   *
   * @param result the result
   * @param message the message
   * @return the log record
   */
  public static LogRecord getStageResultRecord(boolean result, Supplier<String> message) {
    final Level l = (result) ? TestLevel.TEST_INFO : TestLevel.TEST_WARNING;
    return new TestLogRecord(l, message);
  }

  /**
   * Get the record to log the result of two timing tasks. A test is made to determine if the fast
   * has a lower time than the slow. <ul> <li>If true the record will be created at the
   * {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at the
   * {@link TestLevel#TEST_FAILURE} level. </ul>
   *
   * <p>This is a helper method for speed tests that may not always pass.
   *
   * @param slow the slow task
   * @param fast the fast task
   * @return the log record
   */
  public static LogRecord getTimingRecord(TimingResult slow, TimingResult fast) {
    return getTimingRecord(slow, fast, false);
  }

  /**
   * Get the record to log the result of two timing tasks. A test is made to determine if the fast
   * has a lower time than the slow. <ul> <li>If true the record will be created at the
   * {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at the
   * {@link TestLevel#TEST_FAILURE} level. </ul>
   *
   * <p>This is a helper method for speed tests that may not always pass.
   *
   * @param slow the slow task
   * @param fast the fast task
   * @param useMin Set to true to use the min execution time (the default is mean)
   * @return the record
   */
  public static LogRecord getTimingRecord(TimingResult slow, TimingResult fast, boolean useMin) {
    final String slowName = slow.getTask().getName();
    final double slowTime = (useMin) ? slow.getMin() : slow.getMean();
    final String fastName = fast.getTask().getName();
    final double fastTime = (useMin) ? fast.getMin() : fast.getMean();
    return getTimingRecord(slowName, slowTime, fastName, fastTime, TestLevel.TEST_INFO,
        TestLevel.TEST_FAILURE);
  }

  /**
   * Get the record to log the result of two tasks. A test is made to determine if the fast has a
   * lower time than the slow. <ul> <li>If true the record will be created at the
   * {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at the
   * {@link TestLevel#TEST_FAILURE} level. </ul>
   *
   * <p>This is a helper method for speed tests that may not always pass.
   *
   * @param slowName the slow task name
   * @param slowTime the slow task time
   * @param fastName the fast task name
   * @param fastTime the fast task time
   * @return the record
   */
  public static LogRecord getTimingRecord(String slowName, long slowTime, String fastName,
      long fastTime) {
    return getTimingRecord(slowName, slowTime, fastName, fastTime, TestLevel.TEST_INFO,
        TestLevel.TEST_FAILURE);
  }

  /**
   * Get the record to log the result of two tasks. A test is made to determine if the fast has a
   * lower time than the slow. <ul> <li>If true the record will be created at the pass level. <li>If
   * false the record will be created at the fail level. </ul>
   *
   * <p>This is a helper method for speed tests that may not always pass.
   *
   * @param slowName the slow task name
   * @param slowTime the slow task time
   * @param fastName the fast task name
   * @param fastTime the fast task time
   * @param passLevel the pass level
   * @param failLevel the fail level
   * @return the record
   */
  public static LogRecord getTimingRecord(String slowName, double slowTime, String fastName,
      double fastTime, Level passLevel, Level failLevel) {
    final Level l = (fastTime <= slowTime) ? passLevel : failLevel;
    return new TestLogRecord(l, "%s (%s) => %s (%s) : %.2fx", slowName, slowTime, fastName,
        fastTime, slowTime / fastTime);
  }

  /**
   * Get the record to log the result of two tasks. A test is made to determine if the fast has a
   * lower time than the slow. <ul> <li>If true the record will be created at the pass level. <li>If
   * false the record will be created at the fail level. </ul>
   *
   * <p>This is a helper method for speed tests that may not always pass.
   *
   * @param slowName the slow task name
   * @param slowTime the slow task time
   * @param fastName the fast task name
   * @param fastTime the fast task time
   * @param passLevel the pass level
   * @param failLevel the fail level
   * @return the record
   */
  public static LogRecord getTimingRecord(String slowName, long slowTime, String fastName,
      long fastTime, Level passLevel, Level failLevel) {
    final Level l = (fastTime <= slowTime) ? passLevel : failLevel;
    return new TestLogRecord(l, "%s (%d) => %s (%d) : %.2fx", slowName, slowTime, fastName,
        fastTime, (double) slowTime / fastTime);
  }

  /**
   * Get the record to log the intermediate stage result of two timing tasks. A test is made to
   * determine if the fast has a lower time than the slow. <ul> <li>If true the record will be
   * created at the {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at
   * the {@link TestLevel#TEST_WARNING} level. </ul>
   *
   * <p>This is a helper method for speed tests that may not always pass.
   *
   * @param slow the slow task
   * @param fast the fast task
   * @return the log record
   */
  public static LogRecord getStageTimingRecord(TimingResult slow, TimingResult fast) {
    return getStageTimingRecord(slow, fast, false);
  }

  /**
   * Get the record to log the intermediate stage result of two timing tasks. A test is made to
   * determine if the fast has a lower time than the slow. <ul> <li>If true the record will be
   * created at the {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at
   * the {@link TestLevel#TEST_WARNING} level. </ul>
   *
   * <p>This is a helper method for speed tests that may not always pass.
   *
   * @param slow the slow task
   * @param fast the fast task
   * @param useMin Set to true to use the min execution time (the default is mean)
   * @return the record
   */
  public static LogRecord getStageTimingRecord(TimingResult slow, TimingResult fast,
      boolean useMin) {
    final String slowName = slow.getTask().getName();
    final double slowTime = (useMin) ? slow.getMin() : slow.getMean();
    final String fastName = fast.getTask().getName();
    final double fastTime = (useMin) ? fast.getMin() : fast.getMean();
    return getTimingRecord(slowName, slowTime, fastName, fastTime, TestLevel.TEST_INFO,
        TestLevel.TEST_WARNING);
  }

  /**
   * Get the record to log the result of two tasks. A test is made to determine if the fast has a
   * lower time than the slow. <ul> <li>If true the record will be created at the
   * {@link TestLevel#TEST_INFO} level. <li>If false the record will be created at the
   * {@link TestLevel#TEST_WARNING} level. </ul>
   *
   * <p>This is a helper method for speed tests that may not always pass.
   *
   * @param slowName the slow task name
   * @param slowTime the slow task time
   * @param fastName the fast task name
   * @param fastTime the fast task time
   * @return the record
   */
  public static LogRecord getStageTimingRecord(String slowName, long slowTime, String fastName,
      long fastTime) {
    return getTimingRecord(slowName, slowTime, fastName, fastTime, TestLevel.TEST_INFO,
        TestLevel.TEST_WARNING);
  }
}

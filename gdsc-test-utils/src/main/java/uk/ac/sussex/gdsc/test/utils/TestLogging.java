/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 - 2022 Alex Herbert
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package uk.ac.sussex.gdsc.test.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Class used to generate {@link LogRecord}s to record test results. Records can be writtin using a
 * {@link java.util.logging.Logger}.
 */
public final class TestLogging {
  /**
   * Extend {@link Level} to add levels for test logging.
   */
  public static final class TestLevel extends Level {
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
    private TestLevel(String name, int value) {
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
     * Creates a new test log record.
     *
     * @param level the level
     * @param message the message
     */
    TestLogRecord(Level level, Supplier<String> message) {
      super(level, "");
      this.msgSupplier = message;
    }

    /**
     * Creates a new test log record.
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
     * Creates a new test log record.
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
  private TestLogging() {}

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
   * Gets the record to log the test result.
   *
   * <ul>
   *
   * <li>If true the record will be created at the {@link TestLevel#TEST_INFO} level.
   *
   * <li>If false the record will be created at the {@link TestLevel#TEST_FAILURE} level.
   *
   * </ul>
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
   * Gets the record to log the test result.
   *
   * <ul>
   *
   * <li>If true the record will be created at the {@link TestLevel#TEST_INFO} level.
   *
   * <li>If false the record will be created at the {@link TestLevel#TEST_FAILURE} level.
   *
   * </ul>
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
   * Gets the record to log the test result.
   *
   * <ul>
   *
   * <li>If true the record will be created at the {@link TestLevel#TEST_INFO} level.
   *
   * <li>If false the record will be created at the {@link TestLevel#TEST_FAILURE} level.
   *
   * </ul>
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
   * Gets the record to log the test stage result.
   *
   * <ul>
   *
   * <li>If true the record will be created at the {@link TestLevel#TEST_INFO} level.
   *
   * <li>If false the record will be created at the {@link TestLevel#TEST_WARNING} level.
   *
   * </ul>
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
   * Gets the record to log the test stage result.
   *
   * <ul>
   *
   * <li>If true the record will be created at the {@link TestLevel#TEST_INFO} level.
   *
   * <li>If false the record will be created at the {@link TestLevel#TEST_WARNING} level.
   *
   * </ul>
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
   * Gets the record to log the test stage result.
   *
   * <ul>
   *
   * <li>If true the record will be created at the {@link TestLevel#TEST_INFO} level.
   *
   * <li>If false the record will be created at the {@link TestLevel#TEST_WARNING} level.
   *
   * </ul>
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
   * Get the record to log the result of two tasks. A test is made to determine if the fast has a
   * lower time than the slow.
   *
   * <ul>
   *
   * <li>If true the record will be created at the {@link TestLevel#TEST_INFO} level.
   *
   * <li>If false the record will be created at the {@link TestLevel#TEST_FAILURE} level.
   *
   * </ul>
   *
   * <p>This is a helper method for timing tests that may not always pass.
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
   * lower time than the slow.
   *
   * <ul>
   *
   * <li>If true the record will be created at the pass level.
   *
   * <li>If false the record will be created at the fail level.
   *
   * </ul>
   *
   * <p>This is a helper method for timing tests that may not always pass.
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
   * lower time than the slow.
   *
   * <ul>
   *
   * <li>If true the record will be created at the pass level.
   *
   * <li>If false the record will be created at the fail level.
   *
   * </ul>
   *
   * <p>This is a helper method for timing tests that may not always pass.
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
   * Get the record to log the result of two tasks. A test is made to determine if the fast has a
   * lower time than the slow.
   *
   * <ul>
   *
   * <li>If true the record will be created at the {@link TestLevel#TEST_INFO} level.
   *
   * <li>If false the record will be created at the {@link TestLevel#TEST_WARNING} level.
   *
   * </ul>
   *
   * <p>This is a helper method for timing tests that may not always pass.
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

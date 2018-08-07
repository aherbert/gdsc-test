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

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Class used to log messages.
 */
public class TestLog
{
    /**
     * Extend {@link java.util.logging.Level} to add levels for test logging.
     */
    public static class TestLevel extends java.util.logging.Level
    {
        /**
         * Additional level for logging test failures.
         * <p>
         * Equivalent level to {@link java.util.logging.Level#SEVERE}.
         */
        public static final Level TEST_FAILURE = new TestLevel("FAILURE", SEVERE.intValue());

        /**
         * Additional level for logging test stage failures.
         * <p>
         * Equivalent level to {@link java.util.logging.Level#WARNING}.
         */
        public static final Level STAGE_FAILURE = new TestLevel("STAGE FAILURE", WARNING.intValue());

        /**
         * Serial version.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Constructor.
         * 
         * @param name
         *            Name
         * @param value
         *            Value
         */
        public TestLevel(String name, int value)
        {
            super(name, value);
        }

        /**
         * Parse a logging level.
         * 
         * @param levelName
         *            Name of level to parse.
         * @return {@link java.util.logging.Level} level
         */
        public static synchronized java.util.logging.Level parse(String levelName)
        {
            // While this is a pass-through to the parent class,
            // it ensures our own level have been added.
            return java.util.logging.Level.parse(levelName);
        }
    }

    /**
     * Extend {@link java.util.logging.LogRecord} to add support for a Supplier<String> or formatted message.
     */
    private static class TestLogRecord extends LogRecord
    {
        /** The serial version ID. */
        private static final long serialVersionUID = 1L;

        private Supplier<String> msgSupplier;
        private String format;
        private Object[] args;

        /**
         * Instantiates a new test log record.
         *
         * @param level
         *            the level
         * @param msg
         *            the msg
         */
        public TestLogRecord(Level level, String msg)
        {
            super(level, msg);
        }

        /**
         * Instantiates a new test log record.
         *
         * @param level
         *            the level
         * @param message
         *            the message
         */
        public TestLogRecord(Level level, Supplier<String> message)
        {
            super(level, "");
            this.msgSupplier = message;
        }

        /**
         * Instantiates a new test log record.
         *
         * @param level
         *            the level
         * @param format
         *            the format
         * @param args
         *            the arguments
         */
        public TestLogRecord(Level level, String format, Object... args)
        {
            super(level, "");
            this.format = format;
            this.args = args;
        }

        @Override
        public String getMessage()
        {
            // First call to this class will create the message.
            // This should only be used when the message is logged.
            if (format != null)
                setMessage(String.format(format, args));
            else if (msgSupplier != null)
                setMessage(msgSupplier.get());
            return super.getMessage();
        }

        @Override
        public void setMessage(String message)
        {
            // Clear the formatted messages
            format = null;
            msgSupplier = null;
            super.setMessage(message);
        }
    }

    /**
     * Gets the code point (ClassName:MethodName:LineNumber) marking the position where this method was called.
     *
     * @return the code point
     */
    public static String getCodePoint()
    {
        final StackTraceElement e = ___getStaceTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(1);
        return (e == null) ? "" : String.format("%s:%s:%d:", e.getClassName(), e.getMethodName(), e.getLineNumber());
    }

    /**
     * Gets the stacktrace element marking the position where this method was called.
     *
     * @return the stacktrace element
     */
    public static StackTraceElement getStaceTraceElement()
    {
        return ___getStaceTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(1);
    }

    /**
     * Gets the stace trace element 499 ad 503 0184 4099 bf 36 65 c 73 b 4932 d 3.
     *
     * @param countDown
     *            the count down
     * @return the stack trace element
     */
    private static StackTraceElement ___getStaceTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(int countDown)
    {
        // Based on https://stackoverflow.com/questions/17473148/dynamically-get-the-current-line-number/17473358
        boolean thisMethod = false;
        final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < elements.length; i++)
        {
            final StackTraceElement e = elements[i];
            if (thisMethod)
            {
                if (countDown-- == 0)
                    return e;
            }
            else if (e.getMethodName().equals("___getStaceTraceElement_499ad503_0184_4099_bf36_65c73b4932d3"))
            {
                thisMethod = true;
            }
        }
        return null;
    }

    ///////////////////////////////////////////
    // Helper functionality for using a Logger
    ///////////////////////////////////////////

    /**
     * Get a supplier for the string using the format and arguments.
     * <p>
     * This can be used where it is not convenient to create a lambda function directly because the arguments are not
     * effectively final. Returns:
     *
     * <pre>
     * <code>
     * () -> String.format(format, args);
     * </code>
     * </pre>
     *
     * @param format
     *            the format
     * @param args
     *            the arguments
     * @return the supplier
     */
    public static final Supplier<String> getSupplier(final String format, final Object... args)
    {
        return () -> String.format(format, args);
    }

    /**
     * Gets the record to log the message.
     * <p>
     * The message will use lazy construction, only generating the string if passed
     * to a logger that is active at the specified level.
     *
     * @param level
     *            the level
     * @param format
     *            the format
     * @param args
     *            the args
     * @return the log record
     */
    public static LogRecord getRecord(Level level, String format, Object... args)
    {
        return new TestLogRecord(level, format, args);
    }

    /**
     * Gets the record to log the message.
     * <p>
     * The message will use lazy construction, only generating the string if passed
     * to a logger that is active at the specified level.
     * 
     * @param level
     *            the level
     * @param message
     *            the message
     * @return the log record
     */
    public static LogRecord getRecord(Level level, Supplier<String> message)
    {
        return new TestLogRecord(level, message);
    }

    /**
     * Gets the record to log the object using {@code String.valueOf(object)}.
     * <p>
     * The message will use lazy construction, only generating the string if passed
     * to a logger that is active at the specified level.
     *
     * @param level
     *            the level
     * @param object
     *            the object
     * @return the log record
     */
    public static LogRecord getRecord(Level level, Object object)
    {
        return new TestLogRecord(level, () -> String.valueOf(object));
    }

    /**
     * Gets the record to log the test failed. The message will be written at the
     * {@link TestLevel#TEST_FAILURE} level.
     * <p>
     * This is a helper method for tests that may not always pass so they are
     * easily traced in the log.
     *
     * @param message
     *            the message
     * @return the log record
     */
    public static LogRecord getFailRecord(String message)
    {
        return new LogRecord(TestLevel.TEST_FAILURE, message);
    }

    /**
     * Gets the record to log the test result. The message will be written at the
     * {@link TestLevel#TEST_FAILURE} level.
     * <p>
     * This is a helper method for tests that may not always pass so they are
     * easily traced in the log.
     * <p>
     * The message will use lazy construction, only generating the string if passed
     * to a logger that is active at the specified level.
     *
     * @param format
     *            the format
     * @param args
     *            the args
     * @return the log record
     */
    public static LogRecord getFailRecord(String format, Object... args)
    {
        return new TestLogRecord(TestLevel.TEST_FAILURE, format, args);
    }

    /**
     * Gets the record to log the test result. The message will be written at the
     * {@link TestLevel#TEST_FAILURE} level.
     * <p>
     * This is a helper method for tests that may not always pass so they are
     * easily traced in the log.
     * <p>
     * The message will use lazy construction, only generating the string if passed
     * to a logger that is active at the specified level.
     *
     * @param message
     *            the message
     * @return the log record
     */
    public static LogRecord getFailRecord(Supplier<String> message)
    {
        return new TestLogRecord(TestLevel.TEST_FAILURE, message);
    }

    /**
     * Gets the record to log the test result.
     * <p>
     * <ul>
     * <li>If true the record will be created at the {@link Level#INFO} level.
     * <li>If false the record will be created at the {@link TestLevel#TEST_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for tests that may not always pass so they are
     * easily traced in the log.
     *
     * @param result
     *            the result
     * @param message
     *            the message
     * @return the log record
     */
    public static LogRecord getRecord(boolean result, String message)
    {
        Level l = (result) ? Level.INFO : TestLevel.TEST_FAILURE;
        return new LogRecord(l, message);
    }

    /**
     * Gets the record to log the test result.
     * <p>
     * <ul>
     * <li>If true the record will be created at the {@link Level#INFO} level.
     * <li>If false the record will be created at the {@link TestLevel#TEST_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for tests that may not always pass so they are
     * easily traced in the log.
     * <p>
     * The message will use lazy construction, only generating the string if passed
     * to a logger that is active at the specified level.
     *
     * @param result
     *            the result
     * @param format
     *            the format
     * @param args
     *            the args
     * @return the log record
     */
    public static LogRecord getRecord(boolean result, String format, Object... args)
    {
        Level l = (result) ? Level.INFO : TestLevel.TEST_FAILURE;
        return new TestLogRecord(l, format, args);
    }

    /**
     * Gets the record to log the test result.
     * <p>
     * <ul>
     * <li>If true the record will be created at the {@link Level#INFO} level.
     * <li>If false the record will be created at the {@link TestLevel#TEST_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for tests that may not always pass so they are
     * easily traced in the log.
     * <p>
     * The message will use lazy construction, only generating the string if passed
     * to a logger that is active at the specified level.
     *
     * @param result
     *            the result
     * @param message
     *            the message
     * @return the log record
     */
    public static LogRecord getRecord(boolean result, Supplier<String> message)
    {
        Level l = (result) ? Level.INFO : TestLevel.TEST_FAILURE;
        return new TestLogRecord(l, message);
    }

    /**
     * Gets the record to log the test stage result.
     * <p>
     * <ul>
     * <li>If true the record will be created at the {@link Level#INFO} level.
     * <li>If false the record will be created at the {@link TestLevel#STAGE_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for tests that may not always pass so they are
     * easily traced in the log.
     *
     * @param result
     *            the result
     * @param message
     *            the message
     * @return the log record
     */
    public static LogRecord getStageRecord(boolean result, String message)
    {
        Level l = (result) ? Level.INFO : TestLevel.STAGE_FAILURE;
        return new LogRecord(l, message);
    }

    /**
     * Gets the record to log the test stage result.
     * <p>
     * <ul>
     * <li>If true the record will be created at the {@link Level#INFO} level.
     * <li>If false the record will be created at the {@link TestLevel#STAGE_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for tests that may not always pass so they are
     * easily traced in the log.
     * <p>
     * The message will use lazy construction, only generating the string if passed
     * to a logger that is active at the specified level.
     *
     * @param result
     *            the result
     * @param format
     *            the format
     * @param args
     *            the args
     * @return the log record
     */
    public static LogRecord getStageRecord(boolean result, String format, Object... args)
    {
        Level l = (result) ? Level.INFO : TestLevel.STAGE_FAILURE;
        return new TestLogRecord(l, format, args);
    }

    /**
     * Gets the record to log the test stage result.
     * <p>
     * <ul>
     * <li>If true the record will be created at the {@link Level#INFO} level.
     * <li>If false the record will be created at the {@link TestLevel#STAGE_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for tests that may not always pass so they are
     * easily traced in the log.
     * <p>
     * The message will use lazy construction, only generating the string if passed
     * to a logger that is active at the specified level.
     *
     * @param result
     *            the result
     * @param message
     *            the message
     * @return the log record
     */
    public static LogRecord getStageRecord(boolean result, Supplier<String> message)
    {
        Level l = (result) ? Level.INFO : TestLevel.STAGE_FAILURE;
        return new TestLogRecord(l, message);
    }

    /**
     * Get the record to log the result of two timing tasks.
     * A test is made to determine if the fast has a lower time than the
     * slow.
     * <p>
     * <ul>
     * <li>If true the record will be created at the {@link Level#INFO} level.
     * <li>If false the record will be created at the {@link TestLevel#TEST_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for speed tests that may not always pass.
     *
     * @param slow
     *            the slow task
     * @param fast
     *            the fast task
     * @return the log record
     */
    public static LogRecord getRecord(TimingResult slow, TimingResult fast)
    {
        return getRecord(slow, fast, false);
    }

    /**
     * Get the record to log the result of two timing tasks.
     * A test is made to determine if the fast has a lower time than the
     * slow.
     * <p>
     * <ul>
     * <li>If true the record will be created at the {@link Level#INFO} level.
     * <li>If false the record will be created at the {@link TestLevel#TEST_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for speed tests that may not always pass.
     *
     * @param slow
     *            the slow task
     * @param fast
     *            the fast task
     * @param useMin
     *            Set to true to use the min execution time (the default is mean)
     * @return the record
     */
    public static LogRecord getRecord(TimingResult slow, TimingResult fast, boolean useMin)
    {
        final double t1 = (useMin) ? fast.getMin() : fast.getMean();
        final double t2 = (useMin) ? slow.getMin() : slow.getMean();
        Level l = (t1 <= t2) ? Level.INFO : TestLevel.TEST_FAILURE;
        return new TestLogRecord(l, "%s (%s) => %s (%s) : %.2fx", slow.getTask().getName(), t2,
                fast.getTask().getName(), t1, t2 / t1);
    }

    /**
     * Get the record to log the stage result of two timing tasks.
     * A test is made to determine if the fast has a lower time than the
     * slow.
     * <p>
     * <ul>
     * <li>If true the record will be created at the {@link Level#INFO} level.
     * <li>If false the record will be created at the {@link TestLevel#STAGE_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for speed tests that may not always pass.
     *
     * @param slow
     *            the slow task
     * @param fast
     *            the fast task
     * @return the log record
     */
    public static LogRecord getStageRecord(TimingResult slow, TimingResult fast)
    {
        return getStageRecord(slow, fast, false);
    }

    /**
     * Get the record to log the stage result of two timing tasks.
     * A test is made to determine if the fast has a lower time than the
     * slow.
     * <p>
     * <ul>
     * <li>If true the record will be created at the {@link Level#INFO} level.
     * <li>If false the record will be created at the {@link TestLevel#STAGE_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for speed tests that may not always pass.
     *
     * @param slow
     *            the slow task
     * @param fast
     *            the fast task
     * @param useMin
     *            Set to true to use the min execution time (the default is mean)
     * @return the record
     */
    public static LogRecord getStageRecord(TimingResult slow, TimingResult fast, boolean useMin)
    {
        final double t1 = (useMin) ? fast.getMin() : fast.getMean();
        final double t2 = (useMin) ? slow.getMin() : slow.getMean();
        Level l = (t1 <= t2) ? Level.INFO : TestLevel.STAGE_FAILURE;
        return new TestLogRecord(l, "%s (%s) => %s (%s) : %.2fx", slow.getTask().getName(), t2,
                fast.getTask().getName(), t1, t2 / t1);
    }
}

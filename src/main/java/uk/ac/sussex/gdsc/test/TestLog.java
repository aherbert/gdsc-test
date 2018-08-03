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
import java.util.logging.Logger;

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
     * Gets the code point: ClassName:MethodName:LineNumber for the method that initialised the error.
     *
     * @param countDown
     *            the count down
     * @return the code point
     */
    private static String getCodePoint(int countDown)
    {
        final StackTraceElement e = getStaceTraceElement(countDown);
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
     * Gets the stacktrace element marking the position where this method was called.
     *
     * @param countDown
     *            the count down
     * @return the stacktrace element
     */
    private static StackTraceElement getStaceTraceElement(int countDown)
    {
        return ___getStaceTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(countDown);
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
     * Log a message to the logger.
     * <p>
     * Note: This is a special function using varargs.<br>
     * To pass an arguments array to the logger use: {@code logger.log(level, format, args)}.
     *
     * @param logger
     *            the logger
     * @param level
     *            the level
     * @param format
     *            the format
     * @param args
     *            the arguments
     * @deprecated Use e.g. logger.log(Level.INFO, TestLog.getSupplier(format, arg1, arg2, ...));
     */
    public static final void log(Logger logger, Level level, final String format, final Object... args)
    {
        if (logger.isLoggable(level))
        {
            logger.log(level, String.format(format, args));
        }
    }

    /**
     * Log the value of an object to the logger.
     * <p>
     * First calls {@code String.valueOf(Object)} to get the string value.
     *
     * @param logger
     *            the logger
     * @param level
     *            the level
     * @param object
     *            the object
     * @deprecated Use e.g. logger.log(Level.INFO, () -> String.valueOf(object));
     */
    public static final void log(Logger logger, Level level, final Object object)
    {
        if (logger.isLoggable(level))
        {
            logger.log(level, String.valueOf(object));
        }
    }

    /**
     * Log a message to the logger at the {@link Level#SEVERE} level.
     * <p>
     * Note: This is a special function using varargs.<br>
     * To pass an arguments array to the logger use: {@code logger.log(level, format, args)}.
     *
     * @param logger
     *            the logger
     * @param format
     *            the format
     * @param args
     *            the arguments
     * @deprecated Use e.g. logger.log(Level.INFO, TestLog.getSupplier(format, arg1, arg2, ...));
     */
    public static final void severe(Logger logger, final String format, final Object... args)
    {
        log(logger, Level.SEVERE, format, args);
    }

    /**
     * Log a message to the logger at the {@link Level#WARNING} level.
     * <p>
     * Note: This is a special function using varargs.<br>
     * To pass an arguments array to the logger use: {@code logger.log(level, format, args)}.
     *
     * @param logger
     *            the logger
     * @param format
     *            the format
     * @param args
     *            the arguments
     * @deprecated Use e.g. logger.log(Level.INFO, TestLog.getSupplier(format, arg1, arg2, ...));
     */
    public static final void warning(Logger logger, final String format, final Object... args)
    {
        log(logger, Level.WARNING, format, args);
    }

    /**
     * Log a message to the logger at the {@link Level#INFO} level.
     * <p>
     * Note: This is a special function using varargs.<br>
     * To pass an arguments array to the logger use: {@code logger.log(level, format, args)}.
     *
     * @param logger
     *            the logger
     * @param format
     *            the format
     * @param args
     *            the arguments
     * @deprecated Use e.g. logger.log(Level.INFO, TestLog.getSupplier(format, arg1, arg2, ...));
     */
    public static final void info(Logger logger, final String format, final Object... args)
    {
        log(logger, Level.INFO, format, args);
    }

    /**
     * Log a message to the logger at the {@link Level#FINE} level.
     * <p>
     * Note: This is a special function using varargs.<br>
     * To pass an arguments array to the logger use: {@code logger.log(level, format, args)}.
     *
     * @param logger
     *            the logger
     * @param format
     *            the format
     * @param args
     *            the arguments
     * @deprecated Use e.g. logger.log(Level.INFO, TestLog.getSupplier(format, arg1, arg2, ...));
     */
    public static final void fine(Logger logger, final String format, final Object... args)
    {
        log(logger, Level.FINE, format, args);
    }

    /**
     * Log a message to the logger at the {@link Level#FINER} level.
     * <p>
     * Note: This is a special function using varargs.<br>
     * To pass an arguments array to the logger use: {@code logger.log(level, format, args)}.
     *
     * @param logger
     *            the logger
     * @param format
     *            the format
     * @param args
     *            the arguments
     * @deprecated Use e.g. logger.log(Level.INFO, TestLog.getSupplier(format, arg1, arg2, ...));
     */
    public static final void finer(Logger logger, final String format, final Object... args)
    {
        log(logger, Level.FINER, format, args);
    }

    /**
     * Log a message to the logger at the {@link Level#FINEST} level.
     * <p>
     * Note: This is a special function using varargs.<br>
     * To pass an arguments array to the logger use: {@code logger.log(level, format, args)}.
     *
     * @param logger
     *            the logger
     * @param format
     *            the format
     * @param args
     *            the arguments
     * @deprecated Use e.g. logger.log(Level.INFO, TestLog.getSupplier(format, arg1, arg2, ...));
     */
    public static final void finest(Logger logger, final String format, final Object... args)
    {
        log(logger, Level.FINEST, format, args);
    }

    /**
     * Log the test result. If true the message will be written at the {@link Level#INFO}
     * level. If false the message will be written at the {@link Level#SEVERE} level with a failure prefix.
     * <p>
     * This is a helper method for tests that may not always pass.
     *
     * @param logger
     *            the logger
     * @param result
     *            the result
     * @param message
     *            the message
     */
    public static void logTestResult(Logger logger, boolean result, String message)
    {
        Level l;
        if (result)
        {
            l = Level.INFO;
        }
        else
        {
            l = Level.SEVERE;
            message = getCodePoint(3) + "Test Failure: " + message;
        }
        log(logger, l, message);
    }

    /**
     * Log the test result. If true the message will be written at the {@link Level#INFO}
     * level. If false the message will be written at the {@link Level#SEVERE} level with a failure prefix.
     * <p>
     * This is a helper method for tests that may not always pass.
     *
     * @param logger
     *            the logger
     * @param result
     *            the result
     * @param format
     *            the format
     * @param args
     *            the arguments
     */
    public static void logTestResult(Logger logger, boolean result, String format, Object... args)
    {
        Level l;
        if (result)
        {
            l = Level.INFO;
        }
        else
        {
            l = Level.SEVERE;
            format = getCodePoint(3) + "Test Failure: " + format;
        }
        log(logger, l, format, args);
    }

    /**
     * Gets the record to log the test result.
     * <p>
     * <ul>
     * <li>If true the message will be written at the {@link Level#INFO} level.
     * <li>If false the message will be written at the {@link TestLevel#TEST_FAILURE} level.
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
        Level l;
        if (result)
        {
            l = Level.INFO;
        }
        else
        {
            l = TestLevel.TEST_FAILURE;
        }
        return new LogRecord(l, message);
    }

    /**
     * Gets the record to log the test result.
     * <p>
     * <ul>
     * <li>If true the message will be written at the {@link Level#INFO} level.
     * <li>If false the message will be written at the {@link TestLevel#TEST_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for tests that may not always pass so they are
     * easily traced in the log.
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
        Level l;
        if (result)
        {
            l = Level.INFO;
        }
        else
        {
            l = TestLevel.TEST_FAILURE;
        }
        return new TestLogRecord(l, format, args);
    }

    /**
     * Gets the record to log the test result.
     * <p>
     * <ul>
     * <li>If true the message will be written at the {@link Level#INFO} level.
     * <li>If false the message will be written at the {@link TestLevel#TEST_FAILURE} level.
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
    public static LogRecord getRecord(boolean result, Supplier<String> message)
    {
        Level l;
        if (result)
        {
            l = Level.INFO;
        }
        else
        {
            l = TestLevel.TEST_FAILURE;
        }
        return new TestLogRecord(l, message);
    }

    /**
     * Gets the record to log the test stage result.
     * <p>
     * <ul>
     * <li>If true the message will be written at the {@link Level#INFO} level.
     * <li>If false the message will be written at the {@link TestLevel#STAGE_FAILURE} level.
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
        Level l;
        if (result)
        {
            l = Level.INFO;
        }
        else
        {
            l = TestLevel.STAGE_FAILURE;
        }
        return new LogRecord(l, message);
    }

    /**
     * Gets the record to log the test stage result.
     * <p>
     * <ul>
     * <li>If true the message will be written at the {@link Level#INFO} level.
     * <li>If false the message will be written at the {@link TestLevel#STAGE_FAILURE} level.
     * </ul>
     * <p>
     * This is a helper method for tests that may not always pass so they are
     * easily traced in the log.
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
        Level l;
        if (result)
        {
            l = Level.INFO;
        }
        else
        {
            l = TestLevel.STAGE_FAILURE;
        }
        return new TestLogRecord(l, format, args);
    }

    /**
     * Gets the record to log the test stage result.
     * <p>
     * <ul>
     * <li>If true the message will be written at the {@link Level#INFO} level.
     * <li>If false the message will be written at the {@link TestLevel#STAGE_FAILURE} level.
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
    public static LogRecord getStageRecord(boolean result, Supplier<String> message)
    {
        Level l;
        if (result)
        {
            l = Level.INFO;
        }
        else
        {
            l = TestLevel.STAGE_FAILURE;
        }
        return new TestLogRecord(l, message);
    }

    /**
     * Log the speed test result of two timing tasks. A test is made to determine if the fast has a lower time than the
     * slow.
     * <p>
     * If true the message will be written at the {@link Level#INFO} level. If false the message will be written at
     * the {@link Level#SEVERE} level with a failure prefix.
     * <p>
     * This is a helper method for speed tests that may not always pass.
     *
     * @param logger
     *            the logger
     * @param slow
     *            the slow task
     * @param fast
     *            the fast task
     */
    public static void logSpeedTestResult(Logger logger, TimingResult slow, TimingResult fast)
    {
        final double t1 = fast.getMean();
        final double t2 = slow.getMean();
        Level l;
        String message;
        if (t1 <= t2)
        {
            l = Level.INFO;
            if (!logger.isLoggable(l))
                return;
            message = "%s (%s) => %s (%s) : %.2fx";
        }
        else
        {
            l = Level.SEVERE;
            message = getCodePoint(3) + "Speed-Test Failure: %s (%s) => %s (%s) : %.2fx";
        }
        log(logger, l, message, slow.getTask().getName(), t2, fast.getTask().getName(), t1, t2 / t1);
    }

    /**
     * Log the speed test result of two timing tasks. A test is made to determine if the fast has a lower time than the
     * slow.
     * <p>
     * If true the message will be written at the {@link Level#INFO} level. If false the message will be written at
     * the {@link Level#SEVERE} level with a failure prefix.
     * <p>
     * This is a helper method for speed tests that may not always pass.
     *
     * @param logger
     *            the logger
     * @param slow
     *            the slow task
     * @param fast
     *            the fast task
     * @param useMin
     *            Set to true to use the min execution time (the default is mean)
     */
    public static void logSpeedTestResult(Logger logger, TimingResult slow, TimingResult fast, boolean useMin)
    {
        final double t1 = (useMin) ? fast.getMin() : fast.getMean();
        final double t2 = (useMin) ? slow.getMin() : slow.getMean();
        Level l;
        String message;
        if (t1 <= t2)
        {
            l = Level.INFO;
            if (!logger.isLoggable(l))
                return;
            message = "%s (%s) => %s (%s) : %.2fx";
        }
        else
        {
            l = Level.SEVERE;
            message = getCodePoint(3) + "Speed-Test Failure: %s (%s) => %s (%s) : %.2fx";
        }
        log(logger, l, message, slow.getTask().getName(), t2, fast.getTask().getName(), t1, t2 / t1);
    }

    /**
     * Log the test intermediate stage result. If true the message will be written at the {@link Level#INFO}
     * level. If false the message will be written at the {@link Level#WARNING} level with a failure prefix.
     * <p>
     * This is a helper method for tests that may not always pass.
     *
     * @param logger
     *            the logger
     * @param result
     *            the result
     * @param message
     *            the message
     */
    public static void logTestStageResult(Logger logger, boolean result, String message)
    {
        Level l;
        if (result)
        {
            l = Level.INFO;
        }
        else
        {
            l = Level.WARNING;
            if (!logger.isLoggable(l)) // Avoid getting the stack trace if not logging
                return;
            message = getCodePoint(3) + "Test Stage Failure: " + message;
        }
        log(logger, l, message);
    }

    /**
     * Log the test intermediate stage result. If true the message will be written at the {@link Level#INFO}
     * level. If false the message will be written at the {@link Level#WARNING} level with a failure prefix.
     * <p>
     * This is a helper method for tests that may not always pass.
     *
     * @param logger
     *            the logger
     * @param result
     *            the result
     * @param format
     *            the format
     * @param args
     *            the arguments
     */
    public static void logTestStageResult(Logger logger, boolean result, String format, Object... args)
    {
        Level l;
        if (result)
        {
            l = Level.INFO;
        }
        else
        {
            l = Level.WARNING;
            if (!logger.isLoggable(l)) // Avoid getting the stack trace if not logging
                return;
            format = getCodePoint(3) + "Test Stage Failure: " + format;
        }
        log(logger, l, format, args);
    }

    /**
     * Log the speed test stage result of two timing tasks. A test is made to determine if the fast has a lower time
     * than the
     * slow.
     * <p>
     * If true the message will be written at the {@link Level#WARNING} level. If false the message will be written at
     * the {@link Level#SEVERE} level with a failure prefix.
     * <p>
     * This is a helper method for speed tests that may not always pass.
     *
     * @param logger
     *            the logger
     * @param slow
     *            the slow task
     * @param fast
     *            the fast task
     */
    public static void logSpeedTestStageResult(Logger logger, TimingResult slow, TimingResult fast)
    {
        final double t1 = fast.getMean();
        final double t2 = slow.getMean();
        Level l;
        String message;
        if (t1 <= t2)
        {
            l = Level.INFO;
            if (!logger.isLoggable(l))
                return;
            message = "%s (%s) => %s (%s) : %.2fx";
        }
        else
        {
            l = Level.WARNING;
            if (!logger.isLoggable(l))
                return;
            message = getCodePoint(3) + "Speed-Test Failure: %s (%s) => %s (%s) : %.2fx";
        }
        log(logger, l, message, slow.getTask().getName(), t2, fast.getTask().getName(), t1, t2 / t1);
    }

    /**
     * Log the speed test stage result of two timing tasks. A test is made to determine if the fast has a lower time
     * than the
     * slow.
     * <p>
     * If true the message will be written at the {@link Level#WARNING} level. If false the message will be written at
     * the {@link Level#SEVERE} level with a failure prefix.
     * <p>
     * This is a helper method for speed tests that may not always pass.
     *
     * @param logger
     *            the logger
     * @param slow
     *            the slow task
     * @param fast
     *            the fast task
     * @param useMin
     *            Set to true to use the min execution time (the default is mean)
     */
    public static void logSpeedTestStageResult(Logger logger, TimingResult slow, TimingResult fast, boolean useMin)
    {
        final double t1 = (useMin) ? fast.getMin() : fast.getMean();
        final double t2 = (useMin) ? slow.getMin() : slow.getMean();
        Level l;
        String message;
        if (t1 <= t2)
        {
            l = Level.INFO;
            if (!logger.isLoggable(l))
                return;
            message = "%s (%s) => %s (%s) : %.2fx";
        }
        else
        {
            l = Level.WARNING;
            if (!logger.isLoggable(l))
                return;
            message = getCodePoint(3) + "Speed-Test Failure: %s (%s) => %s (%s) : %.2fx";
        }
        log(logger, l, message, slow.getTask().getName(), t2, fast.getTask().getName(), t1, t2 / t1);
    }

    /**
     * Log a failure at the {@link Level#SEVERE} level.
     * <p>
     * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
     *
     * @param logger
     *            the logger
     */
    public static void logFailure(Logger logger)
    {
        log(logger, Level.SEVERE, getCodePoint(3) + "Failure");
    }

    /**
     * Log a failure at the {@link Level#SEVERE} level.
     * <p>
     * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
     *
     * @param logger
     *            the logger
     * @param t
     *            the throwable that caused the failure
     */
    public static void logFailure(Logger logger, Throwable t)
    {
        final String msg = (t == null) ? null : t.getMessage();
        if (msg == null || msg.length() == 0)
        {
            log(logger, Level.SEVERE, getCodePoint(3) + "Failure");
        }
        else
        {
            log(logger, Level.SEVERE, getCodePoint(3) + "Failure: " + msg);
        }
    }

    /**
     * Log a failure at the {@link Level#SEVERE} level.
     * <p>
     * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
     *
     * @param logger
     *            the logger
     * @param message
     *            the message
     */
    public static void logFailure(Logger logger, String message)
    {
        log(logger, Level.SEVERE, getCodePoint(3) + "Failure: " + message);
    }

    /**
     * Log a failure at the {@link Level#SEVERE} level.
     * <p>
     * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
     *
     * @param logger
     *            the logger
     * @param t
     *            the throwable that caused the failure
     * @param message
     *            the message
     */
    public static void logFailure(Logger logger, Throwable t, String message)
    {
        final String msg = (t == null) ? null : t.getMessage();
        if (msg == null || msg.length() == 0)
        {
            log(logger, Level.SEVERE, getCodePoint(3) + "Failure: " + message);
        }
        else
        {
            log(logger, Level.SEVERE, getCodePoint(3) + "Failure: " + msg + " : " + message);
        }
    }

    /**
     * Log a failure at the {@link Level#SEVERE} level.
     * <p>
     * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
     *
     * @param logger
     *            the logger
     * @param format
     *            the format
     * @param args
     *            the arguments
     */
    public static void logFailure(Logger logger, String format, Object... args)
    {
        log(logger, Level.SEVERE, getCodePoint(3) + "Failure: " + format, args);
    }

    /**
     * Log a failure at the {@link Level#SEVERE} level.
     * <p>
     * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
     *
     * @param logger
     *            the logger
     * @param t
     *            the throwable that caused the failure
     * @param format
     *            the format
     * @param args
     *            the arguments
     */
    public static void logFailure(Logger logger, Throwable t, String format, Object... args)
    {
        final String msg = (t == null) ? null : t.getMessage();
        if (msg == null || msg.length() == 0)
        {
            log(logger, Level.SEVERE, getCodePoint(3) + "Failure: " + format, args);
        }
        else
        {
            log(logger, Level.SEVERE, getCodePoint(3) + "Failure: " + msg + " : " + format, args);
        }
    }
}

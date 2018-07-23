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

/**
 * Class used to log messages.
 */
public class TestLog
{
	/**
	 * Log the message using the format and arguments.
	 *
	 * @param level
	 *            the level
	 * @param format
	 *            the format
	 * @param args
	 *            the arguments
	 */
	public static void log(LogLevel level, String format, Object... args)
	{
		if (TestSettings.allow(level))
			System.out.printf(format, args);
	}

	/**
	 * Log the message.
	 *
	 * @param level
	 *            the level
	 * @param msg
	 *            the message
	 */
	public static void log(LogLevel level, MessageProvider msg)
	{
		if (TestSettings.allow(level))
			System.out.print(msg.getMessage());
	}

	/**
	 * Log the message using the format and arguments.
	 *
	 * @param level
	 *            the level
	 * @param msg
	 *            the message
	 */
	public static void log(LogLevel level, Message msg)
	{
		if (TestSettings.allow(level))
			System.out.printf(msg.format, msg.getArgs());
	}

	/**
	 * Log the string value of the object.
	 *
	 * @param level
	 *            the level
	 * @param object
	 *            the object
	 */
	public static void log(LogLevel level, Object object)
	{
		if (TestSettings.allow(level))
			System.out.print(object);
	}

	/**
	 * Log with newline the message.
	 *
	 * @param level
	 *            the level
	 * @param message
	 *            the message
	 */
	public static void logln(LogLevel level, String message)
	{
		if (TestSettings.allow(level))
			System.out.println(message);
	}

	/**
	 * Log with newline the string value of the object.
	 *
	 * @param level
	 *            the level
	 * @param object
	 *            the object
	 */
	public static void logln(LogLevel level, Object object)
	{
		if (TestSettings.allow(level))
			System.out.println(object);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Log at the {@link LogLevel#DEBUG} level the message using the format and arguments.
	 *
	 * @param format
	 *            the format
	 * @param args
	 *            the arguments
	 */
	public static void debug(String format, Object... args)
	{
		log(LogLevel.DEBUG, format, args);
	}

	/**
	 * Log at the {@link LogLevel#DEBUG} level the message.
	 *
	 * @param msg
	 *            the message
	 */
	public static void debug(MessageProvider msg)
	{
		log(LogLevel.DEBUG, msg);
	}

	/**
	 * Log at the {@link LogLevel#DEBUG} level the message using the format and arguments.
	 *
	 * @param msg
	 *            the message
	 */
	public static void debug(Message msg)
	{
		log(LogLevel.DEBUG, msg);
	}

	/**
	 * Log at the {@link LogLevel#DEBUG} level the string value of the object.
	 *
	 * @param object
	 *            the object
	 */
	public static void debug(Object object)
	{
		log(LogLevel.DEBUG, object);
	}

	/**
	 * Log at the {@link LogLevel#DEBUG} level with newline the message.
	 *
	 * @param message
	 *            the message
	 */
	public static void debugln(String message)
	{
		logln(LogLevel.DEBUG, message);
	}

	/**
	 * Log at the {@link LogLevel#DEBUG} level with newline the string value of the object.
	 *
	 * @param object
	 *            the object
	 */
	public static void debugln(Object object)
	{
		logln(LogLevel.DEBUG, object);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Log at the {@link LogLevel#INFO} level the message using the format and arguments.
	 *
	 * @param format
	 *            the format
	 * @param args
	 *            the arguments
	 */
	public static void info(String format, Object... args)
	{
		log(LogLevel.INFO, format, args);
	}

	/**
	 * Log at the {@link LogLevel#INFO} level the message.
	 *
	 * @param msg
	 *            the message
	 */
	public static void info(MessageProvider msg)
	{
		log(LogLevel.INFO, msg);
	}

	/**
	 * Log at the {@link LogLevel#INFO} level the message using the format and arguments.
	 *
	 * @param msg
	 *            the message
	 */
	public static void info(Message msg)
	{
		log(LogLevel.INFO, msg);
	}

	/**
	 * Log at the {@link LogLevel#INFO} level the string value of the object.
	 *
	 * @param object
	 *            the object
	 */
	public static void info(Object object)
	{
		log(LogLevel.INFO, object);
	}

	/**
	 * Log at the {@link LogLevel#INFO} level with newline the message.
	 *
	 * @param message
	 *            the message
	 */
	public static void infoln(String message)
	{
		logln(LogLevel.INFO, message);
	}

	/**
	 * Log at the {@link LogLevel#INFO} level with newline the string value of the object.
	 *
	 * @param object
	 *            the object
	 */
	public static void infoln(Object object)
	{
		logln(LogLevel.INFO, object);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Log at the {@link LogLevel#WARN} level the message using the format and arguments.
	 *
	 * @param format
	 *            the format
	 * @param args
	 *            the arguments
	 */
	public static void warn(String format, Object... args)
	{
		log(LogLevel.WARN, format, args);
	}

	/**
	 * Log at the {@link LogLevel#WARN} level the message.
	 *
	 * @param msg
	 *            the message
	 */
	public static void warn(MessageProvider msg)
	{
		log(LogLevel.WARN, msg);
	}

	/**
	 * Log at the {@link LogLevel#WARN} level the message using the format and arguments.
	 *
	 * @param msg
	 *            the message
	 */
	public static void warn(Message msg)
	{
		log(LogLevel.WARN, msg);
	}

	/**
	 * Log at the {@link LogLevel#WARN} level the string value of the object.
	 *
	 * @param object
	 *            the object
	 */
	public static void warn(Object object)
	{
		log(LogLevel.WARN, object);
	}

	/**
	 * Log at the {@link LogLevel#WARN} level with newline the message.
	 *
	 * @param message
	 *            the message
	 */
	public static void warnln(String message)
	{
		logln(LogLevel.WARN, message);
	}

	/**
	 * Log at the {@link LogLevel#WARN} level with newline the string value of the object.
	 *
	 * @param object
	 *            the object
	 */
	public static void warnln(Object object)
	{
		logln(LogLevel.WARN, object);
	}

	///////////////////////////////////////////////////////////////////////////
	// Helper methods for testing

	/**
	 * Log the speed test result. If true the message will be written at the {@link LogLevel#INFO}
	 * level. If false the message will be written at the {@link LogLevel#SILENT} level with a failure prefix.
	 * <p>
	 * This is a helper method for speed tests that may not always pass.
	 *
	 * @param result
	 *            the result
	 * @param message
	 *            the message
	 */
	public static void logSpeedTestResult(boolean result, String message)
	{
		LogLevel l;
		if (result)
			l = LogLevel.INFO;
		else
		{
			l = LogLevel.SILENT;
			message = getCodePoint(3) + "Speed-Test Failure: " + message;
		}
		logln(l, message);
	}

	/**
	 * Log the speed test result. If true the message will be written at the {@link LogLevel#INFO}
	 * level. If false the message will be written at the {@link LogLevel#SILENT} level with a failure prefix.
	 * <p>
	 * This is a helper method for speed tests that may not always pass.
	 *
	 * @param result
	 *            the result
	 * @param format
	 *            the format
	 * @param args
	 *            the arguments
	 */
	public static void logSpeedTestResult(boolean result, String format, Object... args)
	{
		LogLevel l;
		if (result)
			l = LogLevel.INFO;
		else
		{
			l = LogLevel.SILENT;
			format = getCodePoint(3) + "Speed-Test Failure: " + format;
		}
		log(l, format, args);
	}

	/**
	 * Log the speed test result of two timing tasks. A test is made to determine if the fast has a lower time than the
	 * slow.
	 * <p>
	 * If true the message will be written at the {@link LogLevel#INFO} level. If false the message will be written at
	 * the {@link LogLevel#SILENT} level with a failure prefix.
	 * <p>
	 * This is a helper method for speed tests that may not always pass.
	 *
	 * @param slow
	 *            the slow task
	 * @param fast
	 *            the fast task
	 */
	public static void logSpeedTestResult(TimingResult slow, TimingResult fast)
	{
		final double t1 = fast.getMean();
		final double t2 = slow.getMean();
		LogLevel l;
		String message;
		if (t1 < t2)
		{
			l = LogLevel.INFO;
			if (!TestSettings.allow(l))
				return;
			message = "%s (%s) => %s (%s) : %.2fx\n";
		}
		else
		{
			l = LogLevel.SILENT;
			message = getCodePoint(3) + "Speed-Test Failure: %s (%s) => %s (%s) : %.2fx\n";
		}
		log(l, message, slow.getTask().getName(), t2, fast.getTask().getName(), t1, t2 / t1);
	}

	/**
	 * Log the speed test result of two timing tasks. A test is made to determine if the fast has a lower time than the
	 * slow.
	 * <p>
	 * If true the message will be written at the {@link LogLevel#INFO} level. If false the message will be written at
	 * the {@link LogLevel#SILENT} level with a failure prefix.
	 * <p>
	 * This is a helper method for speed tests that may not always pass.
	 *
	 * @param slow
	 *            the slow task
	 * @param fast
	 *            the fast task
	 * @param useMin
	 *            Set to true to use the min execution time (the default is mean)
	 */
	public static void logSpeedTestResult(TimingResult slow, TimingResult fast, boolean useMin)
	{
		final double t1 = (useMin) ? fast.getMin() : fast.getMean();
		final double t2 = (useMin) ? slow.getMin() : slow.getMean();
		LogLevel l;
		String message;
		if (t1 < t2)
		{
			l = LogLevel.INFO;
			if (!TestSettings.allow(l))
				return;
			message = "%s (%s) => %s (%s) : %.2fx\n";
		}
		else
		{
			l = LogLevel.SILENT;
			message = getCodePoint(3) + "Speed-Test Failure: %s (%s) => %s (%s) : %.2fx\n";
		}
		log(l, message, slow.getTask().getName(), t2, fast.getTask().getName(), t1, t2 / t1);
	}

	/**
	 * Log the speed test intermediate stage result. If true the message will be written at the {@link LogLevel#INFO}
	 * level. If false the message will be written at the {@link LogLevel#WARN} level with a failure prefix.
	 * <p>
	 * This is a helper method for speed tests that may not always pass.
	 *
	 * @param result
	 *            the result
	 * @param message
	 *            the message
	 */
	public static void logSpeedTestStageResult(boolean result, String message)
	{
		LogLevel l;
		if (result)
			l = LogLevel.INFO;
		else
		{
			l = LogLevel.WARN;
			if (!TestSettings.allow(l)) // Avoid getting the stack trace if not logging
				return;
			message = getCodePoint(3) + "Speed-Test Stage Failure: " + message;
		}
		logln(l, message);
	}

	/**
	 * Log the speed test intermediate stage result. If true the message will be written at the {@link LogLevel#INFO}
	 * level. If false the message will be written at the {@link LogLevel#WARN} level with a failure prefix.
	 * <p>
	 * This is a helper method for speed tests that may not always pass.
	 *
	 * @param result
	 *            the result
	 * @param format
	 *            the format
	 * @param args
	 *            the arguments
	 */
	public static void logSpeedTestStageResult(boolean result, String format, Object... args)
	{
		LogLevel l;
		if (result)
			l = LogLevel.INFO;
		else
		{
			l = LogLevel.WARN;
			if (!TestSettings.allow(l)) // Avoid getting the stack trace if not logging
				return;
			format = getCodePoint(3) + "Speed-Test Stage Failure: " + format;
		}
		log(l, format, args);
	}

	/**
	 * Log the speed test stage result of two timing tasks. A test is made to determine if the fast has a lower time
	 * than the
	 * slow.
	 * <p>
	 * If true the message will be written at the {@link LogLevel#WARN} level. If false the message will be written at
	 * the {@link LogLevel#SILENT} level with a failure prefix.
	 * <p>
	 * This is a helper method for speed tests that may not always pass.
	 *
	 * @param slow
	 *            the slow task
	 * @param fast
	 *            the fast task
	 */
	public static void logSpeedTestStageResult(TimingResult slow, TimingResult fast)
	{
		final double t1 = fast.getMean();
		final double t2 = slow.getMean();
		LogLevel l;
		String message;
		if (t1 < t2)
		{
			l = LogLevel.INFO;
			if (!TestSettings.allow(l))
				return;
			message = "%s (%s) => %s (%s) : %.2fx\n";
		}
		else
		{
			l = LogLevel.WARN;
			if (!TestSettings.allow(l))
				return;
			message = getCodePoint(3) + "Speed-Test Failure: %s (%s) => %s (%s) : %.2fx\n";
		}
		log(l, message, slow.getTask().getName(), t2, fast.getTask().getName(), t1, t2 / t1);
	}

	/**
	 * Log the speed test stage result of two timing tasks. A test is made to determine if the fast has a lower time
	 * than the
	 * slow.
	 * <p>
	 * If true the message will be written at the {@link LogLevel#WARN} level. If false the message will be written at
	 * the {@link LogLevel#SILENT} level with a failure prefix.
	 * <p>
	 * This is a helper method for speed tests that may not always pass.
	 *
	 * @param slow
	 *            the slow task
	 * @param fast
	 *            the fast task
	 * @param useMin
	 *            Set to true to use the min execution time (the default is mean)
	 */
	public static void logSpeedTestStageResult(TimingResult slow, TimingResult fast, boolean useMin)
	{
		final double t1 = (useMin) ? fast.getMin() : fast.getMean();
		final double t2 = (useMin) ? slow.getMin() : slow.getMean();
		LogLevel l;
		String message;
		if (t1 < t2)
		{
			l = LogLevel.INFO;
			if (!TestSettings.allow(l))
				return;
			message = "%s (%s) => %s (%s) : %.2fx\n";
		}
		else
		{
			l = LogLevel.WARN;
			if (!TestSettings.allow(l))
				return;
			message = getCodePoint(3) + "Speed-Test Failure: %s (%s) => %s (%s) : %.2fx\n";
		}
		log(l, message, slow.getTask().getName(), t2, fast.getTask().getName(), t1, t2 / t1);
	}

	/**
	 * Log a failure at the {@link LogLevel#SILENT} level.
	 * <p>
	 * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
	 */
	public static void logFailure()
	{
		logln(LogLevel.SILENT, getCodePoint(3) + "Failure");
	}

	/**
	 * Log a failure at the {@link LogLevel#SILENT} level.
	 * <p>
	 * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
	 *
	 * @param t
	 *            the throwable that caused the failure
	 */
	public static void logFailure(Throwable t)
	{
		final String msg = (t == null) ? null : t.getMessage();
		if (msg == null || msg.length() == 0)
			logln(LogLevel.SILENT, getCodePoint(3) + "Failure");
		else
			logln(LogLevel.SILENT, getCodePoint(3) + "Failure: " + msg);
	}

	/**
	 * Log a failure at the {@link LogLevel#SILENT} level.
	 * <p>
	 * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
	 *
	 * @param message
	 *            the message
	 */
	public static void logFailure(String message)
	{
		logln(LogLevel.SILENT, getCodePoint(3) + "Failure: " + message);
	}

	/**
	 * Log a failure at the {@link LogLevel#SILENT} level.
	 * <p>
	 * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
	 *
	 * @param t
	 *            the throwable that caused the failure
	 * @param message
	 *            the message
	 */
	public static void logFailure(Throwable t, String message)
	{
		final String msg = (t == null) ? null : t.getMessage();
		if (msg == null || msg.length() == 0)
			logln(LogLevel.SILENT, getCodePoint(3) + "Failure: " + message);
		else
			logln(LogLevel.SILENT, getCodePoint(3) + "Failure: " + msg + " : " + message);
	}

	/**
	 * Log a failure at the {@link LogLevel#SILENT} level.
	 * <p>
	 * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
	 *
	 * @param format
	 *            the format
	 * @param args
	 *            the arguments
	 */
	public static void logFailure(String format, Object... args)
	{
		log(LogLevel.SILENT, getCodePoint(3) + "Failure: " + format, args);
	}

	/**
	 * Log a failure at the {@link LogLevel#SILENT} level.
	 * <p>
	 * This is a helper method for tests that may not strictly pass but do not warrant an AssertError.
	 *
	 * @param t
	 *            the throwable that caused the failure
	 * @param format
	 *            the format
	 * @param args
	 *            the arguments
	 */
	public static void logFailure(Throwable t, String format, Object... args)
	{
		final String msg = (t == null) ? null : t.getMessage();
		if (msg == null || msg.length() == 0)
			log(LogLevel.SILENT, getCodePoint(3) + "Failure: " + format, args);
		else
			log(LogLevel.SILENT, getCodePoint(3) + "Failure: " + msg + " : " + format, args);
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
				thisMethod = true;
		}
		return null;
	}
}

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
package gdsc.test;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assume;

/**
 * Class used to control running and logging within tests.
 * This class is configured once using system properties.
 */
public class TestSettings
{
	//@formatter:off
	/**
	 * The Log Level. Lower levels result is less verbose output.
	 */
	public enum LogLevel
	{
		/** Silent. Use this level to output information even if logging is disabled. */
		SILENT { @Override public int getValue() { return 0; }},
		/** Warning logging. For example this can be used to log speed test results that fail but are not critical. */
		WARN { @Override public int getValue() { return 1; }},
		/** Information logging. */
		INFO {@Override public int getValue() {	return 2; }},
		/** Debug logging. */
		DEBUG {	@Override public int getValue()	{ return 3; }};
		/**
		 * Gets the value
		 *
		 * @return the value
		 */
		public abstract int getValue();
	}

	/**
	 * The test complexity. Lower complexity are assumed to be faster.
	 */
	public enum TestComplexity
	{
		/** No complexity */
		NONE { @Override public int getValue() { return 0; }},
		/** Low complexity */
		LOW { @Override	public int getValue() {	return 1; }},
		/** Medium complexity */ 
		MEDIUM { @Override	public int getValue() { return 2; }},
		/** High complexity */
		HIGH { @Override public int getValue() { return 3; }},
		/** Very high complexity */
		VERY_HIGH {	@Override public int getValue()	{ return 4;	}},
		/** Maximum. Used to run any test that checks complexity settings */
		MAXIMUM { @Override public int getValue() { return Integer.MAX_VALUE; }};

		/**
		 * Gets the value
		 *
		 * @return the value
		 */
		public abstract int getValue();
	}
	//@formatter:on

	/**
	 * Provide messages dynamically for logging.
	 */
	public interface MessageProvider
	{
		/**
		 * Gets the message.
		 *
		 * @return the message
		 */
		String getMessage();
	}

	/**
	 * Provide messages for logging. To be used when providing the arguments for the message is computationally intense.
	 */
	public static abstract class Message
	{
		/** The format. */
		final String format;

		/**
		 * Instantiates a new message.
		 *
		 * @param format
		 *            the format
		 */
		public Message(String format)
		{
			this.format = format;
		}

		/**
		 * Helper method to wrap the variable length arguments list for use in {@link #getArgs()}.
		 *
		 * @param args
		 *            the arguments for the message
		 * @return the arguments for the message as an array
		 */
		final public Object[] wrap(Object... args)
		{
			return args;
		}

		/**
		 * Gets the arguments for the message.
		 *
		 * @return the arguments
		 */
		public abstract Object[] getArgs();
	}

	/** The allowed log level. */
	private static int logLevel;

	/** The allowed test complexity */
	private static int testComplexity;

	/** The fixed seed for random generator */
	private static long seed;

	static
	{
		logLevel = LogLevel.SILENT.getValue();
		testComplexity = TestComplexity.NONE.getValue();
		seed = 30051977;

		try
		{
			logLevel = Integer.parseInt(System.getProperty("gdsc.test.logging"));
		}
		catch (Exception e)
		{
		}
		try
		{
			testComplexity = Integer.parseInt(System.getProperty("gdsc.test.level"));
		}
		catch (Exception e)
		{
		}
		try
		{
			seed = Long.parseLong(System.getProperty("gdsc.test.seed"));
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Sets the log level. Package scope for testing.
	 *
	 * @param level
	 *            the new log level
	 */
	static void setLogLevel(LogLevel level)
	{
		logLevel = level.getValue();
	}

	/**
	 * Sets the text complexity. Package scope for testing.
	 *
	 * @param complexity
	 *            the new text complexity
	 */
	static void setTextComplexity(TestComplexity complexity)
	{
		testComplexity = complexity.getValue();
	}

	/**
	 * Gets the log level. This is setting using the system property gdsc.test.logging.
	 *
	 * @return the log level
	 */
	public static int getLogLevel()
	{
		return logLevel;
	}

	/**
	 * Gets the test complexity. This is setting using the system property gdsc.test.level.
	 *
	 * @return the test complexity
	 */
	public static int getTestComplexity()
	{
		return testComplexity;
	}

	/**
	 * Gets the seed. This is setting using the system property gdsc.test.seed.
	 *
	 * @return the seed
	 */
	public static long getSeed()
	{
		return seed;
	}

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
		if (allow(level))
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
		if (allow(level))
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
		if (allow(level))
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
		if (allow(level))
			System.out.print(object);
	}

	/**
	 * Log with newline the string value of the object.
	 *
	 * @param level
	 *            the level
	 * @param object
	 *            the object
	 */
	public static void logln(LogLevel level, String object)
	{
		if (allow(level))
			System.out.println(object);
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
		if (allow(level))
			System.out.println(object);
	}

	/**
	 * Check if logging is allowed at the given level.
	 *
	 * @param level
	 *            the level
	 * @return true, if successful
	 */
	public static boolean allow(LogLevel level)
	{
		return level.getValue() <= logLevel;
	}

	/**
	 * Check if testing is allowed at the given complexity.
	 *
	 * @param omplexity
	 *            the test complexity
	 * @return true, if successful
	 */
	public static boolean allow(TestComplexity complexity)
	{
		return complexity.getValue() <= testComplexity;
	}

	/**
	 * Check if logging and testing is allowed at the given level and complexity.
	 *
	 * @param level
	 *            the level
	 * @param complexity
	 *            the complexity
	 * @return true, if successful
	 */
	public static boolean allow(LogLevel level, TestComplexity complexity)
	{
		return allow(level) && allow(complexity);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Log at the debug level the message using the format and arguments.
	 *
	 * @param level
	 *            the level
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
	 * Log at the debug level the message.
	 *
	 * @param level
	 *            the level
	 * @param msg
	 *            the message
	 */
	public static void debug(MessageProvider msg)
	{
		log(LogLevel.DEBUG, msg);
	}

	/**
	 * Log at the debug level the message using the format and arguments.
	 *
	 * @param level
	 *            the level
	 * @param msg
	 *            the message
	 */
	public static void debug(Message msg)
	{
		log(LogLevel.DEBUG, msg);
	}

	/**
	 * Log at the debug level the string value of the object.
	 *
	 * @param level
	 *            the level
	 * @param object
	 *            the object
	 */
	public static void debug(Object object)
	{
		log(LogLevel.DEBUG, object);
	}

	/**
	 * Log at the debug level with newline the string value of the object.
	 *
	 * @param level
	 *            the level
	 * @param object
	 *            the object
	 */
	public static void debugln(String object)
	{
		logln(LogLevel.DEBUG, object);
	}

	/**
	 * Log at the debug level with newline the string value of the object.
	 *
	 * @param level
	 *            the level
	 * @param object
	 *            the object
	 */
	public static void debugln(Object object)
	{
		logln(LogLevel.DEBUG, object);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Log at the info level the message using the format and arguments.
	 *
	 * @param level
	 *            the level
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
	 * Log at the info level the message.
	 *
	 * @param level
	 *            the level
	 * @param msg
	 *            the message
	 */
	public static void info(MessageProvider msg)
	{
		log(LogLevel.INFO, msg);
	}

	/**
	 * Log at the info level the message using the format and arguments.
	 *
	 * @param level
	 *            the level
	 * @param msg
	 *            the message
	 */
	public static void info(Message msg)
	{
		log(LogLevel.INFO, msg);
	}

	/**
	 * Log at the info level the string value of the object.
	 *
	 * @param level
	 *            the level
	 * @param object
	 *            the object
	 */
	public static void info(Object object)
	{
		log(LogLevel.INFO, object);
	}

	/**
	 * Log at the info level with newline the string value of the object.
	 *
	 * @param level
	 *            the level
	 * @param object
	 *            the object
	 */
	public static void infoln(String object)
	{
		logln(LogLevel.INFO, object);
	}

	/**
	 * Log at the info level with newline the string value of the object.
	 *
	 * @param level
	 *            the level
	 * @param object
	 *            the object
	 */
	public static void infoln(Object object)
	{
		logln(LogLevel.INFO, object);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Log at the warn level the message using the format and arguments.
	 *
	 * @param level
	 *            the level
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
	 * Log at the warn level the message.
	 *
	 * @param level
	 *            the level
	 * @param msg
	 *            the message
	 */
	public static void warn(MessageProvider msg)
	{
		log(LogLevel.WARN, msg);
	}

	/**
	 * Log at the warn level the message using the format and arguments.
	 *
	 * @param level
	 *            the level
	 * @param msg
	 *            the message
	 */
	public static void warn(Message msg)
	{
		log(LogLevel.WARN, msg);
	}

	/**
	 * Log at the warn level the string value of the object.
	 *
	 * @param level
	 *            the level
	 * @param object
	 *            the object
	 */
	public static void warn(Object object)
	{
		log(LogLevel.WARN, object);
	}

	/**
	 * Log at the warn level with newline the string value of the object.
	 *
	 * @param level
	 *            the level
	 * @param object
	 *            the object
	 */
	public static void warnln(String object)
	{
		logln(LogLevel.WARN, object);
	}

	/**
	 * Log at the warn level with newline the string value of the object.
	 *
	 * @param level
	 *            the level
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
	 * Assume logging is allowed at the given level.
	 * <p>
	 * Use this at the start of a test that only produces logging output (no assertions) to skip
	 * the test as logging will be ignored.
	 *
	 * @param level
	 *            the level
	 */
	public static void assume(LogLevel level)
	{
		Assume.assumeTrue(allow(level));
	}

	/**
	 * Assume testing is logging at the warn level.
	 */
	public static void assumeWarn()
	{
		assume(LogLevel.WARN);
	}

	/**
	 * Assume testing is logging at the info level.
	 */
	public static void assumeInfo()
	{
		assume(LogLevel.INFO);
	}

	/**
	 * Assume testing is logging at the debug level.
	 */
	public static void assumeDebug()
	{
		assume(LogLevel.DEBUG);
	}

	/**
	 * Assume testing is allowed at the given complexity.
	 * <p>
	 * Use this at the start of a test that has a long run time or is otherwise complex
	 * enough to warrant skipping the test if not testing at that level of complexity.
	 *
	 * @param complexity
	 *            the complexity
	 */
	public static void assume(TestComplexity complexity)
	{
		Assume.assumeTrue(allow(complexity));
	}

	/**
	 * Assume testing is allowed at low complexity.
	 */
	public static void assumeLowComplexity()
	{
		assume(TestComplexity.LOW);
	}

	/**
	 * Assume testing is allowed at medium complexity.
	 */
	public static void assumeMediumComplexity()
	{
		assume(TestComplexity.MEDIUM);
	}

	/**
	 * Assume testing is allowed at high complexity.
	 */
	public static void assumeHighComplexity()
	{
		assume(TestComplexity.HIGH);
	}

	/**
	 * Assume testing is allowed at very high complexity.
	 */
	public static void assumeVeryHighComplexity()
	{
		assume(TestComplexity.VERY_HIGH);
	}

	/**
	 * Assume testing is allowed at maximum complexity.
	 */
	public static void assumeMaximumComplexity()
	{
		assume(TestComplexity.MAXIMUM);
	}

	/**
	 * Assume logging and testing is allowed at the given level and complexity.
	 *
	 * @param level
	 *            the level
	 * @param complexity
	 *            the complexity
	 */
	public static void assume(LogLevel level, TestComplexity complexity)
	{
		Assume.assumeTrue(allow(level, complexity));
	}

	/**
	 * Gets the random generator. If the seed is 0 then a random seed will be used.
	 *
	 * @param seed
	 *            the seed
	 * @return the random generator
	 */
	public static RandomGenerator getRandomGenerator(long seed)
	{
		return (seed == 0) ? new Well19937c() : new Well19937c(seed);
	}

	/**
	 * Gets a random generator with a fixed seed set using the system property gdsc.test.seed.
	 *
	 * @return the random generator
	 */
	public static RandomGenerator getRandomGenerator()
	{
		return getRandomGenerator(seed);
	}

	/**
	 * Assume speed testing is allowed the {@link TestComplexity#MEDIUM} complexity level.
	 * <p>
	 * Use this at the start of a speed test that has a long run time or is otherwise complex
	 * enough to warrant skipping the test if not testing at that level of complexity.
	 * <p>
	 * This method is distinct from {@link #assume(TestComplexity)} so that speed tests can be optionally disabled.
	 *
	 * @param complexity
	 *            the complexity
	 */
	public static void assumeSpeedTest()
	{
		assumeSpeedTest(TestComplexity.MEDIUM);
	}

	/**
	 * Assume speed testing is allowed at the given complexity.
	 * <p>
	 * Use this at the start of a speed test that has a long run time or is otherwise complex
	 * enough to warrant skipping the test if not testing at that level of complexity.
	 * <p>
	 * This method is distinct from {@link #assume(TestComplexity)} so that speed tests can be optionally disabled.
	 *
	 * @param complexity
	 *            the complexity
	 */
	public static void assumeSpeedTest(TestComplexity complexity)
	{
		assume(complexity);
	}

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
		{
			l = LogLevel.INFO;
		}
		else
		{
			l = LogLevel.SILENT;
			message = "Speed-Test Failure: " + message;
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
		{
			l = LogLevel.INFO;
		}
		else
		{
			l = LogLevel.SILENT;
			format = "Speed-Test Failure: " + format;
		}
		log(l, format, args);
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
		{
			l = LogLevel.INFO;
		}
		else
		{
			l = LogLevel.WARN;
			message = "Speed-Test Stage Failure: " + message;
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
		{
			l = LogLevel.INFO;
		}
		else
		{
			l = LogLevel.WARN;
			format = "Speed-Test Stage Failure: " + format;
		}
		log(l, format, args);
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
		logFailure(null, format, args);
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
		String msg = (t == null) ? null : t.getMessage();
		if (msg == null || msg.length() == 0)
			log(LogLevel.SILENT, "Failure: " + format, args);
		else
			log(LogLevel.SILENT, "Failure: " + format + " : " + msg, args);
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
		logFailure(null, message);
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
		String msg = (t == null) ? null : t.getMessage();
		if (msg == null || msg.length() == 0)
			log(LogLevel.SILENT, "Failure: " + message);
		else
			log(LogLevel.SILENT, "Failure: " + message + " : " + msg);
	}
}

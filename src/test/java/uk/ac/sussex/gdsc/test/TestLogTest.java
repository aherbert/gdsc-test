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

import java.util.IllegalFormatConversionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class TestLogTest
{
	private static Logger logger;

	@BeforeAll
	public static void beforeAll()
	{
		logger = Logger.getLogger(TestLogTest.class.getName());
	}

	@AfterAll
	public static void afterAll()
	{
		logger = null;
	}

	@Test
	public void canLogVarArgs()
	{
		TestLog.log(logger, Level.WARNING, "log varargs = %d %f", 1, 2.3);
	}

	@Test
	public void canLogObjectArray()
	{
		final Object[] args = new Object[] { 1, 2.3 };
		TestLog.log(logger, Level.WARNING, "log Object[] = %d %f", args);
	}

	@Test
	public void cannotLogObjectArrayAndVarargs()
	{
		// Use severe to always run
		Assumptions.assumeTrue(logger.isLoggable(Level.SEVERE));
		final Object[] args = new Object[] { 1, 2.3 };
		Assertions.assertThrows(IllegalFormatConversionException.class, () -> {
			TestLog.log(logger, Level.SEVERE, "%d %f %d", args, 3);
		});
	}

	@Test
	public void canStaceTraceElement()
	{
		final StackTraceElement e = new Throwable().getStackTrace()[0];
		final StackTraceElement o = TestLog.getStaceTraceElement();
		Assertions.assertNotNull(e);
		Assertions.assertNotNull(o);
		Assertions.assertEquals(e.getClassName(), o.getClassName());
		Assertions.assertEquals(e.getMethodName(), o.getMethodName());
		Assertions.assertEquals(e.getLineNumber() + 1, o.getLineNumber());
		TestLog.log(logger, Level.INFO, "%s:%s:%d", o.getClassName(), o.getMethodName(), o.getLineNumber());
	}

	@Test
	public void canLogFailure()
	{
		TestLog.logFailure(logger);
		TestLog.logFailure(logger,"This is a test failure message");
		TestLog.logFailure(logger,(Throwable) null);
		TestLog.logFailure(logger,new Throwable("Throwable message"));
		TestLog.logFailure(logger,new Throwable("Throwable message"), "This is a test failure message with throwable");
		TestLog.logFailure(logger,(Throwable) null, "This is a test failure message with null throwable");
		TestLog.logFailure(logger,"This is a formatted test failure message: %d", 1);
		TestLog.logFailure(logger,new Throwable("Throwable message"),
				"This is a formatted test failure message with throwable: %d", 2);
		TestLog.logFailure(logger,(Throwable) null, "This is a formatted test failure message with null throwable: %d", 3);
	}

	@Test
	public void canLogTestResult()
	{
		TestLog.logTestResult(logger,false, "This is a test failure message");
		TestLog.logTestResult(logger,false, "This is a test failure formatted message: %d", 1);
		TestLog.logTestStageResult(logger,false, "This is a test stage failure message");
		TestLog.logTestStageResult(logger,false, "This is a test stage failure formatted message: %d", 1);
	}

	@Test
	public void canLogSpeedTestTaskFailure()
	{
		//@formatter:off
		final TimingTask fast = new TimingTask()
		{
			@Override
			public Object run(Object data) { return null; }
			@Override
			public int getSize() { return 0; }
			@Override
			public String getName()	{ return "fast"; }
			@Override
			public Object getData(int i) { return null; }
			@Override
			public void check(int i, Object result) { /* Do nothing */ }
		};
		final TimingTask slow = new TimingTask()
		{
			@Override
			public Object run(Object data) { return null; }
			@Override
			public int getSize() { return 0; }
			@Override
			public String getName()	{ return "slow"; }
			@Override
			public Object getData(int i) { return null; }
			@Override
			public void check(int i, Object result) { /* Do nothing */ }
		};
		final TimingResult fastR = new TimingResult(fast, new long[] { 100 });
		final TimingResult slowR = new TimingResult(slow, new long[] { 1000 });
		final TimingResult slowFastR = new TimingResult(fast, new long[] { 10000 });
		//@formatter:on

		TestLog.logSpeedTestResult(logger,slowR, fastR);
		TestLog.logSpeedTestResult(logger,slowR, slowFastR);
		TestLog.logSpeedTestStageResult(logger,slowR, fastR);
		TestLog.logSpeedTestStageResult(logger,slowR, slowFastR);
	}
}

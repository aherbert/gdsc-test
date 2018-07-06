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

import java.util.IllegalFormatConversionException;

import org.junit.Test;

import gdsc.test.TestSettings;
import gdsc.test.TestSettings.LogLevel;
import org.junit.Assert;

public class TestSettingsTest
{
	@Test
	public void printSettings()
	{
		TestSettings.assume(LogLevel.WARN);
		TestSettings.warn("TestSettings Log Level = %d\n", TestSettings.getLogLevel());
		TestSettings.warn("TestSettings Test Complexity = %d\n", TestSettings.getTestComplexity());
		TestSettings.warn("TestSettings Seed = %d\n", TestSettings.getSeed());
	}

	@Test
	public void canLogVarArgs()
	{
		TestSettings.log(LogLevel.WARN, "log varargs = %d %f\n", 1, 2.3);
	}

	@Test
	public void canLogObjectArray()
	{
		Object[] args = new Object[] { 1, 2.3 };
		TestSettings.log(LogLevel.WARN, "log Object[] = %d %f\n", args);
	}

	@Test(expected = IllegalFormatConversionException.class)
	public void cannotLogObjectArrayAndVarargs()
	{
		Object[] args = new Object[] { 1, 2.3 };
		// Use silent to always run
		TestSettings.log(LogLevel.SILENT, "%d %f %d\n", args, 3);
	}

	@Test
	public void canStaceTraceElement()
	{
		StackTraceElement e = new Throwable().getStackTrace()[0];
		StackTraceElement o = TestSettings.getStaceTraceElement();
		Assert.assertNotNull(e);
		Assert.assertNotNull(o);
		Assert.assertEquals(e.getClassName(), o.getClassName());
		Assert.assertEquals(e.getMethodName(), o.getMethodName());
		Assert.assertEquals(e.getLineNumber() + 1, o.getLineNumber());
		TestSettings.log(LogLevel.INFO, "%s:%s:%d\n", o.getClassName(), o.getMethodName(), o.getLineNumber());
	}

	@Test
	public void canLogFailure()
	{
		TestSettings.logFailure("This is a test failure");
		TestSettings.logFailure(new Throwable("Fail message"), "This is a test failure with throwable");
		TestSettings.logFailure((Throwable) null, "This is a test failure with null throwable");
		TestSettings.logFailure("This is a formatted test failure: %d\n", 1);
		TestSettings.logFailure(new Throwable("Fail message"), "This is a formatted test failure with throwable: %d\n", 2);
		TestSettings.logFailure((Throwable) null, "This is a formatted test failure with null throwable: %d\n", 3);
	}
}

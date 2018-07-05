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

import org.junit.Assert;
import org.junit.Test;

public class TestCounterTest
{
	@Test(expected = AssertionError.class)
	public void canAllowNoTestAssertFailure()
	{
		runTestAssert(0, 1);
	}

	@Test
	public void canAllowSingleTestAssertFailure()
	{
		runTestAssert(1, 1);
	}

	@Test(expected = AssertionError.class)
	public void throwsAfterSingleTestAssertFailure()
	{
		runTestAssert(1, 2);
	}

	@Test
	public void canAllowMultiTestAssertFailure()
	{
		runTestAssert(2, 2);
	}

	@Test(expected = AssertionError.class)
	public void throwsAfterMultiTestAssertFailure()
	{
		runTestAssert(2, 3);
	}

	private void runTestAssert(int limit, int fails)
	{
		TestCounter fc = new TestCounter(limit);
		while (fails-- > 0)
		{
			fc.run(() -> {
				Assert.fail();
			});
		}
	}

	@Test(expected = AssertionError.class)
	public void canAllowNoTestCaseFailure()
	{
		runTestCase(0, 1);
	}

	@Test
	public void canAllowSingleTestCaseFailure()
	{
		runTestCase(1, 1);
	}

	@Test(expected = AssertionError.class)
	public void throwsAfterSingleTestCaseFailure()
	{
		runTestCase(1, 2);
	}

	@Test
	public void canAllowMultiTestCaseFailure()
	{
		runTestCase(2, 2);
	}

	@Test(expected = AssertionError.class)
	public void throwsAfterMultiTestCaseFailure()
	{
		runTestCase(2, 3);
	}

	private void runTestCase(int limit, int fails)
	{
		TestCounter fc = new TestCounter(limit);
		while (fails-- > 0)
		{
			fc.run(() -> {
				return false;
			}, () -> {
				Assert.fail();
			});
		}
	}

	@Test(expected = AssertionError.class)
	public void throwsTestCaseFailureWithNoError()
	{
		TestCounter fc = new TestCounter(0);
		fc.run(() -> {
			return false;
		}, () -> {
		});
	}
}

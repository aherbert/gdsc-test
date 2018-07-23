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
 * Runs test assertions and accumulates any thrown {@link AssertionError}s. If the limit is exceeded then the last generated
 * error is thrown.
 * <p>
 * Use this class to fail tests that accumulate too many errors during random repeats, e.g. &gt;5 out of 100.
 * <p>
 * The class can be used with lambda functions, e.g.
 * <pre>
 * TestCounter c = new TestCounter(3);
 * c.run( () -&gt; { Assert.fail(); } );
 * c.run( () -&gt; { return false; }, () -&gt; { Assert.fail(); } );
 * </pre>
 */
public class TestCounter
{
	/** The failure limit. */
	private final int failureLimit;

	/** The failures. */
	private final int[] failures;

	/**
	 * Compute the failure limit.
	 *
	 * <pre>
	 * return (int) Math.floor(size * fraction);
	 * </pre>
	 *
	 * @param size
	 *            the number of repeats
	 * @param fraction
	 *            the fraction of repeats that fail that will trigger an error
	 * @return the failure limit
	 * @throws IllegalArgumentException
	 *             if fraction is not in the range 0-1 or size is not positive
	 */
	public static int computeFailureLimit(int size, double fraction) throws IllegalArgumentException
	{
		if (size < 1)
			throw new IllegalArgumentException("Size must be strictly positive: " + size);
		if (fraction < 0 || fraction > 1)
			throw new IllegalArgumentException("Fraction must be in the range 0-1");
		return (int) Math.floor(size * fraction);
	}

	/**
	 * Instantiates a new fail counter.
	 *
	 * @param failureLimit
	 *            the failure limit that will generate an AssertionError to be thrown
	 */
	public TestCounter(int failureLimit)
	{
		this(failureLimit, 1);
	}

	/**
	 * Instantiates a new fail counter.
	 *
	 * @param failureLimit
	 *            the failure limit that will generate an {@link AssertionError} to be thrown
	 * @param size
	 *            the number of different tests to be address by index in {@link #run(int, TestAssertion)}
	 */
	public TestCounter(int failureLimit, int size)
	{
		this.failureLimit = failureLimit;
		if (size < 1)
			throw new IllegalArgumentException("Size must be strictly positive: " + size);
		failures = new int[size];
	}

	/**
	 * Run the test (assuming test index 0).
	 *
	 * @param test
	 *            the test
	 * @throws AssertionError
	 *             the assertion error if the failure limit has been exceeded
	 */
	public void run(TestAssertion test) throws AssertionError
	{
		run(0, test);
	}

	/**
	 * Run the test.
	 *
	 * @param index
	 *            the test index
	 * @param test
	 *            the test
	 * @throws IndexOutOfBoundsException
	 *             If the test index is invalid
	 * @throws AssertionError
	 *             the assertion error if the failure limit has been exceeded
	 */
	public void run(int index, TestAssertion test) throws IndexOutOfBoundsException, AssertionError
	{
		try
		{
			test.test();
		}
		catch (final AssertionError e)
		{
			if (++failures[index] > failureLimit)
				throw e;
		}
	}

	/**
	 * Run the test (assuming test index 0).
	 *
	 * @param test
	 *            the test
	 * @param error
	 *            the error (this will be called if the failure limit has been exceeded)
	 * @throws AssertionError
	 *             the assertion error if the failure limit has been exceeded
	 */
	public void run(TestCase test, TestAssertion error) throws AssertionError
	{
		run(0, test, error);
	}

	/**
	 * Run the test.
	 *
	 * @param index
	 *            the test index
	 * @param test
	 *            the test
	 * @param error
	 *            the error (this will be called if the failure limit has been exceeded)
	 * @throws IndexOutOfBoundsException
	 *             If the test index is invalid
	 * @throws AssertionError
	 *             the assertion error if the failure limit has been exceeded
	 */
	public void run(int index, TestCase test, TestAssertion error) throws IndexOutOfBoundsException, AssertionError
	{
		if (!test.test())
			if (++failures[index] > failureLimit)
			{
				// This should throw
				error.test();
				 // In case it doesn't then throw a default error
				//org.junit.Assert.fail();
				throw new AssertionError();
			}
	}
}

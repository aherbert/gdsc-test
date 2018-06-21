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

import org.junit.Test;

import org.junit.Assert;

public class TestAssertTest
{
	@Test
	public void canAssertEqualsDoubleArraysUsingRelativeError()
	{
		double[] e = new double[] { Math.PI };
		for (double error : new double[] { 0, 1e-8, 1e-6 })
		{
			double[] o = e.clone();
			if (error == 0)
			{
				TestAssert.assertArrayEqualsRelative(e, o, 0);
				continue;
			}

			for (int i = 0; i < o.length; i++)
				o[i] = Math.nextUp(e[i] - e[i] * error);
			TestAssert.assertArrayEqualsRelative(e, o, error);

			for (int i = 0; i < o.length; i++)
				o[i] = Math.nextDown(e[i] + e[i] * error);
			TestAssert.assertArrayEqualsRelative(e, o, error);
		}
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsDownDoubleArraysUsingRelativeError()
	{
		double[] e = new double[] { Math.PI };
		double error = 1e-8;
		double[] o = e.clone();

		for (int i = 0; i < o.length; i++)
			o[i] = Math.nextDown(e[i] - e[i] * error);
		TestAssert.assertArrayEqualsRelative(e, o, error);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsUpDoubleArraysUsingRelativeError()
	{
		double[] e = new double[] { Math.PI };
		double error = 1e-8;
		double[] o = e.clone();

		for (int i = 0; i < o.length; i++)
			o[i] = Math.nextUp(e[i] + e[i] * error);
		TestAssert.assertArrayEqualsRelative(e, o, error);
	}

	@Test
	public void canAssertEqualsObjectDoubleArraysUsingRelativeError()
	{
		double[] e = new double[] { 2 };
		double[] o = new double[] { 2.1 };
		TestAssert.assertDoubleArrayEqualsRelative((Object) e, (Object) o, 0.05);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsObjectDoubleArraysUsingRelativeError()
	{
		double[] e = new double[] { 2 };
		double[] o = new double[] { 2.1 };
		TestAssert.assertDoubleArrayEqualsRelative((Object) e, (Object) o, 0.01);
	}

	@Test
	public void canAssertEqualsObjectDoubleDoubleArraysUsingRelativeError()
	{
		double[][] e = new double[][] { { 2 } };
		double[][] o = new double[][] { { 2.1 } };
		TestAssert.assertDoubleArrayEqualsRelative((Object) e, (Object) o, 0.05);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsObjectDoubleDoubleArraysUsingRelativeError()
	{
		double[][] e = new double[][] { { 2 } };
		double[][] o = new double[][] { { 2.1 } };
		TestAssert.assertDoubleArrayEqualsRelative((Object) e, (Object) o, 0.01);
	}

	@Test
	public void canAssertEqualsFloatArraysUsingRelativeError()
	{
		float[] e = new float[] { (float) Math.PI };
		for (double error : new double[] { 0, 1e-8, 1e-6 })
		{
			float[] o = e.clone();
			if (error == 0)
			{
				TestAssert.assertArrayEqualsRelative(e, o, 0);
				continue;
			}

			for (int i = 0; i < o.length; i++)
				o[i] = (float) Math.nextUp(e[i] - e[i] * error);
			TestAssert.assertArrayEqualsRelative(e, o, error);

			for (int i = 0; i < o.length; i++)
				o[i] = (float) Math.nextDown(e[i] + e[i] * error);
			TestAssert.assertArrayEqualsRelative(e, o, error);
		}
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsDownFloatArraysUsingRelativeError()
	{
		float[] e = new float[] { (float) Math.PI };
		double error = 1e-8;
		float[] o = e.clone();

		for (int i = 0; i < o.length; i++)
			o[i] = Math.nextDown((float) (e[i] - e[i] * error));
		TestAssert.assertArrayEqualsRelative(e, o, error);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsUpFloatArraysUsingRelativeError()
	{
		float[] e = new float[] { (float) Math.PI };
		double error = 1e-8;
		float[] o = e.clone();

		for (int i = 0; i < o.length; i++)
			o[i] = Math.nextUp((float) (e[i] + e[i] * error));
		TestAssert.assertArrayEqualsRelative(e, o, error);
	}

	@Test
	public void canAssertEqualsObjectFloatArraysUsingRelativeError()
	{
		float[] e = new float[] { 2 };
		float[] o = new float[] { 2.1f };
		TestAssert.assertFloatArrayEqualsRelative((Object) e, (Object) o, 0.05);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsObjectFloatArraysUsingRelativeError()
	{
		float[] e = new float[] { 2 };
		float[] o = new float[] { 2.1f };
		TestAssert.assertFloatArrayEqualsRelative((Object) e, (Object) o, 0.01);
	}

	@Test
	public void canAssertEqualsObjectFloatFloatArraysUsingRelativeError()
	{
		float[][] e = new float[][] { { 2 } };
		float[][] o = new float[][] { { 2.1f } };
		TestAssert.assertFloatArrayEqualsRelative((Object) e, (Object) o, 0.05);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsObjectFloatFloatArraysUsingRelativeError()
	{
		float[][] e = new float[][] { { 2 } };
		float[][] o = new float[][] { { 2.1f } };
		TestAssert.assertFloatArrayEqualsRelative((Object) e, (Object) o, 0.01);
	}

	@Test
	public void canAssertWithFormattedMessage()
	{
		try
		{
			TestAssert.assertLongEquals(0, 1, "[%d] == %.2f", 2, 3.5);
		}
		catch (AssertionError e)
		{
			String msg = e.getMessage();
			TestSettings.info(msg);
			Assert.assertTrue("Unexpected message", msg.startsWith("[2] == 3.50"));
		}
	}
}

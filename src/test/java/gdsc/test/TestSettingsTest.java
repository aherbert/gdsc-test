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

import gdsc.test.TestSettings;
import gdsc.test.TestSettings.LogLevel;

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
}

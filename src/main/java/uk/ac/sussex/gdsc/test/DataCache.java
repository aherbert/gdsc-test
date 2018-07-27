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

import java.util.HashMap;

/**
 * Cache data generated using a random source.
 *
 * @param <S>
 *            the type of the random source
 * @param <T>
 *            the type of the data
 */
public class DataCache<S, T>
{
	private HashMap<S, T> data = new HashMap<>();

	/**
	 * Gets the data.
	 * <p>
	 * Uses the cached data if available, otherwise generates the data
	 * using the data provider using a random source.
	 * <p>
	 * Note: The data should be considered immutable if the cache is to be reused.
	 *
	 * @param source
	 *            the random source
	 * @param provider
	 *            the provider to generate the data (if not cached)
	 * @return the data
	 */
	public synchronized T getData(S source, DataProvider<S, T> provider)
	{
		T t = data.get(source);
		if (t == null)
		{
			synchronized (data)
			{
				t = data.get(source);
				if (t == null)
				{
					t = provider.getData(source);
					data.put(source, t);
				}
			}
		}
		return t;
	}
}

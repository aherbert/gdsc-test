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
package uk.ac.sussex.gdsc.test.utils;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Cache data under a given key and generate it if absent.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class DataCache<K, V> {
    private final HashMap<K, V> data = new HashMap<>();

    /**
     * Gets the value stored under the given key.
     * <p>
     * Uses the cached value if available, otherwise generates the value using the
     * provider.
     * <p>
     * Note: The value should be considered immutable if the cache is to be reused.
     *
     * @param key      the key
     * @param provider the provider to generate the value (if not cached)
     * @return the value
     */
    public synchronized V getOrComputeIfAbsent(K key, Function<K, V> provider) {
        return data.computeIfAbsent(key, provider);
    }

    /**
     * Clear the cache.
     */
    public void clear() {
        data.clear();
    }
}

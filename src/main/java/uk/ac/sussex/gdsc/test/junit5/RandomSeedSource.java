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
package uk.ac.sussex.gdsc.test.junit5;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import uk.ac.sussex.gdsc.test.TestSettings;

/**
 * Provides random seeds as a {@link RandomSeed} for use as an
 * {@link ArgumentsSource}.
 * <p>
 * The starting seed and number of repeats are obtained from runtime system
 * properties.
 *
 * @see TestSettings
 */
public class RandomSeedSource implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext arg0) {
        final long seed = TestSettings.getSeed();
        final int repeats = TestSettings.getRepeats();
        return IntStream.iterate(0, n -> n + 1).limit(repeats)
                .mapToObj(n -> Arguments.of(new RandomSeed(n + seed, n + 1, repeats)));
    }
}

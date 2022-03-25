/*-
 * #%L
 * Genome Damage and Stability Centre Test RNG
 *
 * Contains utilities for use with Commons RNG for random tests.
 * %%
 * Copyright (C) 2018 - 2022 Alex Herbert
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Provides classes for working with the <a
 * href="https://commons.apache.org/proper/commons-rng/">Commons RNG</a> library
 * to add a source of randomness for tests.
 *
 * <p>The package contains:
 *
 * <ul>
 *
 * <li>A factory for generating a random provider</li>
 *
 * <li>Implementations of
 * {@link org.apache.commons.rng.UniformRandomProvider UniformRandomProvider}</li>
 *
 * </ul>
 *
 * @see <a href="https://commons.apache.org/proper/commons-rng/">Commons RNG</a>
 * @since 1.0
 */
package uk.ac.sussex.gdsc.test.rng;

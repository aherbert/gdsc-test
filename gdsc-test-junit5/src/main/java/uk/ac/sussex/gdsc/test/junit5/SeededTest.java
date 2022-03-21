/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with JUnit 5.
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

package uk.ac.sussex.gdsc.test.junit5;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import uk.ac.sussex.gdsc.test.utils.RandomSeed;

/**
 * {@code @SeededTest} is used to signal that the annotated method is a
 * {@link org.junit.jupiter.params.ParameterizedTest} using an argument of type {@link RandomSeed}.
 * The {@link org.junit.jupiter.params.provider.ArgumentsSource} for the test will use a
 * {@link RandomSeedSource} to provide the seeds. At least one seed will be provided.
 *
 * <p>The annotation adds a {@code "seeded"} <code>@</code>{@link Tag} and
 * <code>@</code>{@link RandomTag}.
 *
 * <p>{@code @SeededTest} may also be used as a meta-annotation in order to create a custom
 * <em>composed annotation</em> that inherits the semantics of {@code @SeededTest}.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("seeded")
@ParameterizedTest
@ArgumentsSource(RandomSeedSource.class)
@RandomTag
public @interface SeededTest {
  // Annotation
}

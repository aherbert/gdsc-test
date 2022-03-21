/*-
 * #%L
 * Genome Damage and Stability Centre Test API
 *
 * Contains a generic framework for test predicates.
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

package uk.ac.sussex.gdsc.test.api;

/**
 * Annotation used to indicates that the visibility of a type or member has been relaxed to make the
 * code testable.
 *
 * <p>This idea originates from Google Guava. PMD will not flag package private members when using
 * the DefaultPackage rule if they are annotated with VisibleForTesting.
 *
 * @see <a href="https://github.com/google/guava">Guava: Google Core Libraries for Java</a>
 * @see <a href="https://pmd.github.io/latest/pmd_rules_java_codestyle.html#defaultpackage">PMD Rule
 *      DefaultPackage</a>
 */
public @interface VisibleForTesting {
  // Annotation
}

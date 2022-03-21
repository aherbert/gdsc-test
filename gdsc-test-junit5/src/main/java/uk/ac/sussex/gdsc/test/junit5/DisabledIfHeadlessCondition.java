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

import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;

import java.awt.GraphicsEnvironment;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * {@link ExecutionCondition} for {@link DisabledIfHeadless @DisabledIfHeadless}.
 *
 * @see DisabledIfHeadless
 */
class DisabledIfHeadlessCondition implements ExecutionCondition {

  private static final ConditionEvaluationResult ENABLED =
      enabled("Enabled on non-headless environment");
  private static final ConditionEvaluationResult DISABLED =
      disabled("Disabled on headless environment");

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    return GraphicsEnvironment.isHeadless() ? DISABLED : ENABLED;
  }
}

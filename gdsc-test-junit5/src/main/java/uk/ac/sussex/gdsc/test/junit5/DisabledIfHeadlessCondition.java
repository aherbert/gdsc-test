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

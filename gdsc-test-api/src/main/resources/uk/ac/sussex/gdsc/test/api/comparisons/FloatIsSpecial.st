<package>

import uk.ac.sussex.gdsc.test.api.function.<Float>Predicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests a {@code <float>} is <Special>.
 *
 * @see <Float>Predicate#test(<float>)
 * @see <Float>#is<Special>(<float>)
 */
public class <Float>Is<Special>
    implements <Float>Predicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** A default instance. */
  private static final <Float>Is<Special> INSTANCE = new <Float>Is<Special>();

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that the value is <Special>.
   *
   * @return true if <Special>
   * @see <Float>#is<Special>(<float>)
   */
  @Override
  public boolean test(<float> value) {
    return <Float>.is<Special>(value);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return "value is <Special>";
  }

  /**
   * Gets an instance.
   *
   * @return an instance
   */
  public static <Float>Is<Special> getInstance() {
    return INSTANCE;
  }
}

<package>

<! Explicit imports (not templated) so the sort order is correct for Checkstyle !>
import uk.ac.sussex.gdsc.test.api.equality.BooleanBooleanEquals;
import uk.ac.sussex.gdsc.test.api.equality.BooleanEquals;
import uk.ac.sussex.gdsc.test.api.equality.ByteByteEquals;
import uk.ac.sussex.gdsc.test.api.equality.ByteEquals;
import uk.ac.sussex.gdsc.test.api.equality.CharCharEquals;
import uk.ac.sussex.gdsc.test.api.equality.CharEquals;
import uk.ac.sussex.gdsc.test.api.equality.DoubleCmp;
import uk.ac.sussex.gdsc.test.api.equality.DoubleDoubleAlmostEquals;
import uk.ac.sussex.gdsc.test.api.equality.DoubleDoubleCmp;
import uk.ac.sussex.gdsc.test.api.equality.DoubleDoubleEquals;
import uk.ac.sussex.gdsc.test.api.equality.DoubleEquals;
import uk.ac.sussex.gdsc.test.api.equality.FloatCmp;
import uk.ac.sussex.gdsc.test.api.equality.FloatEquals;
import uk.ac.sussex.gdsc.test.api.equality.FloatFloatAlmostEquals;
import uk.ac.sussex.gdsc.test.api.equality.FloatFloatCmp;
import uk.ac.sussex.gdsc.test.api.equality.FloatFloatEquals;
import uk.ac.sussex.gdsc.test.api.equality.IntEquals;
import uk.ac.sussex.gdsc.test.api.equality.IntIntEquals;
import uk.ac.sussex.gdsc.test.api.equality.LongEquals;
import uk.ac.sussex.gdsc.test.api.equality.LongLongEquals;
import uk.ac.sussex.gdsc.test.api.equality.ShortEquals;
import uk.ac.sussex.gdsc.test.api.equality.ShortShortEquals;
import uk.ac.sussex.gdsc.test.api.function.BooleanBooleanBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.ByteByteBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicate;
import uk.ac.sussex.gdsc.test.api.function.CharCharBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoubleDoubleBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatFloatBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntIntBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongLongBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortShortBiPredicate;

/**
 * Helper class to create predicates for tests.
 */
public final class TestHelper {

  /**
   * Do not allow public construction.
   */
  private TestHelper() {}
<types,Types:{type,Type |
<\n>
  /**
   * Creates a predicate to compare a {@code <type>\} value to the reference value using the
   * {@code ==\} operator.
   *
   * @param referenceValue the reference value
   * @return the predicate
   */
  public static <Type>Predicate equals(<type> referenceValue) {
    return new <Type>Equals(referenceValue);
  \}}><types,Types:{type,Type |
<\n>
  /**
   * Creates a predicate to compare {@code <type>\} values using the {@code ==\} operator.
   *
   * @return the predicate
   */
  public static <Type><Type>BiPredicate equal<Type>s() {
    return <Type><Type>Equals.getInstance();
  \}}><floats,Floats,Ints:{float,Float,Int |
<\n>
  /**
   * Creates a predicate to compare a {@code <type>\} value to the reference value using binary
   * comparison of the values converted using {@link <Float>#<float>To<Int>Bits(<float>)\}.
   *
   * @param referenceValue the reference value
   * @return the predicate
   */
  public static <Float>Predicate cmp(<float> referenceValue) {
    return new <Float>Cmp(referenceValue);
  \}}><floats,Floats,Ints:{float,Float,Int |
<\n>
  /**
   * Creates a predicate to compare {@code <float>\} values using binary comparison of the values
   * converted using {@link <Float>#<float>To<Int>Bits(<float>)\}.
   *
   * @return the predicate
   */
  public static <Float><Float>BiPredicate cmp<Float>s() {
    return <Float><Float>Cmp.getInstance();
  \}}><floats,Floats,Ints:{float,Float,Int |
<\n>
  /**
   * Creates a predicate to compare {@code <float>\} values using a relative and/or an absolute
   * error tolerance.
   *
   * \<p>Setting the relative error to negative tests equality using only absolute
   * error.
   *
   * \<p>Setting the absolute error to negative tests equality using only relative
   * error.
   *
   * \<p>If both errors are disabled an exception is thrown since no equality test is
   * performed.
   *
   * \<p>Note: the relative error is a double since the relative error computation is
   * performed in double precision.
   *
   * @param relativeError The maximum relative error between \<code>value1\</code>
   *                      and \<code>value2\</code> for which both numbers are still
   *                      considered equal. Ignored if set to negative.
   * @param absoluteError The maximum absolute error between \<code>value1\</code>
   *                      and \<code>value2\</code> for which both numbers are still
   *                      considered equal. Ignored if set to negative.
   * @throws IllegalArgumentException If the relative error is not below 2
   * @throws IllegalArgumentException If the absolute error is not finite
   * @throws IllegalArgumentException If both tolerances are ignored (i.e. no
   *                                  equality test is performed)
   * @return the predicate
   */
  public static <Float><Float>BiPredicate almostEqual<Float>s(double relativeError,
      <float> absoluteError) {
    return new <Float><Float>AlmostEquals(relativeError, absoluteError);
  \}}>
}

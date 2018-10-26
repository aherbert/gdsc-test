<package>

<! Explicit imports (not templated) so the sort order is correct for Checkstyle !>
import uk.ac.sussex.gdsc.test.api.comparisons.BooleanBooleanEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.BooleanEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteByteEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteByteWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.CharCharEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.CharEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleCmp;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleClose;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleCmp;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatCmp;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatClose;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatCmp;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.IntEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIntEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIntWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.LongEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.LongLongBigWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.LongLongEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.LongLongWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortShortEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortShortWithin;
import uk.ac.sussex.gdsc.test.api.function.BooleanBooleanBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicateAndBooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicateOrBooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicateXorBooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.ByteByteBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicateAndBytePredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicateOrBytePredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicateXorBytePredicate;
import uk.ac.sussex.gdsc.test.api.function.CharCharBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicateAndCharPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicateOrCharPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicateXorCharPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoubleDoubleBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicateAndDoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicateOrDoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicateXorDoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatFloatBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicateAndFloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicateOrFloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicateXorFloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntIntBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicateAndIntPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicateOrIntPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicateXorIntPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongLongBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicateAndLongPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicateOrLongPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicateXorLongPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicateAndShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicateOrShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicateXorShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortShortBiPredicate;

import java.math.BigInteger;

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
   * Creates a predicate to test a {@code <type>\} value is equal to the reference value using the
   * {@code ==\} operator.
   *
   * @param referenceValue the reference value
   * @return the predicate
   */
  public static <Type>Predicate <type>Equals(<type> referenceValue) {
    return new <Type>Equals(referenceValue);
  \}}><types,Types:{type,Type |
<\n>
  /**
   * Creates a predicate to test {@code <type>\} values are equal using the {@code ==\} operator.
   *
   * @return the predicate
   */
  public static <Type><Type>BiPredicate <type>sEqual() {
    return <Type><Type>Equals.getInstance();
  \}}><floats,Floats,Ints:{float,Float,Int |
<\n>
  /**
   * Creates a predicate to test a {@code <float>\} value is equal to the reference value using
   * binary comparison of the values converted using {@link <Float>#<float>To<Int>Bits(<float>)\}.
   *
   * @param referenceValue the reference value
   * @return the predicate
   */
  public static <Float>Predicate <float>Cmp(<float> referenceValue) {
    return new <Float>Cmp(referenceValue);
  \}}><floats,Floats,Ints:{float,Float,Int |
<\n>
  /**
   * Creates a predicate to test {@code <float>\} values are equal using binary comparison of the
   * values converted using {@link <Float>#<float>To<Int>Bits(<float>)\}.
   *
   * @return the predicate
   */
  public static <Float><Float>BiPredicate <float>sCmp() {
    return <Float><Float>Cmp.getInstance();
  \}}><numbers,Numbers,numberAbsTypes,Withins:{number,Number,numberAbsType,Within |
<\n>
  /**
   * Creates a predicate to test {@code <number>\} values are equal within an absolute error.
   *
   * @param absoluteError The maximum absolute error between \<code>value1\</code> and
   *        \<code>value2\</code> for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException If the absolute error is not positive or below the maximum
   *         measurable difference between <number> values
   */
  public static <Number><Number>BiPredicate <number>sWithin(<numberAbsType> absoluteError) {
    return new <Number><Number><Within>(absoluteError);
  \}}><floats,Floats,Ints:{float,Float,Int |
<\n>
  /**
   * Creates a predicate to test that {@code <float>\} values are close using a relative error.
   * The relative error between values {@code value1\} and {@code value2\} is
   * relative to the largest magnitude of the two values:
   *
   * \<pre>
   * relativeError = |value1-value2| / max(|value1|, |value2|)
   * \</pre>
   *
   * \<p>The relative error is symmetric for {@code value1\} and {@code value2\}. The test is
   * equivalent to testing convergence of two values.
   *
   * \<p>Note: If either value is {@link <Float>#NaN\} or infinite then there is no valid
   * difference to another value and the predicate will return false. This includes a test of
   * infinity with itself which is in contrast to the {@code ==\} operator. Thus the predicate
   * is a test of the measured distance between two finite values.
   *
   * \<p>An absolute error of {@code 0\} is assumed allowing all values to equal themselves
   * with the exception of {@link <Float>#NaN\} or infinite values.
   *
   * @param relativeError The maximum relative error between \<code>value1\</code> and
   *        \<code>value2\</code> for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   */
  public static <Float><Float>BiPredicate <float>sClose(double relativeError) {
    return new <Float><Float>Close(relativeError, 0);
  \}

  /**
   * Creates a predicate to test that {@code <float>\} values are close using a relative and/or
   * an absolute error. The relative error between values {@code value1\} and {@code value2\} is
   * relative to the largest magnitude of the two values:
   *
   * \<pre>
   * relativeError = |value1-value2| / max(|value1|, |value2|)
   * \</pre>
   *
   * \<p>The relative error is symmetric for {@code value1\} and {@code value2\}. The test is
   * equivalent to testing convergence of two values.
   *
   * \<p>Note: If either value is {@link <Float>#NaN\} or infinite then there is no valid
   * difference to another value and the predicate will return false. This includes a test of
   * infinity with itself which is in contrast to the {@code ==\} operator. Thus the predicate
   * is a test of the measured distance between two finite values.
   *
   * @param relativeError The maximum relative error between \<code>value1\</code> and
   *        \<code>value2\</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between \<code>value1\</code> and
   *        \<code>value2\</code> for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  public static <Float><Float>BiPredicate <float>sClose(double relativeError,
      <float> absoluteError) {
    return new <Float><Float>Close(relativeError, absoluteError);
  \}}><floats,Floats,Ints:{float,Float,Int |
<\n>
  /**
   * Creates a predicate to test a {@code <float>\} value is close to an expected value using a
   * relative error. The relative error between values
   * {@code expected\} and {@code actual\} is relative to the magnitude of {@code expected\}:
   *
   * \<pre>
   * relativeError = |expected-actual| / |expected|
   * \</pre>
   *
   * \<p>The relative error is asymmetric for {@code expected\} and {@code actual\}. The test is
   * equivalent to testing testing {@code actual\} falls within a relative range of
   * {@code expected\}.
   *
   * \<p>The arguments for the method {@link <Float><Float>BiPredicate#test(<float>, <float>)\}
   * are {@code expected\} then {@code actual\}.
   *
   * \<p>Note: If either value is {@link <Float>#NaN\} or infinite then there is no valid
   * difference to another value and the predicate will return false. This includes a test of
   * infinity with itself which is in contrast to the {@code ==\} operator. Thus the predicate
   * is a test of the measured distance between two finite values.
   *
   * \<p>An absolute error of {@code 0\} is assumed allowing all values to equal themselves
   * with the exception of {@link <Float>#NaN\} or infinite values.
   *
   * @param relativeError The maximum error, relative to \<code>expected\</code>, between
   *        \<code>expected\</code> and \<code>actual\</code> for which both numbers are still
   *        considered equal.
   * @return the predicate
   * @throws IllegalArgumentException If the relative error is not positive finite
   */
  public static <Float><Float>BiPredicate <float>sIsCloseTo(double relativeError) {
    return new <Float><Float>IsCloseTo(relativeError, 0);
  \}

  /**
   * Creates a predicate to test a {@code <float>\} value is close to an expected value using a
   * relative and/or an absolute error. The relative error between values
   * {@code expected\} and {@code actual\} is relative to the magnitude of {@code expected\}:
   *
   * \<pre>
   * relativeError = |expected-actual| / |expected|
   * \</pre>
   *
   * \<p>The relative error is asymmetric for {@code expected\} and {@code actual\}. The test is
   * equivalent to testing testing {@code actual\} falls within a relative and/or absolute range of
   * {@code expected\}.
   *
   * \<p>The arguments for the method {@link <Float><Float>BiPredicate#test(<float>, <float>)\}
   * are {@code expected\} then {@code actual\}.
   *
   * \<p>Note: If either value is {@link <Float>#NaN\} or infinite then there is no valid
   * difference to another value and the predicate will return false. This includes a test of
   * infinity with itself which is in contrast to the {@code ==\} operator. Thus the predicate
   * is a test of the measured distance between two finite values.
   *
   * @param relativeError The maximum error, relative to \<code>expected\</code>, between
   *        \<code>expected\</code> and \<code>actual\</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between \<code>expected\</code> and
   *        \<code>actual\</code> for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  public static <Float><Float>BiPredicate <float>sIsCloseTo(double relativeError,
      <float> absoluteError) {
    return new <Float><Float>IsCloseTo(relativeError, absoluteError);
  \}}><Ops,ops:{Op,op |<types,Types:{type,Type |
<\n>
  /**
   * Creates a predicate to test {@code <type>\} values using a logical {@code <Op>\} of two
   * single-value predicates, each predicate testing a different value of the
   * {@link <Type><Type>BiPredicate#test(<type>, <type>)\} method.
   *
   * @param predicate1 The predicate to test the first value
   * @param predicate2 The predicate to test the second value
   * @return the predicate
   */
  public static <Type><Type>BiPredicate <op>(<Type>Predicate predicate1,
      <Type>Predicate predicate2) {
    return new <Type>Predicate<Op><Type>Predicate(predicate1, predicate2);
  \}}>}>
}

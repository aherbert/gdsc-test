<package>

<! Explicit imports (not templated) so the sort order is correct for Checkstyle !>
import uk.ac.sussex.gdsc.test.api.comparisons.BooleanBooleanEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.BooleanEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteByteAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteByteAreWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteByteEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteByteIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.CharCharEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.CharEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleBinaryEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleAreWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleAreWithinUlp;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleBinaryEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsFinite;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsInfinite;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsNaN;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatBinaryEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatAreWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatAreWithinUlp;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatBinaryEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsFinite;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsInfinite;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsNaN;
import uk.ac.sussex.gdsc.test.api.comparisons.IntEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIntAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIntAreWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIntEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIntIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.LongEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.LongLongAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.LongLongAreWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.LongLongEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.LongLongIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortShortAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortShortAreWithin;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortShortEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortShortIsCloseTo;
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

/**
 * Create predicates for tests.
 */
public final class Predicates {

  /**
   * Do not allow public construction.
   */
  private Predicates() {}
<types,Types:{type,Type |
<\n>
  /**
   * Creates a predicate to test a {@code <type>\} value is equal to the reference value using the
   * {@code ==\} operator.
   *
   * @param referenceValue the reference value
   * @return the predicate
   */
  public static <Type>Predicate <type>IsEqualTo(<type> referenceValue) {
    return new <Type>Equals(referenceValue);
  \}}><types,Types:{type,Type |
<\n>
  /**
   * Creates a predicate to test {@code <type>\} values are equal using the {@code ==\} operator.
   *
   * @return the predicate
   */
  public static <Type><Type>BiPredicate <type>sAreEqual() {
    return <Type><Type>Equals.INSTANCE;
  \}}><floats,Floats,Ints:{float,Float,Int |
<\n>
  /**
   * Creates a predicate to test a {@code <float>\} value is equal to the reference value using
   * binary comparison of the values converted using {@link <Float>#<float>To<Int>Bits(<float>)\}.
   *
   * @param referenceValue the reference value
   * @return the predicate
   */
  public static <Float>Predicate <float>IsBinaryEqualTo(<float> referenceValue) {
    return new <Float>BinaryEqual(referenceValue);
  \}}><floats,Floats,Ints:{float,Float,Int |
<\n>
  /**
   * Creates a predicate to test {@code <float>\} values are equal using binary comparison of the
   * values converted using {@link <Float>#<float>To<Int>Bits(<float>)\}.
   *
   * @return the predicate
   */
  public static <Float><Float>BiPredicate <float>sAreBinaryEqual() {
    return <Float><Float>BinaryEqual.INSTANCE;
  \}}><floats,Floats,ulpTypes:{float,Float,ulpType |
<\n>
  /**
   * Creates a predicate to test {@code <float>\} values are equal within a ULP error.
   *
   * @param ulpError The maximum ULP error between \<code>value1\</code> and
   *        \<code>value2\</code> for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException If the ulp error is not positive
   */
  public static <Float><Float>BiPredicate <float>sAreUlpClose(<ulpType> ulpError) {
    return new <Float><Float>AreWithinUlp(ulpError);
  \}}><numbers,Numbers,numberAbsTypes:{number,Number,numberAbsType |
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
  public static <Number><Number>BiPredicate <number>sAreAbsolutelyClose(<numberAbsType> absoluteError) {
    return new <Number><Number>AreWithin(absoluteError);
  \}}><numbers,Numbers,numberAbsTypes,javadocNaNs:{number,Number,numberAbsType,javadocNaN |
<\n>
  /**
   * Creates a predicate to test that {@code <number>\} values are close using a relative error.
   * The relative error between values {@code value1\} and {@code value2\} is relative to the
   * largest magnitude of the two values and the test is:
   *
   * \<pre>
   * {@code
   * |value1-value2| \<= max(|value1|, |value2|) * relativeError
   * \}
   * \</pre>
   *
   * \<p>The relative error is symmetric for {@code value1\} and {@code value2\}. The test is
   * equivalent to testing convergence of two values.
<if(javadocNaN)>
   *
   * \<p>Note: If either value is {@link <Number>#NaN\} or infinite then there is no valid
   * difference to another value and the predicate will return false. This includes a test of
   * infinity with itself which is in contrast to the {@code ==\} operator. Thus the predicate
   * is a test of the measured distance between two finite values.
   *
   * \<p>An absolute error of {@code 0\} is assumed allowing all values to equal themselves
   * with the exception of {@link <Number>#NaN\} or infinite values. This equality will
   * include the special case of {@code -0.0 == 0.0\}.
<else>
   *
   * \<p>An absolute error of {@code 0\} is assumed allowing all values to equal themselves.
<endif>
   *
   * @param relativeError The maximum relative error between \<code>value1\</code> and
   *        \<code>value2\</code> for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   */
  public static <Number><Number>BiPredicate <number>sAreRelativelyClose(double relativeError) {
    return new <Number><Number>AreClose(relativeError, 0);
  \}

  /**
   * Creates a predicate to test that {@code <number>\} values are close using a relative and/or
   * an absolute error. The relative error between values {@code value1\} and {@code value2\} is
   * relative to the largest magnitude of the two values and the test is:
   *
   * \<pre>
   * {@code
   * |value1-value2| \<= max(|value1|, |value2|) * relativeError
   * \}
   * \</pre>
   *
   * \<p>The relative error is symmetric for {@code value1\} and {@code value2\}. The test is
   * equivalent to testing convergence of two values.
<if(javadocNaN)>
   *
   * \<p>Note: If either value is {@link <Number>#NaN\} or infinite then there is no valid
   * difference to another value and the predicate will return false. This includes a test of
   * infinity with itself which is in contrast to the {@code ==\} operator. Thus the predicate
   * is a test of the measured distance between two finite values.
<endif>
   *
   * @param relativeError The maximum relative error between \<code>value1\</code> and
   *        \<code>value2\</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between \<code>value1\</code> and
   *        \<code>value2\</code> for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive or is
   *         \<code>&lt;=\</code> than the maximum difference between \<code><number>\</code>
   *         primitives
   */
  public static <Number><Number>BiPredicate <number>sAreClose(double relativeError,
      <numberAbsType> absoluteError) {
    return new <Number><Number>AreClose(relativeError, absoluteError);
  \}}><numbers,Numbers,numberAbsTypes,javadocNaNs:{number,Number,numberAbsType,javadocNaN |
<\n>
  /**
   * Creates a predicate to test a {@code <number>\} value {@code B\} is close to an expected
   * value {@code A\} using a relative error. The relative error between values
   * {@code expected\} and {@code actual\} is relative to the magnitude of {@code expected\}
   * and the test is:
   *
   * \<pre>
   * {@code
   * |expected-actual| \<= |expected| * relativeError
   * \}
   * \</pre>
   *
   * \<p>The relative error is asymmetric for {@code expected\} and {@code actual\}. The test is
   * equivalent to testing {@code actual\} falls within a relative range of
   * {@code expected\}.
   *
   * \<p>The arguments for the method {@link <Number><Number>BiPredicate#test(<number>, <number>)\}
   * are {@code expected\} then {@code actual\}.
<if(javadocNaN)>
   *
   * \<p>Note: If either value is {@link <Number>#NaN\} or infinite then there is no valid
   * difference to another value and the predicate will return false. This includes a test of
   * infinity with itself which is in contrast to the {@code ==\} operator. Thus the predicate
   * is a test of the measured distance between two finite values.
   *
   * \<p>An absolute error of {@code 0\} is assumed allowing all values to equal themselves
   * with the exception of {@link <Number>#NaN\} or infinite values. This equality will
   * include the special case of {@code -0.0 == 0.0\}.
<else>
   *
   * \<p>An absolute error of {@code 0\} is assumed allowing all values to equal themselves.
<endif>
   *
   * @param relativeError The maximum error, relative to \<code>expected\</code>, between
   *        \<code>expected\</code> and \<code>actual\</code> for which both numbers are still
   *        considered equal.
   * @return the predicate
   * @throws IllegalArgumentException If the relative error is not positive finite
   */
  public static <Number><Number>BiPredicate <number>sBIsRelativelyCloseToA(double relativeError) {
    return new <Number><Number>IsCloseTo(relativeError, 0);
  \}

  /**
   * Creates a predicate to test a {@code <number>\} value {@code B\} is close to an expected
   * value {@code A\} using a relative and/or absolute error. The relative error between values
   * {@code expected\} and {@code actual\} is relative to the magnitude of {@code expected\}
   * and the test is:
   *
   * \<pre>
   * {@code
   * |expected-actual| \<= |expected| * relativeError
   * \}
   * \</pre>:
   *
   * \<p>The relative error is asymmetric for {@code expected\} and {@code actual\}. The test is
   * equivalent to testing {@code actual\} falls within a relative and/or absolute range of
   * {@code expected\}.
   *
   * \<p>The arguments for the method {@link <Number><Number>BiPredicate#test(<number>, <number>)\}
   * are {@code expected\} then {@code actual\}.
<if(javadocNaN)>
   *
   * \<p>Note: If either value is {@link <Number>#NaN\} or infinite then there is no valid
   * difference to another value and the predicate will return false. This includes a test of
   * infinity with itself which is in contrast to the {@code ==\} operator. Thus the predicate
   * is a test of the measured distance between two finite values.
<endif>
   *
   * @param relativeError The maximum error, relative to \<code>expected\</code>, between
   *        \<code>expected\</code> and \<code>actual\</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between \<code>expected\</code> and
   *        \<code>actual\</code> for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive or is
   *         \<code>&lt;=\</code> than the maximum difference between \<code><number>\</code>
   *         primitives
   */
  public static <Number><Number>BiPredicate <number>sBIsCloseToA(double relativeError,
      <numberAbsType> absoluteError) {
    return new <Number><Number>IsCloseTo(relativeError, absoluteError);
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
  \}}>}><Floats,floats:{Float,float |<Specials:{Special |
<\n>
  /**
   * Creates a predicate to test a {@code <float>\} value is <Special>.
   *
   * @return the predicate
   * @see <Float>#is<Special>(<float>)
   */
  public static <Float>Predicate <float>Is<Special>() {
    return <Float>Is<Special>.INSTANCE;
  \}}>}>
}

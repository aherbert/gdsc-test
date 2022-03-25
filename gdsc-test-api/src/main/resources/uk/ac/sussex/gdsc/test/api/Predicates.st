<package>

<! Explicit imports (not templated) so the sort order is correct for Checkstyle !>
import uk.ac.sussex.gdsc.test.api.comparisons.BooleanBooleanAreEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.BooleanIsEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteByteAreAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteByteAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteByteAreEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteByteIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteIsAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteIsClose;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteIsEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.CharCharAreEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.CharIsEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleAreAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleAreBinaryEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleAreEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleAreUlpClose;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleDoubleIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsBinaryEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsClose;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsFinite;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsInfinite;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsNaN;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleIsUlpClose;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatAreAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatAreBinaryEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatAreEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatAreUlpClose;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatFloatIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsBinaryEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsClose;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsFinite;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsInfinite;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsNaN;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatIsUlpClose;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIntAreAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIntAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIntAreEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIntIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIsAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIsClose;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.IntIsEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.LongIsAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.LongIsClose;
import uk.ac.sussex.gdsc.test.api.comparisons.LongIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.LongIsEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.LongLongAreAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.LongLongAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.LongLongAreEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.LongLongIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortIsAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortIsClose;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortIsCloseTo;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortIsEqual;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortShortAreAbsolutelyClose;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortShortAreClose;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortShortAreEqual;
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
<types,Types,tan:{type,Type,an |
<\n>
  /**
   * Creates a predicate to test <an> {@code <type>\} value is equal to the {@code reference\} value
   * using the {@code ==\} operator.
   *
   * @param reference the reference value
   * @return the predicate
   */
  public static <Type>Predicate <type>IsEqualTo(<type> reference) {
    return new <Type>IsEqual(reference);
  \}}><types,Types:{type,Type |
<\n>
  /**
   * Creates a predicate to test two {@code <type>\} values are equal using the {@code ==\}
   * operator.
   *
   * @return the predicate
   */
  public static <Type><Type>BiPredicate <type>sAreEqual() {
    return <Type><Type>AreEqual.INSTANCE;
  \}}><floats,Floats,Ints:{float,Float,Int |
<\n>
  /**
   * Creates a predicate to test a {@code <float>\} value is equal to the {@code reference\} value
   * using binary comparison of the values converted using {@link <Float>#<float>To<Int>Bits(<float>)\}.
   *
   * @param reference the reference value
   * @return the predicate
   */
  public static <Float>Predicate <float>IsBinaryEqual(<float> reference) {
    return new <Float>IsBinaryEqual(reference);
  \}}><floats,Floats,Ints:{float,Float,Int |
<\n>
  /**
   * Creates a predicate to test two {@code <float>\} values are equal using binary comparison
   * of the values converted using {@link <Float>#<float>To<Int>Bits(<float>)\}.
   *
   * @return the predicate
   */
  public static <Float><Float>BiPredicate <float>sAreBinaryEqual() {
    return <Float><Float>AreBinaryEqual.INSTANCE;
  \}}><floats,Floats,ulpTypes:{float,Float,ulpType |
<\n>
  /**
   * Creates a predicate to test a {@code <float>\} value is close to a {@code reference\} value
   * using a ULP error.
   *
   * @param reference the reference value
   * @param ulpError the maximum ULP error between {@code reference\} and
   *        {@code value\} for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the ulp error is not positive
   */
  public static <Float>Predicate <float>IsUlpClose(<float> reference,
      <ulpType> ulpError) {
    return new <Float>IsUlpClose(reference, ulpError);
  \}}><floats,Floats,ulpTypes:{float,Float,ulpType |
<\n>
  /**
   * Creates a predicate to test two {@code <float>\} values are close using a ULP error.
   *
   * @param ulpError the maximum ULP error between {@code value1\} and
   *        {@code value2\} for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the ulp error is not positive
   */
  public static <Float><Float>BiPredicate <float>sAreUlpClose(<ulpType> ulpError) {
    return new <Float><Float>AreUlpClose(ulpError);
  \}}><numbers,Numbers,numberAbsTypes,an:{number,Number,numberAbsType,an |
<\n>
  /**
   * Creates a predicate to test <an> {@code <number>\} value is close to a {@code reference\} value
   * using an absolute error.
   *
   * @param reference the reference value
   * @param absoluteError the maximum absolute error between {@code reference\} and
   *        {@code value\} for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the absolute error is not positive or below the maximum
   *         measurable difference between <number> values
   */
  public static <Number>Predicate <number>IsAbsolutelyClose(<number> reference,
      <numberAbsType> absoluteError) {
    return new <Number>IsAbsolutelyClose(reference, absoluteError);
  \}}><numbers,Numbers,numberAbsTypes:{number,Number,numberAbsType |
<\n>
  /**
   * Creates a predicate to test two {@code <number>\} values are close using an absolute error.
   *
   * @param absoluteError the maximum absolute error between {@code value1\} and
   *        {@code value2\} for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the absolute error is not positive or below the maximum
   *         measurable difference between <number> values
   */
  public static <Number><Number>BiPredicate <number>sAreAbsolutelyClose(
      <numberAbsType> absoluteError) {
    return new <Number><Number>AreAbsolutelyClose(absoluteError);
  \}}><numbers,Numbers,numberAbsTypes,javadocNaNs,zero,an:{number,Number,numberAbsType,javadocNaN,zero,an |
<\n>
  /**
   * Creates a predicate to test <an> {@code <number>\} value is close to a {@code reference\} value
   * using a relative error.
   * The relative error between values {@code reference\} and {@code value\} is relative to the
   * largest magnitude of the two values and the test is:
   *
   * \<pre>
   * {@code
   * |reference - value| \<= max(|reference|, |value|) * relativeError
   * \}\</pre>
   *
   * \<p>The relative error is symmetric for {@code reference\} and {@code value\}. The test is
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
   * include the special case of {@code -<zero> == <zero>\}.
<else>
   *
   * \<p>An absolute error of {@code 0\} is assumed allowing all values to equal themselves.
<endif>
   *
   * @param reference the reference value
   * @param relativeError the maximum relative error between {@code reference\} and
   *        {@code value\} for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the relative error is not positive finite and below 2
   */
  public static <Number>Predicate <number>IsRelativelyClose(<number> reference,
      double relativeError) {
    return new <Number>IsClose(reference, relativeError, 0);
  \}

  /**
   * Creates a predicate to test <an> {@code <number>\} value is close to a {@code reference\} value
   * using a relative and/or an absolute error.
   * The relative error between values {@code value1\} and {@code value2\} is
   * relative to the largest magnitude of the two values and the test is:
   *
   * \<pre>
   * {@code
   * |reference - value| \<= max(|reference|, |value|) * relativeError
   * \}\</pre>
   *
   * \<p>The relative error is symmetric for {@code reference\} and {@code value\}. The test is
   * equivalent to testing convergence of two values.
<if(javadocNaN)>
   *
   * \<p>Note: If either value is {@link <Number>#NaN\} or infinite then there is no valid
   * difference to another value and the predicate will return false. This includes a test of
   * infinity with itself which is in contrast to the {@code ==\} operator. Thus the predicate
   * is a test of the measured distance between two finite values.
<endif>
   *
   * @param reference the reference value
   * @param relativeError the maximum relative error between {@code reference\} and
   *        {@code value\} for which both numbers are still considered equal.
   * @param absoluteError the maximum absolute error between {@code reference\} and
   *        {@code value\} for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the relative error is not positive finite and below 2
   * @throws IllegalArgumentException if the absolute error is not positive or is
   *         {@code \<=\} than the maximum difference between {@code <number>\}
   *         primitives
   */
  public static <Number>Predicate <number>IsClose(<number> reference, double relativeError,
      <numberAbsType> absoluteError) {
    return new <Number>IsClose(reference, relativeError, absoluteError);
  \}}><numbers,Numbers,numberAbsTypes,javadocNaNs,zero:{number,Number,numberAbsType,javadocNaN,zero |
<\n>
  /**
   * Creates a predicate to test two {@code <number>\} values are close using a relative error.
   * The relative error between values {@code value1\} and {@code value2\} is relative to the
   * largest magnitude of the two values and the test is:
   *
   * \<pre>
   * {@code
   * |value1 - value2| \<= max(|value1|, |value2|) * relativeError
   * \}\</pre>
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
   * include the special case of {@code -<zero> == <zero>\}.
<else>
   *
   * \<p>An absolute error of {@code 0\} is assumed allowing all values to equal themselves.
<endif>
   *
   * @param relativeError the maximum relative error between {@code value1\} and
   *        {@code value2\} for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the relative error is not positive finite and below 2
   */
  public static <Number><Number>BiPredicate <number>sAreRelativelyClose(double relativeError) {
    return new <Number><Number>AreClose(relativeError, 0);
  \}

  /**
   * Creates a predicate to test two {@code <number>\} values are close using a relative and/or
   * an absolute error. The relative error between values {@code value1\} and {@code value2\} is
   * relative to the largest magnitude of the two values and the test is:
   *
   * \<pre>
   * {@code
   * |value1 - value2| \<= max(|value1|, |value2|) * relativeError
   * \}\</pre>
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
   * @param relativeError the maximum relative error between {@code value1\} and
   *        {@code value2\} for which both numbers are still considered equal.
   * @param absoluteError the maximum absolute error between {@code value1\} and
   *        {@code value2\} for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the relative error is not positive finite and below 2
   * @throws IllegalArgumentException if the absolute error is not positive or is
   *         {@code \<=\} than the maximum difference between {@code <number>\}
   *         primitives
   */
  public static <Number><Number>BiPredicate <number>sAreClose(double relativeError,
      <numberAbsType> absoluteError) {
    return new <Number><Number>AreClose(relativeError, absoluteError);
  \}}><numbers,Numbers,numberAbsTypes,javadocNaNs,zero,an:{number,Number,numberAbsType,javadocNaN,zero,an |
<\n>
  /**
   * Creates a predicate to test <an> {@code <number>\} value is close to a {@code reference\} value
   * using a relative error. The relative error between values
   * {@code reference\} and {@code value\} is relative to the magnitude of {@code reference\}
   * and the test is:
   *
   * \<pre>
   * {@code
   * |reference - value| \<= |reference| * relativeError
   * \}\</pre>
   *
   * \<p>The relative error is asymmetric for {@code reference\} and {@code value\}. The test is
   * equivalent to testing {@code value\} falls within a relative range of
   * {@code reference\}.
<if(javadocNaN)>
   *
   * \<p>Note: If either value is {@link <Number>#NaN\} or infinite then there is no valid
   * difference to another value and the predicate will return false. This includes a test of
   * infinity with itself which is in contrast to the {@code ==\} operator. Thus the predicate
   * is a test of the measured distance between two finite values.
   *
   * \<p>An absolute error of {@code 0\} is assumed allowing all values to equal themselves
   * with the exception of {@link <Number>#NaN\} or infinite values. This equality will
   * include the special case of {@code -<zero> == <zero>\}.
<else>
   *
   * \<p>An absolute error of {@code 0\} is assumed allowing all values to equal themselves.
<endif>
   *
   * @param reference the reference value
   * @param relativeError the maximum error, relative to {@code reference\}, between
   *        {@code reference\} and {@code value\} for which both numbers are still
   *        considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the relative error is not positive finite
   */
  public static <Number>Predicate <number>IsRelativelyCloseTo(<number> reference,
      double relativeError) {
    return new <Number>IsCloseTo(reference, relativeError, 0);
  \}

  /**
   * Creates a predicate to test <an> {@code <number>\} value is close to a {@code reference\} value
   * using a relative and/or absolute error. The relative error between values
   * {@code reference\} and {@code value\} is relative to the magnitude of {@code reference\}
   * and the test is:
   *
   * \<pre>
   * {@code
   * |reference - value| \<= |reference| * relativeError
   * \}\</pre>
   *
   * \<p>The relative error is asymmetric for {@code reference\} and {@code value\}. The test is
   * equivalent to testing {@code value\} falls within a relative and/or absolute range of
   * {@code reference\}.
<if(javadocNaN)>
   *
   * \<p>Note: If either value is {@link <Number>#NaN\} or infinite then there is no valid
   * difference to another value and the predicate will return false. This includes a test of
   * infinity with itself which is in contrast to the {@code ==\} operator. Thus the predicate
   * is a test of the measured distance between two finite values.
   *
   * \<p>An absolute error of {@code 0\} will allow all values to equal themselves
   * with the exception of {@link <Number>#NaN\} or infinite values. This equality will
   * include the special case of {@code -<zero> == <zero>\}.
<endif>
   *
   * @param reference the reference value
   * @param relativeError the maximum error, relative to {@code reference\}, between
   *        {@code reference\} and {@code value\} for which both numbers are still
   *        considered equal.
   * @param absoluteError the maximum absolute error between {@code reference\} and
   *        {@code value\} for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the relative error is not positive finite
   * @throws IllegalArgumentException if the absolute error is not positive or is
   *         {@code \<=\} than the maximum difference between {@code <number>\}
   *         primitives
   */
  public static <Number>Predicate <number>IsCloseTo(<number> reference,
      double relativeError, <numberAbsType> absoluteError) {
    return new <Number>IsCloseTo(reference, relativeError, absoluteError);
  \}}><numbers,Numbers,numberAbsTypes,javadocNaNs,zero:{number,Number,numberAbsType,javadocNaN,zero |
<\n>
  /**
   * Creates a predicate to test an {@code actual\} value is close to an {@code expected\}
   * value using a relative error. The relative error between values
   * {@code expected\} and {@code actual\} is relative to the magnitude of {@code expected\}
   * and the test is:
   *
   * \<pre>
   * {@code
   * |expected - actual| \<= |expected| * relativeError
   * \}\</pre>
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
   * include the special case of {@code -<zero> == <zero>\}.
<else>
   *
   * \<p>An absolute error of {@code 0\} is assumed allowing all values to equal themselves.
<endif>
   *
   * @param relativeError the maximum error, relative to {@code expected\}, between
   *        {@code expected\} and {@code actual\} for which both numbers are still
   *        considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the relative error is not positive finite
   */
  public static <Number><Number>BiPredicate <number>sIsRelativelyCloseTo(double relativeError) {
    return new <Number><Number>IsCloseTo(relativeError, 0);
  \}

  /**
   * Creates a predicate to test an {@code actual\} value is close to an {@code expected\}
   * value using a relative and/or absolute error. The relative error between values
   * {@code expected\} and {@code actual\} is relative to the magnitude of {@code expected\}
   * and the test is:
   *
   * \<pre>
   * {@code
   * |expected - actual| \<= |expected| * relativeError
   * \}\</pre>:
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
   *
   * \<p>An absolute error of {@code 0\} will allow all values to equal themselves
   * with the exception of {@link <Number>#NaN\} or infinite values. This equality will
   * include the special case of {@code -<zero> == <zero>\}.
<endif>
   *
   * @param relativeError the maximum error, relative to {@code expected\}, between
   *        {@code expected\} and {@code actual\} for which both numbers are still
   *        considered equal.
   * @param absoluteError the maximum absolute error between {@code expected\} and
   *        {@code actual\} for which both numbers are still considered equal.
   * @return the predicate
   * @throws IllegalArgumentException if the relative error is not positive finite
   * @throws IllegalArgumentException if the absolute error is not positive or is
   *         {@code \<=\} than the maximum difference between {@code <number>\}
   *         primitives
   */
  public static <Number><Number>BiPredicate <number>sIsCloseTo(double relativeError,
      <numberAbsType> absoluteError) {
    return new <Number><Number>IsCloseTo(relativeError, absoluteError);
  \}}><Ops,ops:{Op,op |<types,Types:{type,Type |
<\n>
  /**
   * Creates a predicate to test {@code <type>\} values using a logical {@code <Op>\} of two
   * single-value predicates, each predicate testing a different value of the
   * {@link <Type><Type>BiPredicate#test(<type>, <type>)\} method.
   *
   * @param predicate1 the predicate to test the first value
   * @param predicate2 the predicate to test the second value
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

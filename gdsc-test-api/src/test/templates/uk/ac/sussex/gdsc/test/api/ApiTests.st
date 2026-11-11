<package>

/**
 * Contains constants for the tests.
 */
public final class ApiTests {

  /** The boolean test values. */
  private static final boolean[] booleanValues = {<booleanValues:{v|<\n>      <v>,}>
  \};

  /** The byte test values. */
  private static final byte[] byteValues = {<byteValues:{v|<\n>      <v>,}>
  \};

  /** The char test values. */
  private static final char[] charValues = {<charValues:{v|<\n>      <v>,}>
  \};

  /** The double test values. */
  private static final double[] doubleValues = {<doubleValues:{v|<\n>      <v>,}>
  \};

  /** The float test values. */
  private static final float[] floatValues = {<floatValues:{v|<\n>      <v>,}>
  \};

  /** The int test values. */
  private static final int[] intValues = {<intValues:{v|<\n>      <v>,}>
  \};

  /** The long test values. */
  private static final long[] longValues = {<longValues:{v|<\n>      <v>,}>
  \};

  /** The short test values. */
  private static final short[] shortValues = {<shortValues:{v|<\n>      <v>,}>
  \};

  /** The unsigned byte test values. Includes values either side of the signed limits. */
  private static final byte[] unsignedByteValues = {<unsignedByteValues:{v|<\n>      <v>,}>
  \};

  /** The unsigned int test values. Includes values either side of the signed limits. */
  private static final int[] unsignedIntValues = {<unsignedIntValues:{v|<\n>      <v>,}>
  \};

  /** The unsigned long test values. Includes values either side of the signed limits. */
  private static final long[] unsignedLongValues = {<unsignedLongValues:{v|<\n>      <v>,}>
  \};

  /** The unsigned short test values. Includes values either side of the signed limits. */
  private static final short[] unsignedShortValues = {<unsignedShortValues:{v|<\n>      <v>,}>
  \};

  /** Do not allow public construction. */
  private ApiTests() {
    // No construction
  }<types,Types:{type,Type |
<\n>
  /**
   * Get the test values for the {@code <type>\} type.
   *
   * @return the <type> values
   */
  public static <type>[] get<Type>Values() {
    return <type>Values.clone();
  \}}>
<unsignedTypes,UnsignedTypes:{type,Type |
<\n>
  /**
   * Get the test values for the {@code <type>\} type interpreted as unsigned.
   *
   * @return the unsigned <type> values
   */
  public static <type>[] getUnsigned<Type>Values() {
    return unsigned<Type>Values.clone();
  \}}>
}

<package>

/**
 * Contains constants for the tests.
 */
public final class ApiTestUtils {

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

  /** Do not allow public construction. */
  private ApiTestUtils() {
    // No construction
  }<types,Types,TypeValues:{type,Type,values |
<\n>
  /**
   * Get the test values for the {@code <type>\} type.
   *
   * @return the <type> values
   */
  public static <type>[] get<Type>Values() {
    return <type>Values.clone();
  \}}>
}

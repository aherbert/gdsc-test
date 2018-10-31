/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 Alex Herbert
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package uk.ac.sussex.gdsc.test.utils;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;

@SuppressWarnings("javadoc")
public class ByteScramblerTest {

  private static final String NO_LENGTH = "NoLength";
  private static final String NO_CLONEABLE = "NoCloneable";
  private static final String ONE_TIME_CLONEABLE = "OneTimeCloneable";

  /**
   * A simple provider of message digests for testing edge cases.
   */
  public static class ByteScramblerTestProvider extends Provider {
    private static final long serialVersionUID = 1L;

    public ByteScramblerTestProvider() {
      super("ByteScramblerTest", 1.0, "ByteScramblerTest Security Provider v1.0");
      put("MessageDigest." + NO_LENGTH,
          "uk.ac.sussex.gdsc.test.utils.ByteScramblerTest$NoLengthMessageDigest");
      put("MessageDigest." + NO_CLONEABLE,
          "uk.ac.sussex.gdsc.test.utils.ByteScramblerTest$NoCloneableMessageDigest");
      put("MessageDigest." + ONE_TIME_CLONEABLE,
          "uk.ac.sussex.gdsc.test.utils.ByteScramblerTest$OneTimeCloneableMessageDigest");
    }
  }

  /**
   * A class that does not have a message length but is {@link Cloneable}.
   */
  public static class NoLengthMessageDigest extends MessageDigest implements Cloneable {
    public NoLengthMessageDigest() {
      super(NO_LENGTH);
    }

    @Override
    protected int engineGetDigestLength() {
      return 0;
    }

    @Override
    public void engineUpdate(byte b) {}

    @Override
    public void engineUpdate(byte b[], int offset, int length) {}

    @Override
    public void engineReset() {}

    @Override
    public byte[] engineDigest() {
      return new byte[0];
    }
  }

  /**
   * A class that has a message length but is not {@link Cloneable}.
   */
  public static class NoCloneableMessageDigest extends MessageDigest {
    public NoCloneableMessageDigest() {
      super(NO_CLONEABLE);
    }

    @Override
    protected int engineGetDigestLength() {
      return 4;
    }

    @Override
    public void engineUpdate(byte b) {}

    @Override
    public void engineUpdate(byte b[], int offset, int length) {}

    @Override
    public void engineReset() {}

    @Override
    public byte[] engineDigest() {
      return new byte[engineGetDigestLength()];
    }
  }

  /**
   * A class that has a message length but is only {@link Cloneable} once.
   */
  public static class OneTimeCloneableMessageDigest extends MessageDigest implements Cloneable {
    int count = 0;

    public OneTimeCloneableMessageDigest() {
      super(ONE_TIME_CLONEABLE);
    }

    @Override
    protected int engineGetDigestLength() {
      return 4;
    }

    @Override
    public void engineUpdate(byte b) {}

    @Override
    public void engineUpdate(byte b[], int offset, int length) {}

    @Override
    public void engineReset() {}

    @Override
    public byte[] engineDigest() {
      return new byte[engineGetDigestLength()];
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
      // Only allow one clone
      if (++count > 1) {
        throw new CloneNotSupportedException();
      }
      return super.clone();
    }
  }

  @BeforeAll
  public static void initialise() {
    Security.addProvider(new ByteScramblerTestProvider());
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithBadAlgorithm() {
    Assertions.assertThrows(NoSuchAlgorithmException.class, () -> {
      new ByteScrambler(new byte[4], "Bad algorithm");
    });
  }

  @Test
  public void testCanProvideMessageDigest() throws NoSuchAlgorithmException {
    Assertions.assertEquals(NoLengthMessageDigest.class,
        MessageDigest.getInstance(NO_LENGTH).getClass());
    Assertions.assertEquals(NoCloneableMessageDigest.class,
        MessageDigest.getInstance(NO_CLONEABLE).getClass());
    Assertions.assertEquals(OneTimeCloneableMessageDigest.class,
        MessageDigest.getInstance(ONE_TIME_CLONEABLE).getClass());
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithNoLengthAlgorithm() {
    final String message = Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new ByteScrambler(new byte[1], NO_LENGTH);
    }).getMessage();
    final String expected = "Unknown block size";
    Assertions.assertTrue(message.contains(expected), "Missing: " + expected);
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithNoCloneableAlgorithm() {
    Assertions.assertThrows(CloneNotSupportedException.class, () -> {
      new ByteScrambler(new byte[1], NO_CLONEABLE);
    });
  }

  @Test
  public void testScrambleThrowsWithIfCloneNotSupportedException()
      throws NoSuchAlgorithmException, CloneNotSupportedException {
    // Construction is OK
    final ByteScrambler bs = new ByteScrambler(new byte[1], ONE_TIME_CLONEABLE);

    // The first scramble should be the second clone
    Assertions.assertThrows(RuntimeException.class, () -> {
      bs.scramble();
    });
  }

  @Test
  public void testScramble() {
    final UniformRandomProvider rng = RandomSource.create(RandomSource.SPLIT_MIX_64);

    // Get a seed size that is not the block size of MD5 = 16 bytes
    final byte[] bytes = new byte[16 + 1];
    rng.nextBytes(bytes);
    final ByteScrambler bs = ByteScrambler.getByteScrambler(bytes);

    final byte[] next1 = bs.scramble();
    final byte[] next2 = bs.scramble();

    Assertions.assertEquals(bytes.length, next1.length);
    Assertions.assertEquals(bytes.length, next2.length);

    Assertions.assertFalse(Arrays.equals(bytes, next1),
        "Seed bytes and first scramble are the same");
    Assertions.assertFalse(Arrays.equals(bytes, next2),
        "Seed bytes and second scramble are the same");
    Assertions.assertFalse(Arrays.equals(next1, next2), "First and second scramble are the same");
  }
}

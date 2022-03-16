/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 - 2020 Alex Herbert
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

import uk.ac.sussex.gdsc.test.utils.TestLogUtils.TestLevel;

import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class CallStackTest {
  private static Logger logger;

  @BeforeAll
  public static void beforeAll() {
    logger = Logger.getLogger(CallStackTest.class.getName());
  }

  @AfterAll
  public static void afterAll() {
    logger = null;
  }

  @Test
  void canGetStaceTraceElement() {
    final StackTraceElement[] e = new Throwable().getStackTrace();
    final StackTraceElement o = CallStack.getStackTraceElement();
    final StackTraceElement o2 = CallStack.getStackTraceElement(1);
    Assertions.assertNotNull(e);
    Assertions.assertNotNull(o);
    Assertions.assertEquals(e[0].getClassName(), o.getClassName());
    Assertions.assertEquals(e[0].getMethodName(), o.getMethodName());
    Assertions.assertEquals(e[0].getLineNumber() + 1, o.getLineNumber());
    logger.log(TestLevel.TEST_INFO,
        () -> String.format("%s:%s:%d", o.getClassName(), o.getMethodName(), o.getLineNumber()));

    Assertions.assertNotNull(o2);
    Assertions.assertEquals(e[1].getClassName(), o2.getClassName());
    Assertions.assertEquals(e[1].getMethodName(), o2.getMethodName());
    Assertions.assertEquals(e[1].getLineNumber(), o2.getLineNumber());
  }

  @Test
  void getStaceTraceElementIsNullWhenNotLocated() {
    final int size = new Throwable().getStackTrace().length;
    final StackTraceElement o = CallStack.getStackTraceElement(size);
    Assertions.assertNull(o);
  }

  @Test
  void getStaceTraceElementThrowsWhenCountIsNegative() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      CallStack.getStackTraceElement(-1);
    });
  }

  @Test
  void canGetCodePoint() {
    final StackTraceElement e = new Throwable().getStackTrace()[0];
    final String codePoint = CallStack.getCodePoint();
    Assertions.assertNotNull(e);
    Assertions.assertNotNull(codePoint);
    final String[] o = codePoint.split(":");
    Assertions.assertEquals(e.getClassName(), o[0]);
    Assertions.assertEquals(e.getMethodName(), o[1]);
    Assertions.assertEquals(e.getLineNumber() + 1, Integer.parseInt(o[2]));
    logger.log(TestLevel.TEST_INFO, codePoint);
    Assertions.assertEquals("", CallStack.getCodePoint(null));
  }
}

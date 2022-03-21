/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 - 2022 Alex Herbert
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package uk.ac.sussex.gdsc.test.utils;

import uk.ac.sussex.gdsc.test.utils.TestLogging.TestLevel;

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

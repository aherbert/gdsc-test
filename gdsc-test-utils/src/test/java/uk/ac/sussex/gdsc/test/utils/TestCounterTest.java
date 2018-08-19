/*-
 * #%L
 * Genome Damage and Stability Centre Test Package
 *
 * The GDSC Test package contains code for use with the JUnit test framework.
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.ac.sussex.gdsc.test.utils.TestCounter;

@SuppressWarnings("javadoc")
public class TestCounterTest {
    @Test
    public void canComputeFailureLimit() {
        int size = 345;
        double fraction = 0.789;
        int e = (int) Math.floor(size * fraction);
        Assertions.assertEquals(e, TestCounter.computeFailureLimit(size, fraction));
        // Edge cases
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TestCounter.computeFailureLimit(0, fraction);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TestCounter.computeFailureLimit(size, -0.1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TestCounter.computeFailureLimit(size, 1.1);
        });
    }

    @Test
    public void canConstruct() {
        int failLimit = 1;
        int size = 1;
        Assertions.assertNotNull(new TestCounter(failLimit));
        Assertions.assertNotNull(new TestCounter(failLimit + 5));
        Assertions.assertNotNull(new TestCounter(failLimit, size));
        Assertions.assertNotNull(new TestCounter(failLimit, size + 5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            int size0 = 0;
            @SuppressWarnings("unused")
            TestCounter tc = new TestCounter(failLimit, size0);
        });
    }

    @Test
    public void canRunTests() {
        int failLimit = 0;
        int size = 2;
        // All the following methods simulate a pass
        {
            TestCounter tc = new TestCounter(failLimit, size);
            tc.run(() -> {
                // No failure
            });
        }
        {
            TestCounter tc = new TestCounter(failLimit, size);
            tc.run(1, () -> {
                // No failure
            });
        }
        {
            TestCounter tc = new TestCounter(failLimit, size);
            tc.run(() -> {
                return true;
            }, () -> {
                throw new AssertionError();
            });
        }
        {
            TestCounter tc = new TestCounter(failLimit, size);
            tc.run(1, () -> {
                return true;
            }, () -> {
                throw new AssertionError();
            });
        }
    }

    @Test
    public void canThrowAssertionError() {
        int failLimit = 0;
        int size = 2;
        // All the following methods simulate a failure
        Assertions.assertThrows(AssertionError.class, () -> {
            TestCounter tc = new TestCounter(failLimit, size);
            tc.run(() -> {
                throw new AssertionError();
            });
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            TestCounter tc = new TestCounter(failLimit, size);
            tc.run(1, () -> {
                throw new AssertionError();
            });
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            TestCounter tc = new TestCounter(failLimit, size);
            tc.run(() -> {
                return false;
            }, () -> {
                throw new AssertionError();
            });
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            TestCounter tc = new TestCounter(failLimit, size);
            tc.run(1, () -> {
                return false;
            }, () -> {
                throw new AssertionError();
            });
        });
    }

    @Test
    public void canSwallowAssertionError() {
        int failLimit = 1;
        int size = 2;
        // All the following methods simulate a failure.
        // Both indices can fail and it should be OK.
        {
            TestCounter tc = new TestCounter(failLimit, size);
            tc.run(() -> {
                throw new AssertionError();
            });
            tc.run(1, () -> {
                throw new AssertionError();
            });
        }
        {
            TestCounter tc = new TestCounter(failLimit, size);
            tc.run(() -> {
                return false;
            }, () -> {
                throw new AssertionError();
            });
            tc.run(1, () -> {
                return false;
            }, () -> {
                throw new AssertionError();
            });
        }
    }

    @Test
    public void canThrowDefaultAssertionErrorForTestAssertion() {
        int failLimit = 0;
        int size = 2;
        TestCounter tc = new TestCounter(failLimit, size);
        Assertions.assertThrows(AssertionError.class, () -> {
            tc.run(1, () -> {
                return false;
            }, () -> {
                // Do nothing. The TestCounter will generate a default error.
            });
        });
    }
}

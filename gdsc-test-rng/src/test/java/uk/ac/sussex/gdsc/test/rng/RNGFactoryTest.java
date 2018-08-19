package uk.ac.sussex.gdsc.test.rng;

import org.apache.commons.rng.UniformRandomProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import uk.ac.sussex.gdsc.test.rng.RNGFactory;
import uk.ac.sussex.gdsc.test.utils.TestSettings;

@SuppressWarnings("javadoc")
public class RNGFactoryTest {

    @Test
    public void canGetSameRandomWithSameSeed() {
        final long seed = 5656787697789L;
        UniformRandomProvider r = RNGFactory.create(seed);
        final double[] e = { r.nextDouble(), r.nextDouble() };
        r = RNGFactory.create(seed);
        final double[] o = { r.nextDouble(), r.nextDouble() };
        Assertions.assertArrayEquals(e, o);
    }

    @Test
    public void canGetDifferentRandomWithDifferentSeed() {
        final long seed = 5656787697789L;
        UniformRandomProvider r = RNGFactory.create(seed);
        final double[] e = { r.nextDouble(), r.nextDouble() };
        r = RNGFactory.create(seed * 2);
        final double[] o = { r.nextDouble(), r.nextDouble() };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            Assertions.assertArrayEquals(e, o);
        });
    }

    @Test
    public void canGetSameRandomWithoutSeedMatchingConfiguredSeed() {
        final long seed = TestSettings.getSeed();
        UniformRandomProvider r = RNGFactory.create();
        final double[] e = { r.nextDouble(), r.nextDouble() };
        r = RNGFactory.create(seed);
        final double[] o = { r.nextDouble(), r.nextDouble() };
        Assertions.assertArrayEquals(e, o);
    }

    @Test
    public void canGetDifferentRandomWithZeroSeed() {
        UniformRandomProvider r = RNGFactory.create(0);
        final double[] e = { r.nextDouble(), r.nextDouble() };
        r = RNGFactory.create(0);
        final double[] o = { r.nextDouble(), r.nextDouble() };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            Assertions.assertArrayEquals(e, o);
        });
    }
}

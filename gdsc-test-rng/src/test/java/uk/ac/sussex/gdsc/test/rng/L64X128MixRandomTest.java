/*-
 * #%L
 * Genome Damage and Stability Centre Test RNG
 *
 * Contains utilities for use with Commons RNG for random tests.
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

package uk.ac.sussex.gdsc.test.rng;

import java.util.stream.Stream;
import org.apache.commons.rng.RestorableUniformRandomProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("javadoc")
class L64X128MixRandomTest extends BaseLongUniformRandomProviderTest {
  @Override
  protected RestorableUniformRandomProvider createRng(long seed) {
    return new L64X128MixRandom(seed);
  }

  static Stream<Arguments> testNextLongReference() {
    // Tested with respect to JDK 17 implementation.
    // The seeds were created using the same method in the JDK source from a single
    // random long value
    final long[] seed0 = {0xaff65436b20ac0cdL, 0x1L, 0x33168d08f487818fL, 0x57522a4615e4aa54L};
    final long[] expected0 =
        {0x93b7e549b4276205L, 0x5884fec4f3d2d093L, 0x9daa13cd68cf0062L, 0x1cdc4264ab458528L,
            0x51d7f51dfa575481L, 0x24aba6e6b876a552L, 0xcb1d0967ed8f838L, 0x509c7bd4ba36b6bbL,
            0xbc35284ed8927088L, 0x4f1100908c9c1a90L, 0xb088219cc5b09e83L, 0x93973ed24fae361L,
            0x205444c63ad50061L, 0x1b93f6c9703751baL, 0xf6fc1245c68e3591L, 0x25c9ed66f6958d65L,
            0x78493a2f878d803bL, 0x461e71b88cd6ab63L, 0x7b08d75c0ee5da4aL, 0xa7be4cd7d9b34f24L,
            0x98fc889b6cb0ef6aL, 0xaf7aac2b02ed6646L, 0x42398329829ed0f6L, 0x991f5e9a04f73ea1L,
            0x22f06a22940e1f0cL, 0x52bdf3245280f328L, 0x5516747d413c280bL, 0xddd0f850df96c98L,
            0xdfb9116eea895a14L, 0x25f60644acc87ce4L, 0xb94c8052b25b565bL, 0xbbb536f152b9a2aL,
            0x96a5ce9ee3b33b5L, 0x39aea581c5664cadL, 0x380c937f40d03b5fL, 0x45da7bcf336e4705L,
            0x41cb2a1bc8a58a3fL, 0x1b7b23957c4bfe0bL, 0x1a46b40e434da627L, 0x475a6df8672ff740L,};
    final long[] seed1 = {0xff77c956b975e221L, 0x1L, 0xbe4864af048f18adL, 0x94cbd6584dcee383L};
    final long[] expected1 =
        {0x58af99006463a1ddL, 0x5232385f7bb44cabL, 0xf7bcd93fca2f8a9eL, 0xa4d017f7ea78641aL,
            0x8a164cc936f0c789L, 0xcfea7a26f811eac0L, 0xa1bceed0424d826fL, 0xeb0d0ab22254bb3L,
            0xc99167ecf8dbf08eL, 0xd6b4ac277b897180L, 0xefce7b1eb4b2b841L, 0x53b54dd7dae133d4L,
            0xf1284ec118796d88L, 0x28542f233aadae4aL, 0x8df7d933749ad731L, 0x1d2056ef305671e8L,
            0x9ad83ed7c0f0508cL, 0x6b594b7261e48f7aL, 0x32782dba7f47d9a3L, 0x3bf0d18f3e54accaL,
            0x7cd39100f2625678L, 0xed136c3c5a7bfbbfL, 0xdf4dca8f8f838dabL, 0xac90d3d6e4f92423L,
            0xf686b56bf84e459cL, 0x95a8f1c00399620L, 0xfbdde4dc485f438cL, 0x1dc9c3982ec804e8L,
            0xc2c9a7a867a709ebL, 0x70ce59c971b69c09L, 0x7e741cbd1b70ad3fL, 0xcd62b31bd28c8002L,
            0xcce013271a31a5fL, 0x7f7bdc34e56c684bL, 0xc4c6bb013e5afb0aL, 0xe21fdb4e6f3c7bbbL,
            0x61f47af483e5b81eL, 0x727ce5d845d51e90L, 0x263158b930f3c549L, 0x5368e2efdebbd3edL,};
    final long[] seed2 = {0x4d470502e5836d10L, 0x1L, 0x4d242a741503e6d7L, 0xdbbca68fe75b4976L};
    final long[] expected2 =
        {0x8a72f5d6b27500c2L, 0x56af3f0f17cafd19L, 0xe5818e6e037ee4deL, 0x5c4ce05a73225bb8L,
            0xa20f68c208b8a40cL, 0x366218e45a83ad2dL, 0xb3ab91bff585a59aL, 0x755467cda8c1cc30L,
            0x3feb83bd696753e5L, 0x469b4b2c6171b8a2L, 0x11ba283113734945L, 0x6420b106a7005acaL,
            0x5c75f2292bdf4a1cL, 0xff18478028992525L, 0x4158d9ba77848d80L, 0x7e64f2ff6b1705adL,
            0xf24d4c48b51296caL, 0xf0fbb6171540d2baL, 0x426091e932e691d4L, 0x3bd9fb6629eaa05L,
            0xcca386b5a8b1a0f7L, 0xfb7f1d41f8b6fb85L, 0x4770eb7876d97025L, 0xb13ba5b8d6cd91bdL,
            0x268dde1392fe7d1cL, 0xbe9b570115ffac61L, 0x2653e9e0a45af50L, 0x4ed197bad36e2f1eL,
            0xf2af50247f7031c1L, 0xfa4ca4ca2e1fad26L, 0x1f045d23a5658106L, 0x636481d62fd2cf1eL,
            0xa78354e2a703fe3eL, 0xd291c6985db6b497L, 0x306121671da01781L, 0x9d2d0319d7e9c13cL,
            0xb2e18743866bb542L, 0xdfc9ca2c79e48196L, 0x9e15f55e5edf352eL, 0xcb01ec8e71e566f3L,};
    return Stream.of(Arguments.of(seed0, expected0), Arguments.of(seed1, expected1),
        Arguments.of(seed2, expected2));
  }

  @ParameterizedTest
  @MethodSource
  void testNextLongReference(long[] seed, long[] expected) {
    final L64X128MixRandom rng = new L64X128MixRandom(seed[0], seed[1], seed[2], seed[3]);
    for (int i = 0; i < expected.length; i++) {
      final long v = expected[i];
      final int index = i;
      Assertions.assertEquals(expected[i], rng.nextLong(),
          () -> String.format("[%d] %s", index, Long.toHexString(v)));
    }
  }

  @Test
  void testZeroSeed() {
    final L64X128MixRandom rng1 = new L64X128MixRandom(0);
    final L64X128MixRandom rng2 = new L64X128MixRandom(0, 0);
    long zeroOutput = 0;
    for (int i = 0; i < 10; i++) {
      final long value = rng1.nextLong();
      Assertions.assertEquals(value, rng2.nextLong());
      zeroOutput |= value;
    }
    Assertions.assertNotEquals(0, zeroOutput, "Zero seed should not create all zero output");
  }

  @Test
  void testFullZeroSeed() {
    final L64X128MixRandom rng = new L64X128MixRandom(0, 0, 0, 0);
    long zeroOutput = 0;
    for (int i = 0; i < 10; i++) {
      zeroOutput |= rng.nextLong();
    }
    Assertions.assertNotEquals(0, zeroOutput, "Zero seed should not create all zero output");
  }
}

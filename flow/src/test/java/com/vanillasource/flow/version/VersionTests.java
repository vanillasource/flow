/**
 * Copyright (C) 2017 VanillaSource
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.vanillasource.flow.version;

import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static java.util.Arrays.asList;

@Test
public class VersionTests {
   private static final String DEFAULT_HASH = "E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855";

   public void testZeroVersionIsTheDefaultHash() {
      assertEquals(Version.ZERO.serialize(), DEFAULT_HASH);
   }

   public void testNonemptyStringDoesNotProduceTheDefaultHash() {
      assertNotEquals(Version.from("Something"), Version.ZERO);
   }

   public void testTwoVersionsFromSameStringEqual() {
      assertEquals(Version.from("Something"), Version.from("Something"));
   }

   public void testTwoVersionsFromDifferentStringDoNotEqual() {
      assertNotEquals(Version.from("Something1"), Version.from("Something2"));
   }

   public void testDeserializingSerializedVersionProducesEqualVersion() {
      Version original = Version.from("v1.0");
      Version deserialized = Version.deserialize(original.serialize());

      assertEquals(deserialized, original);
   }

   public void testCombinationOfTwoVersionsInOtherOrderDontEqual() {
      Version combination1 = Version.combine(asList(Version.from("one"), Version.from("two")));
      Version combination2 = Version.combine(asList(Version.from("two"), Version.from("one")));

      assertNotEquals(combination1, combination2);
   }
}

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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;
import java.util.List;

/**
 * A version that stores the structure too.
 */
public final class Version {
   public static final Version ZERO = new Version("");
   private final String hexHash;

   private Version(String hexHash) {
      this.hexHash = hexHash;
   }

   /**
    * Combine the given versions into a new version that preserves the order of the
    * given versions (another ordering of the same versions will not produce the same
    * version). Also, this combination is not associative, meaning
    * combine(a, combine(b, c)) != combine(combine(a, b), c). In fact this implementation
    * preserves the original associations.
    */
   public static Version combine(List<Version> versions) {
      return from(versions.stream().map(Version::serialize).reduce("", String::concat));
   }

   /**
    * Create a version object from any arbitrary version indicator of any format.
    */
   public static Version from(String versionIndicator) {
      try {
         MessageDigest hashDigest = MessageDigest.getInstance("SHA-256");
         hashDigest.update(versionIndicator.getBytes());
         return new Version(toHex(hashDigest.digest()));
      } catch (NoSuchAlgorithmException e) {
         throw new IllegalStateException("can not initialize hashing algorithm", e); 
      }  
   }

   private static String toHex(byte[] bytes) {
      BigInteger value = new BigInteger(1, bytes);
      return String.format("%0" + (bytes.length << 1) + "X", value);
   }

   /**
    * Serialize this version into a string representation.
    */
   public String serialize() {
      return hexHash;
   }

   /**
    * Deserialize from a string representation.
    */
   public static Version deserialize(String serializedVersion) {
      return new Version(serializedVersion);
   }
}

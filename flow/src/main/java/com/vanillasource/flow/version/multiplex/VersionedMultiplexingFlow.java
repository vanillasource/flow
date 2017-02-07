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

package com.vanillasource.flow.version.multiplex;

import com.vanillasource.flow.multiplex.MultiplexingFlow;
import com.vanillasource.flow.version.VersionedFlow;
import com.vanillasource.flow.version.Version;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import static java.util.Arrays.asList;

/**
 * A multiplexing flow which is also a versioned flow. It takes into account
 * the versions of the individual threads to calculate its own version. If
 * the structure of the thread flow changes, this multiplexing flow's version changes.
 * The <code>flowFactory</code> <strong>must</strong> support a creation of the flow
 * with a <code>null</code> key, to support getting the version.
 */
public final class VersionedMultiplexingFlow<I, K, O> implements VersionedFlow<O> {
   private final MultiplexingFlow<I, K, O> delegate;
   private final Version version;

   public VersionedMultiplexingFlow(VersionedFlow<I> upstream, 
         Function<I, K> selector, BiFunction<K, VersionedFlow<I>, VersionedFlow<O>> flowFactory,
         Version version) {
      Version threadUpstreamVersion = Version.combine(asList(upstream.getVersion(), version));
      this.version = flowFactory.apply(null, new VersionedFlow<I>() {
         @Override
         public Version getVersion() {
            return threadUpstreamVersion;
         }

         @Override
         public void registerDownstream(Consumer<? super I> downstream) {
         }
      }).getVersion();
      this.delegate = new MultiplexingFlow<>(upstream, selector, (key, flow) -> flowFactory.apply(key, new VersionedFlow<I>() {
         @Override
         public Version getVersion() {
            return threadUpstreamVersion;
         }

         @Override
         public void registerDownstream(Consumer<? super I> downstream) {
            flow.registerDownstream(downstream);
         }
      }));
   }

   @Override
   public Version getVersion() {
      return version;
   }

   @Override
   public void registerDownstream(Consumer<? super O> downstream) {
      delegate.registerDownstream(downstream);
   }
}


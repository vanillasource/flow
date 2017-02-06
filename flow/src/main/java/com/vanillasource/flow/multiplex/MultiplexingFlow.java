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

package com.vanillasource.flow.multiplex;

import com.vanillasource.flow.basic.ConsumerFlow;
import com.vanillasource.flow.Flow;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.Map;
import java.util.HashMap;

/**
 * Multiplexes multiple flows based on some given multiplexing selector. The input
 * flow is split into multiple parallel flows based on keys derived from the input objects.
 * These flows can be dynamic, based on the input. The output of this flow is the outputs
 * of all flows in the multiplexer. The flow factory is only used if a new key is selected.
 * @param <I> The input type this flow is coming from.
 * @param <K> The 'key' that selects between flows. This object should have its
 * <code>equals()</code> and <code>hashCode()</code> properly implemented.
 */
public final class MultiplexingFlow<I, K, O> implements Flow<O> {
   private final Map<K, ConsumerFlow<I>> flowCache = new HashMap<>();
   private final ConsumerFlow<O> outputFlow = new ConsumerFlow<>();

   public MultiplexingFlow(Flow<I> upstream, 
         Function<I, K> selector, BiFunction<K, Flow<I>, Flow<O>> flowFactory) {
      upstream.registerDownstream(input -> {
         K key = selector.apply(input);
         ConsumerFlow<I> flow = getCachedThread(key);
         if (flow == null) {
            flow = createThread(key, flowFactory);
         }
         flow.accept(input);
      });
   }

   private ConsumerFlow<I> getCachedThread(K key) {
      return flowCache.get(key);
   }

   private ConsumerFlow<I> createThread(K key, BiFunction<K, Flow<I>, Flow<O>> threadFactory) {
      ConsumerFlow<I> flow = new ConsumerFlow<>();
      Flow<O> thread = threadFactory.apply(key, flow);
      thread.registerDownstream(outputFlow);
      flowCache.put(key, flow);
      return flow;
   }

   @Override
   public void registerDownstream(Consumer<? super O> downstream) {
      outputFlow.registerDownstream(downstream);
   }
}




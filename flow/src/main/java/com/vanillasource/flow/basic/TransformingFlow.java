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

package com.vanillasource.flow.basic;

import com.vanillasource.flow.Flow;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

/**
 * A flow that connects to a previous flow and transforms its output (input to this flow) to this output.
 */
public final class TransformingFlow<I, O> implements Flow<O> {
   private final ConsumerFlow<O> consumerFlow = new ConsumerFlow<>();

   public TransformingFlow(Flow<I> upstream, BiConsumer<I, Consumer<O>> processor) {
      upstream.registerDownstream(input -> processor.accept(input, consumerFlow));
   }

   public void registerDownstream(Consumer<? super O> downstream) {
      consumerFlow.registerDownstream(downstream);
   }
}

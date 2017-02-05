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

package com.vanillasource.flow.list;

import com.vanillasource.flow.Flow;
import com.vanillasource.flow.basic.ConsumerFlow;
import java.util.List;
import java.util.function.Consumer;

/**
 * A flow that will send a list of predefined items downstream.
 */
public final class ListSourceFlow<O> implements Flow<O> {
   private final ConsumerFlow<O> consumerFlow;
   private final List<O> objects;

   public ListSourceFlow(List<O> objects) {
      this.objects = objects;
      this.consumerFlow = new ConsumerFlow<>();
   }

   @Override
   public void registerDownstream(Consumer<? super O> downstream) {
      consumerFlow.registerDownstream(downstream);
   }

   /**
    * Send all objects downstream.
    */
   public void sendAll() {
      objects.forEach(consumerFlow);
   }
}

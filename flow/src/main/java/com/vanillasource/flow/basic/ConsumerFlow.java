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

/**
 * A flow that acts as a consumer and forwards all objects to all registered
 * downstreams.
 */
public final class ConsumerFlow<O> implements Flow<O>, Consumer<O> {
   private volatile Consumer<O> downstreams;

   @Override
   @SuppressWarnings("unchecked")
   public void registerDownstream(Consumer<? super O> downstream) {
      if (downstreams == null) {
         downstreams = (Consumer<O>) downstream;         
      } else {
         downstreams = downstreams.andThen(downstream);
      }
   }

   @Override
   public void accept(O object) {
      if (downstreams != null) {
         downstreams.accept(object);
      }
   }
}

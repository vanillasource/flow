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

public final class ClassFilterFlow<I, O extends I> implements Flow<O> {
   private final TransformingFlow<I, O> delegate;

   @SuppressWarnings("unchecked")
   public ClassFilterFlow(Flow<I> upstream, Class<O> filterClass) {
      this.delegate = new TransformingFlow<>(upstream, (input, output) -> {
         if (filterClass.isAssignableFrom(input.getClass())) {
            output.accept((O) input);
         }
      });
   }

   @Override
   public void registerDownstream(Consumer<? super O> downstream) {
      delegate.registerDownstream(downstream);
   }
}

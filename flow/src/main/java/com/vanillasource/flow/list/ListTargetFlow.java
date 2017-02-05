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
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * A flow that will hold all objects received in a list. There will be no
 * downstream objects.
 */
public final class ListTargetFlow<O> implements Flow<O> {
   private final List<O> objects = new LinkedList<>();

   public ListTargetFlow(Flow<O> upstream) {
      upstream.registerDownstream(objects::add);
   }

   @Override
   public void registerDownstream(Consumer<? super O> downstream) {
   }

   public List<O> getObjects() {
      return Collections.unmodifiableList(objects);
   }
}


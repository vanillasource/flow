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

package com.vanillasource.flow.version.basic;

import com.vanillasource.flow.Flow;
import com.vanillasource.flow.version.VersionedFlow;
import com.vanillasource.flow.version.Version;
import java.util.List;
import java.util.function.Consumer;
import static java.util.Arrays.*;
import java.util.stream.Collectors;

/**
 * A flow that has a version explicitly assigned and has one or more upstreams that
 * are also versioned.
 */

public final class ExplicitlyVersionedFlow<O> implements VersionedFlow<O> {
   private final Flow<O> delegate;
   private final Version version;

   public ExplicitlyVersionedFlow(List<VersionedFlow<?>> upstreams, Flow<O> delegate, Version version) {
      this.delegate = delegate;
      Version upstreamSum = Version.combine(
            upstreams.stream().map(VersionedFlow::getVersion).collect(Collectors.toList()));
      this.version = Version.combine(asList(upstreamSum, version));
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


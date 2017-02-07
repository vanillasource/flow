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

import com.vanillasource.flow.Flow;

/**
 * A flow that produces versioned items. If a flow's version does not change,
 * it's output should also not change for the same input. Remember, a flow represents
 * not just itself, but all upstreams as well.
 */
public interface VersionedFlow<O> extends Flow<O> {
   /**
    * Get the version of objects from this flow, which includes all
    * possible upstream flows and the structure of those flows up to this point.
    */
   Version getVersion();
}


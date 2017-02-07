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

import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static org.mockito.Mockito.*;
import java.util.function.Consumer;

@Test
public class ConsumerFlowTests {
   public void testFlowAcceptsObjectsWithNoSubscribers() {
      ConsumerFlow<String> flow = new ConsumerFlow<>();

      flow.accept("Ni");
   }

   @SuppressWarnings("unchecked")
   public void testFlowPropagatesObjectToSubscriber() {
      ConsumerFlow<String> flow = new ConsumerFlow<>();
      Consumer<String> consumer = mock(Consumer.class);
      flow.registerDownstream(consumer);

      flow.accept("Ni");

      verify(consumer).accept("Ni");
   }

   @SuppressWarnings("unchecked")
   public void testSecondSubscriberAttachesToFirstSubscriber() {
      ConsumerFlow<String> flow = new ConsumerFlow<>();
      Consumer<String> consumer1 = mock(Consumer.class);
      flow.registerDownstream(consumer1);
      Consumer<String> consumer2 = mock(Consumer.class);
      flow.registerDownstream(consumer2);

      verify(consumer1).andThen(consumer2);
   }
}



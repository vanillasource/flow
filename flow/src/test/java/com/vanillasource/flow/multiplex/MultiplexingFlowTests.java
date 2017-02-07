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

import org.testng.annotations.*;
import static org.testng.Assert.*;
import static org.mockito.Mockito.*;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import com.vanillasource.flow.basic.ConsumerFlow;
import com.vanillasource.flow.basic.TransformingFlow;
import com.vanillasource.flow.Flow;

@Test
public class MultiplexingFlowTests {
   private MultiplexingFlow<String, Character, String> flow;
   private BiFunction<Character, Flow<String>, Flow<String>> flowFactory;
   private ConsumerFlow<String> input;
   private Consumer<String> output;

   public void testThreadFlowIsEstablishedWithKey() {
      flow = new MultiplexingFlow<>(input, line -> line.charAt(0), flowFactory);

      input.accept("A Line");

      verify(flowFactory).apply(eq('A'), any());
   }

   public void testThreadFlowIsNotEstablishedTwiceForSameKey() {
      flow = new MultiplexingFlow<>(input, line -> line.charAt(0), flowFactory);

      input.accept("A Line");
      input.accept("Another Line");

      verify(flowFactory, times(1)).apply(eq('A'), any());
   }

   public void testObjectGoesThroughThread() {
      flow = new MultiplexingFlow<>(input, line -> line.charAt(0),
            (key, upstream) -> new TransformingFlow<>(upstream, (line, downstream) -> downstream.accept(line+" (key: "+key+")")));
      flow.registerDownstream(output);

      input.accept("A Line");

      verify(output).accept("A Line (key: A)");
   }

   @BeforeMethod
   @SuppressWarnings("unchecked")
   protected void setUp() {
      input = new ConsumerFlow<>();
      output = mock(Consumer.class);
      flowFactory = mock(BiFunction.class);
      when(flowFactory.apply(any(), any())).thenReturn(mock(Flow.class));
   }
}


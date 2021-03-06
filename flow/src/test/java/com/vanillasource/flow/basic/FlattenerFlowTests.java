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

import org.testng.annotations.*;
import static org.testng.Assert.*;
import static org.mockito.Mockito.*;
import java.util.function.Consumer;
import java.util.List;
import static java.util.Arrays.asList;

@Test
public class FlattenerFlowTests {
   private FlattenerFlow<String> flow;
   private ConsumerFlow<List<String>> input;
   private Consumer<String> output;

   public void testEmptyListDoesNothing() {
      input.accept(asList());

      verifyNoMoreInteractions(output);
   }

   public void testListOfStringsIfFlattenedToSeparateCalls() {
      input.accept(asList("a", "b", "c"));

      verify(output).accept("a");
      verify(output).accept("b");
      verify(output).accept("c");
   }

   @BeforeMethod
   @SuppressWarnings("unchecked")
   protected void setUp() {
      input = new ConsumerFlow<>();
      flow = new FlattenerFlow<>(input);
      output = mock(Consumer.class);
      flow.registerDownstream(output);
   }
}


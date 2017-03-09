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

@Test
public class ClassFilterFlowTests {
   private ClassFilterFlow<Number, Integer> flow;
   private ConsumerFlow<Number> input;
   private Consumer<Integer> output;

   public void testDoesNotLetDoubleThrough() {
      input.accept(1.0d);

      verifyNoMoreInteractions(output);
   }

   public void testDoesLetIntegerThrough() {
      input.accept(1);

      verify(output).accept(1);
   }

   @BeforeMethod
   @SuppressWarnings("unchecked")
   protected void setUp() {
      input = new ConsumerFlow<>();
      flow = new ClassFilterFlow<>(input, Integer.class);
      output = mock(Consumer.class);
      flow.registerDownstream(output);
   }
}


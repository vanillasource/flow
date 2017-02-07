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
public class TransformingFlowTests {
   private TransformingFlow<String, Integer> flow;
   private ConsumerFlow<String> input;
   private Consumer<Integer> output;

   public void testInputIsTransformed() {
      input.accept("1234");

      verify(output).accept(4);
   }

   public void testMultipleInputsAreTransformed() {
      input.accept("1234");
      input.accept("123");
      input.accept("12345");

      verify(output).accept(4);
      verify(output).accept(3);
      verify(output).accept(5);
      verifyNoMoreInteractions(output);
   }

   @Test(expectedExceptions = IllegalStateException.class)
   public void testExceptionGetsPropagatedToSource() {
      flow = new TransformingFlow<>(input, (line, output) -> {
         throw new IllegalStateException("test");
      });

      input.accept("x");
   }

   @BeforeMethod
   @SuppressWarnings("unchecked")
   protected void setUp() {
      input = new ConsumerFlow<>();
      flow = new TransformingFlow<>(input, (line, output) -> output.accept(line.length()));
      output = mock(Consumer.class);
      flow.registerDownstream(output);
   }
}


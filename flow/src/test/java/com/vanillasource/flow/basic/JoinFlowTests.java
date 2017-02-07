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
import static java.util.Arrays.*;

@Test
public class JoinFlowTests {
   private ConsumerFlow<String> input1;
   private ConsumerFlow<String> input2;
   private JoinFlow<String> flow;
   private Consumer<String> output;

   public void testForwardObjectsFromInput1() {
      input1.accept("Ni");

      verify(output).accept("Ni");
   }

   public void testForwardObjectsFromInput2() {
      input1.accept("Ni");

      verify(output).accept("Ni");
   }

   @BeforeMethod
   @SuppressWarnings("unchecked")
   protected void setUp() {
      input1 = new ConsumerFlow<>();
      input2 = new ConsumerFlow<>();
      flow = new JoinFlow<>(asList(input1, input2));
      output = mock(Consumer.class);
      flow.registerDownstream(output);
   }
}


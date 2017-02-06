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

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static java.util.Collections.*;
import static java.util.Arrays.asList;
import com.vanillasource.flow.basic.*;
import com.vanillasource.flow.multiplex.*;
import com.vanillasource.flow.Flow;

@Test
public class TransformingITTests {
   private static final List<String> LOGENTRIES = asList(
         "systemA:DEBUG:nothing important is happening",
         "systemA:WARN:something is now happening",
         "systemB:WARN:i join",
         "this is not a valid entry",
         "systemA:ERROR:i don't feel so good",
         "systemC:FATAL:dead"
   );

   public void testCleanAndHtmlizeLinearChain() throws Exception {
      ListSourceFlow<String> source = new ListSourceFlow<>(LOGENTRIES);
      FilterFlow<String> sanitizer = new FilterFlow<>(source, line -> line.split(":").length == 3);
      TransformingFlow<String, String> htmlizer = new TransformingFlow<>(sanitizer, (line, output) -> output.accept("<p>"+line+"</p>"));
      ListTargetFlow<String> target = new ListTargetFlow<>(htmlizer);

      source.sendAll();

      assertEquals(
            target.getObjects(),
            asList(
               "<p>systemA:DEBUG:nothing important is happening</p>",
               "<p>systemA:WARN:something is now happening</p>",
               "<p>systemB:WARN:i join</p>",
               "<p>systemA:ERROR:i don't feel so good</p>",
               "<p>systemC:FATAL:dead</p>"));
   }

   public void testSeveritySplitProcessingChain() throws Exception {
      ListSourceFlow<String> source = new ListSourceFlow<>(LOGENTRIES);
      FilterFlow<String> sanitizer = new FilterFlow<>(source, line -> line.split(":").length == 3);
      MultiplexingFlow<String, Severity, String> htmlizer = new MultiplexingFlow<>(sanitizer,
            input -> Severity.valueOf(input.split(":")[1]),
            (severity, upstream) -> new TransformingFlow<>(upstream, (line, output) -> output.accept("<p class=\""+severity+"\">"+line+"</p>")));
      ListTargetFlow<String> target = new ListTargetFlow<>(htmlizer);

      source.sendAll();

      assertEquals(
            target.getObjects(),
            asList(
               "<p class=\"DEBUG\">systemA:DEBUG:nothing important is happening</p>",
               "<p class=\"WARN\">systemA:WARN:something is now happening</p>",
               "<p class=\"WARN\">systemB:WARN:i join</p>",
               "<p class=\"ERROR\">systemA:ERROR:i don't feel so good</p>",
               "<p class=\"FATAL\">systemC:FATAL:dead</p>"));
   }

   public void testTypeSwitchLinearChain() throws Exception {
      ListSourceFlow<String> source = new ListSourceFlow<>(LOGENTRIES);
      FilterFlow<String> sanitizer = new FilterFlow<>(source, line -> line.split(":").length == 3);
      LineCounter counter = new LineCounter(sanitizer);
      ListTargetFlow<Integer> target = new ListTargetFlow<>(counter);

      source.sendAll();

      assertEquals(
            target.getObjects(),
            singletonList(5));
   }

   @Test(enabled = false)
   public void testSequencePerformance() throws Exception {
      final int COUNT = 2000000;
      List<String> logentries = new ArrayList<>(COUNT);
      for (int i=0; i<COUNT; i++) {
         logentries.add("system:DEBUG:logentry "+i);
      }

      ListSourceFlow<String> source = new ListSourceFlow<>(logentries);
      FilterFlow<String> sanitizer = new FilterFlow<>(source, line -> line.split(":").length == 3);
      LineCounter counter = new LineCounter(sanitizer);
      ListTargetFlow<Integer> target = new ListTargetFlow<>(counter);

      long startTime = System.currentTimeMillis();
      source.sendAll();
      long endTime = System.currentTimeMillis();

      System.out.println("Processed "+COUNT+" objects in "+(endTime-startTime)+" ms");
      System.out.println("Sequential performance is: "+(1000l*COUNT/(endTime-startTime))+" objects / sec");
   }

   public static class LineCounter implements Flow<Integer> {
      private final TransformingFlow<String, Integer> delegate;
      private int count = 0;

      public LineCounter(Flow<String> upstream) {
         this.delegate = new TransformingFlow<>(upstream, this::count);
      }

      private void count(String line, Consumer<Integer> downstream) {
         count++;
         if (count % 5 == 0) {
            downstream.accept(count);
         }
      }

      @Override
      public void registerDownstream(Consumer<? super Integer> downstream) {
         delegate.registerDownstream(downstream);
      }
   }

   public static enum Severity {
      DEBUG, WARN, ERROR, FATAL;
   }
}


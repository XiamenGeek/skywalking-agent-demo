package org.mydemo;

import java.lang.instrument.Instrumentation;

public class SkywalkingAgent {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("Hello, this is a Skywalking java agent demo.");
        instrumentation.addTransformer(new SkywalkingTransformer());
    }
}

package com.example;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class InstrumentAgent {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("premain：开始记录执行时间");
        ClassFileTransformer transformer = new PerformMonitorTransformer();
        instrumentation.addTransformer(transformer);
    }
}

package com.ott.neuron.utils;

public class ErrorLogger {
    public static void log(String context, String message, Exception e) {
        System.out.println("[Error] [" + context + "] " + message);
        if (e != null) System.out.println(e.getMessage());
    }

    public static void log(String context, String message) {
        log(context, message, null);
    }
}

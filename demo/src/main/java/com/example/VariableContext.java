package com.example;

import java.util.HashMap;
import java.util.Map;

public class VariableContext {
    private static Map<String, Integer> context = new HashMap<>();

    public static void set(String name, int value) {
        context.put(name, value);
    }

    public static Integer get(String name) {
        return context.get(name);
    }

    public static void remove(String name) {
        context.remove(name);
    }
}

package com.example;

import java.util.HashMap;
import java.util.Map;

public class VariableContext {

    private Map<String, Object> variables;

    public VariableContext() {
        variables = new HashMap<>();
    }

    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    public Object getVariable(String name) {
        return variables.get(name);
    }

    public boolean containsVariable(String name) {
        return variables.containsKey(name);
    }

    public void removeVariable(String name) {
        variables.remove(name);
    }

    public String getVariableType(String name) {
        Object value = variables.get(name);
        if (value != null) {
            return value.getClass().getSimpleName();
        }
        return null;
    }
}

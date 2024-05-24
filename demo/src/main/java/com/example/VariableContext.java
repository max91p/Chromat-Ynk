package com.example;

import java.util.HashMap;
import java.util.Map;

public class VariableContext {

    private Map<String, Object> variables;

    public VariableContext() {
        variables = new HashMap<>();
    }

    public void setVariable(String name, Object value) {
        variables.put(name.trim(), value);
    }

    public Object getVariable(String name) {
        return variables.get(name);
    }

    public boolean containsVariable(String name) {
        return variables.containsKey(name.trim());
    }

    public void removeVariable(String name) {
        variables.remove(name.trim());
    }

    public String getVariableType(String name) {
        Object value = variables.get(name.trim());
        if (value != null) {
            return value.getClass().getSimpleName();
        }
        return null;
    }

    public void createNumericVariable(String name,Double value){
        if(value==null){
            value=0.0;
        }
        setVariable(name.trim(),value);
    }

    public void createStringVariable(String name,String value){
        if(value==null){
            value="";
        }
        setVariable(name.trim(),value);
    }

    public void createBooleanVariable(String name,Boolean value){
        if(value==null){
            value=false;
        }
        setVariable(name.trim(),value);
    }
}

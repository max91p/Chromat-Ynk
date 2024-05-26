package com.example;

import java.util.HashMap;
import java.util.Map;

public class VariableContext {

    private Map<String, Object> variables;

    /**
     * Constructs a new VariableContext
     */
    public VariableContext() {
        variables = new HashMap<>();
    }

    /**
     * Sets a variable in the context
     *
     * @param name  The name of the variable
     * @param value The value of the variable
     */
    public void setVariable(String name, Object value) {
        variables.put(name.trim(), value);
    }

    /**
     * Return the value of a variable by its name
     *
     * @param name The name of the variable
     * @return The value of the variable, or null if not found
     */
    public Object getVariable(String name) {
        return variables.get(name);
    }

    /**
     * Check if a variable with the given name exists in the context
     *
     * @param name The name of the variable
     * @return True if the variable exists, false otherwise
     */
    public boolean containsVariable(String name) {
        return variables.containsKey(name.trim());
    }

    /**
     * Removes a variable from the context by its name
     *
     * @param name The name of the variable to remove
     */
    public void removeVariable(String name) {
        variables.remove(name.trim());
    }

    /**
     * Return the type of a variable by its name
     *
     * @param name The name of the variable
     * @return The type of the variable as a string, or null if the variable does not exist
     */
    public String getVariableType(String name) {
        Object value = variables.get(name.trim());
        if (value != null) {
            return value.getClass().getSimpleName();
        }
        return null;
    }

    /**
     * Create a numeric variable with the specified name and value
     *
     * @param name  The name of the variable
     * @param value The value of the variable
     */
    public void createNumericVariable(String name,Number value){
        if(value==null){
            value=0.0;
        }
        setVariable(name.trim(),value);
    }

    /**
     * Create a string variable with the specified name and value
     *
     * @param name  The name of the variable
     * @param value The value of the variable
     */
    public void createStringVariable(String name,String value){
        if(value==null){
            value="";
        }
        setVariable(name.trim(),value);
    }

    /**
     * Create a boolean variable with the specified name and value
     *
     * @param name  The name of the variable
     * @param value The value of the variable
     */
    public void createBooleanVariable(String name,Boolean value){
        if(value==null){
            value=false;
        }
        setVariable(name.trim(),value);
    }
}

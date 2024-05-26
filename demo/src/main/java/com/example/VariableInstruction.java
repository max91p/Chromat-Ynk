package com.example;

/**
 * Represents a variable instruction
 */
public class VariableInstruction extends Instruction{

    private String type;
    private String name;
    private Object value;
    private VariableContext variableContext;


    /**
     * Construct a new VariableInstruction.
     *
     * @param type            The type of variable instruction
     * @param name            The name of the variable
     * @param value           The value associated with the variable instruction
     * @param variableContext The VariableContext instance for managing variables
     */
    public VariableInstruction(String type, String name, Object value, VariableContext variableContext){
        this.type=type;
        this.name=name;
        this.value=value;
        this.variableContext=variableContext;
    }

    /**
     * Resolve the parameter of the instruction (replace variable by value)
     *
     * @param parameter       The type of variable instruction
     * @return The resolved parameter
     */
    private Object resolveParameter(Object parameter) {
        //Check if the parameter is a variable name
        if (parameter instanceof String && variableContext.containsVariable((String) parameter)) {
            //If it is, return the corresponding value
            return variableContext.getVariable((String) parameter);
        }
        //If not return the parameter
        return parameter;
    }

    /**
     * Execute the variable instruction
     *
     * @throws ErrorLogger if an error occured during execution
     */
    @Override
    public void execute() throws ErrorLogger {
        //Resolve the parameter before executing the instruction
        Object resolvedParameter = resolveParameter(value);
        switch (type){
            case "NUM":
                // Create a numeric variable with the specified name and value
                variableContext.createNumericVariable(name,(Number) resolvedParameter);
                break;
            case "STR":
                // Create a String variable with the specified name and value
                variableContext.createStringVariable(name,(String) resolvedParameter);
                break;
            case "BOOL":
                // Create a boolean variable with the specified name and value
                variableContext.createBooleanVariable(name,(Boolean) resolvedParameter);
                break;
            case "DEL":
                // Remove the variable with the specified name
                variableContext.removeVariable(name);
                break;
        }

    }

    /**
     * Execute the mirror operation
     *
     * @param Axial
     * @param parameter
     * @throws ErrorLogger if an error occured during execution
     */
    @Override
    public void mirrorExecute(boolean Axial, Object parameter) throws ErrorLogger {
        Object resolvedParameter = resolveParameter(value);
        switch (type){
            case "NUM":
                variableContext.createNumericVariable(name,(Number) resolvedParameter);
                break;
            case "STR":
                variableContext.createStringVariable(name,(String) resolvedParameter);
                break;
            case "BOOL":
                variableContext.createBooleanVariable(name,(Boolean) resolvedParameter);
                break;
            case "DEL":
                variableContext.removeVariable(name);
                break;
        }
    }


    /**
     * check if the variable instruction is valid
     *
     * @return True if the instruction is valid, false if it's not
     * @throws ErrorLogger if an error occured during execution
     */
    @Override
    public boolean isValid() throws ErrorLogger {
        Object resolvedParameter = resolveParameter(value);
        boolean res=false;
        switch (type){
            case "NUM":
                // Check if the parameter is null or numeric
                if(resolvedParameter==null||resolvedParameter instanceof Integer || resolvedParameter instanceof Double){
                    res=true;
                } else {
                    throw new ErrorLogger("Variable value needs to be numeric for NUM type");
                }
                break;
            case "STR":
                // Check if the parameter is null or a string
                if (resolvedParameter == null || resolvedParameter instanceof String) {
                    res = true;
                } else {
                    throw new ErrorLogger("Variable value needs to be a String for STR type");
                }
                break;
            case "BOOL":
                // Check if the parameter is null or a boolean
                if (resolvedParameter == null || resolvedParameter instanceof Boolean) {
                    res = true;
                } else {
                    throw new ErrorLogger("Variable value needs to be a Boolean for BOOL type");
                }
                break;
            case "DEL":
                // Check if the variable name is not null or empty and if it exists
                if (name != null && !name.trim().isEmpty() && variableContext.containsVariable(name)) {
                    res = true;
                } else {
                    throw new ErrorLogger("Variable name can't be empty for DELETE operation");
                }
                break;
        }
        return res;
    }
}

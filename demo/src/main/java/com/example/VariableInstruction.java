package com.example;

public class VariableInstruction extends Instruction{

    private String type;
    private String name;
    private Object value;
    private VariableContext variableContext;

    public VariableInstruction(String type, String name, Object value, VariableContext variableContext){
        this.type=type;
        this.name=name;
        this.value=value;
        this.variableContext=variableContext;
    }

    private Object resolveParameter(Object parameter) {
        if (parameter instanceof String && variableContext.containsVariable((String) parameter)) {
            return variableContext.getVariable((String) parameter);
        }
        return parameter;
    }

    @Override
    public void execute() throws ErrorLogger {
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


    @Override
    public boolean isValid() throws ErrorLogger {
        Object resolvedParameter = resolveParameter(value);
        boolean res=false;
        switch (type){
            case "NUM":
                if(resolvedParameter==null||resolvedParameter instanceof Integer || resolvedParameter instanceof Double){
                    res=true;
                } else {
                    throw new ErrorLogger("Variable value needs to be numeric for NUM type");
                }
                break;
            case "STR":
                if (resolvedParameter == null || resolvedParameter instanceof String) {
                    res = true;
                } else {
                    throw new ErrorLogger("Variable value needs to be a String for STR type");
                }
                break;
            case "BOOL":
                if (resolvedParameter == null || resolvedParameter instanceof Boolean) {
                    res = true;
                } else {
                    throw new ErrorLogger("Variable value needs to be a Boolean for BOOL type");
                }
                break;
            case "DEL":
                if (name != null && !name.trim().isEmpty()) {
                    res = true;
                } else {
                    throw new ErrorLogger("Variable name can't be empty for DELETE operation");
                }
                break;
        }
        return res;
    }
}

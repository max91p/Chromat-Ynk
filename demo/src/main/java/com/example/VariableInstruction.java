package com.example;

public class VariableInstruction extends Instruction{

    private String type;
    private String name;
    private Object value;
    private VariableContext variableContext;

    public VariableInstruction(String type, String name, String value, VariableContext variableContext){
        this.type=type;
        this.name=name;
        this.value=value;
        this.variableContext=variableContext;
    }

    @Override
    public void execute() throws ErrorLogger {
        switch (type){
            case "NUM":
                variableContext.createNumericVariable(name,(Number) value);
                break;
            case "STR":
                variableContext.createStringVariable(name,(String) value);
                break;
            case "BOOL":
                variableContext.createBooleanVariable(name,(Boolean) value);
                break;
            case "DEL":
                variableContext.removeVariable(name);
                break;
        }

    }

    @Override
    public void mirrorExecute(boolean Axial, Object parameter) throws ErrorLogger {

    }


    @Override
    public boolean isValid() throws ErrorLogger {
        boolean res=false;
        switch (type){
            case "NUM":
                if(value==null||value instanceof Integer || value instanceof Double){
                    res=true;
                } else {
                    throw new ErrorLogger("Variable value needs to be numeric for NUM type");
                }
                break;
            case "STR":
                if (value == null || value instanceof String) {
                    res = true;
                } else {
                    throw new ErrorLogger("Variable value needs to be a String for STR type");
                }
                break;
            case "BOOL":
                if (value == null || value instanceof Boolean) {
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

package com.example;

import java.security.KeyStore;
import java.util.Calendar;
import java.util.List;
import java.util.function.BooleanSupplier;
import javafx.scene.Scene;

/**
 * Class to represent a block of instruction
 */
public class InstructionsBlock extends Instruction {
    private String type;
    private List<Instruction> instructions;
    private String argument;
    private CursorManager cursorManager;
    private VariableContext variableContext;
    private Scene scene;

    /**
     * Constructor
     * @param type              Type of the block
     * @param argument          Argument
     * @param instructions      List of instructions
     * @param cursorManager     Cursors manager
     * @param variableContext   Variables manager
     * @param scene             Scene
     */
    public InstructionsBlock(String type, String argument, List<Instruction> instructions, CursorManager cursorManager, VariableContext variableContext, Scene scene) {
        this.type = type;
        this.argument = argument;
        this.instructions = instructions;
        this.cursorManager = cursorManager;
        this.variableContext = variableContext;
        this.scene = scene;
    }

    /**
     * Evaluate the value of a comparison
     * @param condition
     * @return True if it's the value of the comprarison, otherwise false
     */
    private boolean evaluateComparison(String condition){
        condition=condition.trim();

        // Define comparison operators
        String[] operators={"==","<=",">=","<",">","!="};
        try {
            //check for each operator in the condition string
            for (String operator : operators) {
                int position = condition.indexOf(operator);
                //if the operator is find
                if (position != -1) {
                    //split the condition into left and right parts of the operator
                    String leftPart = condition.substring(0, position).trim();
                    String rightPart = condition.substring(position + operator.length()).trim();
                    // evaluate the boolean expression
                    return evaluateBooleanExpression(leftPart, rightPart, operator);
                }
            }

            //Handle negation at the start of the boolean condition
            if (condition.startsWith("!")) {
                String expression = condition.substring(1).trim();
                //check for "true" or "false" values (direct String or boolean variable)
                if (expression == "true" || (Boolean) resolvedParameter(expression) == true) {
                    return false;
                } else if (expression == "false" || (Boolean) resolvedParameter(expression) == false) {
                    return true;
                }
            }

            //Check for "true" or "false values (String or boolean variable)
            if (condition == "true" || (Boolean) resolvedParameter(condition) == true) {
                return true;
            } else if (condition == "false" || (Boolean) resolvedParameter(condition) == false) {
                return false;
            }
            //throw an error if the condition is unsupported
            throw new ErrorLogger("Condition :" + condition + "unsupported");
        }catch (ErrorLogger e){
            e.logError();
            return false;
        }
    }

    /**
     *  Evaluate the value of a boolean expression
     * @param leftPart      Left part of the Operator
     * @param rightPart     Right part of the Operator
     * @param operator      Operator
     * @return
     */
    private boolean evaluateBooleanExpression(String leftPart, String rightPart,String operator){
        //resolve parameters to handle variable
        Object leftOperand=resolvedParameter(leftPart);
        Object rightOperand= resolvedParameter(rightPart);

        try {
            //Convert operands to compatible types
            if (leftOperand instanceof String && rightOperand instanceof Number) {
                leftOperand = convertToCompatibleType((String) leftOperand, (Number) rightOperand);
            } else if (rightOperand instanceof String && leftOperand instanceof Number) {
                rightOperand = convertToCompatibleType((String) rightOperand, (Number) leftOperand);
            } else if (leftOperand instanceof String && rightOperand instanceof String) {
                leftOperand = Double.parseDouble((String) leftOperand);
                rightOperand = Double.parseDouble((String) rightOperand);
            }


            //Comparison based on the operator
            switch (operator) {
                case "==":
                    return compareTo(rightOperand, leftOperand) == 0;
                case "!=":
                    return !(compareTo(rightOperand, leftOperand) == 0);
                case "<":
                    return compareTo(leftOperand, rightOperand) < 0;
                case ">":
                    return compareTo(leftOperand, rightOperand) > 0;
                case "<=":
                    return compareTo(leftOperand, rightOperand) <= 0;
                case ">=":
                    return compareTo(leftOperand, rightOperand) >= 0;
                default:
                    throw new ErrorLogger("Operator" + operator + "unsupported");
            }
        }catch(ErrorLogger e){
            e.logError();
            return false;
        }
    }

    /**
     * Convert values to compatible type to compare
     * @param stringValue
     * @param numericValue
     * @return a number
     */
    private Object convertToCompatibleType(String stringValue, Number numericValue) {
        try {
            //Check the type of numeric Value to convert stringValue
            if (numericValue instanceof Integer) {
                return Integer.parseInt(stringValue);
            } else if (numericValue instanceof Double) {
                return Double.parseDouble(stringValue);
            }

            throw new ErrorLogger("Non numeric type");
        }catch (ErrorLogger e){
            e.logError();
            return false;
        }
    }

    /**
     * Evaluate the boolean value of a logical expression (to handle && and || operator)
     * @param expression
     * @return  True if the expression is true, false otherwise
     */
    private boolean evaluateLogicalExpression(String expression){
        //Split the expression by "||"
        String[] parts = expression.split("\\|\\|");
        //Check if parts contains mutiple parts
        if(parts.length>1){
            //evaluate each part one by one
            for (String part : parts){
                //If any part is true then the result of the expression is true ("OR")
                if(evaluateLogicalExpression(part.trim())){
                    return true;
                }
            }
            //If none of the parts is true, the expression is false
            return false;
        }

        //Split the expression by "&&"
        parts = expression.split("&&");
        //Check if parts contains mutiple parts
        if(parts.length>1){
            //evaluate each part one by one
            for(String part : parts){
                //If any part is false then the result of the expression is false ("AND")
                if(!evaluateLogicalExpression(part.trim())){
                    return false;
                }
            }
            //If all parts are true then the result is true
            return true;
        }

        //If the expression has no logical operators (parts.length==1) evaluate the expression as a comparison
        return evaluateComparison(expression);
    }

    /**
     * Replace a variable by its value if it's a known by the variables manager
     * @param parameter
     * @return the resolved parameter
     */
    private Object resolvedParameter(String parameter){
        //Check if the paramater is a variable name
        if(variableContext.containsVariable(parameter.trim())){
            //If it is, return the corresponding value
            return variableContext.getVariable(parameter.trim());
        }
        //If not return the parameter
        return parameter;
    }

    /**
     * Compare values
     * @param leftValue
     * @param rightValue
     * @return a comparison integer
     */
    private int compareTo(Object leftValue, Object rightValue){
        try{
            //Check if both values have the same number type and compare them
            if(leftValue instanceof Integer && rightValue instanceof Integer){
                return Integer.compare((Integer)leftValue,(Integer)rightValue);
            } else if (leftValue instanceof Double && rightValue instanceof Double) {
                return Double.compare((Double)leftValue,(Double)rightValue);
            } else{
                //Error if not comparable
                throw new ErrorLogger("Comparison between different types is impossible");
            }
        }catch (ErrorLogger e){
            e.logError();
            return -1;
        }
    }

    /**
     * Execute instructions
     * @throws ErrorLogger
     */
    @Override
    public void execute() throws ErrorLogger {
        try{
            switch (type) {
                case "IF":
                    //Execute instrcutions if the logical expression is true
                    if (evaluateLogicalExpression(argument)) {
                        for (Instruction instruction : instructions) {
                            if (instruction.isValid()) {
                                instruction.execute();
                            }
                        }
                    }
                    break;

                case "FOR":
                    String[] parts= argument.split("\\s+");
                    String variableName=parts[0];

                    //loop parameters
                    int from=0;
                    int to;
                    int step=1;

                    //Handle different loop format
                    if(parts.length == 5){
                        // [FOR] name FROM v1 TO v2
                        from=Integer.parseInt(parts[2]);
                        to=Integer.parseInt(parts[4]);
                    } else if(parts.length==7){
                        // [FOR] name FROM v1 TO v2 STEP v3
                        from=Integer.parseInt(parts[2]);
                        to=Integer.parseInt(parts[4]);
                        step=Integer.parseInt(parts[6]);
                    } else if (parts.length==3) {
                        //[FOR] name TO v1
                        to=Integer.parseInt(parts[2]);
                    } else {
                        throw new ErrorLogger("Invalid loop format");
                    }

                    //execute the instructions for each iteration
                    for(int i = from; i<=to;i+=step){
                        //Update the iterative variable
                        variableContext.setVariable(variableName,i);
                        for(Instruction instruction : instructions){
                            if (instruction.isValid()){
                                instruction.execute();
                            }
                        }
                    }
                    break;

                case "WHILE":
                    //Execute instruction while logical expression is true
                    while (evaluateLogicalExpression(argument)) {
                        for (Instruction instruction : instructions) {
                            if (instruction.isValid()) {
                                instruction.execute();
                            }
                        }
                    }
                    break;

                case "MIMIC":
                    //execute instructions on both currentCursor and original cursor (ID on parameters)
                    int cursorID = Integer.parseInt(argument.trim());
                    int originalCursor = cursorManager.getCurrentCursor().getId();

                    cursorManager.selectCursor(cursorID);
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
                            cursorManager.selectCursor(originalCursor);
                            instruction.execute();
                            cursorManager.selectCursor(cursorID);
                        }
                    }

                    cursorManager.selectCursor(originalCursor);
                    break;

                case "MIRROR":
                    //Execute instructions with mirrrored movement
                    Point[] points = parsePoints((String) argument);
                    Point pointA = points[0];
                    int currentCursorId = cursorManager.getCurrentCursor().getId();

                    if (points.length > 1) {
                        Point pointB = points[1];

                        Cursor currentCursor = cursorManager.getCurrentCursor();
                        int a= 31;
                        while (cursorManager.isCursorIdExists(currentCursor.getId() * a)){
                            a++;
                        }
                        Cursor mirroredCursor = new Cursor(
                                axialSymmetry(currentCursor.getPosition(), pointA, pointB),
                                (180-currentCursor.getAngle()+360)%360, // Angle adapted to +180
                                currentCursor.getColor(),
                                currentCursor.getWidth(),
                                currentCursor.getOpacity(),
                                currentCursor.getId() * a,
                                scene
                        );
                        cursorManager.addCursor(mirroredCursor);

                        // Axial symmetry logic
                        for (Instruction instruction : instructions) {
                            if (instruction.isValid()) {
                                instruction.execute();
                                cursorManager.selectCursor(mirroredCursor.getId());
                                instruction.mirrorExecute(true, argument);
                                cursorManager.selectCursor(currentCursorId);
                            }
                        }
                    } else {
                        Cursor currentCursor = cursorManager.getCurrentCursor();
                        int a= 31;
                        while (cursorManager.isCursorIdExists(currentCursor.getId() * a)){
                            a++;
                        }
                        Cursor mirroredCursor = new Cursor(
                                centralSymmetry(currentCursor.getPosition(), pointA),
                                (180-currentCursor.getAngle()+360)%360, // Angle adapted to +180
                                currentCursor.getColor(),
                                currentCursor.getWidth(),
                                currentCursor.getOpacity(),
                                currentCursor.getId() * a,
                                scene
                        );
                        cursorManager.addCursor(mirroredCursor);

                        // Central symmetry logic
                        for (Instruction instruction : instructions) {
                            if (instruction.isValid()) {
                                instruction.execute();
                                cursorManager.selectCursor(mirroredCursor.getId());
                                instruction.mirrorExecute(false, argument);
                                cursorManager.selectCursor(currentCursorId);
                            }
                        }
                    }
                    break;

                default:
                    throw new ErrorLogger("Unknown block instruction type: " + type);
            }
        }catch(ErrorLogger e){
            e.logError();
        }
    }

    /**
     * Execute mirror instructions
     * @param Axial
     * @param parameter
     * @throws ErrorLogger
     */
    @Override
    public void mirrorExecute(boolean Axial,Object parameter) throws ErrorLogger {
        try{
            switch (type) {
                case "IF":
                    if (evaluateLogicalExpression(argument)) {
                        for (Instruction instruction : instructions) {
                            if (instruction.isValid()) {
                                instruction.mirrorExecute(Axial, parameter);
                            }
                        }
                    }
                    break;

                case "FOR":
                    String[] parts = argument.split("\\s+");
                    String variableName = parts[0];

                    int from = 0;
                    int to;
                    int step = 1;

                    if (parts.length == 5) {
                        from = Integer.parseInt(parts[2]);
                        to = Integer.parseInt(parts[4]);
                    } else if (parts.length == 7) {
                        from = Integer.parseInt(parts[2]);
                        to = Integer.parseInt(parts[4]);
                        step = Integer.parseInt(parts[6]);
                    } else if (parts.length == 3) {
                        to = Integer.parseInt(parts[2]);
                    } else {
                        throw new ErrorLogger("Invalid loop format");
                    }

                    for (int i = from; i <= to; i += step) {
                        variableContext.setVariable(variableName, i);
                        for (Instruction instruction : instructions) {
                            if (instruction.isValid()) {
                                instruction.mirrorExecute(Axial, parameter);
                            }
                        }
                    }
                    break;

                case "WHILE":
                    while (evaluateLogicalExpression(argument)) {
                        for (Instruction instruction : instructions) {
                            if (instruction.isValid()) {
                                instruction.mirrorExecute(Axial, parameter);
                            }
                        }
                    }
                    break;

                case "MIMIC":
                    int cursorID = Integer.parseInt(argument.trim());
                    int originalCursor = cursorManager.getCurrentCursor().getId();

                    cursorManager.selectCursor(cursorID);
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.mirrorExecute(Axial, parameter);
                            cursorManager.selectCursor(originalCursor);
                            instruction.mirrorExecute(Axial, parameter);
                            cursorManager.selectCursor(cursorID);
                        }
                    }

                    cursorManager.selectCursor(originalCursor);
                    break;
                case "MIRROR":
                    Point[] points = parsePoints((String) argument);
                    Point pointA = points[0];
                    int currentCursorId = cursorManager.getCurrentCursor().getId();

                    if (points.length > 1) {
                        Point pointB = points[1];
                        Cursor currentCursor = cursorManager.getCurrentCursor();
                        int a= 31;
                        while (cursorManager.isCursorIdExists(currentCursor.getId() * a)){
                            a++;
                        }
                        Cursor mirroredCursor = new Cursor(
                                axialSymmetry(currentCursor.getPosition(), pointA, pointB),
                                (180-currentCursor.getAngle()+360)%360, // Angle adapted to +180
                                currentCursor.getColor(),
                                currentCursor.getWidth(),
                                currentCursor.getOpacity(),
                                currentCursor.getId() * a,
                                scene
                        );
                        cursorManager.addCursor(mirroredCursor);

                        // Axial symmetry logic
                        for (Instruction instruction : instructions) {
                            if (instruction.isValid()) {
                                instruction.mirrorExecute(true, argument);
                                cursorManager.selectCursor(mirroredCursor.getId());
                                instruction.execute();
                                cursorManager.selectCursor(currentCursorId);
                            }
                        }
                    } else {
                        Cursor currentCursor = cursorManager.getCurrentCursor();
                        int a= 31;
                        while (cursorManager.isCursorIdExists(currentCursor.getId() * a)){
                            a++;
                        }
                        Cursor mirroredCursor = new Cursor(
                                centralSymmetry(currentCursor.getPosition(), pointA),
                                (180-currentCursor.getAngle()+360)%360, // Angle adapted to +180
                                currentCursor.getColor(),
                                currentCursor.getWidth(),
                                currentCursor.getOpacity(),
                                currentCursor.getId() * a,
                                scene
                        );
                        cursorManager.addCursor(mirroredCursor);

                        // Central symmetry logic
                        for (Instruction instruction : instructions) {
                            if (instruction.isValid()) {
                                instruction.mirrorExecute(false, argument);
                                cursorManager.selectCursor(mirroredCursor.getId());
                                instruction.execute();
                                cursorManager.selectCursor(currentCursorId);
                            }
                        }
                    }

                    break;

                default:
                    throw new ErrorLogger("Unknown block instruction type: " + type);
            }
        }catch (ErrorLogger e){
            e.logError();
        }
    }

    /**
     *
     * @param p
     * @param a
     * @param b
     * @return
     */
    public static Point axialSymmetry(Point p, Point a, Point b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double t = ((p.getX() - a.getX()) * dx + (p.getY() - a.getY()) * dy) / (dx * dx + dy * dy);
        double x_p = a.getX() + t * dx;
        double y_p = a.getY() + t * dy;
        double x_s = 2 * x_p - p.getX();
        double y_s = 2 * y_p - p.getY();
        return new Point(x_s, y_s);
    }

    /**
     *
     * @param p
     * @param c
     * @return
     */
    public static Point centralSymmetry(Point p, Point c) {
        double x_s = 2 * c.getX() - p.getX();
        double y_s = 2 * c.getY() - p.getY();
        return new Point(x_s, y_s);
    }

    /**
     * Parse a String to a Point
     * @param pointString
     * @return A point
     */
    private Point parsePoint(String pointString) {
        // Remove parentheses and trim any whitespace
        pointString = pointString.replace("(", "").replace(")", "").trim();

        // Split the string by comma to get the coordinates
        String[] coordinates = pointString.split(",");
        double x = Double.parseDouble(coordinates[0].trim());
        double y = Double.parseDouble(coordinates[1].trim());

        return new Point(x, y);
    }

    private Point[] parsePoints(String pointsString) {
        // Remove parentheses and split by "),("
        String[] pointStrings = pointsString.split("\\),\\(");

        // Parse each point
        Point[] points = new Point[pointStrings.length];
        for (int i = 0; i < pointStrings.length; i++) {
            points[i] = parsePoint(pointStrings[i]);
        }
        return points;
    }

    /**
     * Check if the instruction is valid
     * @return True if it's valid, false otherwise
     * @throws ErrorLogger
     */
    public boolean isValid() throws ErrorLogger {
        try {
            switch (type) {
                case "IF":
                    // Check if both condition and instructions are not null
                    if (argument != null && instructions != null){
                        return true;
                    } else {
                        throw new ErrorLogger("condition required and instructions cannot be null");
                    }

                case "FOR":
                    //Validate FOR block arguments based on their format and  check if instructions is not null
                    String[] parts = argument.split("\\s+");
                    if (parts.length == 3 && argument.contains("TO") || parts.length == 5 && argument.contains("TO") && argument.contains("FROM")|| parts.length == 7&& argument.contains("TO") && argument.contains("FROM") && argument.contains("STEP")) {
                        if (instructions != null) {
                            return true;
                        } else {
                            throw new ErrorLogger("Invalid FOR block: instructions cannot be null.");
                        }
                    } else {
                        throw new ErrorLogger("Invalid FOR block: incorrect number of arguments.");
                    }


                case "WHILE":
                    //check if both condition and instructions are not null
                    if (argument != null && instructions != null){
                        return true;
                    } else {
                        throw new ErrorLogger("condition needs to be a BooleanSupplier type and instructions cannot be null");
                    }

                case "MIMIC":
                    // Check if both argument and instructions are not null
                    if (argument != null && instructions != null){
                    return true;
                    } else {
                    throw new ErrorLogger("condition needs to be an Integer type and instructions cannot be null");
                }

                case "MIRROR":
                    //Validate MIRROR arguments
                    String[] points = argument.split("\\),\\(");
                    if (points.length == 1 || points.length == 2) {
                        for (String point : points) {
                            try {
                                parsePoint(point); //parse each point
                                } catch (Exception e) {
                                    return false;
                                }
                            }
                            if( instructions != null){
                                return true;
                            }
                        }
                    return false;

                default:
                    return false;
            }
        }
        catch (ErrorLogger e){
            throw e;
        }
        catch (Exception e){
            throw new ErrorLogger("Error during execution of the instruction :" + type , e);
        }
    }

    /**
     * Getter of instructions list
     * @return a list of instructions
     */
    public List<Instruction> getInstructions() {
        return instructions;
    }
}

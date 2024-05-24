package com.example;

import java.security.KeyStore;
import java.util.Calendar;
import java.util.List;
import java.util.function.BooleanSupplier;
import javafx.scene.Scene;

public class InstructionsBlock extends Instruction {
    private String type;
    private List<Instruction> instructions;
    private String argument;
    private CursorManager cursorManager;
    private VariableContext variableContext;
    private Scene scene;

    public InstructionsBlock(String type, String argument, List<Instruction> instructions, CursorManager cursorManager, VariableContext variableContext, Scene scene) {
        this.type = type;
        this.argument = argument;
        this.instructions = instructions;
        this.cursorManager = cursorManager;
        this.variableContext = variableContext;
        this.scene = scene;
    }

    private boolean evaluateComparison(String condition){
        condition=condition.trim();

        String[] operators={"==","<=",">=","<",">","!="};
        for (String operator : operators){
            int position = condition.indexOf(operator);
            if(position != -1){
                String leftPart = condition.substring(0,position).trim();
                String rightPart = condition.substring(position+operator.length()).trim();
                return evaluateBooleanExpression(leftPart,rightPart,operator);
            }
        }

        if(condition=="true" || (Boolean)resolvedParameter(condition)==true){
            return true;
        } else if (condition=="false" || (Boolean)resolvedParameter(condition)==false) {
            return false;
        }

        if(condition.startsWith("!")){
            String expression= condition.substring(1).trim();
            if(expression=="true" || (Boolean)resolvedParameter(expression)==true){
                return false;
            } else if (expression=="false" || (Boolean)resolvedParameter(expression)==false) {
                return true;
            }
        }
        throw new IllegalArgumentException("Condition :"+ condition + "unsupported");
    }

    private boolean evaluateBooleanExpression(String leftPart, String rightPart,String operator){
        Object leftOperand=resolvedParameter(leftPart);
        Object rightOperand= resolvedParameter(rightPart);

        if (leftOperand instanceof String && rightOperand instanceof Number) {
            leftOperand = convertToCompatibleType((String) leftOperand, (Number) rightOperand);
        } else if (rightOperand instanceof String && leftOperand instanceof Number) {
            rightOperand = convertToCompatibleType((String) rightOperand, (Number) leftOperand);
        } else if (leftOperand instanceof String && rightOperand instanceof String) {
            leftOperand = Double.parseDouble((String) leftOperand);
            rightOperand = Double.parseDouble((String) rightOperand);
        }

        switch (operator){
            case "==":
                return compareTo(rightOperand,leftOperand)==0;
            case "!=":
                return !(compareTo(rightOperand,leftOperand)==0);
            case "<":
                return compareTo(leftOperand,rightOperand)<0;
            case ">":
                return compareTo(leftOperand,rightOperand)>0;
            case "<=":
                return compareTo(leftOperand,rightOperand)<=0;
            case ">=":
                return compareTo(leftOperand,rightOperand)>=0;
            default:
                throw new IllegalArgumentException("Operator"+ operator + "unsupported");
        }
    }

    private Object convertToCompatibleType(String stringValue, Number numericValue) {
        if (numericValue instanceof Integer) {
            return Integer.parseInt(stringValue);
        } else if (numericValue instanceof Double) {
            return Double.parseDouble(stringValue);
        }
        throw new IllegalArgumentException("Non numeric type");
    }
    private boolean evaluateLogicalExpression(String expression){
        String[] parts = expression.split("\\|\\|");
        if(parts.length>1){
            for (String part : parts){
                if(evaluateLogicalExpression(part.trim())){
                    return true;
                }
            }
            return false;
        }

        parts = expression.split("&&");
        if(parts.length>1){
            for(String part : parts){
                if(!evaluateLogicalExpression(part.trim())){
                    return false;
                }
            }
            return true;
        }

        return evaluateComparison(expression);
    }

    private Object resolvedParameter(String parameter){
        if(variableContext.containsVariable(parameter.trim())){
            return variableContext.getVariable(parameter.trim());
        }
        return parameter;
    }

    private int compareTo(Object leftValue, Object rightValue){
        if(leftValue instanceof Integer && rightValue instanceof Integer){
            return Integer.compare((Integer)leftValue,(Integer)rightValue);
        } else if (leftValue instanceof Double && rightValue instanceof Double) {
            return Double.compare((Double)leftValue,(Double)rightValue);
        } else{
            throw new IllegalArgumentException("Comparison between different types is impossible");
        }
    }

    @Override
    public void execute() throws ErrorLogger {
        switch (type) {
            case "IF":
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

                int from=0;
                int to;
                int step=1;

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
                    throw new IllegalArgumentException("Invalid loop format");
                }

                for(int i = from; i<=to;i+=step){
                    variableContext.setVariable(variableName,i);
                    for(Instruction instruction : instructions){
                        if (instruction.isValid()){
                            instruction.execute();
                        }
                    }
                }
                break;

            case "WHILE":
                while (evaluateLogicalExpression(argument)) {
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
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
                        instruction.execute();
                        cursorManager.selectCursor(originalCursor);
                        instruction.execute();
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
                throw new UnsupportedOperationException("Unknown block instruction type: " + type);
        }
    }

    @Override
    public void mirrorExecute(boolean Axial,Object parameter) throws ErrorLogger {
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
                    throw new IllegalArgumentException("Invalid loop format");
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
                throw new UnsupportedOperationException("Unknown block instruction type: " + type);
        }
    }

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

    public static Point centralSymmetry(Point p, Point c) {
        double x_s = 2 * c.getX() - p.getX();
        double y_s = 2 * c.getY() - p.getY();
        return new Point(x_s, y_s);
    }

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

    public boolean isValid() throws ErrorLogger {
        try {
            switch (type) {
                case "IF":
                    if (argument != null && instructions != null){
                        return true;
                    } else {
                        throw new ErrorLogger("condition needs to be a BooleanSupplier type and instructions cannot be null");
                    }

                case "FOR":
                    String[] parts = argument.split("\\s+");
                    if (parts.length == 3 || parts.length == 5 || parts.length == 7) {
                        for (int i = 1; i < parts.length; i += 2) {
                            try {
                                Integer.parseInt(parts[i]);
                            } catch (NumberFormatException e) {
                                throw new ErrorLogger("Invalid FOR block: FROM, TO, and STEP must be integers.");
                            }
                        }
                        if (instructions != null) {
                            return true;
                        } else {
                            throw new ErrorLogger("Invalid FOR block: instructions cannot be null.");
                        }
                    } else {
                        throw new ErrorLogger("Invalid FOR block: incorrect number of arguments.");
                    }


                case "WHILE":
                    if (argument != null && instructions != null){
                        return true;
                    } else {
                        throw new ErrorLogger("condition needs to be a BooleanSupplier type and instructions cannot be null");
                    }

                case "MIMIC":
                    if (argument != null && instructions != null){
                    return true;
                    } else {
                    throw new ErrorLogger("condition needs to be an Integer type and instructions cannot be null");
                }

                case "MIRROR":
                    String[] points = argument.split("\\),\\(");
                    if (points.length == 1 || points.length == 2) {
                        for (String point : points) {
                            try {
                                parsePoint(point);
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

    public List<Instruction> getInstructions() {
        return instructions;
    }
}

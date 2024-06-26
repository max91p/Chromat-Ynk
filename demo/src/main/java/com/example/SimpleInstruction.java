package com.example;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;

import java.nio.channels.Pipe;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a simple drawing instruction.
 */

public class SimpleInstruction extends Instruction {
    private String type;
    private Object parameters;
    private CursorManager cursors;
    private VariableContext variableContext;
    private Scene scene;

    /**
     * Constructs a new SimpleInstruction with the specified type and parameters.
     *
     * @param type       the type of the instruction
     * @param parameters the parameters of the instruction
     */
    public SimpleInstruction(String type, Object parameters, CursorManager cursors, VariableContext variableContext,Scene scene) {
        this.type = type;
        this.parameters = parameters;
        this.cursors = cursors;
        this.variableContext=variableContext;
        this.scene=scene;
    }

    /**
     * Constructs a new SimpleInstruction with the specified type and parameters.
     *
     * @param type the type of the instruction
     */
    public SimpleInstruction(String type, CursorManager cursors, VariableContext variableContext,Scene scene) {
        this.type = type;
        this.parameters = null;
        this.cursors = cursors;
        this.variableContext=variableContext;
        this.scene=scene;
    }

    /**
     * Replace variable by values in parameters of instructions
     *
     * @param parameter
     * @return
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
     * Executes the instruction on the given cursor.
     * @throws ErrorLogger
     */

    @Override
    public void execute() throws ErrorLogger{
        //Resolve the parameter before executing the instruction
        Object resolvedParameter = resolveParameter(parameters);

        //Execute instruction
        switch (type) {
            case "FWD":
                //Move the current cursor forward by the parameter value
                cursors.getCurrentCursor().moveForward((Double)resolvedParameter);
                break;
            case "BWD":
                //Move the current cursor backward by the parameter value
                cursors.getCurrentCursor().moveBackward((Double)resolvedParameter);
                break;
            case "TURN":
                //Turn the current cursorby the parameter value
                cursors.getCurrentCursor().turn((Double)resolvedParameter);
                break;
            case "SHOW":
                //Set the cursor visible
                cursors.getCurrentCursor().setVisible(true);
                break;
            case "HIDE":
                //Set the cursor invisible
                cursors.getCurrentCursor().setVisible(false);
                break;
            case "MOV":
                //Move the current cursor to a specified position
                String valuePoint = (String)resolvedParameter;
                if(valuePoint.contains(",")){
                    String[] valueCo = valuePoint.split(",");
                    double[] values = new double[valueCo.length];
                    for (int i = 0; i < valueCo.length; i++) {
                        values[i]= Double.parseDouble(valueCo[i]);
                    }
                    cursors.getCurrentCursor().move(values[0],values[1]);
                }
                break;
            case "POS":
                //Set the position of the current cursor
                String valuePointPos = (String)resolvedParameter;
                if(valuePointPos.contains(",")){
                    String[] valueCo = valuePointPos.split(",");
                    double[] values = new double[valueCo.length];
                    for (int i = 0; i < valueCo.length; i++) {
                        values[i]= Double.parseDouble(valueCo[i]);
                    }
                    cursors.getCurrentCursor().setPosition(values[0],values[1]);
                }

                break;
            case "PRESS":
                //Set the opacity of the current cursor
                //Handle with double parameter
                if (resolvedParameter instanceof Double) {
                    cursors.getCurrentCursor().setOpacity((Double)resolvedParameter);
                }
                //handle with String parameter
                if (resolvedParameter instanceof String) {
                    String value = (String)resolvedParameter;
                    // Vérification si la valeur correspond au format numérique avec un % à la fin
                    Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?%$");
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.matches()) {
                        // La valeur est au format numérique avec un % à la fin
                        double numericValue = Double.parseDouble(value.substring(0, value.length() - 1));
                        cursors.getCurrentCursor().setOpacity(numericValue / 100);
                    }
                }

                break;
            case "COLOR":
                //Set the color of the current cursor
                String valueString = (String)resolvedParameter;
                //Handle (R,G,B) format with R,G,B between 0 and 255
                if(valueString.contains(",") && !valueString.contains(".")){
                    String[] valueStrings = valueString.split(",");
                    int[] values = new int[valueStrings.length];
                    for (int i = 0; i < valueStrings.length; i++) {
                        if(variableContext.containsVariable(valueStrings[i])){
                            values[i]=(Integer)variableContext.getVariable(valueStrings[i]);
                        }
                        else {
                            values[i] = Integer.parseInt(valueStrings[i]);
                        }
                    }
                    cursors.getCurrentCursor().setColor(new ColorOfLine(values[0], values[1], values[2]));
                }
                //Handle #RRGGBB format
                else if (valueString.contains("#")){
                    cursors.getCurrentCursor().setColor(new ColorOfLine(valueString));
                }
                //Handle (R,G,B) format with R,G,B between 0 and 1
                else if(valueString.contains(",") && valueString.contains(".")){
                    String[] valueStrings = valueString.split(",");
                    double[] values = new double[valueStrings.length];
                    for (int i = 0; i < valueStrings.length; i++) {
                        if(variableContext.containsVariable(valueStrings[i])){
                            values[i]=(Double) variableContext.getVariable(valueStrings[i]);
                        }
                        else {
                            values[i]= Double.parseDouble(valueStrings[i]);
                        }

                    }
                    cursors.getCurrentCursor().setColor(new ColorOfLine(values[0], values[1], values[2]));
                }
                break;
            case "THICK":
                //Set the width of the cursor
                cursors.getCurrentCursor().setWidth((Double)resolvedParameter);
                break;
            case "LOOKAT":
                //Set the angle of the current cursor to face a point (or another cursor)
                if (resolvedParameter instanceof Integer) {
                    //Get the id of the cursor
                    Cursor cursor = cursors.getCursor((Integer)resolvedParameter);
                    double deltaX = cursor.getPosition().getX() - cursors.getCurrentCursor().getPosition().getX();
                    double deltaY = cursor.getPosition().getY() - cursors.getCurrentCursor().getPosition().getY();

                    double angleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));

                    cursors.getCurrentCursor().setAngle(angleToTarget);
                }
                if (resolvedParameter instanceof String) {
                    //Get the point to face
                    String valuePointLook = (String) resolvedParameter;
                    if (valuePointLook.contains(",")) {
                        String[] valueCo = valuePointLook.split(",");
                        double[] values = new double[valueCo.length];
                        for (int i = 0; i < valueCo.length; i++) {
                            values[i] = Double.parseDouble(valueCo[i]);
                        }
                        Point point = new Point(values[0], values[1]);

                        double deltaX = point.getX() - cursors.getCurrentCursor().getPosition().getX();
                        double deltaY = point.getY() - cursors.getCurrentCursor().getPosition().getY();

                        double angleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));

                        cursors.getCurrentCursor().setAngle(angleToTarget);
                    }
                }
                break;
            case "CURSOR":
                //Create a new cursor
                cursors.createCursor((Integer)resolvedParameter);
                break;
            case "SELECT":
                //Select an existing cursor
                cursors.selectCursor((Integer)resolvedParameter);
                break;
            case "REMOVE":
                //remove an existing cursor
                cursors.removeCursor((Integer)resolvedParameter);
                break;

        }
    }

    /**
     * Execute mirror instructions
     * @param Axial
     * @param parameter
     * @throws ErrorLogger
     */
    public void mirrorExecute(boolean Axial,Object parameter)throws ErrorLogger{
        switch (type) {
            case "FWD":
                if(Axial) {
                    cursors.getCurrentCursor().moveForward((Double) parameters);
                }
                else {
                    cursors.getCurrentCursor().moveForward(-((Double) parameters));
                }
                break;
            case "BWD":
                if(Axial) {
                    cursors.getCurrentCursor().moveBackward((Double) parameters);
                }
                else {
                    cursors.getCurrentCursor().moveBackward(-((Double) parameters));
                }
                break;
            case "TURN":
                cursors.getCurrentCursor().turn(-((Double)  parameters));
                break;
            case "SHOW":
                cursors.getCurrentCursor().setVisible(true);
                break;
            case "HIDE":
                cursors.getCurrentCursor().setVisible(false);
                break;
            case "MOV": {
                String symmetryPoints = (String) parameter;
                String destinationPoint = (String) parameters;
                Point destination = parsePoint(destinationPoint);
                Point newPos;
                if (Axial) {
                    // Handle axial symmetry with two points
                    if (symmetryPoints.contains("),(")) {
                        String[] pointStrings = symmetryPoints.split("\\),\\(");
                        Point p1 = parsePoint(pointStrings[0] + ")");
                        Point p2 = parsePoint("(" + pointStrings[1]);
                        newPos = axialSymmetry(destination, p1, p2);
                    } else {
                        newPos = destination;
                    }
                } else {
                    // Handle central symmetry with one point
                    if (symmetryPoints.contains(",")) {
                        Point center = parsePoint(symmetryPoints);
                        newPos = centralSymmetry(destination, center);
                    } else {
                        newPos = destination;
                    }
                }
                cursors.getCurrentCursor().move(newPos.getX(), newPos.getY());
                break;
            }
            case "POS": {
                String symmetryPointsPos = (String) parameter;
                String destinationPointPos = (String) parameters;
                Point destination = parsePoint(destinationPointPos);
                Point newPos;
                if (Axial) {
                    // Handle axial symmetry with two points
                    if (symmetryPointsPos.contains("),(")) {
                        String[] pointStrings = symmetryPointsPos.split("\\),\\(");
                        Point p1 = parsePoint(pointStrings[0] + ")");
                        Point p2 = parsePoint("(" + pointStrings[1]);
                        newPos = axialSymmetry(destination, p1, p2);
                    } else {
                        newPos = destination;
                    }
                } else {
                    // Handle central symmetry with one point
                    if (symmetryPointsPos.contains(",")) {
                        Point center = parsePoint(symmetryPointsPos);
                        newPos = centralSymmetry(destination, center);
                    } else {
                        newPos = destination;
                    }
                }
                cursors.getCurrentCursor().setPosition(newPos.getX(), newPos.getY());
                break;
            }
            case "PRESS":
                if (parameters instanceof Double) {
                    cursors.getCurrentCursor().setOpacity((Double) parameters);
                } else if (parameters instanceof String) {
                    String value = (String) parameters;
                    // Vérification si la valeur correspond au format numérique avec un % à la fin
                    Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?%$");
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.matches()) {
                        // La valeur est au format numérique avec un % à la fin
                        double numericValue = Double.parseDouble(value.substring(0, value.length() - 1));
                        cursors.getCurrentCursor().setOpacity(numericValue / 100);
                    }
                }
                break;
            case "COLOR": {
                String valueString = (String) parameters;
                if (valueString.contains(",") && !valueString.contains(".")) {
                    String[] valueStrings = valueString.split(",");
                    int[] values = new int[valueStrings.length];
                    for (int i = 0; i < valueStrings.length; i++) {
                        values[i] = Integer.parseInt(valueStrings[i]);
                    }
                    cursors.getCurrentCursor().setColor(new ColorOfLine(values[0], values[1], values[2]));
                } else if (valueString.contains("#")) {
                    cursors.getCurrentCursor().setColor(new ColorOfLine(valueString));
                } else if (valueString.contains(",") && valueString.contains(".")) {
                    String[] valueStrings = valueString.split(",");
                    double[] values = new double[valueStrings.length];
                    for (int i = 0; i < valueStrings.length; i++) {
                        values[i] = Double.parseDouble(valueStrings[i]);
                    }
                    cursors.getCurrentCursor().setColor(new ColorOfLine(values[0], values[1], values[2]));
                }
                break;
            }
            case "THICK":
                cursors.getCurrentCursor().setWidth((Double) parameters);
                break;
            case "LOOKAT": {
                if (parameters instanceof Integer) {
                    Cursor cursor = cursors.getCursor((Integer) parameters);
                    Point targetPos = cursor.getPosition();
                    Point newPos;

                    if (Axial) {
                        String symmetryPoints = (String) parameter;
                        if (symmetryPoints.contains("),(")) {
                            String[] pointStrings = symmetryPoints.split("\\),\\(");
                            Point p1 = parsePoint(pointStrings[0] + ")");
                            Point p2 = parsePoint("(" + pointStrings[1]);
                            newPos = axialSymmetry(targetPos, p1, p2);
                        } else {
                            newPos = targetPos;
                        }
                    } else {
                        String symmetryPoint = (String) parameter;
                        if (symmetryPoint.contains(",")) {
                            Point center = parsePoint(symmetryPoint);
                            newPos = centralSymmetry(targetPos, center);
                        } else {
                            newPos = targetPos;
                        }
                    }

                    double deltaX = newPos.getX() - cursors.getCurrentCursor().getPosition().getX();
                    double deltaY = newPos.getY() - cursors.getCurrentCursor().getPosition().getY();

                    double angleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));
                    cursors.getCurrentCursor().setAngle(angleToTarget);
                } else if (parameters instanceof String) {
                    String destinationPoint = (String) parameters;
                    Point destination = parsePoint(destinationPoint);
                    Point newPos;

                    if (Axial) {
                        String symmetryPoints = (String) parameter;
                        if (symmetryPoints.contains("),(")) {
                            String[] pointStrings = symmetryPoints.split("\\),\\(");
                            Point p1 = parsePoint(pointStrings[0] + ")");
                            Point p2 = parsePoint("(" + pointStrings[1]);
                            newPos = axialSymmetry(destination, p1, p2);
                        } else {
                            newPos = destination;
                        }
                    } else {
                        String symmetryPoint = (String) parameter;
                        if (symmetryPoint.contains(",")) {
                            Point center = parsePoint(symmetryPoint);
                            newPos = centralSymmetry(destination, center);
                        } else {
                            newPos = destination;
                        }
                    }

                    double deltaX = newPos.getX() - cursors.getCurrentCursor().getPosition().getX();
                    double deltaY = newPos.getY() - cursors.getCurrentCursor().getPosition().getY();

                    double angleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));
                    cursors.getCurrentCursor().setAngle(angleToTarget);
                }
                break;
            }
            case "CURSOR":
                cursors.createCursor((Integer) parameters);
                break;
            case "SELECT":
                cursors.selectCursor((Integer) parameters);
                break;
            case "REMOVE":
                cursors.removeCursor((Integer) parameters);
                break;
        }
    }

    /**
     * Axial Symetry
     * @param p
     * @param a
     * @param b
     * @return Point values
     */
    private static Point axialSymmetry(Point p, Point a, Point b) {
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
    private static Point centralSymmetry(Point p, Point c) {
        double x_s = 2 * c.getX() - p.getX();
        double y_s = 2 * c.getY() - p.getY();
        return new Point(x_s, y_s);
    }

    /**
     * Parse a String to a Point
     * @param pointString
     * @return New point
     */
    private Point parsePoint(String pointString) {
        pointString = pointString.replace("(", "").replace(")", "").trim();
        String[] coordinates = pointString.split(",");
        double x = Double.parseDouble(coordinates[0]);
        double y = Double.parseDouble(coordinates[1]);
        return new Point(x, y);
    }

    /**
     * Check if the instruction is valid
     * @return true if it's valid, otherwise false
     * @throws ErrorLogger
     */
    public boolean isValid() throws ErrorLogger{
        Object resolvedParameter = resolveParameter(parameters);
        boolean res = false;
        try {
            switch (type) {
                case "FWD":
                case "BWD":
                case "TURN":
                    //Parameter value must be a double
                    if (resolvedParameter instanceof Double) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Double");
                    }
                    break;
                case "SHOW":
                case "HIDE":
                    //No parameter required
                    res = true;
                    break;
                case "MOV":
                    //Parameter value must be a Point
                    if (resolvedParameter instanceof String) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Point");
                    }
                    break;
                case "POS":
                    if (resolvedParameter instanceof String) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Point");
                    }
                    break;
                case "PRESS":
                    //Parameter value must be a double between 0 and 1 or a String representing a percentage
                    if (resolvedParameter instanceof Double || resolvedParameter instanceof String) {
                        if (resolvedParameter instanceof Double) {
                            double value = (Double)resolvedParameter;
                            if (value >= 0.0 && value <= 1.0) {
                                res = true;
                            } else {
                                throw new ErrorLogger("parameter needs to be between 0 and 1");
                            }
                        }
                        else if (resolvedParameter instanceof String) {
                            String value = (String)resolvedParameter;
                            // Check if the string matches the format of a percentage (e.g., "50%")
                            Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?%$");
                            Matcher matcher = pattern.matcher(value);
                            if (matcher.matches()) {
                                // Percentage to numeric value and check if it's between 0 and 100
                                double numericValue = Double.parseDouble(value.substring(0, value.length() - 1));
                                if (numericValue >= 0 && numericValue <= 100) {
                                    res = true;
                                } else {
                                    throw new ErrorLogger("parameter needs to be between 0 and 100");
                                }
                            }
                        } else {
                            throw new ErrorLogger("parameter needs to be a Double or String");
                        }
                    }
                    break;
                case "COLOR":
                    // Parameter must represent a color in various formats
                    if (resolvedParameter instanceof String && ((String) resolvedParameter).contains("#")){
                        //Hexadecimal
                        if(((String) resolvedParameter).substring(1).matches("[0-9A-Fa-f]+") && ((String) resolvedParameter).charAt(0) == '#' && ((String) resolvedParameter).length() == 7){
                            res = true;
                        } else {
                            throw new ErrorLogger("parameter needs to be a String like #RRGGBB with RRGGBB between 0 and F");
                        }
                    } else if (resolvedParameter instanceof String && ((String) resolvedParameter).contains(",") && !((String) resolvedParameter).contains(".")) {
                        //RGB integer
                        String[] valueStrings = ((String) resolvedParameter).split(",");
                        if (valueStrings.length == 3) {
                            int[] values = new int[valueStrings.length];
                            for (int i = 0; i < valueStrings.length; i++) {
                                if(variableContext.containsVariable(valueStrings[i]) && variableContext.getVariable(valueStrings[i]) instanceof Integer){
                                    values[i]=(Integer)variableContext.getVariable(valueStrings[i]);
                                }
                                else {
                                    values[i] = Integer.parseInt(valueStrings[i]);
                                }
                            }
                            if (values[0] >= 0 && values[0] <= 255 && values[1] >= 0 && values[1] <= 255 && values[2] >= 0 && values[2] <= 255) {
                                res = true;
                            } else {
                                throw new ErrorLogger("parameter needs to be a String like R,G,B with R,G,B between 0 and 255");
                            }
                        } else {
                            throw new ErrorLogger("parameter needs to be a String like R,G,B with R,G,B between 0 and 255");
                        }
                    } else if (resolvedParameter instanceof String && ((String) resolvedParameter).contains(",") && ((String) resolvedParameter).contains(".")) {
                        //RGB double format
                        String[] valueStrings = ((String) resolvedParameter).split(",");
                        if (valueStrings.length == 3) {
                            double[] values = new double[valueStrings.length];
                            for (int i = 0; i < valueStrings.length; i++) {
                                if(variableContext.containsVariable(valueStrings[i]) && variableContext.getVariable(valueStrings[i]) instanceof Double){
                                    values[i]=(Double)variableContext.getVariable(valueStrings[i]);
                                }
                                else {
                                    values[i]= Double.parseDouble(valueStrings[i]);
                                }
                            }
                            if (values[0] >= 0 && values[0] <= 1 && values[1] >= 0 && values[1] <= 1 && values[2] >= 0 && values[2] <= 1) {
                                res = true;
                            } else {
                                throw new ErrorLogger("parameter needs to be a String like R,G,B with R,G,B between 0 and 1");
                            }
                        } else {
                            throw new ErrorLogger("parameter needs to be a String like R,G,B with R,G,B between 0 and 1");
                        }
                    } else {
                        throw new ErrorLogger("parameter needs to be a String like #RRGGBB or R,G,B or R,G,B with R,G,B between 0 and 255 or R,G,B with R,G,B between 0 and 1");
                    }
                    break;
                case "THICK":
                    // Parameter must be a Double
                    if (resolvedParameter instanceof Double) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Double");
                    }
                    break;
                case "LOOKAT":
                    // Parameter must be a String representing a Point or an Interger representing a Cursor ID
                    if (resolvedParameter instanceof String || resolvedParameter instanceof Integer) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Cursor or Point");
                    }
                    break;
                case "CURSOR":
                    // Parameter must be an Integer representing a non-existent Cursor ID
                    if (resolvedParameter instanceof Integer) {
                        int cursorID = (Integer) resolvedParameter;
                        if(!cursors.isCursorIdExists(cursorID)){
                            res = true;
                        } else {
                            throw new ErrorLogger("Cursor ID already exists");
                        }
                    } else {
                        throw new ErrorLogger("parameter needs to be a Integer");
                    }
                    break;
                case "SELECT":
                    // Parameter must be an Integer representing an existing Cursor ID
                    if (resolvedParameter instanceof Integer) {
                        int cursorID = (Integer) resolvedParameter;
                        if(cursors.isCursorIdExists(cursorID)){
                            res = true;
                        } else {
                            throw new ErrorLogger("Cursor ID doesn't exist");
                        }
                    } else {
                        throw new ErrorLogger("parameter needs to be a Integer");
                    }
                    break;
                case "REMOVE":
                    // Parameter must be an Integer representing an existing Cursor ID
                    if (resolvedParameter instanceof Integer) {
                        int cursorID = (Integer) resolvedParameter;
                        if(cursors.isCursorIdExists(cursorID)){
                            res = true;
                        } else {
                            throw new ErrorLogger("Cursor ID doesn't exist");
                        }
                    } else {
                        throw new ErrorLogger("parameter needs to be a Integer");
                    }
                    break;
                default:
                    throw new ErrorLogger("type of instruction is unknown :" + type);
            }
            return (res);
        }
        catch (ErrorLogger e){
            throw e;
        }
        catch (Exception e){
            if (e.getStackTrace().length > 0) {
                System.out.println(e.getStackTrace()[0]);
            }
        }
        return res;
    }
}

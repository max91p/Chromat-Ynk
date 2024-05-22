package com.example;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;

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
        if (parameter instanceof String && variableContext.containsVariable((String) parameter)) {
            return variableContext.getVariable((String) parameter);
        }
        return parameter;
    }


    /**
     * Executes the instruction on the given cursor.
     */

    @Override
    public void execute(){
        Object resolvedParameter = resolveParameter(parameters);
        switch (type) {
            case "FWD":
                cursors.getCurrentCursor().moveForward((Double)resolvedParameter);
                break;
            case "BWD":
                cursors.getCurrentCursor().moveBackward((Double)resolvedParameter);
                break;
            case "TURN":
                cursors.getCurrentCursor().turn((Double)resolvedParameter);
                break;
            case "SHOW":
                cursors.getCurrentCursor().setVisible(true);
                break;
            case "HIDE":
                cursors.getCurrentCursor().setVisible(false);
                break;
            case "MOV":
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
                if (resolvedParameter instanceof Double) {
                    cursors.getCurrentCursor().setOpacity((Double)resolvedParameter);
                }
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

                cursors.getCurrentCursor().setOpacity((Double)resolvedParameter);
                break;
            case "COLOR":
                String valueString = (String)resolvedParameter;
                if(valueString.contains(",") && !valueString.contains(".")){
                    String[] valueStrings = valueString.split(",");
                    int[] values = new int[valueStrings.length];
                    for (int i = 0; i < valueStrings.length; i++) {
                        values[i]= Integer.parseInt(valueStrings[i]);
                    }
                    cursors.getCurrentCursor().setColor(new ColorOfLine(values[0], values[1], values[2]));
                }
                else if (valueString.contains("#")){
                    cursors.getCurrentCursor().setColor(new ColorOfLine(valueString));
                }
                else if(valueString.contains(",") && valueString.contains(".")){
                    String[] valueStrings = valueString.split(",");
                    double[] values = new double[valueStrings.length];
                    for (int i = 0; i < valueStrings.length; i++) {
                        values[i]= Double.parseDouble(valueStrings[i]);
                    }
                    cursors.getCurrentCursor().setColor(new ColorOfLine(values[0], values[1], values[2]));
                }
                break;
            case "THICK":
                cursors.getCurrentCursor().setWidth((Double)resolvedParameter);
                break;
            case "LOOKAT":
                if (resolvedParameter instanceof Integer) {
                    Cursor cursor = cursors.getCursor((Integer)resolvedParameter);
                    double deltaX = cursor.getPosition().getX() - cursors.getCurrentCursor().getPosition().getX();
                    double deltaY = cursor.getPosition().getY() - cursors.getCurrentCursor().getPosition().getY();

                    double angleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));

                    cursors.getCurrentCursor().setAngle(angleToTarget);
                }
                if (resolvedParameter instanceof String) {
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
                cursors.createCursor((Integer)resolvedParameter);
                break;
            case "SELECT":
                cursors.selectCursor((Integer)resolvedParameter);
                break;
            case "REMOVE":
                cursors.removeCursor((Integer)resolvedParameter);
                break;

        }
    }

    public void mirrorExecute(boolean Axial,Object parameters){
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
            case "MOV":
                String valuePoint = (String) parameters;
                if (Axial) {
                    // Handle axial symmetry with two points
                    if (valuePoint.contains("),(")) {
                        String[] pointStrings = valuePoint.split("\\),\\(");
                        Point p1 = parsePoint(pointStrings[0]);
                        Point p2 = parsePoint(pointStrings[1]);
                        Point cursorPos = cursors.getCurrentCursor().getPosition();
                        Point newPos = axialSymmetry(cursorPos, p1, p2);
                        cursors.getCurrentCursor().move(newPos.getX(), newPos.getY());
                    }
                } else {
                    // Handle central symmetry with one point
                    if (valuePoint.contains(",")) {
                        Point center = parsePoint(valuePoint);
                        Point cursorPos = cursors.getCurrentCursor().getPosition();
                        Point newPos = centralSymmetry(cursorPos, center);
                        cursors.getCurrentCursor().move(newPos.getX(), newPos.getY());
                    }
                }
                break;
            case "POS":
                String valuePointPos = (String) parameters;
                if (Axial) {
                    // Handle axial symmetry with two points
                    if (valuePointPos.contains("),(")) {
                        String[] pointStrings = valuePointPos.split("\\),\\(");
                        Point p1 = parsePoint(pointStrings[0]);
                        Point p2 = parsePoint(pointStrings[1]);
                        Point cursorPos = cursors.getCurrentCursor().getPosition();
                        Point newPos = axialSymmetry(cursorPos, p1, p2);
                        cursors.getCurrentCursor().setPosition(newPos.getX(), newPos.getY());
                    }
                } else {
                    // Handle central symmetry with one point
                    if (valuePointPos.contains(",")) {
                        Point center = parsePoint(valuePointPos);
                        Point cursorPos = cursors.getCurrentCursor().getPosition();
                        Point newPos = centralSymmetry(cursorPos, center);
                        cursors.getCurrentCursor().setPosition(newPos.getX(), newPos.getY());
                    }
                }
                break;
            case "PRESS":
                if (parameters instanceof Double) {
                    cursors.getCurrentCursor().setOpacity((Double) parameters);
                }
                if (parameters instanceof String) {
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

                cursors.getCurrentCursor().setOpacity((Double) parameters);
                break;
            case "COLOR":
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
            case "THICK":
                cursors.getCurrentCursor().setWidth((Double) parameters);
                break;
            case "LOOKAT":
                if (parameters instanceof Integer) {
                    Cursor cursor = cursors.getCursor((Integer) parameters);
                    double deltaX = cursor.getPosition().getX() - cursors.getCurrentCursor().getPosition().getX();
                    double deltaY = cursor.getPosition().getY() - cursors.getCurrentCursor().getPosition().getY();

                    double angleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));

                    cursors.getCurrentCursor().setAngle(angleToTarget);
                }
                if (parameters instanceof String) {
                    String valuePointLook = (String) parameters;
                    if (valuePointLook.contains(",")) {
                        String[] valueCo = valuePointLook.split(",");
                        double[] values = new double[valueCo.length];
                        for (int i = 0; i < valueCo.length; i++) {
                            values[i] = Double.parseDouble(valueCo[i]);
                        }
                        Point point = new Point(values[0], values[1]);

                        // Symétrie axiale du point de regard
                        if (Axial) {
                            if (valuePointLook.contains(",")) {
                                String[] pointStrings = valuePointLook.split(",");
                                Point p1 = parsePoint(pointStrings[0]);
                                Point p2 = parsePoint(pointStrings[1]);
                                Point cursorPos = cursors.getCurrentCursor().getPosition();
                                Point newPos = axialSymmetry(cursorPos, p1, p2);
                                point = newPos;
                            }
                        }

                        // Symétrie centrale du point de regard
                        if (!Axial) {
                            if (valuePointLook.contains(",")) {
                                Point center = parsePoint(valuePointLook);
                                Point cursorPos = cursors.getCurrentCursor().getPosition();
                                Point newPos = centralSymmetry(cursorPos, center);
                                point = newPos;
                            }
                        }

                        double deltaX = point.getX() - cursors.getCurrentCursor().getPosition().getX();
                        double deltaY = point.getY() - cursors.getCurrentCursor().getPosition().getY();

                        double angleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));

                        cursors.getCurrentCursor().setAngle(angleToTarget);
                    }
                }
                break;
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
        pointString = pointString.replace("(", "").replace(")", "").trim();
        String[] coordinates = pointString.split(",");
        double x = Double.parseDouble(coordinates[0]);
        double y = Double.parseDouble(coordinates[1]);
        return new Point(x, y);
    }

    public boolean isValid() throws ErrorLogger{
        Object resolvedParameter = resolveParameter(parameters);
        boolean res = false;
        try {
            switch (type) {
                case "FWD":
                case "BWD":
                case "TURN":
                    if (resolvedParameter instanceof Double) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Double");
                    }
                    break;
                case "SHOW":
                case "HIDE":
                    res = true;
                    break;
                case "MOV":
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
                    if (resolvedParameter instanceof Double || resolvedParameter instanceof String) {
                        if (resolvedParameter instanceof Double) {
                            Double value = (Double) resolvedParameter;
                            if (value >= 0 && value <= 1) {
                                res = true;
                            } else {
                                throw new ErrorLogger("parameter needs to be between 0 and 1");
                            }
                        }
                        if (resolvedParameter instanceof String) {
                            String value = (String) resolvedParameter;
                            // Vérification si la valeur correspond au format numérique avec un % à la fin
                            Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?%$");
                            Matcher matcher = pattern.matcher(value);
                            if (matcher.matches()) {
                                // La valeur est au format numérique avec un % à la fin
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
                    if (resolvedParameter instanceof String) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a ColorOfLine");
                    }
                    break;
                case "THICK":
                    if (resolvedParameter instanceof Double) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Double");
                    }
                    break;
                case "LOOKAT":
                    if (resolvedParameter instanceof String || resolvedParameter instanceof Integer) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Cursor or Point");
                    }
                    break;
                case "CURSOR":
                    if (resolvedParameter instanceof Integer) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Integer");
                    }
                    break;
                case "SELECT":
                    if (resolvedParameter instanceof Integer) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Integer");
                    }
                    break;
                case "REMOVE":
                    if (resolvedParameter instanceof Integer) {
                        res = true;
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
            throw new ErrorLogger("Error during execution of the instruction :" + type , e);
        }
    }
}

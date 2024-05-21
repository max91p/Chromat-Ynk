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
    private Scene scene;

    /**
     * Constructs a new SimpleInstruction with the specified type and parameters.
     *
     * @param type       the type of the instruction
     * @param parameters the parameters of the instruction
     */

    public SimpleInstruction(String type, Object parameters, CursorManager cursors,Scene scene) {
        this.type = type;
        this.parameters = parameters;
        this.cursors = cursors;
        this.scene=scene;
    }

    /**
     * Constructs a new SimpleInstruction with the specified type and parameters.
     *
     * @param type the type of the instruction
     */

    public SimpleInstruction(String type, CursorManager cursors, Scene scene) {
        this.type = type;
        this.parameters = null;
        this.cursors = cursors;
        this.scene=scene;
    }

    /**
     * Executes the instruction on the given cursor.
     */

    @Override
    public void execute(){
        switch (type) {
            case "FWD":
                cursors.getCurrentCursor().moveForward((Double)parameters);
                break;
            case "BWD":
                cursors.getCurrentCursor().moveBackward((Double)parameters);
                break;
            case "TURN":
                cursors.getCurrentCursor().turn((Double)parameters);
                break;
            case "SHOW":
                cursors.getCurrentCursor().setVisible(true);
                break;
            case "HIDE":
                cursors.getCurrentCursor().setVisible(false);
                break;
            case "MOV":
                cursors.getCurrentCursor().setPosition((Point)parameters);
                break;
            case "POS":
                cursors.getCurrentCursor().setPosition((Point)parameters);
                break;
            case "PRESS":
                if (parameters instanceof Double) {
                    cursors.getCurrentCursor().setOpacity((Double)parameters);
                }
                if (parameters instanceof String) {
                    String value = (String)parameters;
                    // Vérification si la valeur correspond au format numérique avec un % à la fin
                    Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?%$");
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.matches()) {
                        // La valeur est au format numérique avec un % à la fin
                        double numericValue = Double.parseDouble(value.substring(0, value.length() - 1));
                        cursors.getCurrentCursor().setOpacity(numericValue / 100);
                    }
                }

                cursors.getCurrentCursor().setOpacity((Double)parameters);
                break;
            case "COLOR":
                String valueString = (String)parameters;
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
                cursors.getCurrentCursor().setWidth((Double)parameters);
                break;
            case "LOOKAT":
                if (parameters instanceof Cursor) {
                    Cursor cursor = (Cursor) parameters;
                    double deltaX = cursor.getPosition().getX() - cursors.getCurrentCursor().getPosition().getX();
                    double deltaY = cursor.getPosition().getY() - cursors.getCurrentCursor().getPosition().getY();

                    double angleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));

                    cursors.getCurrentCursor().setAngle(angleToTarget);
                }
                if (parameters instanceof Point) {
                    Point point = (Point) parameters;
                    double deltaX = point.getX() - cursors.getCurrentCursor().getPosition().getX();
                    double deltaY = point.getY() - cursors.getCurrentCursor().getPosition().getY();

                    double angleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));

                    cursors.getCurrentCursor().setAngle(angleToTarget);
                }
                break;
            case "CURSOR":
                cursors.addCursor((Integer)parameters);
                break;
            case "SELECT":
                cursors.selectCursor((Integer)parameters);
                break;
            case "REMOVE":
                cursors.removeCursor((Integer)parameters);
                break;

        }
    }


    public boolean isValid() throws ErrorLogger{
        boolean res = false;
        try {
            switch (type) {
                case "FWD":
                    if (parameters instanceof Double) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Double");
                    }
                    break;
                case "BWD":
                    if (parameters instanceof Double) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Double");
                    }
                    break;
                case "TURN":
                    if (parameters instanceof Double) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Double");
                    }
                    break;
                case "SHOW":
                    res = true;
                    break;
                case "HIDE":
                    res = true;
                    break;
                case "MOV":
                    if (parameters instanceof Point) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Point");
                    }
                    break;
                case "POS":
                    if (parameters instanceof Point) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Point");
                    }
                    break;
                case "PRESS":
                    if (parameters instanceof Double || parameters instanceof String) {
                        if (parameters instanceof Double) {
                            Double value = (Double) parameters;
                            if (value >= 0 && value <= 1) {
                                res = true;
                            } else {
                                throw new ErrorLogger("parameter needs to be between 0 and 1");
                            }
                        }
                        if (parameters instanceof String) {
                            String value = (String) parameters;
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
                    if (parameters instanceof String) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a ColorOfLine");
                    }
                    break;
                case "THICK":
                    if (parameters instanceof Double) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Double");
                    }
                    break;
                case "LOOKAT":
                    if (parameters instanceof Point || parameters instanceof Cursor) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Cursor or Point");
                    }
                    break;
                case "CURSOR":
                    if (parameters instanceof Integer) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Integer");
                    }
                    break;
                case "SELECT":
                    if (parameters instanceof Integer) {
                        res = true;
                    } else {
                        throw new ErrorLogger("parameter needs to be a Integer");
                    }
                    break;
                case "REMOVE":
                    if (parameters instanceof Integer) {
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

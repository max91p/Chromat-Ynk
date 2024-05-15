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
    public void execute() {
        switch (type) {
            case "FWD":
                cursors.getCurrentCursor().moveForward((Double)parameters,scene);
                break;
            case "BWD":
                cursors.getCurrentCursor().moveBackward((Double)parameters,scene);
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
                    cursors.getCurrentCursor().setPress((Double)parameters);
                }
                if (parameters instanceof String) {
                    String value = (String)parameters;
                    // Vérification si la valeur correspond au format numérique avec un % à la fin
                    Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?%$");
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.matches()) {
                        // La valeur est au format numérique avec un % à la fin
                        double numericValue = Double.parseDouble(value.substring(0, value.length() - 1));
                        cursors.getCurrentCursor().setPress(numericValue / 100);
                    }
                }

                cursors.getCurrentCursor().setPress((Double)parameters);
                break;
            case "COLOR":
                cursors.getCurrentCursor().setColor((ColorOfLine)parameters);
                break;
            case "THICK":
                cursors.getCurrentCursor().setThick((Double)parameters);
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


    public boolean isValid(){
        boolean res = false;
        switch (type) {
            case "FWD":
                if (parameters instanceof Double) {
                    res = true;
                }
                break;
            case "BWD":
                if (parameters instanceof Double) {
                    res = true;
                }
                break;
            case "TURN":
                if (parameters instanceof Integer) {
                    res = true;
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
                }
                break;
            case "POS":
                if (parameters instanceof Point) {
                    res = true;
                }
                break;
            case "PRESS":
                if (parameters instanceof Double || parameters instanceof String) {
                    if (parameters instanceof Double) {
                        Double value = (Double) parameters;
                        if (value >= 0 && value <= 1) {
                            res = true;
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
                            }
                        }
                    }
                }
                break;
            case "COLOR":
                if (parameters instanceof ColorOfLine) {
                    res = true;
                }
                break;
            case "THICK":
                if (parameters instanceof Double) {
                    res = true;
                }
                break;
            case "LOOKAT":
                if (parameters instanceof Point || parameters instanceof Cursor) {
                    res = true;
                }
                break;
            case "CURSOR":
                if (parameters instanceof Integer) {
                    res = true;
                }
                break;
            case "SELECT":
                if (parameters instanceof Integer) {
                    res = true;
                }
                break;
            case "REMOVE":
                if (parameters instanceof Integer) {
                    res = true;
                }
                break;
        }
        return (res);
    }
}

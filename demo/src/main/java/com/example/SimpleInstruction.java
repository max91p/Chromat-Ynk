package com.example;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import java.awt.geom.Point2D;

/**
 * Represents a simple drawing instruction.
 */
public class SimpleInstruction extends Instruction {
    private String type;
    private Objects parameters;
    private Cursor cursor;
    /**
     * Constructs a new SimpleInstruction with the specified type and parameters.
     *
     * @param type       the type of the instruction
     * @param parameters the parameters of the instruction
     */
    public SimpleInstruction(String type, Objects parameters,Cursor cursor) {
        this.type = type;
        this.parameters = parameters;
        this.cursor = cursor;
    }
    /**
     * Constructs a new SimpleInstruction with the specified type and parameters.
     *
     * @param type       the type of the instruction
     */
    public SimpleInstruction(String type,Cursor cursor) {
        this.type = type;
        this.parameters = 0;
        this.cursor = cursor;
    }

    /**
     * Executes the instruction on the given cursor.
     *
     * @param cursor the cursor to execute the instruction on
     */
    @Override
    public void execute(Cursor cursor) {
        switch (type) {
            case "FWD":
                cursor.moveForward(parameters);
                break;
            case "BWD":
                cursor.moveBackward(parameters);
                break;
            case "TURN":
                cursor.turn(parameters);
                break;
            case "SHOW":
                Polygon cursorTriangle = new Polygon(
                        cursor.getPosition().getX(), cursor.getPosition().getY(),
                        cursor.getPosition().getX() + 10 * Math.cos(Math.toRadians(cursor.getAngle() - 150)), cursor.getPosition().getY() + 10 * Math.sin(Math.toRadians(cursor.getAngle() - 150)),
                        cursor.getPosition().getX() + 10 * Math.cos(Math.toRadians(cursor.getAngle() + 150)), cursor.getPosition().getY() + 10 * Math.sin(Math.toRadians(cursor.getAngle() + 150))
                );
                /**
                 * Cursor triangle color
                 */
                cursorTriangle.setFill(Color.RED);
                /**
                 * Add cursor triangle to group
                 */
                root.getChildren().add(cursorTriangle);
                break;
            case "HIDE":
                Polygon cursorTriangle = new Polygon(
                        cursor.getPosition().getX(), cursor.getPosition().getY(),
                        cursor.getPosition().getX() + 10 * Math.cos(Math.toRadians(cursor.getAngle() - 150)), cursor.getPosition().getY() + 10 * Math.sin(Math.toRadians(cursor.getAngle() - 150)),
                        cursor.getPosition().getX() + 10 * Math.cos(Math.toRadians(cursor.getAngle() + 150)), cursor.getPosition().getY() + 10 * Math.sin(Math.toRadians(cursor.getAngle() + 150))
                );
                /**
                 * Remove cursor triangle to group
                 */
                root.getChildren().remove(cursorTriangle);
                break;
            case "MOV":
                cursor.setPosition(parameters);
                break;
            case "POS":
                cursor.setPosition(parameters);
                break;
            case "PRESS":
                if (parameters instanceof Double) {
                    cursor.setPress(parameters);
                }
                if (parameters instanceof String) {
                    String value = (String) parameters;
                    // Vérification si la valeur correspond au format numérique avec un % à la fin
                    Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?%$");
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.matches()) {
                        // La valeur est au format numérique avec un % à la fin
                        double numericValue = Double.parseDouble(value.substring(0, value.length() - 1));
                        cursor.setPress(numericValue/100);
                        }
                    }
                }
                cursor.setPress(parameters);
                break;
            case "COLOR":
                cursor.setColor(parameters);
                break;
            case "THICK":
                cursor.setThick(parameters);
                break;
            case "LOOKAT":
                 if (parameters instanceof Cursor) {
                     double deltaX = parameters.getPosition().getX() - cursor.getPosition().getX();
                     double deltaY = parameters.getPosition().getY() - cursor.getPosition().getY();

                     double angleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));

                     cursor.setAngle(angleToTarget);
                 }
                 if (parameters instanceof Point) {
                     double deltaX = parameters.getX() - cursor.getPosition().getX();
                     double deltaY = parameters.getY() - cursor.getPosition().getY();

                     double angleToTarget = Math.toDegrees(Math.atan2(deltaY, deltaX));

                     cursor.setAngle(angleToTarget);
                 }
                break;
            case "CURSOR":
                cursor.moveForward(parameters);
                break;
            case "SELECT":
                cursor.moveForward(parameters);
                break;
            case "REMOVE":
                cursor.moveForward(parameters);
                break;
        }
    }
    public boolean isValid(){
        res = false;
        switch (type) {
            case "FWD":
                if (parameters instanceof Int) {
                    res = true;
                }
                break;
            case "BWD":
                if (parameters instanceof Int) {
                    res = true;
                }
                break;
            case "TURN":
                if (parameters instanceof Int) {
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
                                res = true
                            }
                        }
                    }
                }
                break;
            case "COLOR":
                if (parameters instanceof Color) {
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
                if (parameters instanceof Cursor) {
                    res = true;
                }
                break;
            case "SELECT":
                if (parameters instanceof String) {
                    res = true;
                }
                break;
            case "REMOVE":
                if (parameters instanceof Cursor) {
                    res = true;
                }
                break;
        }
        return (res);
    }
}
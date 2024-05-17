package com.example;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class Cursor {
    private Point position; // Position of the cursor
    private double angle; // Orientation of the cursor
    private ColorOfLine color; //Color of the line
    private double thick; //thickness of the line
    private double press; //pressure of the line
    private int id; //id of the cursor
    private boolean visible; //visibility of the cursor
    private List<SimpleInstruction> history; //history of the cursor


    // Constructors with all the parameters
    public Cursor(Point position, double angle, ColorOfLine color, double thick, double press, int id) {
        if (position == null) {
            throw new IllegalArgumentException("La position ne peut pas être nulle");
        } else if (angle < 0 || angle >= 360) {
            throw new IllegalArgumentException("L'angle doit être compris entre 0 et 360");
        } else {
            this.position = position;
            this.angle = angle;
            this.color = color;
            this.thick = thick;
            this.press = press;
            this.id = id;
            this.visible = true;
            this.history= new ArrayList<>();
        }
    }

    // Constructors with all the parameters except the color
    public Cursor(Point position, double angle, int id) {
        if (position == null) {
            throw new IllegalArgumentException("La position ne peut pas être nulle");
        }
        else if (angle < 0 || angle >= 360) {
            throw new IllegalArgumentException("L'angle doit être compris entre 0 et 360");
        }
        else{
            this.position = position;
            this.angle = angle;
            this.color = new ColorOfLine(0,0,0);
            this.thick = 1;
            this.press = 1;
            this.id = id;
            this.visible = true;
            this.history= new ArrayList<>();

        }
    }

    public Cursor(int id) {
                this.position = new Point();
                this.angle = 0;
                this.color = new ColorOfLine(0,0,0);
                this.thick = 1;
                this.press = 1;
                this.id = id;
                this.visible = true;
                this.history= new ArrayList<>();

    }

    public Cursor(Point point, int id) {
        if (point == null) {
            throw new IllegalArgumentException("La position ne peut pas être nulle");
        }
        else{
            this.position = new Point(point.getX(), point.getY());
            this.angle = 0;
            this.color = new ColorOfLine(0,0,0);
            this.thick = 1;
            this.press = 1;
            this.id = id;
            this.visible = true;
            this.history= new ArrayList<>();
        }
    }

    // Method to move the cursor forward
    public void moveForward(double distance, Scene scene) {
        Point oldPosition = new Point(position.getX(), position.getY());
        //Move the cursor to the new position according to the distance
        position.setX(position.getX() + distance * Math.cos(Math.toRadians(angle)));
        position.setY(position.getY() + distance * Math.sin(Math.toRadians(angle)));
        DrawingCanvas draw = new DrawingCanvas(scene, oldPosition,position);
        draw.drawLine();
    }

    // Method to move the cursor backward
    public void moveBackward(double distance, Scene scene) {
        Point oldPosition = new Point(position.getX(), position.getY());
        //Move the cursor to the new position according to the distance
        position.setX(position.getX() - distance * Math.cos(Math.toRadians(angle)));
        position.setY(position.getY() - distance * Math.sin(Math.toRadians(angle)));
        DrawingCanvas draw = new DrawingCanvas(scene, oldPosition,position);
        draw.drawLine();
    }

    // Method to turn the cursor
    public void turn(double degrees) {
        if (angle + degrees < 0) {
            angle = 360 + (angle + degrees);
        } else if (angle + degrees >= 360) {
            angle = (angle + degrees) - 360;
        } else {
            angle += degrees;
        }
    }

    // Getters et Setters
    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        if (angle < 0 || angle >= 360) {
            throw new IllegalArgumentException("L'angle doit être compris entre 0 et 360");
        }
        this.angle = angle;
    }

    public void mimic(Cursor other) {
        this.position = new Point(other.position.getX(), other.position.getY());
        this.angle = other.angle;
        this.thick = other.thick;
        this.color = other.color;
        this.press = other.press;
    }

    public ColorOfLine getColor() {
        return color;
    }

    public void setColor(ColorOfLine color){ this.color=color; }

    public double getThick(){ return thick; }

    public void setThick(double thick) { this.thick = thick; }

    public double getPress() { return press; }

    public void setPress(double press) { this.press = press; }

    public int getId() { return id; }

    public void setId (int id) { this.id = id; }

    public void setVisible (boolean visible) { this.visible = visible;}

    public boolean getVisible () { return visible;}
    public List<SimpleInstruction> getHistory(){ return history;}

    @Override
    public String toString() {
        return "Le curseur "+ id +" est à la position " + position + " et est orienté à " + angle + " degrés";
    }

}
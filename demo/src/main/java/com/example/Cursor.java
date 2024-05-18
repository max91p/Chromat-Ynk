package com.example;

import javafx.scene.Scene;

public class Cursor {
    //Position for the cursor
    private Point position;
    //Orientation for the cursor
    private double angle;
    private ColorOfLine color;
    private double width;
    private double opacity;
    private int id;
    private Scene scene;

    // Constructeur
    public Cursor(Point position, double angle, ColorOfLine color, double thick, double press, int id, Scene scene) {
        if (angle < 0 || angle >= 360) {
            throw new IllegalArgumentException("L'angle doit être compris entre 0 et 360");
        } else {
            this.position = position;
            this.angle = angle;
            this.color = color;
            this.width = thick;
            this.opacity = press;
            this.id = id;
            this.scene=scene;
        }
    }
    public Cursor(Point position, double angle, int id, Scene scene) {
        if (angle < 0 || angle >= 360) {
            throw new IllegalArgumentException("L'angle doit être compris entre 0 et 360");
        }
        else{
            this.position = position;
            this.angle = angle;
            this.color = new ColorOfLine(0,0,0);
            this.width = 1;
            this.opacity = 1;
            this.id = id;
            this.scene=scene;
        }
    }

    public Cursor(int id,Scene scene) {
        if (angle < 0 || angle >= 360) {
            throw new IllegalArgumentException("L'angle doit être compris entre 0 et 360");
        }
        else{
            this.position = new Point();
            this.angle = 0;
            this.color = new ColorOfLine(0,0,0);
            this.width = 1;
            this.opacity= 1;
            this.id = id;
            this.scene=scene;
        }
    }

    public Cursor(Point point, int id, Scene scene) {
        if (angle < 0 || angle >= 360) {
            throw new IllegalArgumentException("L'angle doit être compris entre 0 et 360");
        }
        else{
            this.position = new Point(point.getX(), point.getY());
            this.angle = 0;
            this.color = new ColorOfLine(0,0,0);
            this.width = 1;
            this.opacity = 1;
            this.id = id;
            this.scene=scene;
        }
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
        DrawingCanvas draw = new DrawingCanvas(this.scene);
        draw.drawPosCursor(this);
    }

    public void moveForward(double distance) {
        Point oldPosition = new Point(position.getX(), position.getY());
        //Move the cursor to the new position according to the distance
        position.setX(position.getX() + distance * Math.cos(Math.toRadians(angle)));
        position.setY(position.getY() + distance * Math.sin(Math.toRadians(angle)));
        //Create a drawing canva to draw the line
        DrawingCanvas draw = new DrawingCanvas(this.scene, oldPosition,this.getPosition());
        draw.drawLine(this.getWidth(),this.getOpacity(),this.getColor());
        draw.drawCursor(this);
    }
    public void moveBackward(double distance) {
        Point oldPosition = new Point(position.getX(), position.getY());
        //Move the cursor to the new position according to the distance
        position.setX(position.getX() - distance * Math.cos(Math.toRadians(angle)));
        position.setY(position.getY() - distance * Math.sin(Math.toRadians(angle)));
        //Create a drawing canva to draw the line
        DrawingCanvas draw = new DrawingCanvas(this.scene, oldPosition,this.getPosition());
        draw.drawLine(this.getWidth(),this.getOpacity(),this.getColor());
        draw.drawCursor(this);
    }


    public void move(double x,double y){
        Point move = new Point(position.getX()+x, position.getY()+y);
        DrawingCanvas draw = new DrawingCanvas(this.scene,position,move);
        this.setPosition(move);
        draw.drawLine(this.getWidth(),this.getOpacity(),this.getColor());
        draw.drawCursor(this);
    }

    public void turn(double degrees) {
        angle += degrees;
        DrawingCanvas draw = new DrawingCanvas(this.scene);
        draw.drawCursor(this);
    }

    // Getters et Setters

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public ColorOfLine getColor() {
        return color;
    }

    public void setColor(ColorOfLine color) {
        this.color = color;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

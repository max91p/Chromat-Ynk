package com.example;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class Cursor {
    private Point position; // Position of the cursor
    private double angle; // Orientation of the cursor
    private ColorOfLine color; //Color of the line
    private double width;
    private double opacity;
    private int id;
    private Scene scene;
    private boolean visible; //visibility of the cursor

    // Constructors with all the parameters
    public Cursor(Point position, double angle, ColorOfLine color, double thick, double press, int id, Scene scene) {
        if (position == null) {
            throw new IllegalArgumentException("La position ne peut pas être nulle");
        } else if (angle < 0 || angle >= 360) {
            throw new IllegalArgumentException("L'angle doit être compris entre 0 et 360");
        } else {
            this.position = position;
            this.angle = angle;
            this.color = color;
            this.width = thick;
            this.opacity = press;
            this.id = id;
            this.scene=scene;
            this.visible = true;
        }
    }

    // Constructors with all the parameters except the color
    public Cursor(Point position, double angle, int id, Scene scene) {
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
            this.width = 1;
            this.opacity = 1;
            this.id = id;
            this.scene=scene;
            this.visible = true;
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
            this.visible = true;
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
            this.visible = true;
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

    public void setPosition(double x,double y) {
        this.position = new Point(x,y);
        DrawingCanvas draw = new DrawingCanvas(this.scene);
        draw.drawPosCursor(this);
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
        draw.drawPosCursor(this);
    }

    // Getters et Setters

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
        DrawingCanvas draw = new DrawingCanvas(this.scene);
        draw.drawPosCursor(this);
    }

    public void mimic(Cursor other) {
        this.position = new Point(other.position.getX(), other.position.getY());
        this.angle = other.angle;
        this.width = other.opacity;
        this.color = other.color;
        this.opacity = other.opacity;
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
    public void setVisible (boolean visible) {
        this.visible = visible;
        if (this.visible){
            DrawingCanvas draw = new DrawingCanvas(this.scene,position);
            draw.drawCursor(this);
        }
        else{
            Group rootf=(Group)scene.getRoot();
            int lastIndex = rootf.getChildren().size() - 1;
            if (lastIndex >= 0) {
                rootf.getChildren().remove(lastIndex);
            }
        }
    }

    public boolean getVisible () { return visible;}

    @Override
    public String toString() {
        return "Le curseur "+ id +" est à la position " + position + " et est orienté à " + angle + " degrés";
    }
}

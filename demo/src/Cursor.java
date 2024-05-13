package src;

import javafx.scene.Scene;

public class Cursor {
    //Position for the cursor
    private Point position;
    //Orientation for the cursor
    private double angle;
    private ColorOfLine color;
    private double width;
    private double opacity;

    // Constructeur
    public Cursor(Point pos, double angle) {
        this.position=pos;
        this.angle = angle;

    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void moveForward(double distance, Scene scene,double newAngle,double width, double opacity,ColorOfLine color) {
        Point oldPosition = new Point(position.getX(), position.getY());
        //Set the angle according to new angle
        this.setAngle(this.getAngle()+newAngle);
        //Move the cursor to the new position according to the distance
        position.setX(position.getX() + distance * Math.cos(Math.toRadians(angle)));
        position.setY(position.getY() + distance * Math.sin(Math.toRadians(angle)));
        //Create a drawing canva to draw the line
        DrawingCanvas draw = new DrawingCanvas(scene, oldPosition,position);
        draw.drawLine(width,opacity,color);
        draw.drawCursor(this);

    }
    public void moveForward(double distance, Scene scene,double width, double opacity,ColorOfLine color) {

    }
    public void moveBackward(double distance, Scene scene, double newAngle, double width, double opacity, ColorOfLine color) {
        Point oldPosition = new Point(position.getX(), position.getY());
        //Set the angle according to new angle
        this.setAngle(this.getAngle()+newAngle);
        //Move the cursor to the new position according to the distance
        position.setX(position.getX() - distance * Math.cos(Math.toRadians(angle)));
        position.setY(position.getY() - distance * Math.sin(Math.toRadians(angle)));
        //Create a drawing canva to draw the line
        DrawingCanvas draw = new DrawingCanvas(scene, oldPosition,position);
        draw.drawLine(width, opacity, color);
        draw.drawCursor(this);
    }



    public void turn(double degrees) {
        angle += degrees;
    }

    // Getters et Setters

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}

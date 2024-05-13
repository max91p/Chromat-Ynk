package src;

import javafx.scene.Scene;

public class Cursor {
    private Point position; // Position du curseur
    private double angle; // Orientation du curseur

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

    public void moveForward(double distance, Scene scene) {
        Point oldPosition = new Point(position.getX(), position.getY());
        //Move the cursor to the new position according to the distance
        position.setX(position.getX() + distance * Math.cos(Math.toRadians(angle)));
        position.setY(position.getY() + distance * Math.sin(Math.toRadians(angle)));
        DrawingCanvas draw = new DrawingCanvas(scene, oldPosition,position);
        draw.drawLine();
    }
    public void moveBackward(double distance, Scene scene) {
        Point oldPosition = new Point(position.getX(), position.getY());
        //Move the cursor to the new position according to the distance
        position.setX(position.getX() - distance * Math.cos(Math.toRadians(angle)));
        position.setY(position.getY() - distance * Math.sin(Math.toRadians(angle)));
        DrawingCanvas draw = new DrawingCanvas(scene, oldPosition,position);
        draw.drawLine();
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

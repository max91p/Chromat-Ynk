package com.example;

public class Cursor {
    private Point position; // Position du curseur
    private double angle; // Orientation du curseur

    // Constructeur
    public Cursor(Point position, double angle) {
        if (position == null) {
            throw new IllegalArgumentException("La position ne peut pas être nulle");
        }
        else if (angle < 0 || angle >= 360) {
            throw new IllegalArgumentException("L'angle doit être compris entre 0 et 360");
        }
        else{
            this.position = position;
            this.angle = angle;
        }
    }

    // Méthodes pour déplacer le curseur
    public void moveForward(double distance) {
        position.setX(position.getX() + distance * Math.cos(Math.toRadians(angle))); 
        position.setY(position.getY() + distance * Math.sin(Math.toRadians(angle)));
    }

    public void moveBackward(double distance) {
        position.setX(position.getX() - distance * Math.cos(Math.toRadians(angle)));
        position.setY(position.getY() - distance * Math.sin(Math.toRadians(angle)));
    }

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

    @Override
    public String toString() {
        return "Le curseur est à la position " + position + " et est orienté à " + angle + " degrés";
    }

}
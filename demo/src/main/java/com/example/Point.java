package com.example;

/**
 * Represents a point in a 2D space.
 */
public class Point {
    private double x;
    private double y;

    /**
     * Constructs a new point at the specified (x, y) location.
     *
     * @param x the X coordinate of the new point
     * @param y the Y coordinate of the new point
     */
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new point at the origin (0, 0).
     */
    public Point(){
        this(0, 0);
    }
    
    /**
     * Returns the X coordinate of this point.
     *
     * @return the X coordinate of this point
     */
    public double getX(){
        return x;
    }

    /**
     * Returns the Y coordinate of this point.
     *
     * @return the Y coordinate of this point
     */
    public double getY(){
        return y;
    }
    
    /**
     * Sets the X coordinate of this point.
     *
     * @param x the new X coordinate of this point
     */
    public void setX(double x){
        this.x = x;
    }

    /**
     * Sets the Y coordinate of this point.
     *
     * @param y the new Y coordinate of this point
     */
    public void setY(double y){
        this.y = y;
    }

    /**
     * Returns a string representation of this point.
     *
     * @return a string representation of this point
     */
    @Override
    public String toString() {
        return "Le point a pour coordonnées : x = " + this.getX() + " et y = " + this.getY();
    }
    
    /**
     * Calculates the distance between this point and the specified point.
     *
     * @param point the other point
     * @return the distance between this point and the specified point
     */
    public double distanceTo(Point point){
        return Math.sqrt(Math.pow(point.getX() - this.getX(), 2) + Math.pow(point.getY() - this.getY(), 2));
    }   
}
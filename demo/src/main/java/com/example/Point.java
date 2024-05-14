package com.example;

public class Point {
    private double x;
    private double y;

    //Constructors
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Point(){
        this(0, 0);
    }
    
    //Getters and setters
    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
    
    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    @Override
    public String toString() {
        return "Le point a pour coordonnées : x = " + this.getX() + " et y = " + this.getY();
    }
    
    //Method to calculate the distance between two points
    public double distanceTo(Point point){
        return Math.sqrt(Math.pow(point.getX() - this.getX(), 2) + Math.pow(point.getY() - this.getY(), 2));
    }   

}

package com.example;

public class Point {
    private double x;
    private double y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Point(){
        this(0, 0);
    }
    
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
    
    public double distanceTo(Point otherPoint){
        return Math.sqrt(Math.pow(otherPoint.getX() - this.getX(), 2) + Math.pow(otherPoint.getY() - this.getY(), 2));
    }

}

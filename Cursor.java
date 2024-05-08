public class Cursor {
    private double x; // Position X du curseur
    private double y; // Position Y du curseur
    private double angle; // Orientation du curseur

    // Constructeur
    public Cursor(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    // Méthodes pour déplacer le curseur
    public void moveForward(double distance) {
        x += distance * Math.cos(Math.toRadians(angle));
        y += distance * Math.sin(Math.toRadians(angle));
    }

    public void moveBackward(double distance) {
        x -= distance * Math.cos(Math.toRadians(angle));
        y -= distance * Math.sin(Math.toRadians(angle));
    }

    public void turn(double degrees) {
        angle += degrees;
    }

    // Getters et Setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}



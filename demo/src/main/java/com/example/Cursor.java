package com.example;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.Iterator;

/**
 * Class representing a cursor.
 */
public class Cursor {
    private Point position; // Position of the cursor
    private double angle; // Orientation of the cursor
    private ColorOfLine color; //Color of the line
    private double width; //Width of the line
    private double opacity; //Opacity of the line
    private int id; //Id of the cursor
    private Scene scene; //Scene where the cursor is
    private boolean visible; //visibility of the cursor

    /**
     * Constructor with all the parameters
     * @param position Position of the cursor
     * @param angle Orientation of the cursor
     * @param color Color of the line
     * @param thick Width of the line
     * @param press Opacity of the line
     * @param id Id of the cursor
     * @param scene Scene where the cursor is
     */
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

    /**
     * Constructor of the cursor with position, angle, id and scene
     * @param position Position of the cursor
     * @param angle Orientation of the cursor
     * @param id Id of the cursor
     * @param scene Scene where the cursor is
     */
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

    /**
     * Constructor of the cursor with id and scene
     * @param id Id of the cursor
     * @param scene Scene where the cursor is
     */
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

    /**
     * Constructor of the cursor with position, id and scene
     * @param point Position of the cursor
     * @param id Id of the cursor
     * @param scene Scene where the cursor is
     */
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

    /**
     * return the scene where the cursor is
     * @return the scene where the cursor is
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * set the scene where the cursor is
     * @param scene the scene where the cursor is
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * return the position of the cursor
     * @return the position of the cursor
     */
    public Point getPosition() {
        return position;
    }

    /**
     * set the position of the cursor with the cordonate x and y
     * @param x the x coordinate of the position
     * @param y the y coordinate of the position
     */
    public void setPosition(double x,double y) {
        this.position = new Point(x,y);
        DrawingCanvas draw = new DrawingCanvas(this.scene);
        draw.drawPosCursor(this);
    }

    /**
     * set the position of the cursor with a point
     * @param position the position of the cursor
     */
    public void setPosition(Point position) {
        this.position = position;
        DrawingCanvas draw = new DrawingCanvas(this.scene);
        draw.drawPosCursor(this);
    }

    /**
     * Move the cursor forward according to the distance
     * @param distance the distance to move
     */
    public void moveForward(double distance) {
        Point oldPosition = new Point(position.getX(), position.getY());
        //Move the cursor to the new position according to the distance
        position.setX(position.getX() + distance * Math.cos(Math.toRadians(angle)));
        position.setY(position.getY() + distance * Math.sin(Math.toRadians(angle)));
        //Create a drawing canva to draw the line
        DrawingCanvas draw = new DrawingCanvas(this.scene, oldPosition,this.getPosition());
        draw.drawLine(this,this.getWidth(),this.getOpacity(),this.getColor());
        draw.drawCursor(this);
    }

    /**
     * Move the cursor backward according to the distance
     * @param distance the distance to move
     */
    public void moveBackward(double distance) {
        Point oldPosition = new Point(position.getX(), position.getY());
        //Move the cursor to the new position according to the distance
        position.setX(position.getX() - distance * Math.cos(Math.toRadians(angle)));
        position.setY(position.getY() - distance * Math.sin(Math.toRadians(angle)));
        //Create a drawing canva to draw the line
        DrawingCanvas draw = new DrawingCanvas(this.scene, oldPosition,this.getPosition());
        draw.drawLine(this,this.getWidth(),this.getOpacity(),this.getColor());
        draw.drawCursor(this);
    }

    /**
     * Move the cursor to a new position according to the coordinates x and y
     * @param x the x coordinate of the new position
     * @param y the y coordinate of the new position
     */
    public void move(double x,double y){
        Point move = new Point(position.getX()+x, position.getY()+y);
        DrawingCanvas draw = new DrawingCanvas(this.scene,position,move);
        this.setPosition(move);
        draw.drawLine(this,this.getWidth(),this.getOpacity(),this.getColor());
        draw.drawCursor(this);
    }

    /**
     * Turn the cursor according to the degrees
     * @param degrees the degrees to turn
     */
    public void turn(double degrees) {
        angle += degrees;
        DrawingCanvas draw = new DrawingCanvas(this.scene);
        draw.drawPosCursor(this);
    }

    // Getters et Setters

    /**
     * return the angle of the cursor
     * @return the angle of the cursor
     */
    public double getAngle() {
        return angle;
    }

    /**
     * set the angle of the cursor
     * @param angle the angle of the cursor
     */
    public void setAngle(double angle) {
        this.angle = angle;
        DrawingCanvas draw = new DrawingCanvas(this.scene);
        draw.drawPosCursor(this);
    }

    /**
     * Mimic the cursor with the same position, angle, width, color and opacity
     * @param other the cursor to mimic
     */
    public void mimic(Cursor other) {
        this.position = new Point(other.position.getX(), other.position.getY());
        this.angle = other.angle;
        this.width = other.opacity;
        this.color = other.color;
        this.opacity = other.opacity;
    }

    /**
     * return the color of the line
     * @return the color of the line
     */
    public ColorOfLine getColor() {
        return color;
    }

    /**
     * set the color of the line
     * @param color the color of the line
     */
    public void setColor(ColorOfLine color) {
        this.color = color;
    }

    /**
     * return the width of the line
     * @return the width of the line
     */
    public double getWidth() {
        return width;
    }

    /**
     * set the width of the line
     * @param width the width of the line
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * return the opacity of the line
     * @return the opacity of the line
     */
    public double getOpacity() {
        return opacity;
    }

    /**
     * set the opacity of the line
     * @param opacity the opacity of the line
     */
    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    /**
     * return the id of the cursor
     * @return the id of the cursor
     */
    public int getId() {
        return id;
    }

    /**
     * set the id of the cursor
     * @param id the id of the cursor
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Remove the cursor with the id
     */
    public void removeCursorWithId() {
        String idToRemove = Integer.toString(this.getId());
        Parent root = this.scene.getRoot();

        // Vérifiez si la racine peut être castée en Group ou Pane
        if (root instanceof Group) {
            Group groupRoot = (Group) root;
            ObservableList<Node> children = groupRoot.getChildren();

            // Utilisez un itérateur pour éviter les problèmes de modification de la liste
            for (Iterator<Node> iterator = children.iterator(); iterator.hasNext();) {
                Node child = iterator.next();
                if (idToRemove.equals(child.getId())) {
                    iterator.remove();
                }
            }

        } else if (root instanceof Pane) {
            Pane paneRoot = (Pane) root;
            ObservableList<Node> children = paneRoot.getChildren();

            // Utilisez un itérateur pour éviter les problèmes de modification de la liste
            for (Iterator<Node> iterator = children.iterator(); iterator.hasNext();) {
                Node child = iterator.next();
                if (idToRemove.equals(child.getId())) {
                    iterator.remove();
                }
            }
        } else {
            throw new IllegalArgumentException("Unsupported root type: " + root.getClass().getName());
        }

        // Pas besoin de réaffecter la racine, elle est déjà mise à jour
        System.out.println(root+"root reaffecter");
    }

    /**
     * Set the visibility of the cursor
     * @param visible the visibility of the cursor
     */
    public void setVisible (boolean visible) {
        this.visible = visible;
        if (this.visible){
            DrawingCanvas draw = new DrawingCanvas(this.scene,position);
            draw.drawCursor(this);
            Group root = (Group)scene.getRoot();
        }
        else{
            this.removeCursorWithId();
        }
    }

    /**
     * Return the visibility of the cursor
     * @return the visibility of the cursor
     */
    public boolean getVisible () { return visible;}

    /**
     * Return the affichage of the cursor
     * @return the string of the cursor
     */
    @Override
    public String toString() {
        return "Le curseur "+ id +" est à la position " + position + " et est orienté à " + angle + " degrés";
    }
    public void deconstructor(){
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



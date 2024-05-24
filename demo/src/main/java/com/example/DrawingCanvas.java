package com.example;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;


/**
 * Represents a drawing canvas where lines can be drawn.
 */
public class DrawingCanvas{
    private Scene scene;
    private Point oldPosition;
    private Point newPosition;

    /**
     * Constructs a new drawing canvas with the specified scene and old position.
     *
     * @param scene the scene where the lines are drawn
     * @param oldPosition the old position of the cursor
     */
    public DrawingCanvas(Scene scene, Point oldPosition) {
        this.scene=scene;
        this.oldPosition=oldPosition;
    }

    /**
     * Returns the new position of the cursor.
     * @return the new position of the cursor
     */
    public Point getNewPosition() {
        return newPosition;
    }

    /**
     * Sets the new position of the cursor.
     * @param newPosition the new position of the cursor
     */
    public void setNewPosition(Point newPosition) {
        this.newPosition = newPosition;
    }

    /**
     * Returns the scene where the lines are drawn.
     *
     * @return the scene where the lines are drawn
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Sets the scene where the lines are drawn.
     *
     * @param scene the scene where the lines are drawn
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Returns the old position of the cursor.
     *
     * @return the old position of the cursor
     */
    public Point getOldPosition() {
        return oldPosition;
    }

    /**
     * Sets the old position of the cursor.
     *
     * @param oldPosition the old position of the cursor
     */
    public void setOldPosition(Point oldPosition){
        this.oldPosition=oldPosition;
    }

    /**
     * Constructs a new drawing canvas with the specified scene, old position and new position.
     * @param scene the scene where the lines are drawn
     * @param oldPos the old position of the cursor
     * @param newPos the new position of the cursor
     */
    DrawingCanvas(Scene scene, Point oldPos, Point newPos){
        this.scene=scene;
        this.oldPosition=oldPos;
        this.newPosition=newPos;
    }

    /**
     * Constructs a new drawing canvas with the specified scene.
     * @param s the scene where the lines are drawn
     */
    DrawingCanvas(Scene s){
        this.scene=s;
        this.oldPosition=null;
        this.newPosition=null;
    }

    /**
     * Draws a line with the specified width, opacity and color.
     *
     * @param cursor the cursor that draws the line
     * @param width the width of the line
     * @param opacity the opacity of the line
     * @param color the color of the line
     */
    void drawLine(Cursor cursor, double width,double opacity, ColorOfLine color){
        //Set the point where the line will start
        MoveTo moveTo=new MoveTo(oldPosition.getX(), oldPosition.getY());
        //Creation and addition of the initial point to the path
        Path path = new Path();
        path.getElements().add(moveTo);
        //Creation of the line
        LineTo line = new LineTo();
        line.setX(newPosition.getX());
        line.setY(newPosition.getY());
        path.setStrokeWidth(width);
        path.setOpacity(opacity);
        path.setStroke(Color.rgb(color.getRed(),color.getGreen(),color.getBlue()));
        //Add the line to the path
        path.getElements().add(line);
        //Creation of the group that contain the path
        Group root = new Group();
        //Take the last element added to the scene and delete it (delete the cursor as it is always the last element added to the scene)
        cursor.removeCursorWithId();
        drawCursor(cursor);
        Group rootf=(Group)scene.getRoot();
        rootf.getChildren().add(path);
        //Adding the root to the scene
        scene.setRoot(rootf);
    }

    /**
     * Draws a cursor with the specified cursor.
     *
     * @param cursor the cursor to draw
     */
    void drawCursor(Cursor cursor){
        //Create a Polygon that will be the shape of the cursor (here we have a triangle)
        Polygon cursorTriangle = new Polygon(
                cursor.getPosition().getX(), cursor.getPosition().getY(),
                cursor.getPosition().getX() + 10 * Math.cos(Math.toRadians(cursor.getAngle() - 150)), cursor.getPosition().getY() + 10 * Math.sin(Math.toRadians(cursor.getAngle() - 150)),
                cursor.getPosition().getX() + 10 * Math.cos(Math.toRadians(cursor.getAngle() + 150)), cursor.getPosition().getY() + 10 * Math.sin(Math.toRadians(cursor.getAngle() + 150))
        );
        cursorTriangle.setId(Integer.toString(cursor.getId()));
        //Set the color of the cursor
        cursorTriangle.setFill(Color.RED);
        //Adding the cursor to the root
        Group roots = (Group)scene.getRoot();
        roots.getChildren().add(cursorTriangle);
        //Adding the root to the scene
        scene.setRoot(roots);
    }

    /**
     * Draws a cursor with the specified cursor and removes the cursor.
     *
     * @param cursor the cursor to draw
     */
    void drawPosCursor(Cursor cursor){
        cursor.removeCursorWithId();
        this.drawCursor(cursor);
    }

}


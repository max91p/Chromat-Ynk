package com.example;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;

public class DrawingCanvas{
    private Scene scene;
    private Point oldPosition;
    private Point newPosition;

    public DrawingCanvas(Scene scene, Point oldPosition) {
        this.scene=scene;
        this.oldPosition=oldPosition;
    }

    public Point getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Point newPosition) {
        this.newPosition = newPosition;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Point getOldPosition() {
        return oldPosition;
    }
    public void setOldPosition(Point oldPosition){
        this.oldPosition=oldPosition;
    }

    DrawingCanvas(Scene s, Point oldPos, Point newPos){
        this.scene=s;
        this.oldPosition=oldPos;
        this.newPosition=newPos;
    }
    DrawingCanvas(Scene s){
        this.scene=s;
        this.oldPosition=null;
        this.newPosition=null;
    }

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

    void drawPosCursor(Cursor cursor){
        Group rootf=(Group)scene.getRoot();
        int lastIndex = rootf.getChildren().size() - 1;
        if (lastIndex >= 0) {
            rootf.getChildren().remove(lastIndex);
        }
        this.drawCursor(cursor);
    }

}


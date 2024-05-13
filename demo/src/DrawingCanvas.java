package src;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class DrawingCanvas {
    private Scene scene;
    private Point oldPosition;
    private Point newPosition;

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

    DrawingCanvas(Scene s, Point oldPos,Point newPos){
        this.scene=s;
        this.oldPosition=oldPos;
        this.newPosition=newPos;
    }

    void drawLine(){
        //Set the point where the line will start
        MoveTo moveTo=new MoveTo(oldPosition.getX(), oldPosition.getY());
        //Creation and addition of the initial point to the path
        Path path = new Path();
        path.getElements().add(moveTo);
        //Creation of the line
        LineTo line = new LineTo();
        line.setX(newPosition.getX());
        line.setY(newPosition.getY());
        //Add the line to the path
        path.getElements().add(line);
        //Creation of the group that contain the path
        Group root = new Group();
        root.getChildren().addAll(scene.getRoot(),path);
        //Adding the root to the scene
        scene.setRoot(root);
    }
}


package com.example;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class Dessin extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Création d'un chemin
        Path path = new Path();

        // Point initial à partir duquel le trait commencera
        MoveTo moveTo = new MoveTo( 0, 0);
        // Ajout du point initial au chemin
        path.getElements().add(moveTo);
        // Création du groupe pour contenir le chemin
        Group root = new Group();
        Scene scene = new Scene(root,1080,500);
        Point start = new Point(0,0);
        Cursor cursor = new Cursor(start, 0,scene);
        Polygon cursorTriangle = new Polygon();
        // Ajout du triangle représentant le curseur au groupe
        root.getChildren().add(cursorTriangle);
        // Création de la scène et ajout du groupe
        scene.setRoot(root);
        ColorOfLine red=new ColorOfLine( 39,168,136);
        ColorOfLine blue=new ColorOfLine(0.139,0.135,0.98);
        cursor.setColor(blue);
        cursor.setWidth(5);
        cursor.setOpacity(0.7);
        cursor.setAngle(55);
        cursor.setScene(scene);
        cursor.moveForward(300);
        cursor.setColor(red);
        cursor.setWidth(40);
        cursor.setOpacity(0.2);
        cursor.setAngle(125);
        cursor.moveBackward(100);
        cursor.setPosition(new Point(600,200));
        cursor.setColor(blue);
        cursor.setWidth(5);
        cursor.setOpacity(0.7);
        cursor.setAngle(180);
        cursor.moveForward(300);
        cursor.setColor(red);
        cursor.setWidth(40);
        cursor.setOpacity(0.2);
        cursor.setAngle(260);
        cursor.moveBackward(100);
        cursor.move(50,50);
        cursor.setPosition(new Point(900,300));


        // Configuration de la scène principale
        primaryStage.setTitle("Dessin d'un trait avec une courbe en JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
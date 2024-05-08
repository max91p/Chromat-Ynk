package com.example;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class Dessin extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Création d'un chemin
        Path path = new Path();

        // Point initial à partir duquel le trait commencera
        MoveTo moveTo = new MoveTo( 100, 100);

        // Ajout du point initial au chemin
        path.getElements().add(moveTo);

        // Ajout d'une courbe quadratique
        LineTo line = new LineTo();
        line.setX(400);
        line.setY(300);
        QuadCurveTo quadCurveTo = new QuadCurveTo();

        quadCurveTo.setX(200); // Position X du point d'arriver de la courbe
        quadCurveTo.setY(100); // Position Y du point de d'arriver de la courbe
        quadCurveTo.setControlX(400); // Position X du point de contrôle de la courbe
        quadCurveTo.setControlY(400); // Position Y du point de contrôle de la courbe

        QuadCurveTo quadCurveTo2 = new QuadCurveTo();

        quadCurveTo2.setX(500); // Position X du point d'arriver de la courbe
        quadCurveTo2.setY(200); // Position Y du point de d'arriver de la courbe
        quadCurveTo2.setControlX(400); // Position X du point de contrôle de la courbe
        quadCurveTo2.setControlY(0); // Position Y du point de contrôle de la courbe

        // Ajout de la courbe au chemin
        path.getElements().addAll(line,quadCurveTo,quadCurveTo2);

        // Définition de la couleur de trait
        path.setStroke(Color.BLUE);
        path.setOpacity(0.2);

        // Définition de l'épaisseur de trait
        path.setStrokeWidth(5);

        // Création du groupe pour contenir le chemin
        Group root = new Group();
        root.getChildren().add(path);

        Cursor cursor = new Cursor(500,200,165);
        Polygon cursorTriangle = new Polygon(
                cursor.getX(), cursor.getY(),
                cursor.getX() + 10 * Math.cos(Math.toRadians(cursor.getAngle() - 150)), cursor.getY() + 10 * Math.sin(Math.toRadians(cursor.getAngle() - 150)),
                cursor.getX() + 10 * Math.cos(Math.toRadians(cursor.getAngle() + 150)), cursor.getY() + 10 * Math.sin(Math.toRadians(cursor.getAngle() + 150))
        );
        cursorTriangle.setFill(Color.RED); // Couleur du triangle représentant le curseur

        // Ajout du triangle représentant le curseur au groupe
        root.getChildren().add(cursorTriangle);
        // Création de la scène et ajout du groupe
        Scene scene = new Scene(root, 600, 400);



        // Configuration de la scène principale
        primaryStage.setTitle("Dessin d'un trait avec une courbe en JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

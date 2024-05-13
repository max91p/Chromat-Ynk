package src;

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
        Point start = new Point(0,0);
        Cursor cursor = new Cursor(start, 0);
        Polygon cursorTriangle = new Polygon();
        // Ajout du triangle représentant le curseur au groupe
        root.getChildren().add(cursorTriangle);
        // Création de la scène et ajout du groupe
        Scene scene = new Scene(root, 600, 400);
        ColorOfLine red=new ColorOfLine( 39,168,136);
        ColorOfLine blue=new ColorOfLine(0.139,0.135,0.98);
        cursor.moveForward(300,scene,55,5,0.7,blue);
        cursor.moveBackward(100,scene,125,40,0.2,red);


        // Configuration de la scène principale
        primaryStage.setTitle("Dessin d'un trait avec une courbe en JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
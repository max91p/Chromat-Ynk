package src;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {
    private final String WINDOW_TITLE = "Application";
    private final double WINDOW_WIDTH = 640;
    private final double WINDOW_HEIGHT = 480;

    @Override
    public void start(Stage primaryStage){

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        Scene mainViewScene = new Scene(scrollPane, WINDOW_WIDTH, WINDOW_HEIGHT);


        Group root = new Group();

        // Ajout de quelques éléments au groupe (par exemple, des cercles)
        Circle circle1 = new Circle(50, 50, 30);
        Circle circle2 = new Circle(150, 50, 30);
        Circle circle3 = new Circle(250, 50, 30);

        root.getChildren().addAll(circle1, circle2, circle3);

        // Création de la scène avec la racine
        Scene scene = new Scene(root, 400, 200);

        // Récupération de la racine de la scène
        Group sceneRoot = (Group) scene.getRoot();

        // Suppression du dernier élément ajouté à la racine de la scène
        int lastIndex = sceneRoot.getChildren().size() - 1;
        if (lastIndex >= 0) {
            sceneRoot.getChildren().remove(lastIndex);
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

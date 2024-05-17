package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
    private final String WINDOW_TITLE = "Application";
    private final double WINDOW_WIDTH = 640;
    private final double WINDOW_HEIGHT = 480;

    @Override
    public void start(Stage primaryStage){

        MainView view = new MainView(10);
        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);

        // Création de la scène avec la racine
        Scene scene = new Scene(scrollPane, 400, 200);


        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

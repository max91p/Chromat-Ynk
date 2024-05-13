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

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        Scene mainViewScene = new Scene(scrollPane, WINDOW_WIDTH, WINDOW_HEIGHT);


        primaryStage.setScene(mainViewScene);
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

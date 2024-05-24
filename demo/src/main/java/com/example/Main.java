package com.example;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Main class for the application.
 */
public class Main extends Application {
    private final String WINDOW_TITLE = "Application";
    private final double WINDOW_WIDTH = 1080;
    private final double WINDOW_HEIGHT = 800;

    /**
     * Starts the application.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            try {
                ErrorLogger errorlogger = new ErrorLogger("Exception not handled" + throwable.getMessage(), throwable);
                errorlogger.logError();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {


            Group root = new Group();
            Group root2 = new Group();

            // Création de la scène avec la racine
            Scene scene = new Scene(root, 400, 200);
            Scene scene2 = new Scene(root2, 400, 200);

            primaryStage.setTitle(WINDOW_TITLE);
            primaryStage.setWidth(WINDOW_WIDTH);
            primaryStage.setHeight(WINDOW_HEIGHT);

            CursorManager cursorManager = new CursorManager(scene2);
            VariableContext variable = new VariableContext();
            MainView view = new MainView(10, new TextArea(), new Button("Submit"), new Button("Save"), new Text(), cursorManager,variable,new Button("Clear"));
            Pane container = new Pane();
            container.getChildren().add(view);

// Ajouter le conteneur à root
            root.getChildren().add(container);

            scene.setRoot(root);


            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            ErrorLogger errorLogger = new ErrorLogger("An error occurred while starting the application." + e.getMessage(), e);
            errorLogger.logError();
        }
    }

    /**
     * The main entry point for all JavaFX applications.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

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

public class Main extends Application {
    private final String WINDOW_TITLE = "Application";
    private final double WINDOW_WIDTH = 640;
    private final double WINDOW_HEIGHT = 480;

    @Override
    public void start(Stage primaryStage){



        Group root = new Group();
        Group root2 = new Group();

        // Création de la scène avec la racine
        Scene scene = new Scene(root, 400, 200);
        Scene scene2 = new Scene(root2, 400, 200);
        Cursor c1=new Cursor(new Point(100,100),10,scene2);

        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setWidth(WINDOW_WIDTH);
        primaryStage.setHeight(WINDOW_HEIGHT);

        CursorManager cursorManager=new CursorManager(scene2);
        cursorManager.addCursor(c1);
        cursorManager.selectCursor(10);
        MainView view = new MainView(10,new TextArea(),new Button("Submit"),new Text(),cursorManager);

        Pane container = new Pane();
        container.getChildren().add(view);

// Ajouter le conteneur à root
        root.getChildren().add(container);

        scene.setRoot(root);


        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

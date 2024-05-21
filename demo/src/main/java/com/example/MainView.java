package com.example;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainView extends VBox {
    public void getText(TextArea text, Text test){
        test.setText(text.getText());


    }
    public MainView(double spacing){
        super(spacing);
        ObservableList<Node> components = this.getChildren();

        TextArea text = new TextArea();
        Button button = new Button("Submit");
        Text test = new Text();
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Appel de la méthode que vous souhaitez exécuter
                getText(text,test);
            }
        });
        components.addAll(text,button,test);
    }


}

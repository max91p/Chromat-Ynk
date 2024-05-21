package com.example;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainView extends VBox {
    public void getText(TextArea text, Text test){
        test.setText(text.getText());
        Pattern pattern = Pattern.compile("^(.*?)\\((.*?)\\)$");
        Matcher matcher = pattern.matcher(test.getText());

        // Vérification de la correspondance et extraction du texte et de la valeur
        if (matcher.matches()) {
            String texte = matcher.group(1); // Texte entre les parenthèses
            String valeurString = matcher.group(2); // Valeur entre les parenthèses

            // Convertir la valeur en type Object
            Object valeur = null;

            // Vérifier le type de la valeur
            if (isInteger(valeurString)) {
                valeur = Integer.parseInt(valeurString);
            } else if (isDouble(valeurString)) {
                valeur = Double.parseDouble(valeurString);
            } else {
                valeur = valeurString;
            }
            // Afficher le texte et la valeur
            test.setText("Texte : " + texte + "Valeur : " + valeur);
        } else {
            test.setText("Format invalide.");
        }
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


    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Méthode pour vérifier si une chaîne peut être convertie en double
    private static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

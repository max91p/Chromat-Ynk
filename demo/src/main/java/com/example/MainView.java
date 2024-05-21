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

    private final TextArea text;
    private final Button button;
    private final Text resultText;

    public MainView(double spacing, TextArea text, Button button, Text resultText, CursorManager cursorManager) {
        super(spacing);
        this.text = text;
        this.button = button;
        this.resultText = resultText;

        button.setOnAction(event -> {
            try {
                getText(text,resultText,cursorManager);
            } catch (ErrorLogger e) {
                throw new RuntimeException(e);
            }
        });

        VBox inputBox = createInputBox();
        getChildren().addAll(inputBox, resultText);
    }

    private VBox createInputBox() {
        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(text, button);
        return inputBox;
    }
    public void getText(TextArea text, Text test,CursorManager cursorManager) throws ErrorLogger {
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
            test.setText("Texte : " + texte + " Valeur : " + valeur);
            SimpleInstruction test1 = new SimpleInstruction(texte,valeur,cursorManager,cursorManager.getCursor(10).getScene());
            test1.isValid();
            test1.execute();
            System.out.println(test1.isValid());
        } else {
            test.setText("Format invalide.");
        }
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

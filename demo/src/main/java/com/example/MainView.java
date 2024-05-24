package com.example;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainView extends VBox {

    private final TextArea text;
    private final Button button;
    private final Button save;
    private final Text resultText;

    public MainView(double spacing, TextArea text, Button button, Button save, Text resultText, CursorManager cursorManager1, VariableContext variable) {
        super(spacing);
        this.text = text;
        this.button = button;
        this.save = save;
        this.resultText = resultText;

        Stage secondaryStage = new Stage();
        button.setOnAction(event -> {
            try {
                getText(text, resultText, cursorManager1, variable);
                secondaryStage.setScene(cursorManager1.getScene());
                secondaryStage.show();
            } catch (ErrorLogger e) {
                throw new RuntimeException(e);
            }
        });

        save.setOnAction(event -> {
            WritableImage image = cursorManager1.getScene().snapshot(null);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File file = new File("./Chromat-Ynk/demo/src/save/screenshot_" + timestamp + ".png");

            try {
                BufferedImage bufferedImage = new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                PixelReader reader = image.getPixelReader();
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        bufferedImage.setRGB(x, y, reader.getArgb(x, y));
                    }
                }

                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });

        VBox inputBox = createInputBox();
        getChildren().addAll(inputBox, resultText);
    }

    private VBox createInputBox() {
        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(text, button, save);
        return inputBox;
    }

    public void processInstruction(Text test, CursorManager cursorManager, String line, VariableContext variable) throws ErrorLogger {

        Pattern pattern = Pattern.compile("^(.*?)\\((.*?)\\)$");
        Matcher matcherInstruction = pattern.matcher(line);

        // Vérification de la correspondance et extraction du texte et de la valeur
        if (matcherInstruction.matches()) {
            String texte = matcherInstruction.group(1); // Texte entre les parenthèses
            String valeurString = matcherInstruction.group(2); // Valeur entre les parenthèses

            // Convertir la valeur en type Object
            Object valeur = null;

            // Vérifier le type de la valeur
            if (isInteger(valeurString)) {
                valeur = Integer.parseInt(valeurString);
                if ((int) valeur < 0) {
                    throw new ErrorLogger("La valeur doit être positive");
                }

            } else if (isDouble(valeurString)) {
                valeur = Double.parseDouble(valeurString);
            } else {
                valeur = valeurString;
            }
            // Afficher le texte et la valeur
            test.setText("Texte : " + texte + " Valeur : " + valeur);

            SimpleInstruction test1 = new SimpleInstruction(texte, valeur, cursorManager, variable, cursorManager.getScene());
            test1.isValid();
            test1.execute();
            System.out.println(test1.isValid());
        } else {
            test.setText("Format invalide.");
        }
    }

    public void processVariable(Text text, String line, VariableContext variable) throws ErrorLogger {
        Pattern pattern = Pattern.compile("^(\\w+)\\s+(.+)$");
        Matcher matcherVariable = pattern.matcher(line);
        if (matcherVariable.matches()) {
            String type = matcherVariable.group(1);
            String argument = matcherVariable.group(2);
            if (argument.contains("=")) {
                int operator = argument.indexOf("=");
                String name = argument.substring(0, operator).trim();
                String value = argument.substring(operator + 1).trim();
                // Convertir la valeur en type Object
                Object valeur = null;

                // Vérifier le type de la valeur
                if (isInteger(value)) {
                    valeur = Integer.parseInt(value);
                    if ((int) valeur < 0) {
                        throw new ErrorLogger("La valeur doit être positive");
                    }

                } else if (isDouble(value)) {
                    valeur = Double.parseDouble(value);
                } else if (isBoolean(value)) {
                    if (value.equals("true")){
                        valeur = true;
                    }
                    else {
                        valeur=false;
                    }
                } else {
                    valeur = value;
                }
                VariableInstruction variableInstruction = new VariableInstruction(type, name, valeur, variable);
                variableInstruction.isValid();
                variableInstruction.execute();
            } else {
                VariableInstruction variableInstructionNull = new VariableInstruction(type, argument, null, variable);
                variableInstructionNull.isValid();
                variableInstructionNull.execute();
            }
        }
    }

    public void getText(TextArea text, Text test, CursorManager cursorManager, VariableContext variable) throws ErrorLogger {
        test.setText(text.getText());
        String textAll = test.getText();
        textAll = textAll.replaceAll("[\n\r]", "");
        if (textAll.contains(";")) {
            if (textAll.contains("{") && textAll.contains("}")) {
                //faire le bloc d'instruction
            } else {
                List<String> listInstruction = new ArrayList<>();
                Pattern instruction = Pattern.compile("([^;]*);");
                Matcher matcher = instruction.matcher(textAll);
                while (matcher.find()) {
                    listInstruction.add(matcher.group(1));
                }
                for (String line : listInstruction) {
                    for (EnumSimpleInstructions value : EnumSimpleInstructions.values()) {
                        if (line.startsWith(value.toString())) {
                            System.out.println("oetrg");
                            processInstruction(test, cursorManager, line, variable);
                        } else {
                            processVariable(test, line, variable);
                        }
                    }
                }


            }

        } else {
            for (EnumSimpleInstructions value : EnumSimpleInstructions.values()) {
                if (textAll.startsWith(value.toString())) {
                    processInstruction(test, cursorManager, textAll, variable);
                } else {
                    processVariable(test, textAll, variable);
                }
            }
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

    private static boolean isBoolean(String s) {
        return Objects.equals(s, "true") || Objects.equals(s, "false");
    }
}

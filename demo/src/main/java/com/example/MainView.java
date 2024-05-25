package com.example;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainView extends VBox {

    private final TextArea text;
    private final Button button;
    private final Button save;
    private final Text resultText;
    private final Button clearButton;
    private final Button oppenFileButton;



    public MainView(double spacing, TextArea text, Button button, Button save, Text resultText, CursorManager cursorManager1, VariableContext variable,Button clearButton, Button openFileButton,Stage primaryStage) {
        super(spacing);
        this.text = text;
        this.button = button;
        this.save = save;
        this.clearButton=clearButton;
        this.resultText = resultText;
        this.oppenFileButton=openFileButton;

        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Drawing Canva");
        secondaryStage.setWidth(1080);
        secondaryStage.setHeight(800);
        button.setOnAction(event -> {
            try {
                getText(text, resultText, cursorManager1, variable);
                secondaryStage.setScene(cursorManager1.getScene());
                secondaryStage.show();
            } catch (ErrorLogger e) {
                throw new RuntimeException(e);
            }
        });
        Group root = (Group)cursorManager1.getScene().getRoot();

        clearButton.setOnAction(event -> {
            root.getChildren().clear(); // Vider tous les enfants de la racine
            for (Cursor cursor : cursorManager1.getListCursor()){
                try {
                    cursorManager1.removeCursor(cursor.getId());
                } catch (ErrorLogger e) {
                    throw new RuntimeException(e);
                }
            }
        });

        openFileButton.setOnAction(e -> {
            // Utiliser le FileChooser pour obtenir le chemin du fichier
            String filePath = showFileChooser(primaryStage);
            if (filePath != null) {

                    // Appeler la méthode getText avec les paramètres nécessaires
                try {
                    getTextFromFile(cursorManager1, variable, filePath);
                } catch (ErrorLogger ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                secondaryStage.setScene(cursorManager1.getScene());
                    secondaryStage.show();

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

    public String showFileChooser(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            return selectedFile.getPath();
        }
        return null;
    }

    private VBox createInputBox() {
        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(text, button, save,clearButton,oppenFileButton);
        return inputBox;
    }

    public void getText(TextArea text, Text test, CursorManager cursorManager, VariableContext variable) throws ErrorLogger {
        test.setText(text.getText());
        String textAll = test.getText();
        textAll = textAll.replaceAll("[\n\r]", "");
        processInstructions(textAll,cursorManager,variable);
    }

    public void getTextFromFile(CursorManager cursorManager,VariableContext variable,String filePath) throws ErrorLogger, IOException {
        String textAll="";
        if (filePath != null && !filePath.isEmpty()) {
            // Lecture du contenu du fichier texte
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            // Ajout du contenu du fichier au texte récupéré
            textAll += fileContent;
        }

        // Remplacer les nouvelles lignes et les retours chariot
        textAll = textAll.replaceAll("[\n\r]", "");

        // Traitement des instructions
        processInstructions(textAll, cursorManager, variable);
    }

    public void processInstructions(String inputText,CursorManager cursorManager, VariableContext variableContext) throws ErrorLogger {
        List<Instruction> instructions = parseInstructions(inputText,cursorManager,variableContext);
        for (Instruction instruction : instructions) {
            if (instruction.isValid()) {
                instruction.execute();
            }
        }
    }

    private List<Instruction> parseInstructions(String inputText,CursorManager cursorManager, VariableContext variableContext) throws ErrorLogger {
        List<Instruction> instructions = new ArrayList<>();
        Deque<InstructionsBlock> stack = new ArrayDeque<>();
        String[] lines = inputText.split("(?<=;|\\{|\\})|(?=\\{|\\})");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.endsWith(";")) {
                String instructionText=line.substring(0,line.length()-1).trim();
                Instruction instruction=null;

                for (EnumSimpleInstructions value : EnumSimpleInstructions.values()) {
                    if (instructionText.startsWith(value.toString())) {
                        instruction = parseSimpleInstruction(instructionText,cursorManager,variableContext);
                        break;
                    }
                }

                if (instruction == null) {
                    for (EnumVariableInstruction value : EnumVariableInstruction.values()) {
                        if (instructionText.startsWith(value.toString())) {
                            instruction = parseVariableInstruction(instructionText,variableContext);
                            break;
                        }
                    }
                }

                if (instruction == null) {
                    throw new ErrorLogger("Unknown expression: " + instructionText);
                }

                if (!stack.isEmpty()) {
                    stack.peek().getInstructions().add(instruction);
                } else {
                    instructions.add(instruction);
                }
            } else if (line.startsWith("IF") || line.startsWith("FOR") || line.startsWith("WHILE") || line.startsWith("MIMIC") || line.startsWith("MIRROR")) {
                InstructionsBlock blockInstruction = parseBlockInstruction(line.trim(),cursorManager,variableContext);
                if (!stack.isEmpty()) {
                    stack.peek().getInstructions().add(blockInstruction);
                } else {
                    instructions.add(blockInstruction);
                }
                stack.push(blockInstruction);
            } else if (line.equals("}")) {
                if (stack.isEmpty()) {
                    throw new ErrorLogger("Syntax error: unexcepted '}'.");
                }
                stack.pop();
            }
        }
        return instructions;
    }

    private Instruction parseVariableInstruction(String line,VariableContext variableContext) throws ErrorLogger {
        String[] parts = line.split("\\s+",2);
        String type = parts[0];
        String argument= parts[1];
        if(argument.contains("=")){
            int operator = argument.indexOf("=");
            String name = argument.substring(0,operator).trim();
            String value = argument.substring(operator+1).trim();

            // Convertir la valeur en type Object
            Object variableValue = null;

            // Vérifier le type de la valeur
            if (isInteger(value)) {
                variableValue = Integer.parseInt(value);
                if ((int) variableValue < 0) {
                    throw new ErrorLogger("La valeur doit être positive");
                }

            } else if (isDouble(value)) {
                variableValue = Double.parseDouble(value);
            } else if (isBoolean(value)) {
                if (value.equals("true")){
                    variableValue = true;
                }
                else {
                    variableValue=false;
                }
            } else {
                variableValue = value;
            }
            return new VariableInstruction(type,name,variableValue,variableContext);
        }
        return new VariableInstruction(type,argument,null,variableContext);
    }

    private InstructionsBlock parseBlockInstruction(String line,CursorManager cursorManager,VariableContext variableContext) {
        String[] parts = line.split("\\s+",2);
        String type = parts[0];
        String argument;
        if (parts.length > 1) {
            argument = parts[1].trim();
        } else {
            argument = "";
        }

        List<Instruction> blockInstructions = new ArrayList<>();

        return new InstructionsBlock(type, argument, blockInstructions, cursorManager, variableContext, cursorManager.getScene());
    }

    private Instruction parseSimpleInstruction(String line,CursorManager cursorManager,VariableContext variableContext) throws ErrorLogger {
        String type = line.substring(0, line.indexOf("(")).trim();
        String parameters = line.substring(line.indexOf("(") + 1,  line.indexOf(")")).trim();

        Object valeur = parseParameter(parameters);

        return new SimpleInstruction(type, valeur, cursorManager,variableContext , cursorManager.getScene());
    }

    private Object parseParameter(String parameter) throws ErrorLogger {
        if (parameter.isEmpty()) {
            return null;
        }
        if (isInteger(parameter)) {
            int value = Integer.parseInt(parameter);
            if (value < 0) {
                throw new ErrorLogger("La valeur doit être positive");
            }
            return value;
        } else if (isDouble(parameter)) {
            return Double.parseDouble(parameter);
        } else {
            return parameter;
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

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


    /**
     * Construct the MainView
     * @param spacing
     * @param text              the input text area
     * @param button            execute button
     * @param save              save button
     * @param resultText        text element
     * @param cursorManager1    the cursor manager
     * @param variable          the variable manager
     * @param clearButton       clear button
     * @param openFileButton    Open file button
     * @param primaryStage      the primary stage
     */
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
        // Event handler for execute button
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
            root.getChildren().clear(); // get the root empty
            for (Cursor cursor : cursorManager1.getListCursor()){
                try {
                    cursorManager1.removeCursor(cursor.getId());
                } catch (ErrorLogger e) {
                    throw new RuntimeException(e);
                }
            }
        });

        openFileButton.setOnAction(e -> {
            // use the FileChooser to get the file path
            String filePath = showFileChooser(primaryStage);
            if (filePath != null) {

                // call the getText method with required parameters
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

        // Event handler for save button
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

    /**
     * Show a file chooser and return the selected file path
     * @param primaryStage The primary stage
     * @return the selected file path
     */
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

    /**
     * Create the input box with textArea and buttons
     * @return The VBox containing input elements
     */
    private VBox createInputBox() {
        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(text, button, save,clearButton,oppenFileButton);
        return inputBox;
    }


    /**
     * Get text from the TextArea, resultText and processe instructions
     * @param text
     * @param test
     * @param cursorManager
     * @param variable
     * @throws ErrorLogger
     */
    public void getText(TextArea text, Text test, CursorManager cursorManager, VariableContext variable) throws ErrorLogger {
        test.setText(text.getText());
        String textAll = test.getText();
        textAll = textAll.replaceAll("[\n\r]", "");
        processInstructions(textAll,cursorManager,variable);
    }

    /**
     * Get text from a file
     * @param cursorManager
     * @param variable
     * @param filePath
     * @throws ErrorLogger
     * @throws IOException
     */
    public void getTextFromFile(CursorManager cursorManager,VariableContext variable,String filePath) throws ErrorLogger, IOException {
        String textAll="";
        if (filePath != null && !filePath.isEmpty()) {
            // Read the content of the text file
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            // Append the content of the file to the retrieved text
            textAll += fileContent;
        }

        // Replace newlines
        textAll = textAll.replaceAll("[\n\r]", "");

        // Process the instructions
        processInstructions(textAll, cursorManager, variable);
    }

    /**
     * Process instructions from the input text and executes them
     * @param inputText         Input text containing Instructions
     * @param cursorManager     cursors manager
     * @param variableContext   varibales manager
     * @throws ErrorLogger
     */
    public void processInstructions(String inputText,CursorManager cursorManager, VariableContext variableContext) throws ErrorLogger {
        //Parse the input text to obtain a list of instructions
        List<Instruction> instructions = parseInstructions(inputText,cursorManager,variableContext);
       //execute valid instructions
        for (Instruction instruction : instructions) {
            if (instruction.isValid()) {
                instruction.execute();
            }
        }
    }

    /**
     * Parse the input text to extract instructions
     * @param inputText         Text containing instructions
     * @param cursorManager     cursors manager
     * @param variableContext   variables manager
     * @return                  List of parsed instructions
     * @throws ErrorLogger
     */
    private List<Instruction> parseInstructions(String inputText,CursorManager cursorManager, VariableContext variableContext) throws ErrorLogger {
        //List to store instructions
        List<Instruction> instructions = new ArrayList<>();
        //Initialize a stack to handle blocks of instructions
        Deque<InstructionsBlock> stack = new ArrayDeque<>();
        // Split the input text into lines using regular expressions
        String[] lines = inputText.split("(?<=;|\\{|\\})|(?=\\{|\\})");

        //Iterate for each line of the input text
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Check if the line ends with a semicolon indicating a simple instruction or a variable instruction
            if (line.endsWith(";")) {
                String instructionText=line.substring(0,line.length()-1).trim();
                Instruction instruction=null;

                //Iterate through enum values to match simple instructions
                for (EnumSimpleInstructions value : EnumSimpleInstructions.values()) {
                    if (instructionText.startsWith(value.toString())) {
                        instruction = parseSimpleInstruction(instructionText,cursorManager,variableContext);
                        break;
                    }
                }

                //if the instruction didn't match simple instruction it may be a variable instruction
                if (instruction == null) {
                    //Iterate through enum values to match Variable instructions
                    for (EnumVariableInstruction value : EnumVariableInstruction.values()) {
                        if (instructionText.startsWith(value.toString())) {
                            instruction = parseVariableInstruction(instructionText,variableContext);
                            break;
                        }
                    }
                }
                //if the instruction is null, it's an unknown expression
                if (instruction == null) {
                    throw new ErrorLogger("Unknown expression: " + instructionText);
                }

                //Add the instruction to the list of the block based on the stack if it's not empty
                if (!stack.isEmpty()) {
                    stack.peek().getInstructions().add(instruction);
                } else {
                    //add the instruction as a simple or variable instruction
                    instructions.add(instruction);
                }
            } else if (line.startsWith("IF") || line.startsWith("FOR") || line.startsWith("WHILE") || line.startsWith("MIMIC") || line.startsWith("MIRROR")) {
                //Parse block instruction
                InstructionsBlock blockInstruction = parseBlockInstruction(line.trim(),cursorManager,variableContext);
                //Handle a block in a block
                if (!stack.isEmpty()) {
                    stack.peek().getInstructions().add(blockInstruction);
                } else {
                    //add the block instruction
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

    /**
     * Parse a variable instruction from the input text
     * @param line              line containing the instrcution
     * @param variableContext   variables manager
     * @return                  Parsed variable
     * @throws ErrorLogger
     */
    private Instruction parseVariableInstruction(String line,VariableContext variableContext) throws ErrorLogger {
        String[] parts = line.split("\\s+",2);
        String type = parts[0];
        String argument= parts[1];
        if(argument.contains("=")){
            int operator = argument.indexOf("=");
            String name = argument.substring(0,operator).trim();
            String value = argument.substring(operator+1).trim();

            // Convert the value to an Object type
            Object variableValue = null;

            // Check the type of the value and assign accordingly
            if (isInteger(value)) {
                variableValue = Integer.parseInt(value);
                if ((int) variableValue < 0) {
                    throw new ErrorLogger("value must be positive");
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
        //if the value is not specified
        return new VariableInstruction(type,argument,null,variableContext);
    }

    /**
     * Parse block instruction from the input text
     * @param line              Line containing the block instruction
     * @param cursorManager     cursors manager
     * @param variableContext   variables manager
     * @return  parsed InstructionsBlock object
     */
    private InstructionsBlock parseBlockInstruction(String line,CursorManager cursorManager,VariableContext variableContext) {
        // Split the line into two parts: type and argument
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

    /**
     * Parse a simple instruction from the input text
     * @param line              Line containing the simple instruction
     * @param cursorManager     cursors manager
     * @param variableContext   variables manager
     * @return               Parsed SimpleInstruction object
     * @throws ErrorLogger
     */
    private Instruction parseSimpleInstruction(String line,CursorManager cursorManager,VariableContext variableContext) throws ErrorLogger {
        String type = line.substring(0, line.indexOf("(")).trim();
        String parameters = line.substring(line.indexOf("(") + 1,  line.indexOf(")")).trim();

        Object value = parseParameter(parameters);

        return new SimpleInstruction(type, value, cursorManager,variableContext, cursorManager.getScene());
    }

    /**
     * Parse a parameter from the input texte
     * @param parameter         Parameter string to be parsed
     * @return                  Parsed parameter value
     * @throws ErrorLogger
     */
    private Object parseParameter(String parameter) throws ErrorLogger {
        if (parameter.isEmpty()) {
            return null;
        }
        if (isInteger(parameter)) {
            int value = Integer.parseInt(parameter);
            if (value < 0) {
                throw new ErrorLogger("value must be positive");
            }
            return value;
        } else if (isDouble(parameter)) {
            return Double.parseDouble(parameter);
        } else {
            return parameter;
        }
    }

    /**
     * check if a string can be parsed as an Integer
     * @param s     String to be checked
     * @return      True if the string can be parsed as an integer, otherwise false
     */
    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * check if a string can be parsed as an Double
     * @param s     String to be checked
     * @return      True if the string can be parsed as an Double, otherwise false
     *
     */
    private static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if a string can be parsed as an Double
     * @param s     String to be checked
     * @return      True if the string can be parsed as a Boolean, otherwise false
     *
     */
    private static boolean isBoolean(String s) {
        return Objects.equals(s, "true") || Objects.equals(s, "false");
    }
}

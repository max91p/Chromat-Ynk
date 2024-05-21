package com.example;

import java.util.List;
import java.util.function.BooleanSupplier;
import javafx.scene.Scene;

public class InstructionsBlock extends Instruction {
    private String type;
    private List<Instruction> instructions;
    private Object condition;
    private Object[] parameters;
    private CursorManager cursorManager;
    private VariableContext variableContext;
    private Scene scene;

    public InstructionsBlock(String type, Object condition, List<Instruction> instructions, CursorManager cursorManager, VariableContext variableContext, Scene scene) {
        this.type = type;
        this.condition = condition;
        this.instructions = instructions;
        this.cursorManager = cursorManager;
        this.variableContext = variableContext;
        this.scene = scene;
        this.parameters = null;
    }

    public InstructionsBlock(String type, List<Instruction> instructions, CursorManager cursorManager, VariableContext variableContext, Scene scene, Object... parameters) {
        this.type = type;
        this.condition = null;
        this.instructions = instructions;
        this.cursorManager = cursorManager;
        this.variableContext = variableContext;
        this.scene = scene;
        this.parameters = parameters;
    }

    @Override
    public void execute() throws ErrorLogger {
        switch (type) {
            case "IF":
                if (((BooleanSupplier) condition).getAsBoolean()) {
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
                        }
                    }
                }
                break;

            case "FOR":
                List<Integer> loopParams = (List<Integer>) condition;
                String variableName = "i";  // Assuming a default variable name
                int from = loopParams.get(0);
                int to = loopParams.get(1);
                int step = loopParams.size() > 2 ? loopParams.get(2) : 1;

                for (int i = from; i <= to; i += step) {
                    variableContext.setVariable(variableName, i);
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
                        }
                    }
                }
                variableContext.removeVariable(variableName);
                break;

            case "WHILE":
                BooleanSupplier conditionSupplier = (BooleanSupplier) condition;
                while (conditionSupplier.getAsBoolean()) {
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
                        }
                    }
                }
                break;

            case "MIMIC":
                int cursorID = (Integer) parameters[0];
                int originalCursor = cursorManager.getCurrentCursor().getId();

                cursorManager.selectCursor(cursorID);
                for (Instruction instruction : instructions) {
                    if (instruction.isValid()) {
                        instruction.execute();
                        cursorManager.selectCursor(originalCursor);
                        instruction.execute();
                        cursorManager.selectCursor(cursorID);
                    }
                }

                cursorManager.selectCursor(originalCursor);
                break;

            case "MIRROR":
                double x1 = (Double) parameters[0];
                double y1 = (Double) parameters[1];
                if (parameters.length > 2) {
                    double x2 = (Double) parameters[2];
                    double y2 = (Double) parameters[3];
                    // Axial symmetry logic
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
                        }
                        // Apply symmetry transformation
                    }
                } else {
                    // Central symmetry logic
                    Cursor currentCursor = cursorManager.getCurrentCursor();
                    Cursor mirroredCursor =  new Cursor(currentCursor.getPosition(),currentCursor.getAngle(), currentCursor.getColor(),currentCursor.getWidth(), currentCursor.getOpacity(),currentCursor.getId()*31,scene);

                    cursorManager.addCursor(mirroredCursor);

                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
                        }
                        mirroredCursor.setPosition(new Point(
                                2 * x1 - currentCursor.getPosition().getX(),
                                2 * y1 - currentCursor.getPosition().getY()
                        ));
                    }

                    cursorManager.removeCursor(mirroredCursor.getId());
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown block instruction type: " + type);
        }
    }
    public boolean isValid() {
        switch (type) {
            case "IF":
                return condition instanceof BooleanSupplier && instructions != null;

            case "FOR":
                return parameters.length >= 2 && parameters[0] instanceof String && parameters[1] instanceof Integer
                        && (parameters.length < 3 || parameters[2] instanceof Integer)
                        && (parameters.length < 4 || parameters[3] instanceof Integer)
                        && instructions != null;

            case "WHILE":
                return condition instanceof BooleanSupplier && instructions != null;

            case "MIMIC":
                return parameters.length == 1 && parameters[0] instanceof Integer && instructions != null;

            case "MIRROR":
                return (parameters.length == 2 || parameters.length == 4)
                        && parameters[0] instanceof Double && parameters[1] instanceof Double
                        && (parameters.length < 3 || (parameters[2] instanceof Double && parameters[3] instanceof Double))
                        && instructions != null;

            default:
                return false;
        }
    }
}

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
    private Scene scene;

    public InstructionsBlock(String type, Object condition, List<Instruction> instructions, CursorManager cursorManager, Scene scene, Object... parameters) {
        this.type = type;
        this.condition = condition;
        this.instructions = instructions;
        this.cursorManager = cursorManager;
        this.scene = scene;
        this.parameters = parameters;
    }

    @Override
    public void execute() {
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
                    VariableContext.set(variableName, i);
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
                        }
                    }
                }
                VariableContext.remove(variableName);
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
                Cursor originalCursor = cursorManager.getCurrentCursor();

                int factor=31;
                while(cursorManager.isCursorIdExists(originalCursor.getId()*factor)){
                    factor++;
                }

                Cursor tempCursor = new Cursor(originalCursor.getPosition(), originalCursor.getAngle(), originalCursor.getColor(),originalCursor.getThick(), originalCursor.getPress(),originalCursor.getId()*factor);

                cursorManager.addCursor(tempCursor);
                cursorManager.selectCursor(tempCursor.getId());


                List<SimpleInstruction> history = cursorManager.getCursor(cursorID).getHistory();
                for (SimpleInstruction instruction : history) {
                    instruction.execute();
                }

                cursorManager.getCursor(originalCursor.getId()).mimic(tempCursor);
                cursorManager.selectCursor(originalCursor.getId());
                cursorManager.removeCursor(tempCursor.getId());
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
                    Cursor mirroredCursor =  new Cursor(currentCursor.getPosition(),currentCursor.getAngle(), currentCursor.getColor(),currentCursor.getThick(), currentCursor.getPress(),currentCursor.getId()*31);

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

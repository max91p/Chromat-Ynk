package com.example;

import java.security.KeyStore;
import java.util.List;
import java.util.function.BooleanSupplier;
import javafx.scene.Scene;

public class InstructionsBlock extends Instruction {
    private String type;
    private List<Instruction> instructions;
    private Object condition;
    private Object parameters;
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

    public InstructionsBlock(String type, List<Instruction> instructions, CursorManager cursorManager, VariableContext variableContext, Scene scene, Object parameters) {
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
                int cursorID = (Integer) parameters;
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
                Point[] points = parsePoints((String) parameters);
                Point pointA = points[0];
                int currentCursorId = cursorManager.getCurrentCursor().getId();

                if (points.length > 1) {
                    Point pointB = points[1];

                    Cursor currentCursor = cursorManager.getCurrentCursor();
                    Cursor mirroredCursor = new Cursor(
                            axialSymmetry(currentCursor.getPosition(), pointA, pointB),
                            currentCursor.getAngle() + 180, // Angle adapted to +180
                            currentCursor.getColor(),
                            currentCursor.getWidth(),
                            currentCursor.getOpacity(),
                            currentCursor.getId() * 31,
                            scene
                    );
                    cursorManager.addCursor(mirroredCursor);

                    // Axial symmetry logic
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
                            cursorManager.selectCursor(mirroredCursor.getId());
                            instruction.mirrorExecute(true, parameters);
                            cursorManager.selectCursor(currentCursorId);
                        }
                    }
                } else {
                    Cursor currentCursor = cursorManager.getCurrentCursor();
                    Cursor mirroredCursor = new Cursor(
                            centralSymmetry(currentCursor.getPosition(), pointA),
                            currentCursor.getAngle() + 180, // Angle adapted to +180
                            currentCursor.getColor(),
                            currentCursor.getWidth(),
                            currentCursor.getOpacity(),
                            currentCursor.getId() * 31,
                            scene
                    );
                    cursorManager.addCursor(mirroredCursor);

                    // Central symmetry logic
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
                            cursorManager.selectCursor(mirroredCursor.getId());
                            instruction.mirrorExecute(false, parameters);
                            cursorManager.selectCursor(currentCursorId);
                        }
                    }
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown block instruction type: " + type);
        }
    }

    @Override
    public void mirrorExecute(boolean Axial,Object parameter) throws ErrorLogger {
        switch (type) {
            case "IF":
                if (((BooleanSupplier) condition).getAsBoolean()) {
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.mirrorExecute(Axial,parameter);
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
                            instruction.mirrorExecute(Axial,parameter);
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
                            instruction.mirrorExecute(Axial,parameter);
                        }
                    }
                }
                break;

            case "MIMIC":
                int cursorID = (Integer) parameters;
                int originalCursor = cursorManager.getCurrentCursor().getId();

                cursorManager.selectCursor(cursorID);
                for (Instruction instruction : instructions) {
                    if (instruction.isValid()) {
                        instruction.mirrorExecute(Axial,parameter);
                        cursorManager.selectCursor(originalCursor);
                        instruction.mirrorExecute(Axial,parameter);
                        cursorManager.selectCursor(cursorID);
                    }
                }

                cursorManager.selectCursor(originalCursor);
                break;
            case "MIRROR":
                Point[] points = parsePoints((String) parameters);
                Point pointA = points[0];
                int currentCursorId = cursorManager.getCurrentCursor().getId();

                if (points.length > 1) {
                    Point pointB = points[1];

                    Cursor currentCursor = cursorManager.getCurrentCursor();
                    Cursor mirroredCursor = new Cursor(
                            axialSymmetry(currentCursor.getPosition(), pointA, pointB),
                            currentCursor.getAngle() + 180, // Angle adapted to +180
                            currentCursor.getColor(),
                            currentCursor.getWidth(),
                            currentCursor.getOpacity(),
                            currentCursor.getId() * 31,
                            scene
                    );
                    cursorManager.addCursor(mirroredCursor);

                    // Axial symmetry logic
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
                            cursorManager.selectCursor(mirroredCursor.getId());
                            instruction.mirrorExecute(true, parameters);
                            cursorManager.selectCursor(currentCursorId);
                        }
                    }
                } else {
                    Cursor currentCursor = cursorManager.getCurrentCursor();
                    Cursor mirroredCursor = new Cursor(
                            centralSymmetry(currentCursor.getPosition(), pointA),
                            currentCursor.getAngle() + 180, // Angle adapted to +180
                            currentCursor.getColor(),
                            currentCursor.getWidth(),
                            currentCursor.getOpacity(),
                            currentCursor.getId() * 31,
                            scene
                    );
                    cursorManager.addCursor(mirroredCursor);

                    // Central symmetry logic
                    for (Instruction instruction : instructions) {
                        if (instruction.isValid()) {
                            instruction.execute();
                            cursorManager.selectCursor(mirroredCursor.getId());
                            instruction.mirrorExecute(false, parameters);
                            cursorManager.selectCursor(currentCursorId);
                        }
                    }
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown block instruction type: " + type);
        }
    }

    public static Point axialSymmetry(Point p, Point a, Point b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double t = ((p.getX() - a.getX()) * dx + (p.getY() - a.getY()) * dy) / (dx * dx + dy * dy);
        double x_p = a.getX() + t * dx;
        double y_p = a.getY() + t * dy;
        double x_s = 2 * x_p - p.getX();
        double y_s = 2 * y_p - p.getY();
        return new Point(x_s, y_s);
    }

    public static Point centralSymmetry(Point p, Point c) {
        double x_s = 2 * c.getX() - p.getX();
        double y_s = 2 * c.getY() - p.getY();
        return new Point(x_s, y_s);
    }

    private Point parsePoint(String pointString) {
        // Remove parentheses and trim any whitespace
        pointString = pointString.replace("(", "").replace(")", "").trim();

        // Split the string by comma to get the coordinates
        String[] coordinates = pointString.split(",");
        double x = Double.parseDouble(coordinates[0].trim());
        double y = Double.parseDouble(coordinates[1].trim());

        return new Point(x, y);
    }

    private Point[] parsePoints(String pointsString) {
        // Remove parentheses and split by "),("
        String[] pointStrings = pointsString.split("\\),\\(");

        // Parse each point
        Point[] points = new Point[pointStrings.length];
        for (int i = 0; i < pointStrings.length; i++) {
            points[i] = parsePoint(pointStrings[i]);
        }
        return points;
    }

    public boolean isValid() {
        switch (type) {
            case "IF":
                if( condition instanceof BooleanSupplier && instructions != null){
                    return true;
                }

            case "FOR":
                if( parameters.length >= 2 && parameters[0] instanceof String && parameters[1] instanceof Integer
                        && (parameters.length < 3 || parameters[2] instanceof Integer)
                        && (parameters.length < 4 || parameters[3] instanceof Integer)
                        && instructions != null){
                    return true;
                }

            case "WHILE":
                if( condition instanceof BooleanSupplier && instructions != null){
                    return true;
                }

            case "MIMIC":
                if parameters instanceof Integer && instructions != null){
                    return true;
            }

            case "MIRROR":
                if (parameters instanceof String) {
                    String params = (String) parameters;
                    String[] points = params.split("\\),\\(");
                    if (points.length == 1 || points.length == 2) {
                        for (String point : points) {
                            try {
                                parsePoint(point);
                            } catch (Exception e) {
                                return false;
                            }
                        }
                        if( instructions != null){
                            return true;
                        }
                    }
                }
                return false;

            default:
                return false;
        }
    }
}

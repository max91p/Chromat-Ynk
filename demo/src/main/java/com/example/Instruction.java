package com.example;

/**
 * Abstract class representing an instruction for drawing.
 */
public abstract class Instruction {

    /**
     * Executes the instruction on the given cursor.
     *
     * @param cursor the cursor to execute the instruction on
     */
    //public abstract void execute(Cursor cursor);

    public abstract void execute();

    /**
     * Checks if the instruction is valid.
     *
     * @return true if the instruction is valid, false otherwise
     */
    public abstract boolean isValid() throws ErrorLogger;
}
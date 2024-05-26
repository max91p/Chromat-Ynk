package com.example;

/**
 * Abstract class representing an instruction for drawing.
 */
public abstract class Instruction {


    /**
     * Executes the instruction on the current cursor
     * @throws ErrorLogger
     */
    public abstract void execute() throws ErrorLogger;

    /**
     * Executes mirror instructions
     * @param axial
     * @param parameter
     * @throws ErrorLogger
     */
    public abstract void mirrorExecute(boolean axial,Object parameter)throws ErrorLogger;

    /**
     * Checks if the instruction is valid.
     *
     * @return true if the instruction is valid, false otherwise
     */
    public abstract boolean isValid() throws ErrorLogger;
}
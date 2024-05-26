package com.example;

/**
 * Class to handle error
 */
public class ErrorLogger extends Exception{



    public ErrorLogger(String message){
        super(message);
    }
    public ErrorLogger(Throwable cause){
        super(cause);
    }
    public ErrorLogger(String message, Throwable cause){
        super(message, cause);
    }

    @Override
    public String toString() {
        return getMessage();
    }

    public void logError(){
        if (getCause() != null){
            System.err.println("Error caused by : " + getCause());
        }
        else {
            System.err.println(toString());
        }
    }
}
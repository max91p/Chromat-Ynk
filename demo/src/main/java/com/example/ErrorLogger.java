package com.example;

public class ErrorLogger extends Exception{

    /*public ErrorLogger(){
        super();
    }*/

    public ErrorLogger(String message){
        super(message);
    }
    public ErrorLogger(Throwable cause){
        super(cause);
    }
    public ErrorLogger(String message, Throwable cause){
        super(message, cause);
    }
}

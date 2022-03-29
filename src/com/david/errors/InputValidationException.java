package com.david.errors;

//used to inform of invalid inputs predifined by regex
public class InputValidationException extends Exception{

    public InputValidationException(String message) {
        super("Input: " + message + ". not valid");
    }
}

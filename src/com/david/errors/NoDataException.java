package com.david.errors;

//used when data in database returns null
public class NoDataException extends Exception{

    public NoDataException() {
        super("Requested DATA does not exist in the data base");
    }
}

package com.david.errors;

import com.david.beans.enums.TableName;
import com.david.beans.enums.CRUDOperation;

//used by database initializer while creating or droping tables
public class DBException extends Exception{

    public DBException(){
        super("Failed to connect to database");
    }

    public DBException(TableName tN, CRUDOperation op) {
        super("Failed to " + op + " database table: " + tN);
    }
}

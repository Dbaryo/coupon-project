package com.david.errors;

import com.david.beans.enums.EntityType;

// used in login operations
public class WrongLoginInfoException extends Exception{

    public WrongLoginInfoException(EntityType eT, String msg) {
        super(msg + " for " + eT +" user");
    }
}

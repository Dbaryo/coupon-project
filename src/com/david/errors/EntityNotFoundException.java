package com.david.errors;

import com.david.beans.enums.EntityType;
//used before making writing operations on entities
public class EntityNotFoundException extends Exception{

    public EntityNotFoundException(EntityType eT) {
        super("\n" + eT + " was not found in database");
    }
}

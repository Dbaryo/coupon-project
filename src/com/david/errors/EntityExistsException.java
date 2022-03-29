package com.david.errors;

import com.david.beans.enums.EntityType;

// used to inform if an entity already exists in the database before creating it
public class EntityExistsException extends Exception{

    public EntityExistsException(EntityType eT, String name) {
        super(eT + ": " + name + " already exists in the data base with similar details");
    }
}

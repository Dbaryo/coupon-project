package com.david.errors;

import com.david.beans.enums.CRUDOperation;
import com.david.beans.enums.EntityType;
import com.david.beans.enums.TableName;

//used by DAO classess to inform of failed operations
public class EntityCRUDDaoException extends Exception{
    public EntityCRUDDaoException(EntityType eT, CRUDOperation op, String msg){
        super("Failed to " + op + " " + eT + ", " + msg );
    }

    public EntityCRUDDaoException(EntityType eT, CRUDOperation op){
        super("Failed to " + op + " " + eT + " in the data base");
    }

    public EntityCRUDDaoException(EntityType eT, CRUDOperation op, Object o){
        super("Failed to " + op + " " + eT + " with input: " + o.toString());
    }

    public EntityCRUDDaoException(CRUDOperation op, TableName tN, String msg){
        super("Failed to " + op + " record in table: " + tN + " - " + msg);
    }
}

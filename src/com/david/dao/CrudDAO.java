package com.david.dao;

import java.util.List;

public interface CrudDAO<ID, Entity> {
    ID create(final Entity entity) throws Exception;
    Entity read(final Object input) throws  Exception;
    void update(final Entity entity) throws Exception;
    void delete(final ID id) throws Exception;
    List<Entity> readAll() throws Exception;

}

package com.david.dao;

import com.david.errors.EntityCRUDDaoException;
import com.david.beans.Customer;
import com.david.beans.enums.CRUDOperation;
import com.david.beans.enums.EntityType;
import com.david.util.ConnectionPool;
import com.david.dao.extraction_tools.CustomerExtractionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements CrudDAO<Long, Customer> {
    private CustomerDAO() {}


    private static final CustomerDAO instance = new CustomerDAO();
    Connection connection;

    public static CustomerDAO getInstance() { return instance; }

    @Override
    public Long create(Customer customer) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "INSERT INTO customers (first_name, last_name, email, password) VALUES(?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            if (!generatedKeysResult.next()) {
                throw new EntityCRUDDaoException(EntityType.CUSTOMER, CRUDOperation.CREATE, "Failed to retrive generated keys.");
            }
            return generatedKeysResult.getLong(1);
        } catch (SQLException | InterruptedException e) {
            throw new EntityCRUDDaoException(EntityType.CUSTOMER, CRUDOperation.CREATE);
        }finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    //can be used with id or email
    public Customer read(Object input) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "";
            ResultSet result = null;
            //checks the input type and casts it accordingly
            if (input instanceof String) {
                String email = (String)input;
                sqlStatement = "SELECT * FROM customers WHERE email = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, email);
                result = preparedStatement.executeQuery();
            }else {
                long id = (long)input;
                sqlStatement = "SELECT * FROM customers WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setLong(1, id);
                result = preparedStatement.executeQuery();
            }

            if (!result.next()) {
                return null;
            }

            return CustomerExtractionUtil.resultToCustomer(result);
        } catch (SQLException | InterruptedException e) {
            throw new EntityCRUDDaoException(EntityType.CUSTOMER, CRUDOperation.READ, input);
        }finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void update(Customer customer) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            preparedStatement.setLong(5, customer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new EntityCRUDDaoException(EntityType.CUSTOMER, CRUDOperation.UPDATE, customer.getId());
        }finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "DELETE FROM customers WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new EntityCRUDDaoException(EntityType.CUSTOMER, CRUDOperation.DELETE, id);
        }finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public ArrayList<Customer> readAll() throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "SELECT * FROM customers";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = preparedStatement.executeQuery();
            ArrayList<Customer> customers = new ArrayList<>();

            while (result.next()) {
                customers.add(CustomerExtractionUtil.resultToCustomer(result));
            }

            return customers;
        } catch (SQLException | InterruptedException e) {
            throw new EntityCRUDDaoException(EntityType.CUSTOMER, CRUDOperation.READ_ALL);
        }finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

}

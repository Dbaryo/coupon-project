package com.david.dao;

import com.david.errors.EntityCRUDDaoException;
import com.david.beans.Company;
import com.david.beans.enums.CRUDOperation;
import com.david.beans.enums.EntityType;
import com.david.errors.EntityNotFoundException;
import com.david.util.ConnectionPool;
import com.david.dao.extraction_tools.CompanyExtractionUtil;
import lombok.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class CompanyDAO implements CrudDAO<Long, Company> {

    private CompanyDAO() {
    }

    private static final CompanyDAO instance = new CompanyDAO();
    Connection connection;

    public static CompanyDAO getInstance() {
        return instance;
    }

    @Override
    public Long create(Company company) throws EntityCRUDDaoException, SQLException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "INSERT INTO companies (name, email, password) VALUES(?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, company.getPassword());
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            if (!generatedKeysResult.next()) {
                throw new EntityCRUDDaoException(EntityType.COMPANY, CRUDOperation.CREATE, "Failed to retrive generated keys.") {
                };
            }

            return generatedKeysResult.getLong(1);
        } catch (EntityCRUDDaoException | SQLException | InterruptedException e) {
            throw new EntityCRUDDaoException(EntityType.COMPANY, CRUDOperation.CREATE) {
            };
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    //can be used with id or email
    public Company read(Object input) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "";
            ResultSet result = null;
            //checks the input type and casts it accordingly
            if (input instanceof Long) {
                long id = (long)input;
                sqlStatement = "SELECT * FROM companies WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setLong(1, id);
                result = preparedStatement.executeQuery();
            }else {
                String email = (String)input;
                sqlStatement = "SELECT * FROM companies WHERE email = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, email);
                result = preparedStatement.executeQuery();
            }

            if (!result.next()) {
                return null;
            }

            return CompanyExtractionUtil.resultToCompany(result);
        } catch (SQLException | InterruptedException e) {
            throw new EntityCRUDDaoException(EntityType.COMPANY, CRUDOperation.READ, input);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }


    @Override
    public void update(Company company) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "UPDATE companies SET email = ?, password = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, company.getEmail());
            preparedStatement.setString(2, company.getPassword());
            preparedStatement.setLong(3, company.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new EntityCRUDDaoException(EntityType.COMPANY, CRUDOperation.UPDATE, company.getId());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "DELETE FROM companies WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException | InterruptedException e) {
            throw new EntityCRUDDaoException(EntityType.COMPANY, CRUDOperation.DELETE, id);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }

    }

    @Override
    public ArrayList<Company> readAll() throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "SELECT * FROM companies";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = preparedStatement.executeQuery();
            ArrayList<Company> companies = new ArrayList<>();

            while (result.next()) {
                companies.add(CompanyExtractionUtil.resultToCompany(result));
            }

            return companies;
        } catch (SQLException | InterruptedException e) {
            throw new EntityCRUDDaoException(EntityType.COMPANY, CRUDOperation.READ_ALL);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }
}

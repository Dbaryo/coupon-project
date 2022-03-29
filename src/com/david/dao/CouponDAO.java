package com.david.dao;


import com.david.beans.enums.TableName;
import com.david.errors.EntityCRUDDaoException;
import com.david.beans.Coupon;
import com.david.beans.enums.CRUDOperation;
import com.david.beans.enums.EntityType;
import com.david.util.ConnectionPool;
import com.david.dao.extraction_tools.CouponExtractionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDAO implements CrudDAO<Long, Coupon> {
    private CouponDAO() {
    }

    private static final CouponDAO instance = new CouponDAO();

    public static CouponDAO getInstance() {
        return instance;
    }

    Connection connection;

    @Override
    public Long create(Coupon coupon) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "INSERT INTO coupons (company_id, category, title, description, " +
                    "start_Date, end_Date, amount, price, image) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, coupon.getCompanyID());
            preparedStatement.setString(2, coupon.getCategory().toString());
            preparedStatement.setString(3, coupon.getTitle());
            preparedStatement.setString(4, coupon.getDescription());
            preparedStatement.setDate(5, Date.valueOf(coupon.getStartDate()));
            preparedStatement.setDate(6, Date.valueOf(coupon.getEndDate()));
            preparedStatement.setInt(7, coupon.getAmount());
            preparedStatement.setDouble(8, coupon.getPrice());
            preparedStatement.setString(9, coupon.getImage());
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            if (!generatedKeysResult.next()) {
                throw new EntityCRUDDaoException(EntityType.COUPON, CRUDOperation.CREATE, "Failed to retrieve generated keys.");
            }

            return generatedKeysResult.getLong(1);
        } catch (SQLException e) {
            System.err.println(e);
            throw new EntityCRUDDaoException(EntityType.COUPON, CRUDOperation.CREATE);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    //can be used with id only
    public Coupon read(Object input) throws Exception {
        long id = (long) input;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "SELECT * FROM coupons WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                return null;
            }

            return CouponExtractionUtil.resultToCoupon(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new EntityCRUDDaoException(EntityType.COUPON, CRUDOperation.READ, id);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void update(Coupon coupon) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "UPDATE coupons SET title = ?, description = ?, end_date = ?, amount = ?, price = ?, image = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, coupon.getTitle());
            preparedStatement.setString(2, coupon.getDescription());
            preparedStatement.setDate(3, Date.valueOf(coupon.getEndDate()));
            preparedStatement.setInt(4, coupon.getAmount());
            preparedStatement.setDouble(5, coupon.getPrice());
            preparedStatement.setString(6, coupon.getImage());
            preparedStatement.setLong(7, coupon.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new EntityCRUDDaoException(EntityType.COUPON, CRUDOperation.UPDATE, coupon.getId());
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "DELETE FROM coupons WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
            throw new EntityCRUDDaoException(EntityType.COUPON, CRUDOperation.DELETE, id);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public ArrayList<Coupon> readAll() throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "SELECT * FROM coupons";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = preparedStatement.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();

            while (result.next()) {
                coupons.add(CouponExtractionUtil.resultToCoupon(result));
            }

            return coupons;
        } catch (SQLException e) {
            System.err.println(e);
            throw new EntityCRUDDaoException(EntityType.COUPON, CRUDOperation.READ_ALL);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }
    //reeads all coupons by company id
    public ArrayList<Coupon> readAllByCompany(Long comapnyID) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "SELECT * FROM coupons WHERE company_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, comapnyID);
            ResultSet result = preparedStatement.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();

            while (result.next()) {
                coupons.add(CouponExtractionUtil.resultToCoupon(result));
            }

            return coupons;
        } catch (SQLException e) {
            System.err.println(e);
            throw new EntityCRUDDaoException(EntityType.COUPON, CRUDOperation.READ_ALL);
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }
}

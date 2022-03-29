package com.david.dao;

import com.david.beans.enums.CRUDOperation;
import com.david.beans.enums.EntityType;
import com.david.beans.enums.TableName;
import com.david.errors.EntityCRUDDaoException;
import com.david.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;

public class CustomerToCouponDAO {

    //An additional DAO class that deals with table customer_to_coupon, performs add, read and delete functions only

    Connection connection;
    private CustomerToCouponDAO() {
    }

    private static final CustomerToCouponDAO instance = new CustomerToCouponDAO();
    public static CustomerToCouponDAO getInstance() {
        return instance;
    }


    public void addToCouponCustomer(long customerID, long couponID) throws Exception {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sqlStatement = "INSERT INTO customer_to_coupon (customer_id, coupon_id) VALUES(?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, customerID);
            preparedStatement.setLong(2, couponID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new EntityCRUDDaoException(CRUDOperation.CREATE, TableName.CUSTOMER_TO_COUPON, " ");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }
    // can be used with coupon id to get list of customers who purchased it
    // or with customer id to get all customer purchased coupons
    public ArrayList<Long> readFromCouponCustomer(long id, EntityType idType) throws Exception {
        String sqlStatement = "";
        String columnName = "";
        ArrayList<Long> idList = new ArrayList<>();
        if (idType.equals(EntityType.CUSTOMER)){
            sqlStatement = "SELECT coupon_id FROM customer_to_coupon WHERE customer_id = ?";
            columnName = "coupon_id";
        }
        if (idType.equals(EntityType.COUPON)){
            sqlStatement = "SELECT customer_id FROM customer_to_coupon WHERE coupon_id = ?";
            columnName = "customer_id";
        }
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                idList.add(result.getLong(columnName));
            }
            return idList;
        } catch (SQLException | InterruptedException e) {
            throw new EntityCRUDDaoException(CRUDOperation.READ, TableName.CUSTOMER_TO_COUPON, "");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    // can be use by coupon id or customer id
    public void deleteFromCustomerCoupon(Long id, EntityType eT) throws Exception {
        String sqlStatement = "";
        if (eT.equals(EntityType.CUSTOMER)){
            sqlStatement = "DELETE FROM customer_to_coupon WHERE customer_id = ?";
        }
        if (eT.equals(EntityType.COUPON)){
            sqlStatement = "DELETE FROM customer_to_coupon WHERE coupon_id = ?";
        }
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new EntityCRUDDaoException(CRUDOperation.DELETE, TableName.CUSTOMER_TO_COUPON, "");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

}

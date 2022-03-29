package com.david.dao.extraction_tools;

import com.david.beans.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerExtractionUtil {

    public static Customer resultToCustomer(ResultSet result) throws SQLException {
        return new Customer(
                result.getLong("id"),
                result.getString("first_name"),
                result.getString("last_name"),
                result.getString("email"),
                result.getString("password"));
    }
}

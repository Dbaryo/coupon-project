package com.david.dao.extraction_tools;

import com.david.beans.Company;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyExtractionUtil {

    public static Company resultToCompany(ResultSet result) throws SQLException {
        return new Company(
                result.getLong("id"),
                result.getString("name"),
                result.getString("email"),
                result.getString("password"));
    }
}
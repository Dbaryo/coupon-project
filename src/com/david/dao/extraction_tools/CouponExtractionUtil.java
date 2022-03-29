package com.david.dao.extraction_tools;

import com.david.beans.Coupon;
import com.david.beans.enums.CouponCategory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CouponExtractionUtil {

    public static Coupon resultToCoupon(ResultSet result) throws SQLException {
        return new Coupon(
                result.getLong("id"),
                result.getLong("company_id"),
                CouponCategory.valueOf(result.getString("category")),
                result.getString("title"),
                result.getString("description"),
                result.getDate("start_Date").toLocalDate(),
                result.getDate("end_Date").toLocalDate(),
                result.getInt("amount"),
                result.getDouble("price"),
                result.getString("image"));
    }
}


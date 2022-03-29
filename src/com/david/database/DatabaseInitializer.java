package com.david.database;

import com.david.errors.DBException;
import com.david.beans.enums.TableName;
import com.david.beans.enums.CRUDOperation;
import com.david.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseInitializer {
    public DatabaseInitializer(){}

    private static Connection connection;


    public static void createTables() throws DBException, SQLException, InterruptedException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            createCategoriesTable();
            createCompaniesTable();
            createCustomersTable();
            createCouponsTable();
            createCustomerToCouponTable();
            setupCategoriesInTable();
            System.out.println("All tables were initialized in data base");
        }finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    public static void createCategoriesTable() throws DBException {
        String sql = "CREATE TABLE IF NOT EXISTS `categories` (\n" +
                "  `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "  `name` enum('FOOD','ELECTRICITY','RESTAURANT','VACATION') NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(TableName.CATEGORIES, CRUDOperation.CREATE);
        }
    }

    public static void createCompaniesTable() throws DBException {
        String sql = "CREATE TABLE IF NOT EXISTS `companies` (\n" +
                "  `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(45) NOT NULL,\n" +
                "  `email` varchar(45) NOT NULL,\n" +
                "  `password` varchar(45) NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `name_UNIQUE` (`name`),\n" +
                "  UNIQUE KEY `email_UNIQUE` (`email`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(TableName.COMPANIES, CRUDOperation.CREATE);
        }
    }

    public static void createCustomersTable() throws DBException {
        String sql = "CREATE TABLE IF NOT EXISTS `customers` (\n" +
                "  `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "  `first_name` varchar(45) NOT NULL,\n" +
                "  `last_name` varchar(45) NOT NULL,\n" +
                "  `email` varchar(45) NOT NULL,\n" +
                "  `password` varchar(45) NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `email_UNIQUE` (`email`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(TableName.CUSTOMERS, CRUDOperation.CREATE);
        }
    }

    public static void createCouponsTable() throws DBException {
        String sql = "CREATE TABLE IF NOT EXISTS `coupons` (\n" +
                "  `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "  `company_id` bigint NOT NULL,\n" +
                "  `category` varchar(45) NOT NULL,\n" +
                "  `title` varchar(45) NOT NULL,\n" +
                "  `description` varchar(45) DEFAULT NULL,\n" +
                "  `start_date` date NOT NULL,\n" +
                "  `end_date` date NOT NULL,\n" +
                "  `amount` int NOT NULL,\n" +
                "  `price` varchar(45) NOT NULL,\n" +
                "  `image` varchar(45) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  CONSTRAINT `company_id` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(TableName.COUPONS, CRUDOperation.CREATE);
        }
    }

    public static void createCustomerToCouponTable() throws DBException {
        String sql = "CREATE TABLE IF NOT EXISTS `customer_to_coupon` (\n" +
                "  `customer_id` bigint NOT NULL,\n" +
                "  `coupon_id` bigint NOT NULL,\n" +
                "  INDEX `customer.id_idx` (`customer_id` ASC) VISIBLE,\n" +
                "  INDEX `coupon.id_idx` (`coupon_id` ASC) VISIBLE,\n" +
                "  CONSTRAINT `coupon.id` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`),\n" +
                "  CONSTRAINT `customer.id` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(TableName.CUSTOMER_TO_COUPON, CRUDOperation.CREATE);
        }
    }

    public static void dropTables() throws DBException, SQLException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String query = "DROP TABLE `coupon_project`.`coupons`, `coupon_project`.`customer_to_coupon`," +
                    " `coupon_project`.`customers`, `coupon_project`.`categories`, `coupon_project`.`companies` ";
            connection.prepareStatement(query).execute();
            System.out.println("All tables have been deleted");
        } catch (SQLException | InterruptedException e) {
            throw new DBException(TableName.ALL, CRUDOperation.DROP);
        }finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }
    // to add categories need to add manuly querry lines here as well in CouponCategory ENUM
    public static void setupCategoriesInTable() throws DBException {
        try {
            String query =  "INSERT INTO `coupon_project`.`categories` (`name`) VALUE ('FOOD')";
            String query1 = "INSERT INTO `coupon_project`.`categories` (`name`) VALUE ('ELECTRICITY')";
            String query2 = "INSERT INTO `coupon_project`.`categories` (`name`) VALUE ('RESTAURANT');";
            String query3 = "INSERT INTO `coupon_project`.`categories` (`name`) VALUE ('VACATION');";


            List<String> queries = new ArrayList<>();
            queries.add(query);
            queries.add(query1);
            queries.add(query2);
            queries.add(query3);

            for (String q : queries) {
                connection.prepareStatement(q).execute();
            }
            System.out.println("Categories table in * coupons_project * has been created with all categories");
        } catch (SQLException e) {
            throw new DBException(TableName.CATEGORIES, CRUDOperation.UPDATE);
        }
    }
}

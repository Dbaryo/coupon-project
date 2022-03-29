package com.david.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Stack;

// manging connections to database (taken from your project)
public class ConnectionPool {

    private static final int NUMBER_OF_CONNECTIONS = 5;
    private final String DB_CONFIG_FILE = "C:\\Users\\david\\IdeaProjects\\coupon-project\\src\\com\\david\\configurations\\db_Config";
    private final String SQL_URL = "sqlUrl";
    private final String SQL_USER = "sqlUser";
    private final String SQL_PASSWORD = "sqlPassword";
    private String sqlUrl;
    private String sqlUser;
    private String sqlPassword;
    private static ConnectionPool instance = null;
    private final Stack<Connection> connections = new Stack<>();

    private ConnectionPool() throws SQLException {
        System.out.println("Created new connection pool");
        openAllConnections();
    }

    public static ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    private void readDBConfig() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(DB_CONFIG_FILE));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(SQL_URL)) {
                sqlUrl = ConfigReader.extractPropertyValue(line);
            }

            if (line.contains(SQL_USER)) {
                sqlUser = ConfigReader.extractPropertyValue(line);
            }

            if (line.contains(SQL_PASSWORD)) {
                sqlPassword = ConfigReader.extractPropertyValue(line);
            }
        }
    }

    private void openAllConnections() throws SQLException {
        try {
            readDBConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int counter = 0; counter < NUMBER_OF_CONNECTIONS; counter++) {
            final Connection connection = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            connections.push(connection);
        }
    }

    public void closeAllConnections() throws InterruptedException {
        synchronized (connections) {
            while (connections.size() < NUMBER_OF_CONNECTIONS){
                connections.wait();
            }
            connections.removeAllElements();
        }
    }

    public Connection getConnection() throws InterruptedException {
        synchronized (connections) {
            final long start = Calendar.getInstance().getTimeInMillis();
            if (connections.isEmpty()) {
                System.out.println(" ");
                //System.out.println((Thread.currentThread().getName() + " is waiting for an available connection"));
            }
            while (connections.isEmpty()) {
                connections.wait();
            }
            final long end = Calendar.getInstance().getTimeInMillis();
            final long duration = end - start;
            //System.out.println((Thread.currentThread().getName() + " found available connection after " + duration + " ms"));
            return connections.pop();
        }
    }

    public void returnConnection(final Connection connection) {
        synchronized (connections) {
            if (connection == null) {
                System.out.println(" ");
                //System.out.println("Attempt to return null connection terminated");
                return;
            }
            connections.push(connection);
            //System.out.println((Thread.currentThread().getName() + " is returning it's connection, now there are " + connections.size()));
            connections.notifyAll();
        }
    }
}
package com.david.task;

import com.david.database.DatabaseInitializer;
import com.david.util.ConnectionPool;

import java.time.LocalDateTime;

// a class to activate all the test task classes and database tests
public class TestTask {

    public TestTask() {
        new TestData();
    }

    AdminTestTask adTestTask = new AdminTestTask();
    CompanyTestTask comTestTask = new CompanyTestTask();
    CustomerTestTask cuTestTask = new CustomerTestTask();
    TaskScheduler ts;

    public void testAll() {

        dataBaseInitializer();

        dailyTaskStart();

        adTestTask.adminTests();
        System.out.println("=============================================================================================================================================");
        System.out.println("=============================================================================================================================================");
        System.out.println("=============================================================================================================================================");
        comTestTask.companyTests();
        System.out.println("=============================================================================================================================================");
        System.out.println("=============================================================================================================================================");
        System.out.println("=============================================================================================================================================");
        cuTestTask.customerTests();
        System.out.println("=============================================================================================================================================");
        System.out.println("=============================================================================================================================================");
        System.out.println("=============================================================================================================================================");
        adTestTask.secondAdminTests();
        closeAllConnections();
        dailyTaskEnd();

    }

    public void dataBaseInitializer() {
        try {
            //first dropping existing tables in the database
            DatabaseInitializer.dropTables();
            //initializing tables in the database
            DatabaseInitializer.createTables();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    //starting daily expired coupon deletion task
    public void dailyTaskStart() {
        //24H = 86400s
        ts = new TaskScheduler(100000);
    }

    public void dailyTaskEnd() {
        ts.stopDailyTask();
    }

    // closing all connections in connection pool
    public void closeAllConnections() {
        try {
            ConnectionPool.getInstance().closeAllConnections();
            System.out.println("Connection pool was shutdown at: " + LocalDateTime.now());
        } catch (Exception e) {
            System.out.println("Connections failed to shutdown");
        }
    }
}

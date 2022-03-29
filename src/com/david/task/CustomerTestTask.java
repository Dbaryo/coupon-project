package com.david.task;

import com.david.beans.Coupon;
import com.david.beans.Customer;
import com.david.beans.enums.CouponCategory;
import com.david.beans.enums.EntityType;
import com.david.facade.ClientFacade;
import com.david.facade.CustomerFacade;
import com.david.login.LoginManager;
import java.util.Random;

// a class to demonstrate CUSTOMER operations with made up data
public class CustomerTestTask {

    LoginManager loginManager = LoginManager.getInstance();

    //login as Company returns ClientFacade so casting is needed
    public CustomerFacade customerLoginTest(String logEmail, String logPassword) {
        ClientFacade clientFacade = loginManager.login(logEmail, logPassword, EntityType.CUSTOMER);
        if (clientFacade instanceof CustomerFacade) {
            return (CustomerFacade) clientFacade;
        }
        return null;
    }

    public void customerTests(){
        System.out.println(" \n login with each customer and buying 3 random coupons each\n" +
                "         one login wil fail because customer was deleted in test (id 5)\n" +
                "         another will fail because customer was updated and now details wont match (id 3)");
        for (Customer customer : TestData.dataCustomers) {
            Random random = new Random();
            CustomerFacade cuF = customerLoginTest(customer.getEmail(), customer.getPassword());
            if (cuF != null) {
                for (int i = 0; i < 3 ; i++) {
                    Coupon randomCoupon = TestData.dataCoupons.get(random.nextInt(12));
                    cuF.purchaseCoupon(customer, randomCoupon);
                }
            }
        }
        System.out.println("\n Login with customer id 1 for customer facade");
        CustomerFacade cuF = customerLoginTest("spiderman@marvel.com", "spiderman");
        System.out.println("\n Printing customer (id 1) coupons");
        System.out.println(cuF.getCustomerCoupons(TestData.dataCustomers.get(0)));
        System.out.println("\n Printing customer (id 1) coupons by category FOOD (because its random may be empty)");
        System.out.println(cuF.getCustomerCoupons(TestData.dataCustomers.get(0), CouponCategory.FOOD));
        System.out.println("\n Printing customer (id 1) coupons by max price 250");
        System.out.println(cuF.getCustomerCoupons(TestData.dataCustomers.get(0), 250d));
        System.out.println("\n Printing customer (id 6) details ");
        System.out.println(cuF.getCustomerDetails(6L, true));
    }
}

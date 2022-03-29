package com.david.task;

import com.david.beans.Company;
import com.david.beans.Coupon;
import com.david.beans.Customer;
import com.david.beans.enums.CouponCategory;

import java.util.ArrayList;

// made up data for tests
public class TestData {

    public TestData() {
        addCompnies();
        addCustomers();
        addCoupons();
    }

    public static ArrayList<Company> dataCompanies = new ArrayList<>();
    public static ArrayList<Customer> dataCustomers = new ArrayList<>();
    public static ArrayList<Coupon> dataCoupons = new ArrayList<>();
    public static ArrayList<Coupon> additionalDataCoupons = new ArrayList<>();


    public static void addCompnies(){
        dataCompanies.add(new Company("Vitrina", "vitrina@gmail.com", "vitrina"));
        dataCompanies.add(new Company("Green Cat", "greencat@gmail.com", "greencat"));
        dataCompanies.add(new Company("Apple", "apple@gmail.com", "apple"));
        dataCompanies.add(new Company("Samsung", "samsung@gmail.com", "samsung"));
        dataCompanies.add(new Company("Mega", "mega@gmail.com", "mega"));
        dataCompanies.add(new Company("Rami levi", "ramilevi@gmail.com", "ramilevi"));
        dataCompanies.add(new Company("Ista", "iste@gmail.com", "ista"));
        dataCompanies.add(new Company("El Al", "elal@gmail.com", "elal"));

    }



    public static void addCustomers(){
        dataCustomers.add(new Customer("Peter", "Parker", "spiderman@marvel.com", "spiderman"));
        dataCustomers.add(new Customer("Bruce", "Wayne", "batman@dc.com", "batman"));
        dataCustomers.add(new Customer("Tony", "Stark", "ironman@marvel.com", "ironman"));
        dataCustomers.add(new Customer("Dianna", "Prince", "wonderwoman@dc.com", "wonderwoman"));
        dataCustomers.add(new Customer("Jean", "grey", "phoenix@marvel.com", "phoenix"));
        dataCustomers.add(new Customer("Natasha", "Romanoff", "blackwidow@marvel.com", "blackwidow"));

    }

    public static void addCoupons(){
        //index = 0
        dataCoupons.add(new Coupon(1L, CouponCategory.RESTAURANT, "Discount","100 NIS discount",
                "2022-04-01", "2022-12-31", 50, 65d, ""));
        dataCoupons.add(new Coupon(1L, CouponCategory.RESTAURANT, "1+1","Two eat for the price of one",
                "2022-03-29", "2022-12-31", 100, 100d, ""));
        dataCoupons.add(new Coupon(4L, CouponCategory.ELECTRICITY, "1+1", "on all accessories",
                "2022-03-31", "2022-05-31", 50, 100d, "" ));
        dataCoupons.add(new Coupon(4L, CouponCategory.ELECTRICITY, "20% off", "on all smart watches",
                "2022-03-31", "2022-07-31", 100, 100d, "" ));
        dataCoupons.add(new Coupon(5L, CouponCategory.FOOD, "200 NIS gift card", " ",
                "2022-04-01", "2022-07-31", 1000, 150d, ""));
        dataCoupons.add(new Coupon(5L, CouponCategory.FOOD, "1000 NIS gift card", " ",
                "2022-04-01", "2022-07-31", 300, 700d, ""));
        dataCoupons.add(new Coupon(6L, CouponCategory.FOOD, "300 NIS gift card", " ",
                "2022-04-01", "2022-07-31", 200, 250d, ""));
        dataCoupons.add(new Coupon(6L, CouponCategory.FOOD, "1200 NIS gift card", " ",
                "2022-04-01", "2022-07-31", 200, 900d, ""));
        dataCoupons.add(new Coupon(7L, CouponCategory.VACATION, "Amsterdam", "Flights and Hotel for 5 nights",
                "2022-04-13", "2022-04-30", 300, 2500d,""));
        dataCoupons.add(new Coupon(7L, CouponCategory.VACATION, "London", "Flights and Hotel for 2 nights weekends",
                "2022-04-01", "2022-07-31", 300, 4000d,""));
        dataCoupons.add(new Coupon(8L, CouponCategory.VACATION, "Sydney", "Back and forward flights",
                "2022-07-01", "2022-10-01", 1000, 4000d, ""));
        dataCoupons.add(new Coupon(8L, CouponCategory.VACATION, "Miami", "Back and forward flights",
                "2022-04-01", "2022-07-01", 1000, 4000d, ""));
        // index = 0, wrong data coupons for coupon update - dates
        additionalDataCoupons.add(new Coupon(1L, CouponCategory.RESTAURANT, "wrong dates","wrong dates",
                "2022-12-31", "2022-04-01", 10, 10d, ""));
        // index = 1, data coupon for coupon update
        additionalDataCoupons.add(new Coupon(1L, 1L, CouponCategory.RESTAURANT, "DISCOUNT","100 NIS discount",
                "2022-04-01", "2022-12-31", 200, 65d, ""));
    }
}

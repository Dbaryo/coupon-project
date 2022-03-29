package com.david.task;

import com.david.beans.Company;
import com.david.beans.Coupon;
import com.david.beans.enums.CouponCategory;
import com.david.beans.enums.EntityType;
import com.david.facade.ClientFacade;
import com.david.facade.CompanyFacade;
import com.david.login.LoginManager;

// a class to demonstrate COMPANY operations with made up data
public class CompanyTestTask {

    LoginManager loginManager = LoginManager.getInstance();


    //login as Company returns ClientFacade so casting is needed
    public CompanyFacade companyLoginTest(String logEmail, String logPassword) {
        ClientFacade clientFacade = loginManager.login(logEmail, logPassword, EntityType.COMPANY);
        if (clientFacade instanceof CompanyFacade) {
            return (CompanyFacade) clientFacade;
        }
        return null;
    }


    public void companyTests() {
        //login as all the  companies and creating 2 coupons for each
        for (Company company : TestData.dataCompanies) {
            CompanyFacade coF = companyLoginTest(company.getEmail(), company.getPassword());
            if (coF != null) {
                for (Coupon coupon : TestData.dataCoupons) {
                    if (company.getId() == coupon.getCompanyID()) {
                        coF.addCoupon(coupon);
                    }
                }
            }
        }
        System.out.println("login with wrong details:");
        CompanyFacade coF = companyLoginTest("drstrange@marvel.com", "drstrange");
        System.out.println("login with invalid details:");
        coF = companyLoginTest("ThanosRulz", "thanos");
        System.out.println("login with id 1L:");
        coF = companyLoginTest("vitrina@gmail.com", "vitrina");
        if (coF != null) {
            System.out.println("and adding coupon with same details:");
            coF.addCoupon(TestData.dataCoupons.get(0));
            System.out.println("and adding coupon with wrong dates:");
            coF.addCoupon(TestData.additionalDataCoupons.get(0));

            System.out.println("printing all company coupons before updating coupon id 1Ls  data");
            System.out.println(coF.getCompanyCoupons(1L));
            coF.updateCoupon(TestData.additionalDataCoupons.get(1));
            System.out.println(coF.getCompanyCoupons(1L) + "----AFTER UPDATE");
            System.out.println("deleting company (id 1L ) coupon (2L)");
            coF.deleteCoupon(2L);
            System.out.println(coF.getCompanyCoupons(1L));
            System.out.println("printing all company (id 1L) coupons by category RESTAURANT (after deleting coupon) ");
            System.out.println(coF.getCompanyCoupons(1L, CouponCategory.RESTAURANT));
            System.out.println("printing all company coupons by max price 70");
            System.out.println(coF.getCompanyCoupons(1L, 70D));
            System.out.println("printing company (id 5) details:");
            System.out.println(coF.getCompanyDetails(5L, true));

        }
    }
}

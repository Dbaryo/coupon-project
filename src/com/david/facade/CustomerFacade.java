package com.david.facade;

import com.david.beans.Coupon;
import com.david.beans.Customer;
import com.david.beans.enums.CouponCategory;
import com.david.beans.enums.EntityType;
import com.david.errors.*;
import com.david.util.InputValidationUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class CustomerFacade extends ClientFacade {
    private CustomerFacade() {
    }

    private static final CustomerFacade instance = new CustomerFacade();

    public static CustomerFacade getInstance() {
        return instance;
    }

    @Override
    public boolean login(String logEmail, String logPassword) {
        try {
            //input validation according to predefined regex
            if (InputValidationUtils.isEmailValid(logEmail) && InputValidationUtils.isPasswordValid(logPassword)) {
                //retrieving (if exists) the needed company from database
                Customer c = customerDAO.read(logEmail);
                //checking if company exists in the database
                if (c != null) {
                    //comparing passwords
                    if (logPassword.equals(c.getPassword())) {
                        return true;
                    } else {
                        throw new WrongLoginInfoException(EntityType.CUSTOMER, "Input password does not match");
                    }
                } else {
                    throw new EntityNotFoundException(EntityType.CUSTOMER);
                }
            } else {
                throw new InputValidationException("Email and\\or Password");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + logEmail);
            return false;
        }
    }
//-----------------------------------------------------------------------------------------------

    public synchronized void purchaseCoupon(Customer customer, Coupon wantedCoupon) {
        try {
            // checking if coupon exists in database
            if (couponDAO.read(wantedCoupon.getId()) != null) {
                // checking if coupon is not already purchased by customer
                if (!isAlreadyPurchased(customer, wantedCoupon)) {
                    // checking if whe coupon is not expired and in stock
                    if (!wantedCoupon.isExpired()) {
                        if (wantedCoupon.isInStock()) {
                            // decreasing 1 form coupon amount and updating database
                            wantedCoupon.setAmount(wantedCoupon.getAmount() - 1);
                            couponDAO.update(wantedCoupon);
                            // adding purchased coupon to customer purchased coupon in database
                            customerToCouponDAO.addToCouponCustomer(customer.getId(), wantedCoupon.getId());
                            System.out.println("coupon: " + wantedCoupon.getTitle() + " was successfully purchased by "
                                    + customer.getLastName() + " at: " + LocalDateTime.now());
                        } else {
                            throw new CouponPurchaseException(wantedCoupon, " out of stock");
                        }
                    } else {
                        throw new CouponPurchaseException(wantedCoupon, " coupon date expired");
                    }
                } else {
                    throw new CouponPurchaseException(wantedCoupon, " already purchased by this customer");
                }
            } else {
                throw new EntityNotFoundException(EntityType.COUPON);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " - " + wantedCoupon.getTitle() + ", " + wantedCoupon.getDescription());
        }
    }

    public ArrayList<Coupon> getCustomerCoupons(Customer customer) {
        ArrayList<Coupon> customersCoupons = new ArrayList<>();
        try {
            //checking if customer exists
            if (customerDAO.read(customer.getId()) != null) {
                // getting all customer purchase coupon IDs
                ArrayList<Long> customersCouponIDs = customerToCouponDAO.readFromCouponCustomer(customer.getId(), EntityType.CUSTOMER);
                // if there are ids retrieving the coupons and adding them to a list
                if (customersCouponIDs != null) {
                    for (Long id : customersCouponIDs) {
                        customersCoupons.add(couponDAO.read(id));
                    }
                }
            } else {
                throw new NoDataException();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return customersCoupons;
    }

    public ArrayList<Coupon> getCustomerCoupons(Customer customer, CouponCategory couponCategory) {
        try {
            // using the method above
            ArrayList<Coupon> customersCoupons = getCustomerCoupons(customer);
            ArrayList<Coupon> customersCouponsByCategory = new ArrayList<>();
            // if list is not empty checking if coupon category is the same and adding to list
            if (customersCoupons != null) {
                for (Coupon coupon : customersCoupons) {
                    if (coupon.getCategory().equals(couponCategory)) {
                        customersCouponsByCategory.add(coupon);
                    }
                }
                return customersCouponsByCategory;
            } else {
                throw new NoDataException();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<Coupon> getCustomerCoupons(Customer customer, double maxPrice) {
        try{
            // using the method above
        ArrayList<Coupon> customersCoupons = getCustomerCoupons(customer);
        ArrayList<Coupon> customersCouponsByPrice = new ArrayList<>();
            // if list is not empty checking if coupon price is lower than predefined max price and adding to list
        if (customersCoupons != null) {
            for (Coupon coupon : customersCoupons) {
                if (coupon.getPrice() <= maxPrice) {
                    customersCouponsByPrice.add(coupon);
                }
            }
            return customersCouponsByPrice;
        } else {
            System.out.println("Empty list");
            return null;
        }}catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }
    // if exists retrieving customer details with or without coupons
    public Customer getCustomerDetails(Long customerID, boolean withCoupons) {
        try {
            Customer tempCustomer = customerDAO.read(customerID);
            if (withCoupons) {
                tempCustomer.setCoupons(getCustomerCoupons(tempCustomer));
            }
            return tempCustomer;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //---------------------------------------------------------------------------------------------------

    //checking if customer and coupon appear in customer_to_coupon table in database
    public boolean isAlreadyPurchased(Customer customer, Coupon wantedCoupon) throws Exception {
        // retrieving all the customer purchased coupons
        ArrayList<Long> customersPurchasedCoupons = customerToCouponDAO.readFromCouponCustomer(customer.getId(), EntityType.CUSTOMER);
        // if list is not empty
        if (customersPurchasedCoupons != null) {
            // checking if the coupon was purchased by comparing IDs
            for (Long couponID : customersPurchasedCoupons) {
                if (couponID == wantedCoupon.getId()) {
                    return true;
                }
            }
        }
        return false;
    }


}

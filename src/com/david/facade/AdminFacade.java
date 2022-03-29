package com.david.facade;

import com.david.beans.Company;
import com.david.beans.Coupon;
import com.david.beans.Customer;
import com.david.beans.enums.EntityType;
import com.david.errors.EntityExistsException;
import com.david.errors.EntityNotFoundException;
import com.david.errors.InputValidationException;
import com.david.errors.WrongLoginInfoException;
import com.david.util.InputValidationUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class AdminFacade extends ClientFacade {


    private AdminFacade() {
    }

    private static final AdminFacade instance = new AdminFacade();
    //  login info for admin
    private final String ADMIN_USER_NAME = "admin@admin.com";
    private final String ADMIN_PASSWORD = "admin";

    public static AdminFacade getInstance() {
        return instance;
    }

    @Override
    public boolean login(String logEmail, String logPassword) {
        try {
            // checking if email and password match
            if (logEmail.equals(ADMIN_USER_NAME)) {
                if (logPassword.equals(ADMIN_PASSWORD)) {
                    return true;
                } else {
                    throw new WrongLoginInfoException(EntityType.ADMIN, "Input password does not match");
                }
            } else {
                throw new WrongLoginInfoException(EntityType.ADMIN, "Input user name does not match");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    //=====================================================================================================

    public void addCompany(Company company) {
        try {
            // verifying if company is not already in database
            if (!isCompanyExists(company)) {
                // checking input validation by predefined regex
                if (InputValidationUtils.isNameValid(company.getName())
                        && InputValidationUtils.isEmailValid(company.getEmail())) {
                    // creating the company and retrieving the auto incremented ID
                    company.setId(companyDAO.create(company));
                    System.out.println(company + " was successfully created and added to database by ADMIN at: " + LocalDateTime.now());
                } else {
                    throw new InputValidationException("email: " + company.getEmail() + " and\\or name: " + company.getName());
                }
            } else {
                throw new EntityExistsException(EntityType.COMPANY, company.getName());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void updateCompany(Company company) {
        try {
            // checking input validation by predefined regex
            if (InputValidationUtils.isEmailValid(company.getEmail())) {
                companyDAO.update(company);
                System.out.println("Company was successfully updated by ADMIN at: " + LocalDateTime.now());
            } else {
                throw new InputValidationException("email: " + company.getEmail());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void deleteCompany(long companyID) {
        System.out.println("Company with id " + companyID + ":");
        try {
            //checking if company exists in database
            if (companyDAO.read(companyID) == null) {
                throw new EntityNotFoundException(EntityType.COMPANY);
            } else {
                //retrieving all company coupons
                ArrayList<Coupon> companyCoupons = couponDAO.readAllByCompany(companyID);
                for (Coupon coupon : companyCoupons) {
                    // deleting coupons from customer purchases
                    customerToCouponDAO.deleteFromCustomerCoupon(coupon.getId(), EntityType.COUPON);
                    // deleting coupons from coupons table
                    couponDAO.delete(coupon.getId());
                }
                System.out.println("Company coupons were successfully deleted from customers purchased coupons by ADMIN at: " + LocalDateTime.now());
                System.out.println("Company coupons were successfully deleted from company coupon list by ADMIN at: " + LocalDateTime.now());
                //deleting company from companies table
                companyDAO.delete(companyID);
                System.out.println("Company was successfully deleted by ADMIN at: " + LocalDateTime.now());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Company> getAllCompanies() {
        try {
            return companyDAO.readAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Company getOneCompany(long companyID) {
        try {
            return companyDAO.read(companyID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //--------------------------------------------------------------------------------------------------------------

    public void addCustomer(Customer customer) {
        try {
            // verifying if customer is not already in database
            if (!isCustomerExists(customer)) {
                // checking validation by predefined regex
                if (InputValidationUtils.isEmailValid(customer.getEmail())
                        && InputValidationUtils.isNameValid(customer.getFirstName())
                        && InputValidationUtils.isNameValid(customer.getLastName())) {
                    // creating customer and retrieving the auto incremented id from database
                    customer.setId(customerDAO.create(customer));
                    System.out.println(customer + " was successfully created and added to database by ADMIN at:" + LocalDateTime.now());
                } else {
                    throw new InputValidationException("email: " + customer.getEmail() + " and\\or names: "
                            + customer.getFirstName() + " and\\or " + customer.getLastName());
                }
            } else {
                throw new EntityExistsException(EntityType.CUSTOMER, customer.getFirstName() + " " + customer.getLastName());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void updateCustomer(Customer customer) {
        try {
            // checking validation by predefined regex
            if (InputValidationUtils.isEmailValid(customer.getEmail())
                    && InputValidationUtils.isNameValid(customer.getFirstName())
                    && InputValidationUtils.isNameValid(customer.getLastName())) {
                // updating customer in database
                customerDAO.update(customer);
                System.out.println("Customer " + customer.getId() + ", was successfully updated by ADMIN at: " + LocalDateTime.now());
            } else {
                throw new InputValidationException(" email and\\or first and\\or last name");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void deleteCustomer(long customerID) {
        System.out.println("Customer with id " + customerID + ":");
        try {
            // checking if customer do exists in database
            if (customerDAO.read(customerID) == null) {
                throw new EntityNotFoundException(EntityType.CUSTOMER);
            } else {
                //deleting customer purchased coupons form database
                customerToCouponDAO.deleteFromCustomerCoupon(customerID, EntityType.CUSTOMER);
                System.out.println("Purchased coupons by Customer were successfully deleted by ADMIN at: " + LocalDateTime.now());
                //deleting customer from customers table in database
                customerDAO.delete(customerID);
                System.out.println("Customer was successfully deleted by ADMIN at: " + LocalDateTime.now());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Customer> getAllCustomers() {
        try {
            return customerDAO.readAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    // retrieving one customer with or without his coupons
    public Customer getOneCustomer(long customerID, boolean withCoupons) {
        try {
            Customer tempCustomer = customerDAO.read(customerID);
            if (withCoupons) {
                tempCustomer.setCoupons(getCouponListByCustomerID(customerID));
            }
            return tempCustomer;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // retrieving all purchased coupons of a customer
    public ArrayList<Coupon> getCouponListByCustomerID(long customerID) {
        ArrayList<Coupon> customersCoupons = new ArrayList<>();
        try {
            // checking if customer do exist in database
            if (customerDAO.read(customerID) != null) {
                // retrieving all customer coupons IDs by customer id
                ArrayList<Long> customersCouponIDs = customerToCouponDAO.readFromCouponCustomer(customerID, EntityType.CUSTOMER);
                // retrieving coupons and adding to array by coupons id
                for (Long id : customersCouponIDs) {
                    customersCoupons.add(couponDAO.read(id));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return customersCoupons;
    }

    //checking if a new company without ID exists in the database with identical email
    public boolean isCompanyExists(Company company) throws Exception {
        Company tempComp = companyDAO.read(company.getEmail());
        return tempComp != null;
    }

    //checking if a new customer without ID exists in the database with identical email
    public boolean isCustomerExists(Customer customer) throws Exception {
        Customer tempCustomer = customerDAO.read(customer.getEmail());
        return tempCustomer != null;
    }
}

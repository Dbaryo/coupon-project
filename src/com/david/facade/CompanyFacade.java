package com.david.facade;

import com.david.beans.Company;
import com.david.beans.Coupon;
import com.david.beans.enums.CouponCategory;
import com.david.beans.enums.EntityType;
import com.david.errors.*;
import com.david.util.InputValidationUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CompanyFacade extends ClientFacade {
    private CompanyFacade() {
    }

    private static final CompanyFacade instance = new CompanyFacade();

    public static CompanyFacade getInstance() {
        return instance;
    }

    @Override
    public boolean login(String logEmail, String logPassword) {
        try {
            //input validation according to predefined regex
            if (InputValidationUtils.isEmailValid(logEmail) && InputValidationUtils.isPasswordValid(logPassword)) {
                //retrieving if exists the need company from database
                Company c = companyDAO.read(logEmail);
                //checking if company exists in the database
                if (c != null) {
                    //comparing passwords
                    if (logPassword.equals(c.getPassword())) {
                        return true;
                    } else {
                        throw new WrongLoginInfoException(EntityType.COMPANY, "Input password does not match");
                    }
                } else {
                    throw new EntityNotFoundException(EntityType.COMPANY);
                }
            } else {
                throw new InputValidationException("Email and\\or Password");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + logEmail);
            return false;
        }
    }
//-----------------------------------------------------------------------------------------------------------

    public synchronized void addCoupon(Coupon coupon) {
        try {
            // verifying if coupon is not already in database
            if (!isCouponExists(coupon, true)) {
                if (coupon.areDatesLegal()) {
                    if (coupon.getAmount() > 0) {
                        long returnedID = couponDAO.create(coupon);
                        coupon.setId(returnedID);
                        System.out.println(coupon + " was successfully created and added to database at: " + LocalDateTime.now());
                    }else {
                        throw new  InputValidationException("amount has to be positive: ");
                    }
                }
            } else {
                throw new EntityExistsException(EntityType.COUPON, coupon.getTitle());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void updateCoupon(Coupon coupon) {
        try {
            //checking if coupon exists and if the new dates are legal
            if (couponDAO.read(coupon.getId()) != null && coupon.areDatesLegal()) {
                couponDAO.update(coupon);
                System.out.println("Coupon with id: " + coupon.getId() + " was successfully updated at: " + LocalDateTime.now());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void deleteCoupon(long id) {
        try {
            //retrieving coupon by id
            Coupon couponToDelete = couponDAO.read(id);
            //checking if coupon exists in database
            if (couponToDelete != null) {
                //deleting coupon from coupons table in database
                couponDAO.delete(id);
                //deleting coupon from customers purchased coupons
                customerToCouponDAO.deleteFromCustomerCoupon(id, EntityType.COUPON);
                System.out.println("Coupon with  id :" + id + " was successfully deleted at: " + LocalDateTime.now());
            } else {
                throw new EntityNotFoundException(EntityType.COUPON);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Coupon> getCompanyCoupons(long companyID) {
        Company c;
        ArrayList<Coupon> coupons = new ArrayList<>();
        try {
            //retrieving and checking if company exists in database
            c = companyDAO.read(companyID);
            if (c != null) {
                //retrieving all coupons
                coupons = couponDAO.readAll();
                //if list is not empty removing all the coupons who do not share the same company id
                if (coupons != null) {
                    coupons.removeIf(coupon -> coupon.getCompanyID() != companyID);
                } else {
                    throw new NoDataException();
                }
            } else {
                throw new EntityNotFoundException(EntityType.COMPANY);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return coupons;
    }

    public List<Coupon> getCompanyCoupons(long companyID, CouponCategory couponCategory) {
        Company c;
        List<Coupon> coupons = null;
        try {
            //retrieving and checking if company exists in database
            c = companyDAO.read(companyID);
            if (c != null) {
                // using methode above
                coupons = getCompanyCoupons(companyID);
                //if list is not empty removing all the coupons who do not share the same coupon category
                if (coupons != null) {
                    for (Coupon coupon : coupons) {
                        if (!(coupon.getCategory().equals(couponCategory))) {
                            coupons.remove(coupon);
                        }
                    }
                } else {
                    throw new NoDataException();
                }
            } else {
                throw new EntityNotFoundException(EntityType.COMPANY);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return coupons;
    }

    public List<Coupon> getCompanyCoupons(long companyID, double maxPrice) {
        Company c;
        List<Coupon> coupons = null;
        try {
            //retrieving and checking if company exists in database
            c = companyDAO.read(companyID);
            if (c != null) {
                coupons = getCompanyCoupons(companyID);
                //if list is not empty removing all the coupons who are more expensive than predefined max price
                if (coupons != null) {
                    for (Coupon coupon : coupons) {
                        if (coupon.getPrice() > maxPrice) {
                            coupons.remove(coupon);
                        }
                    }
                } else {
                    throw new NoDataException();
                }
            } else {
                throw new EntityNotFoundException(EntityType.COMPANY);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return coupons;
    }

    // retrieving company details with or without company coupons
    public Company getCompanyDetails(long companyID, boolean withCoupons) {
        try {
            Company tempCompany = companyDAO.read(companyID);
            if (withCoupons) {
                tempCompany.setCoupons(getCompanyCoupons(companyID));
            }
            return tempCompany;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // checking if coupon exists in database by id and company id with the option to compare titles by demand
    public boolean isCouponExists(Coupon coupon, boolean withTitle) throws Exception {
        List<Coupon> coupons = couponDAO.readAll();
        if (!withTitle) {
            for (Coupon companyCoupon : coupons) {
                //returns 0 if coupons are equal or 1 if not
                if (coupon.compareTo(companyCoupon) == 0) {
                    return true;
                }
            }
        } else {
            for (Coupon companyCoupon : coupons) {
                if (coupon.compareTo(companyCoupon) == 0 && coupon.isEqualTitle(companyCoupon)) {
                    return true;
                }
            }
        }
        return false;
    }

}



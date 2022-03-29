package com.david.errors;

import com.david.beans.Coupon;
// used in the process of coupon purchase regarding date, stock and whether coupon was purchased already
public class CouponPurchaseException extends Exception{

    public CouponPurchaseException(Coupon coupon, String msg) {
        super("Coupon: " + coupon.getTitle() + ", ID: " + coupon.getId() + msg);
    }

}

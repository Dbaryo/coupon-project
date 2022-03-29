package com.david.errors;

import com.david.beans.enums.CouponDates;

//used by method areDatesLegal in coupon class
public class DateException extends Exception{

    public DateException(String msg) {
        super(msg);
    }

    public DateException(CouponDates cD, String msg) {
        super(cD + " " +  msg);
    }

    public DateException(CouponDates cDS, CouponDates cDE) {
        super(cDE + " is ealier than " + cDS);
    }
}

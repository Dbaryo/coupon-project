package com.david.beans;

import com.david.beans.enums.CouponCategory;
import com.david.beans.enums.CouponDates;
import com.david.errors.DateException;
import com.david.util.InputValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Coupon implements Expirable, Comparable<Coupon> {

    public Coupon(long id, long companyID, CouponCategory category, String title, String description,
                  String startDate, String endDate, int amount, double price, String image) {
        this.id = id;
        this.companyID = companyID;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
        this.amount = amount;
        this.price = price;
        this.image = image;
    }

    public Coupon(long companyID, CouponCategory category, String title, String description,
                  String startDate, String endDate, int amount, double price, String image) {
        this.companyID = companyID;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
        this.amount = amount;
        this.price = price;
        this.image = image;
    }

    private long id;
    private final long companyID;
    private final CouponCategory category;
    private String title;
    private String description;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private int amount;
    private double price;
    private String image;

    @Override
    //checks if the coupon is expired
    public boolean isExpired() {
        return endDate.isBefore(LocalDate.now());
    }
    //checks coupon dates:
    // - if dates are valid according to predifined regex
    // - if start date has not passed and is not after end date
    // - if end date has not passed and is not before start date
    public boolean areDatesLegal() throws DateException {
        if( InputValidationUtils.isDateValid(this.startDate.toString())
                && InputValidationUtils.isDateValid(this.endDate.toString()) ){
            if (this.startDate.isAfter(LocalDate.now()) || this.startDate.equals(LocalDate.now())){
                if (this.endDate.isAfter(LocalDate.now())){
                    if (this.endDate.isAfter(this.startDate)){
                        return true;
                    }else {
                        throw new DateException(CouponDates.START_DATE, CouponDates.END_DATE);
                    }
                }else {
                    throw new DateException(CouponDates.END_DATE, " is in the past");
                }
            }else {
                throw new DateException(CouponDates.START_DATE, "is in the past");
            }
        }else{
            throw new DateException("please insert dates in pattern yyyy-mm-dd");
        }
    }

    // compares 2 coupons by propertied id(if available) and company id
    // returns 0 if equal and 1 if not
    public int compareTo(Coupon o) {
        if (this.id == 0) {
            if (isEqualCompanyID(o)) {
                return 0;
            }else {return 1;}
        }else {
            if (isEqualCompanyID(o) && isEqualID(o)){
                return 0;
            }else {return 1;}
        }
    }

    public boolean isEqualID(Coupon o) {
        return this.id == o.getId();
    }

    public boolean isEqualCompanyID(Coupon o) {
        return this.companyID == o.getCompanyID();
    }

    public boolean isEqualTitle(Coupon o) {
        return this.title.equals(o.getTitle());
    }


    public boolean isInStock(){
        return this.amount > 0;

    }

    @Override
    public String toString() {
        return "\n Coupon{" +
                "id=" + id +
                ", companyID=" + companyID +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", is expired=" + this.isExpired() +
                ", amount=" + amount +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}' + "\n";
    }
}

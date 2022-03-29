package com.david.beans.enums;

public enum CouponCategory {
    FOOD(1L),
    ELECTRICITY(2L),
    RESTAURANT(3L),
    VACATION(4L),
    ;


    private final long levelCode;

    private CouponCategory(long levelCode) {
        this.levelCode = levelCode;
    }

    public long getLevelCode() {
        return levelCode;
    }
}

package com.david.task;

import com.david.beans.Coupon;
import com.david.beans.enums.EntityType;
import com.david.dao.CouponDAO;
import com.david.dao.CustomerToCouponDAO;
import com.david.errors.NoDataException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// a daily task of deleting expired coupons
public class TaskScheduler {

    Timer timer;

    //24H = 86400s
    public TaskScheduler(long milSeconds) {
        this.timer = new Timer();
        timer.schedule(dCDTask, 0, milSeconds);
    }

    private final DailyCouponDeleteTask dCDTask = new DailyCouponDeleteTask();

    public static class DailyCouponDeleteTask extends TimerTask {

        CustomerToCouponDAO customerToCouponDAO = CustomerToCouponDAO.getInstance();
        CouponDAO couponDAO = CouponDAO.getInstance();

        @Override
        public void run() {
            System.out.println("\n DailyCouponDeleteTask has started at: " + LocalDateTime.now());
            int deletedCouponCounter = 0;
            try {
                // retrieving all coupons
                ArrayList<Coupon> allCoupons = couponDAO.readAll();
                // if list is not empty
                if (allCoupons != null) {
                    // checking all expiration dates on all coupons
                    for (Coupon coupon : allCoupons) {
                        if (coupon.isExpired()) {
                            //if expired deleting form customer purchased coupons table in database
                            customerToCouponDAO.deleteFromCustomerCoupon(coupon.getId(), EntityType.COUPON);
                            // deleting from coupons table in database
                            couponDAO.delete(coupon.getId());
                            deletedCouponCounter++;
                        }
                    }
                } else {
                    throw new NoDataException();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("DailyCouponDeleteTask deleted: " + deletedCouponCounter + " coupons");
            System.out.println("DailyCouponDeleteTask finished at: " + LocalDateTime.now());
            System.out.println("                                                                    ");

        }
    }

    public void stopDailyTask() {
        System.out.println("\n TimerScheduler ended at: " + LocalDateTime.now());
        dCDTask.cancel();
        timer.cancel();
    }

}

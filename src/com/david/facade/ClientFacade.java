package com.david.facade;


import com.david.dao.CompanyDAO;
import com.david.dao.CouponDAO;
import com.david.dao.CustomerDAO;
import com.david.dao.CustomerToCouponDAO;

public abstract class ClientFacade {

    protected final CompanyDAO companyDAO = CompanyDAO.getInstance();
    protected final CouponDAO couponDAO = CouponDAO.getInstance();
    protected final CustomerDAO customerDAO = CustomerDAO.getInstance();
    protected final CustomerToCouponDAO customerToCouponDAO = CustomerToCouponDAO.getInstance();

    public abstract boolean login(String logEmail, String logPassword);

}

package com.david.login;

import com.david.beans.enums.EntityType;
import com.david.facade.AdminFacade;
import com.david.facade.ClientFacade;
import com.david.facade.CompanyFacade;
import com.david.facade.CustomerFacade;

import java.time.LocalDateTime;


public class LoginManager {

    private LoginManager() {
    }

    private final AdminFacade adminFacade = AdminFacade.getInstance();
    private final CompanyFacade companyFacade = CompanyFacade.getInstance();
    private final CustomerFacade customerFacade = CustomerFacade.getInstance();

    private static final LoginManager instance = new LoginManager();

    public static LoginManager getInstance(){
        return instance;
    }

    // choosing the suitable login function for each client type
    public ClientFacade login(String logEmail, String logPassword, EntityType eT) {
        switch (eT) {
            case ADMIN: {
                if (adminFacade.login(logEmail, logPassword)) {
                    System.out.println(" ");
                    System.out.println(eT + " user, email: " + logEmail + " has successfully logged in at: " + LocalDateTime.now());
                    return adminFacade;
                }
                break;
            }
            case COMPANY: {
                if (companyFacade.login(logEmail, logPassword)) {
                    System.out.println(" ");
                    System.out.println(eT + " user, email: " + logEmail + " has successfully logged in at: " + LocalDateTime.now());
                    return companyFacade;
                }
                break;
            }
            case CUSTOMER: {
                if (customerFacade.login(logEmail, logPassword)) {
                    System.out.println(" ");
                    System.out.println(eT + " user, email: " + logEmail + " user has successfully logged in at: " + LocalDateTime.now());
                    return customerFacade;
                }
                break;
            }
        }
        return null;
    }


}

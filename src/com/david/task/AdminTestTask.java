package com.david.task;

import com.david.beans.Company;
import com.david.beans.Customer;
import com.david.beans.enums.EntityType;
import com.david.facade.AdminFacade;
import com.david.facade.ClientFacade;
import com.david.login.LoginManager;


// a class to demonstrate ADMIN operations with made up data
public class AdminTestTask {

    LoginManager loginManager = LoginManager.getInstance();

    public void adminTests() {
        AdminFacade aDF = adminLoginTest();
        System.out.println("-------------------------------------Admin -  company related testing:");
        adminCreatingCompaniesTest(aDF);
        System.out.println("--------------------------------COMPANIES--------------------------------");
        System.out.println(aDF.getAllCompanies());
        System.out.println("updating email and password for company with id 2");
        System.out.println("getting and printing one company before update for comparison");
        Company companyToUpdate = aDF.getOneCompany(2L);
        System.out.println(companyToUpdate + "----------------BEFORE UPDATING----------------");
        companyToUpdate.setEmail("GREENCAT@GMAIL.COM");
        companyToUpdate.setPassword("catgreen");
        System.out.println("Updating and printing in the data base");
        aDF.updateCompany(companyToUpdate);
        System.out.println("trying to update invalid email");
        companyToUpdate.setEmail("NOT VALID");
        aDF.updateCompany(companyToUpdate);
        System.out.println("printing to show no update was made");
        System.out.println(aDF.getOneCompany(companyToUpdate.getId()) + "--------------------AFTER UPDATING---------------------");
        System.out.println("deleting companies with id numbers 2, 3, and invalid id (20L)");
        aDF.deleteCompany(2L);
        aDF.deleteCompany(3L);
        aDF.deleteCompany(20L);
        System.out.println("getting and printing all companies to compare after deletion");
        System.out.println("*******************************AFTER DELETING*****************************************************");
        System.out.println(aDF.getAllCompanies());
        System.out.println("*********************************************************************************************");
        System.out.println("-----------------------------------Admin - customer related testing:");
        adminCreatingCustomersTest(aDF);
        System.out.println("--------------------------------CUSTOMERS--------------------------------");
        System.out.println(aDF.getAllCustomers());
        System.out.println("updating names, email and password for customer with id 3");
        System.out.println("getting and printing one customer before update for comparison");
        Customer customerToUpdate = aDF.getOneCustomer(3L, false);
        System.out.println(customerToUpdate + "----------------BEFORE UPDATING----------------");
        customerToUpdate.setFirstName("Clark");
        customerToUpdate.setLastName("Kent");
        customerToUpdate.setEmail("superman@dc.com");
        customerToUpdate.setPassword("superman");
        System.out.println("Updating and printing in the data base");
        aDF.updateCustomer(customerToUpdate);
        System.out.println("trying to update invalid email");
        customerToUpdate.setEmail("NOT VALID");
        System.out.println("printing to show no update was made");
        System.out.println(aDF.getOneCustomer(customerToUpdate.getId(), false) + "--------------------AFTER UPDATING---------------------");
        System.out.println("Deleting id 5L and trying to delete invalid id 30L");
        aDF.deleteCustomer(5L);
        aDF.deleteCustomer(30L);
        System.out.println("getting and printing all companies to compare after deletion");
        System.out.println("***********************************AFTER DELETING*****************************************");
        System.out.println(aDF.getAllCustomers());
        System.out.println("*********************************************************************************************");

    }

    //login as Admin: returns ClientFacade so casting is needed
    public AdminFacade adminLoginTest() {
        ClientFacade clientFacade = loginManager.login(AdminFacade.getInstance().getADMIN_USER_NAME(),
                AdminFacade.getInstance().getADMIN_PASSWORD(), EntityType.ADMIN);
        if (clientFacade instanceof AdminFacade) {
            return (AdminFacade) clientFacade;
        }
        return null;
    }

    public void adminCreatingCompaniesTest(AdminFacade aF) {
        for (Company company : TestData.dataCompanies){
            aF.addCompany(company);
        }
        System.out.println("trying to add a company with the same details");
        aF.addCompany(new Company("Vitrina", "vitrina@gmail.com", "vitrina"));
        System.out.println("trying to add a company with wrong input");
        aF.addCompany(new Company("Shofersal", "1234", "shufersal"));

    }

    public void adminCreatingCustomersTest(AdminFacade aF) {
        for (Customer customer : TestData.dataCustomers){
            aF.addCustomer(customer);
        }
        System.out.println("trying to add customer with the same details");
        aF.addCustomer(new Customer("Peter", "Parker", "spiderman@marvel.com", "spiderman"));
        System.out.println("trying to add customer with wrong input");
        aF.addCustomer(new Customer("Ultron", "Ultron", "peace in our time", "Ultron"));
    }

    public void secondAdminTests(){
        //login with ADMIN
        AdminFacade adFacade = adminLoginTest();
        System.out.println("\n Deleting customer id 2");
        adFacade.deleteCustomer(2L);
        System.out.println("\n Deleting company id 5 ");
        adFacade.deleteCompany(5L);
    }

}

package com.david.beans;

import lombok.Data;


import java.util.List;

@Data
public class Customer implements Comparable<Customer> {

    public Customer(long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Customer(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Coupon> coupons;


    @Override
    //this method compare customers by properties: ID(if evailable), first_name, last_name and email.
    public int compareTo(Customer o) {
        if (this.id == 0) {
            if (this.firstName.equals(o.getFirstName()) && this.lastName.equals(o.getLastName())
                    && this.email.equals(o.getEmail())) {
                return 0;
            }
        } else if (this.firstName.equals(o.getFirstName()) && this.lastName.equals(o.getLastName())
                && this.email.equals(o.getEmail()) && this.id == o.getId()) {
            return 0;
        }
        return 1;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password=" + password +
                ", coupons=" + coupons +
                '}' + "\n";
    }
}

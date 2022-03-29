package com.david.beans;

import lombok.Data;
import java.util.List;

@Data
public class Company implements Comparable<Company> {

    public Company(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public Company(long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private long id;
    private final String name;
    private String email;
    private String password;
    private List<Coupon> coupons;

    //compare 2 companies by id (if available), name and email
    public int compareTo(Company o) {
        if (this.id == 0) {
            if (this.name.equals(o.getName()) && this.email.equals(o.getEmail())) {
                return 0;
            } else {
                return 1;
            }
        } else if (this.name.equals(o.getName()) && this.email.equals(o.getEmail()) && this.id == o.getId()) {
            return 0;
        }
        return 1;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password=" + password +
                ", coupons=" + coupons +
                '}' + "\n";
    }
}

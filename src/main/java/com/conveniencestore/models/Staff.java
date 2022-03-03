package com.conveniencestore.models;

import com.conveniencestore.enums.Gender;
import com.conveniencestore.enums.Role;

public class Staff  extends Person {
    private int staffId;
    private Role role;


    public Staff(String firstName, String secondName, String address, Gender gender, Role role) {
        super(firstName, secondName, address, gender);
        this.role = role;
        staffId++;
    }

    public Role getRole() {
        return role;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "firstName='" + getFirstName() + '\'' +
                "lastName='" + getLastName() + '\'' +
                "address='" + getAddress()+ '\'' +
                "gender=" + getGender() +
                "staffId=" + staffId +
                "role=" + role +
                '}';
    }
}

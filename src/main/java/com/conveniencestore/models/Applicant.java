package com.conveniencestore.models;

import com.conveniencestore.enums.Gender;
import com.conveniencestore.enums.Qualification;

public class Applicant extends Person {
    private final Qualification qualification;

    public Applicant(String firstName, String lastName, String address, Gender gender, Qualification qualification) {
        super(firstName, lastName, address, gender);
        this.qualification = qualification;
    }

    public Qualification getQualification() {
        return qualification;
    }

    @Override
    public String toString() {
        return "Applicants{" +
                "firstName='" + super.getFirstName() + '\'' +
                "lastName='" + super.getLastName() + '\'' +
                "address='" + super.getAddress()+ '\'' +
                "gender=" + super.getGender() +
                "qualification=" + qualification +
                '}';
    }
}

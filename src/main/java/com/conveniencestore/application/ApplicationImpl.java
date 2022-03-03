package com.conveniencestore.application;

import com.conveniencestore.exception.AlreadyAppliedException;
import com.conveniencestore.models.Applicant;
import com.conveniencestore.models.Store;

public class ApplicationImpl implements Application{
    @Override
    public void apply(Applicant applicant, Store store) {
        if(!store.getApplicants().contains(applicant)){
            store.getApplicants().add(applicant);
            System.out.printf("Thank you %s, for applying for this position, we'd get back to you after reviewing your application %n",
                    applicant.getFirstName()
            );
        }else{
            throw new AlreadyAppliedException("You already applied for this position.");
        }
    }
}

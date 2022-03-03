package com.conveniencestore.application;

import com.conveniencestore.models.Applicant;
import com.conveniencestore.models.Store;

public interface Application {
    void apply(Applicant applicant, Store store);
}

package com.conveniencestore.storeoperations;

import com.conveniencestore.application.Application;
import com.conveniencestore.application.ApplicationImpl;
import com.conveniencestore.customer.CustomerService;
import com.conveniencestore.customer.CustomerServiceImpl;
import com.conveniencestore.enums.Gender;
import com.conveniencestore.enums.Qualification;
import com.conveniencestore.enums.Role;
import com.conveniencestore.exception.ApplicantNotAppliedException;
import com.conveniencestore.exception.ApplicantNotQualifiedException;
import com.conveniencestore.exception.StaffNotAuthorizedException;
import com.conveniencestore.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.conveniencestore.enums.Qualification.OND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StoreOperationImplTest {
    private Application application;
    private StoreOperation storeOperation;
    private Customer customer;
    private Store store;
    private Staff manager;
    private Staff cashier;
    private Applicant jane;


    @BeforeEach
    public void setUp(){
        application = new ApplicationImpl();
        CustomerService customerService = new CustomerServiceImpl();
        storeOperation = StoreOperationImpl.getStoreOperationImplInstance();
        customer = new Customer("Stephen", "Gerald", "ETP", Gender.MALE);
        store = new Store("Stevade Collections");
        manager = new Staff("Sola", "Smith", "Tech Park, Edo", Gender.MALE, Role.MANAGER);
        cashier = new Staff("Sola", "Smith", "Tech Park, Edo", Gender.MALE, Role.CASHIER);
        jane = new Applicant("Jane", "Emeka", "No 10 Adejo str, Lagos", Gender.FEMALE, Qualification.OND);
        customerService.fundWallet(customer, 1000000.00);
        customerService.addProductsToCart(customer, store, "Lewis", 7);
    }

    @Test
    void canHireCashierSuccessfully() {
        application.apply(jane, store);
        storeOperation.hireCashier(jane, manager,store);
        assertThat(store.getApplicants().size()).isZero();
        assertThat(store.getStaffs().size()).isEqualTo(1);
        assertThat(store.getApplicants().contains(jane)).isFalse();
        assertThat(store.getStaffs().get(0).getFirstName()).isEqualTo(jane.getFirstName());
    }

    @Test
    void shouldThrowExceptionWhenHiringAnApplicantThatDidNotApply() {
        assertThatThrownBy(()-> storeOperation.hireCashier(jane, manager, store))
                .isInstanceOf(ApplicantNotAppliedException.class)
                .hasMessage(jane.getFirstName() + " did not apply for this role");
    }

    @Test
    void shouldThrowExceptionWhenAnApplicantIsNotQualified() {
        Applicant applicant = new Applicant("John", "Stones", "Edo",Gender.MALE, Qualification.MSC);
        application.apply(applicant, store);
        assertThatThrownBy(()-> storeOperation.hireCashier(applicant, manager, store))
                .isInstanceOf(ApplicantNotQualifiedException.class)
                .hasMessage("You must have an " + OND + " to be eligible for this position");
    }

    @Test
    void shouldThrowExceptionWhenAnUnauthorizedStaffTriesToHire() {
        application.apply(jane, store);
        assertThatThrownBy(()-> storeOperation.hireCashier(jane, cashier, store))
                .isInstanceOf(StaffNotAuthorizedException.class)
                .hasMessage("You are not authorized to hire an applicant");
    }

    @Test
    void canSellProductsSuccessfully() {
        String receipt = storeOperation.sellProducts(store, customer, cashier);
        assertThat(receipt).isNotEmpty();
        assertThat(customer.getCart().getProductsInCart().size()).isZero();
    }

    @Test
    void canViewProductsByTheirCategoriesSuccessfully() {
        Product[] shoes = storeOperation.viewProductsByCategory(store, "Shoes");
        Product[] clothes = storeOperation.viewProductsByCategory(store, "Clothing");
        Product[] hangers = storeOperation.viewProductsByCategory(store, "Hangers");

        assertThat(hangers.length).isZero();
        assertThat(shoes.length).isEqualTo(2);
        assertThat(clothes.length).isEqualTo(2);
        assertThat(Arrays.stream(clothes).anyMatch(product -> product.getName().equals("Polo"))).isTrue();
        assertThat(Arrays.stream(clothes).anyMatch(product -> product.getName().equals("Gucci & Gabanna"))).isTrue();
        assertThat(Arrays.stream(clothes).anyMatch(product -> product.getName().equals("Lewis"))).isFalse();
        assertThat(Arrays.stream(shoes).anyMatch(product -> product.getName().equals("Lewis"))).isTrue();
        assertThat(Arrays.stream(shoes).anyMatch(product -> product.getName().equals("Vans"))).isTrue();
        assertThat(Arrays.stream(shoes).anyMatch(product -> product.getName().equals("Polo"))).isFalse();
    }
}